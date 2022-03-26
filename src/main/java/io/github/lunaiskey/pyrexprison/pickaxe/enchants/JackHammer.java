package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.GridManager;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.nms.NMSBlockChange;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class JackHammer extends PyrexEnchant {
    public JackHammer() {
        super("Jackhammer", 10000, 0, 0, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Random rand = PyrexPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        double roll = rand.nextDouble();
        if (roll*100 <= 0.00025D*level) {
            Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getGridManager().getGridLocation(e.getBlock().getLocation());
            PMine mine = GridManager.getPMine(gridLoc.getLeft(),gridLoc.getRight());
            if (mine == null) {
                return;
            }
            mine.isInMineRegion(e.getBlock().getLocation());
            long counter = 0;
            for (int x = mine.getMin().getBlockX(); x <= mine.getMax().getBlockX();x++) {
                for (int z = mine.getMin().getBlockZ(); z <= mine.getMax().getBlockZ();z++) {
                    e.getBlock().getWorld().getBlockAt(x,e.getBlock().getY(),z).setType(Material.AIR);
                    counter++;
                }
            }
            mine.addMineBlocks(counter);
            p.sendMessage("JackHammer Triggered");
        }
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
            sum += 7500L+((n+300)*Math.pow(1.018,n/15D));
        }
        return sum;
    }

    @Override
    public Pair<Integer,Long> getMaxLevelFromAmount(int start, long amount) {
        long sum = 0;
        for (int n = start;n<super.getMaxLevel();n++) {
            if (sum+7500L+((n+300)*Math.pow(1.018,n/15D)) < amount) {
                sum += 7500L+((n+300)*Math.pow(1.018,n/15D));
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
            sum += 7500L+((n+300)*Math.pow(1.018,n/15D));
        }
        return sum;
    }
}
