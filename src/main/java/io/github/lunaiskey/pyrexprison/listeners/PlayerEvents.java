package io.github.lunaiskey.pyrexprison.listeners;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.commands.CommandPMine;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.mines.*;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.pickaxe.*;
import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorInv;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorPyrexHolder;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorUpgradeInv;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStone;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneInv;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneType;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.apache.commons.lang3.tuple.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private PyrexPrison plugin;
    private PMineManager pMineManager;
    private PlayerManager playerManager;

    public PlayerEvents(PyrexPrison plugin) {
        this.plugin = plugin;
        pMineManager = plugin.getPmineManager();
        playerManager = plugin.getPlayerManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
        PyrexPickaxe pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).getPickaxe();
        if (e.isCancelled()) {
            return;
        }
        if (block.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLoc = pMineManager.getGridLocation(block.getLocation());
            PMine pMine = PMineManager.getPMine(gridLoc.getLeft(), gridLoc.getRight());
            if (pMine != null) {
                if (pMine.isInMineRegion(block.getLocation())) {
                    e.setDropItems(false);
                    e.setExpToDrop(0);
                    pMine.addMineBlocks(1);
                }
            }
        }
        CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(mainHand);
        if (pyrexDataMap.contains("id")) {
            // is custom pickaxe
            if (pyrexDataMap.getString("id").equals(PickaxeHandler.getId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onBlockBreak(e,pickaxe.getEnchants().get(type));
                }

                PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).payForBlocks(1);
                pickaxe.setBlocksBroken(pickaxe.getBlocksBroken()+1);
                PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);

            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        CompoundTag tag = NBTTags.getPyrexDataMap(e.getItemInHand());
        if (tag.contains("id")) {
            String id = tag.get("id").getAsString();
            String[] idArray = id.split("_");
            if (idArray.length == 2 && idArray[1].equals("GEMSTONE")) {
                try {
                    PyrexPrison.getPlugin().getPlayerManager().getGemstonesMap().get(GemStoneType.valueOf(idArray[0])).onPlace(e);
                } catch (Exception ignored){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld().getName().equals(PMineWorld.getWorldName())) {
            p.setAllowFlight(true);
            p.setFlying(true);
        }
        PMine mine = PMineManager.getPMine(p.getUniqueId());
        if (mine == null) {
            PyrexPrison.getPlugin().getPmineManager().newPMine(p.getUniqueId());
        } else {
            mine.scheduleReset();
        }
        Map<UUID, PyrexPlayer> playerMap = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap();
        if (!playerMap.containsKey(p.getUniqueId())) {
            PyrexPrison.getPlugin().getPlayerManager().createPyrexPlayer(p.getUniqueId());
        }
        PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
        PyrexPrison.getPlugin().getPickaxeHandler().hasOrGivePickaxe(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PyrexPlayer player = playerManager.getPlayerMap().get(e.getPlayer().getUniqueId());
        player.save();
        CommandPMine.getResetCooldown().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getTo().getWorld() != null && e.getTo().getWorld().getName().equals(PMineWorld.getWorldName())) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN && e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                e.getPlayer().setAllowFlight(true);
                e.getPlayer().setFlying(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getWhoClicked();
            if (inv.getHolder() instanceof PyrexHolder) {
                PyrexHolder holder = (PyrexHolder) inv.getHolder();
                switch(holder.getInvType()) {
                    case PMINE_MAIN -> new PMineInv().onClick(e);
                    case PMINE_BLOCKS -> new PMineInvBlocks(p).onClick(e);
                    case ENCHANTS -> new EnchantInv(p).onClick(e);
                    case ADD_LEVELS -> {
                        if (holder instanceof EnchantPyrexHolder) {
                            EnchantPyrexHolder enchantHolder = (EnchantPyrexHolder) holder;
                            new AddLevelsInv(p,enchantHolder.getType()).onClick(e);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    case ARMOR -> new ArmorInv(p).onClick(e);
                    case ARMOR_UPGRADES -> {
                        if (holder instanceof ArmorPyrexHolder) {
                            ArmorPyrexHolder armorPyrexHolder = (ArmorPyrexHolder) holder;
                            new ArmorUpgradeInv(p,armorPyrexHolder.getType()).onClick(e);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    case GEMSTONES -> new GemStoneInv().onClick(e);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
            if (NBTTags.hasCustomTagContainer(e.getItem(),"voucher")) {
                Pair<CurrencyType, BigInteger> amountPair = NBTTags.getVoucherValue(e.getItem());
                Currency.giveCurrency(p.getUniqueId(),amountPair.getLeft(),amountPair.getRight());
                e.getItem().setAmount(e.getItem().getAmount()-1);
                switch(amountPair.getLeft()) {
                    case TOKENS -> p.sendMessage(StringUtil.color("&eRedeemed "+CurrencyType.getUnicode(amountPair.getLeft())+"&f"+ Numbers.formattedNumber(amountPair.getRight())+" &eTokens."));
                    case GEMS -> p.sendMessage(StringUtil.color("&aRedeemed "+CurrencyType.getUnicode(amountPair.getLeft())+"&f"+Numbers.formattedNumber(amountPair.getRight())+" &aGems."));
                    case PYREX_POINTS -> p.sendMessage(StringUtil.color("&dRedeemed "+CurrencyType.getUnicode(amountPair.getLeft())+"&f"+Numbers.formattedNumber(amountPair.getRight())+" &dPyrex Points."));
                }
            }
        }
        CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(e.getItem());
        if (pyrexDataMap.contains("id")) {
            // is custom pickaxe
            if (pyrexDataMap.getString("id").equals(PickaxeHandler.getId())) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    p.openInventory(new EnchantInv(p).getInv());
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (PMineInvBlocks.getEditMap().containsKey(p.getUniqueId())) {
            try {
                double percentage = Double.parseDouble(ChatColor.stripColor(e.getMessage()));
                if (percentage < 0) {
                    percentage = 0;
                }
                if (percentage > 100) {
                    percentage = 100;
                }
                double newValue = (Math.floor(percentage)/100);
                PMine mine = pMineManager.getPMine(p.getUniqueId());
                mine.getComposition().put(PMineInvBlocks.getEditMap().get(p.getUniqueId()),newValue);
            } catch (NumberFormatException ignored) {}
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineInvBlocks(p).getInv()));
            PMineInvBlocks.getEditMap().remove(p.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
                switch (e.getCause()) {
                    case FALL -> e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location to = e.getTo();
        if (to.getWorld().getName().equals(PMineWorld.getWorldName())) {
            PMine mine = pMineManager.getPMine(p.getUniqueId());
            if (mine != null) {
                if (to.getBlockY() < to.getWorld().getMinHeight()) {
                    p.getVelocity().setY(0);
                    e.setTo(mine.getCenter().add(0.5,1,0.5));
                }
            }
        }
    }

    @EventHandler
    public void onEquip(PlayerItemHeldEvent e) {

        Player p = e.getPlayer();
        ItemStack oldItem = p.getInventory().getItem(e.getPreviousSlot()) != null ? p.getInventory().getItem(e.getPreviousSlot()) : new ItemStack(Material.AIR);
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot()) != null ? p.getInventory().getItem(e.getNewSlot()) : new ItemStack(Material.AIR);
        CompoundTag oldMap = NBTTags.getPyrexDataMap(oldItem);
        CompoundTag newMap = NBTTags.getPyrexDataMap(newItem);
        PyrexPickaxe pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).getPickaxe();
        if (oldMap.contains("id")) {
            // is custom pickaxe
            if (oldMap.getString("id").equals(PickaxeHandler.getId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onUnEquip(p,oldItem,pickaxe.getEnchants().get(type));
                }
            }
        }
        if (newMap.contains("id")) {
            // is custom pickaxe
            if (newMap.getString("id").equals(PickaxeHandler.getId())) {
                for (EnchantType type : pickaxe.getEnchants().keySet()) {
                    PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type).onEquip(p,newItem,pickaxe.getEnchants().get(type));
                }
            }
        }

    }


}
