package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.GridManager;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.nms.NMSBlockChange;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class JackHammer extends PyrexEnchant {
    public JackHammer() {
        super("Jackhammer", 10000, 0, 0, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Player p = e.getPlayer();
        Pair<Integer,Integer> gridLoc = PyrexPrison.getPlugin().getGridManager().getGridLocation(e.getBlock().getLocation());
        PMine mine = GridManager.getPMine(gridLoc.getLeft(),gridLoc.getRight());
        if (mine == null) {
            return;
        }
        mine.isInMineRegion(e.getBlock().getLocation());
        for (int x = mine.getMin().getBlockX(); x <= mine.getMax().getBlockX();x++) {
            for (int z = mine.getMin().getBlockZ(); z <= mine.getMax().getBlockZ();z++) {
                e.getBlock().getWorld().getBlockAt(x,e.getBlock().getY(),z).setType(Material.AIR);
            }
        }
    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }
}
