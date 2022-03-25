package io.github.lunaiskey.pyrexprison.pickaxe;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public abstract class PyrexEnchant {

    private String name;
    private int maxLevel;
    private long cost;
    private long increaseCost;
    private boolean enabled;

    public PyrexEnchant(String name, int maxLevel, long cost, long increaseCost, boolean enabled) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.cost = cost;
        this.increaseCost = increaseCost;
        this.enabled = enabled;
    }

    public abstract void onBlockBreak(BlockBreakEvent e, int level);
    public abstract void onEquip(Player player, ItemStack pickaxe, int level);
    public abstract void onUnEquip(Player player, ItemStack pickaxe, int level);

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public long getCost() {
        return cost;
    }

    public long getIncreaseCost() {
        return increaseCost;
    }
}
