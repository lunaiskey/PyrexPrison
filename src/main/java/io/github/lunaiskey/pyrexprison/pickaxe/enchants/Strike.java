package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.util.ShapeUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Strike extends PyrexEnchant {
    public Strike() {
        super("Lightning", List.of("Breaks a sphere around the block you last broke."), 1000, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        double chance = 0.02*level;
        Player p = e.getPlayer();
        if (PyrexPrison.getPlugin().getRand().nextDouble()*100 <= chance*level) {
            long blocksBroken = 0;
            Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getPmineManager().getGridLocation(e.getBlock().getLocation());
            PMine mine = PMineManager.getPMine(gridLoc.getLeft(),gridLoc.getRight());
            e.getBlock().getWorld().strikeLightningEffect(e.getBlock().getLocation());
            for (Location loc : ShapeUtil.generateSphere(e.getBlock().getLocation(),5+(Math.floorDiv(level,100)))) {
                if (mine.isInMineRegion(loc)) {
                    if (loc.getBlock().getType() != Material.AIR) {
                        blocksBroken++;
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
            mine.addMineBlocks(blocksBroken);
            PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).payForBlocks(blocksBroken);
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
        return BigInteger.valueOf(3000+(5000L*n));
    }
}
