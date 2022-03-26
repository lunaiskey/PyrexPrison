package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Fortune extends PyrexEnchant {


    public Fortune() {
        super("Fortune", 10000, 0, 0, true);
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
        long sum = 0;
        for (int n = 0;n<level;n++) {
            sum += 10000L*(n+1);
        }
        return sum;
    }

    @Override
    public Pair<Integer,Long> getMaxLevelFromAmount(int start, long amount) {
        long sum = 0;
        for (int n = start;n<super.getMaxLevel();n++) {
            if (sum+10000L*(n+1) < amount) {
                sum += 10000L*(n+1);
            } else {
                return new ImmutablePair<>(n,sum);
            }
        }
        return new ImmutablePair<>(super.getMaxLevel(),sum);
    }

    @Override
    public long getCostBetweenLevels(int start, int end) {
        long sum = 0;
        for (int n = start;n<end;n++) {
            sum += 10000L * (n+1);
        }
        return sum;
    }
}
