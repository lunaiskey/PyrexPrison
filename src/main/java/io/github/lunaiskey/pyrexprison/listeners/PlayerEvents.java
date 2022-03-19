package io.github.lunaiskey.pyrexprison.listeners;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInv;
import io.github.lunaiskey.pyrexprison.mines.*;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private PyrexPrison plugin;
    private GridManager gridManager;
    private PlayerManager playerManager;

    public PlayerEvents(PyrexPrison plugin) {
        this.plugin = plugin;
        gridManager = plugin.getGridManager();
        playerManager = plugin.getPlayerManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (e.isCancelled()) {
            return;
        }
        if (block.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLoc = gridManager.getGridLocation(block.getLocation());
            PMine pMine = GridManager.getPMine(gridLoc.getLeft(), gridLoc.getRight());
            if (pMine != null) {
                if (pMine.isInMineRegion(block.getLocation())) {
                    e.setDropItems(false);
                    pMine.addMineBlocks(1);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PMine mine = GridManager.getPMine(p.getUniqueId());
        if (mine == null) {
            PyrexPrison.getPlugin().getGridManager().newPMine(p.getUniqueId());
        }
        if (mine != null) {
            mine.scheduleReset();
        }
        Map<UUID, PyrexPlayer> playerMap = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap();
        if (!playerMap.containsKey(p.getUniqueId())) {
            PyrexPrison.getPlugin().getPlayerManager().createPyrexPlayer(p.getUniqueId());
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getWhoClicked();
            if (inv.getHolder() instanceof PyrexInv) {
                PyrexInv holder = (PyrexInv) inv.getHolder();
                switch(holder.getInvType()) {
                    case PMINE_MAIN -> new PMineInv().onClick(e);
                    case PMINE_BLOCKS -> new PMineInvBlocks(p).onClick(e);
                }
            }
        }
    }




    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
            if (NBTTags.hasCustomTagContainer(e.getItem(),"voucher")) {
                Pair<CurrencyType,Long> amountPair = NBTTags.getVoucherValue(e.getItem());
                Currency.giveCurrency(p.getUniqueId(),amountPair.getLeft(),amountPair.getRight());
                e.getItem().setAmount(e.getItem().getAmount()-1);
                switch(amountPair.getLeft()) {
                    case TOKENS -> p.sendMessage(StringUtil.color("&eRedeemed &f"+ Numbers.formattedNumber(amountPair.getRight())+" &eTokens."));
                    case GEMS -> p.sendMessage(StringUtil.color("&aRedeemed &f"+Numbers.formattedNumber(amountPair.getRight())+" &aGems."));
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
                PMine mine = GridManager.getPMine(p.getUniqueId());

                mine.getComposition().put(PMineInvBlocks.getEditMap().get(p.getUniqueId()),newValue);

            } catch (NumberFormatException ignored) {}
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineInvBlocks(p).getInv()));
            PMineInvBlocks.getEditMap().remove(p.getUniqueId());
        }

    }


}
