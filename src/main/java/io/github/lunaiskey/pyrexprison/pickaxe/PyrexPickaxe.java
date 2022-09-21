package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PyrexPickaxe {

    private UUID player;
    private Map<EnchantType,Integer> enchants;
    private Set<EnchantType> disabledEnchants;
    private long blocksBroken;

    public PyrexPickaxe(UUID player, Map<EnchantType,Integer> enchants,Set<EnchantType> disabledEnchants, long blocksBroken) {
        this.player = player;
        if (enchants == null) {
            enchants = new HashMap<>();
        }
        for (EnchantType type : EnchantType.getDefaultMap().keySet()) {
            if (enchants.containsKey(type)) {
                if (enchants.get(type) < EnchantType.getDefaultMap().get(type)) {
                    enchants.put(type,EnchantType.getDefaultMap().get(type));
                }
            } else {
                enchants.put(type,EnchantType.getDefaultMap().get(type));
            }
        }
        this.enchants = enchants;
        if (disabledEnchants == null) {
            this.disabledEnchants = new HashSet<>();
        } else {
            this.disabledEnchants = disabledEnchants;
        }
        this.blocksBroken = blocksBroken;
    }

    public PyrexPickaxe(UUID player) {
        this(player,null,null,0);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        item = NBTTags.addPyrexData(item,"id",PickaxeHandler.getId());
        return PyrexPrison.getPlugin().getPickaxeHandler().updatePickaxe(item,player);
    }


    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        Location blockLoc = block.getLocation();
        if (blockLoc.getWorld().getName().equals(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getPmineManager().getGridLocation(blockLoc);
            PMine pMine = PyrexPrison.getPlugin().getPmineManager().getPMine(gridLoc.getLeft(), gridLoc.getRight());
            if (pMine != null) {
                if (pMine.isInMineRegion(blockLoc)) {
                    e.setDropItems(false);
                    e.setExpToDrop(0);
                    pMine.addMineBlocks(1);
                    for (EnchantType type : getEnchants().keySet()) {
                        PyrexEnchant enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
                        if (enchant.isEnabled()) {
                            if (!getDisabledEnchants().contains(type)) {
                                enchant.onBlockBreak(e,getEnchants().get(type));
                            }
                        }
                    }
                    PyrexPlayer player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId());
                    PyrexPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),1);
                    //p.giveExp(1+player.getXPBoostTotal()); //Not Implemented yet...
                    PyrexPrison.getPlugin().getPlayerManager().tickGemstoneCount(p);
                    setBlocksBroken(getBlocksBroken()+1);
                    PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                }
            }
        }
    }

    public void onInteract(PlayerInteractEvent e) {

    }

    public long getBlocksBroken() {
        return blocksBroken;
    }

    public Map<EnchantType, Integer> getEnchants() {
        return enchants;
    }

    public Set<EnchantType> getDisabledEnchants() {
        return disabledEnchants;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }
}
