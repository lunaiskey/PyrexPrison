package io.github.lunaiskey.pyrexprison.modules.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.pmines.PMine;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.util.ShapeUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Strike extends PyrexEnchant {
    public Strike() {
        super("Lightning", List.of("Breaks a sphere around the block you last broke."), 1000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        double chance = 0.01D*level;
        Player p = e.getPlayer();
        if (PyrexPrison.getPlugin().getRand().nextDouble()*100 <= chance) {
            long blocksBroken = 0;
            Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getPmineManager().getGridLocation(e.getBlock().getLocation());
            PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(gridLoc.getLeft(),gridLoc.getRight());
            for (Location loc : ShapeUtil.generateSphere(e.getBlock().getLocation(),5+(Math.floorDiv(level,getMaxLevel()/10)))) {
                if (mine.isInMineRegion(loc)) {
                    if (loc.getBlock().getType() != Material.AIR) {
                        blocksBroken++;
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
            mine.addMineBlocks(blocksBroken);
            PyrexPrison.getPlugin().getPlayerManager().payForBlocks(e.getPlayer(),blocksBroken);
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
}
