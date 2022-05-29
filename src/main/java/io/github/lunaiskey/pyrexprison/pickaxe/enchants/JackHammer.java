package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class JackHammer extends PyrexEnchant {
    public JackHammer() {
        super("Jackhammer", List.of("Has a chance to break an","entire layer of your mine"), 1000, CurrencyType.TOKENS,   true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Random rand = PyrexPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        double roll = rand.nextDouble();
        if (roll*100 <= getChance(level)) {
            Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getPmineManager().getGridLocation(e.getBlock().getLocation());
            PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(gridLoc.getLeft(),gridLoc.getRight());
            if (mine == null) {
                return;
            }
            mine.isInMineRegion(e.getBlock().getLocation());
            long counter = 0;
            for (int x = mine.getMin().getBlockX(); x <= mine.getMax().getBlockX();x++) {
                for (int z = mine.getMin().getBlockZ(); z <= mine.getMax().getBlockZ();z++) {
                    Block block = e.getBlock().getWorld().getBlockAt(x,e.getBlock().getY(),z);
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                        counter++;
                    }
                }
            }
            mine.addMineBlocks(counter);
            PyrexPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),counter);
            //p.sendMessage("JackHammer Triggered");
        }
    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getEquation(int n) {
        //return BigInteger.valueOf(2000+(1500L*n));
        return BigInteger.valueOf(8000+(25000L*n));
    }
    /*
    @Override
    public BigInteger getEquation(int n) {
        return BigInteger.valueOf((long) (2000L+((n+300)*Math.pow(1.018,n/15D))));
    }

     */

    private double getChance(int level) {
        return 0.01D*level;
    }
}
