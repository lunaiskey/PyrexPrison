package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Efficiency extends PyrexEnchant {

    public Efficiency() {
        super("Efficiency", 100, 0, 0, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public long getLevelCost(int level) {
        return 0;
    }

    @Override
    public Pair<Integer,Long> getMaxLevelFromAmount(int start, long amount) {
        return new ImmutablePair<>(0,0L);
    }

    @Override
    public long getCostBetweenLevels(int start, int end) {
        return 0;
    }
}
