package io.github.lunaiskey.pyrexprison.modules.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.pmines.PMine;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class Explosive extends PyrexEnchant {
    public Explosive() {
        super("Explosive", List.of("Breaks a cube around the broken block"), 1000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Location loc = e.getBlock().getLocation();
        Random rand = PyrexPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        double roll = rand.nextDouble();
        if (roll*100 <= getChance(level)) {
            if (level > getMaxLevel()) {
                level = getMaxLevel();
            }
            int width = 5 + (((level - (level%200)) / 200)*2); //always odd, every 200 levels + 2 to width.
            int radius = (width-1)/2;
            Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getPmineManager().getGridLocation(e.getBlock().getLocation());
            PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(gridLoc.getLeft(),gridLoc.getRight());
            long blocks = 0;
            for (int x = loc.getBlockX()-radius;x<= loc.getBlockX()+radius;x++) {
                for (int z = loc.getBlockZ()-radius;z<= loc.getBlockZ()+radius;z++) {
                    for (int y = loc.getBlockY()-(radius*2);y<=loc.getBlockY();y++) {
                        Block block = loc.getWorld().getBlockAt(x,y,z);
                        if (mine.isInMineRegion(block.getLocation())) {
                            if (block.getType() != Material.AIR) {
                                blocks++;
                                block.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
            PyrexPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),blocks);
            mine.addMineBlocks(blocks);
        }
    }

    @Override
    public void onDrop(PlayerDropItemEvent e, int level) {

    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getCost(int n) {
        return BigInteger.valueOf(8000+(25000L*n));
    }

    private double getChance(int level) {
        return 0.01D*level;
    }
}
