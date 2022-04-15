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
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Strike extends PyrexEnchant {
    public Strike() {
        super("Lightning", List.of("Breaks a sphere around the block you last broke."), 0, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Player p = e.getPlayer();
        long blocksBroken = 0;
        Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getPmineManager().getGridLocation(e.getBlock().getLocation());
        PMine mine = PMineManager.getPMine(gridLoc.getLeft(),gridLoc.getRight());
        for (Location loc : ShapeUtil.generateSphere(e.getBlock().getLocation(),5)) {
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

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getEquation(int n) {
        return BigInteger.ZERO;
    }
}
