package io.github.lunaiskey.pyrexprison.listeners;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInv;
import io.github.lunaiskey.pyrexprison.mines.*;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.math.BigInteger;

public class PlayerEvents implements Listener {

    private PyrexPrison plugin;
    private GridManager gridManager;

    public PlayerEvents(PyrexPrison plugin) {
        this.plugin = plugin;
        gridManager = plugin.getGridManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (e.isCancelled()) {
            return;
        }
        if (block.getLocation().getWorld().getName().equals(PMineWorld.getWorldName())) {
            Pair<Integer,Integer> gridLoc = gridManager.getGridLocation(block.getLocation());
            PMine pMine = GridManager.getPMine(gridLoc.getLeft(), gridLoc.getRight());
            if (pMine != null) {
                if (pMine.isInMineRegion(block.getLocation())) {
                    e.setDropItems(false);
                    pMine.addMineBlocks(1);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PMine mine = GridManager.getPMine(p.getUniqueId());
        if (mine == null) {
            PyrexPrison.getPlugin().getGridManager().newPMine(p.getUniqueId());
        }
        if (mine != null) {
            mine.scheduleReset();
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getWhoClicked();
            if (inv.getHolder() instanceof PyrexInv) {
                PyrexInv holder = (PyrexInv) inv.getHolder();
                switch(holder.getInvType()) {
                    case PMINE_MAIN -> new PMineInv().onClick(e);
                    case PMINE_BLOCKS -> new PMineInvBlocks(p).onClick(e);
                }
            }
        }
    }



    /*
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        //player is in create mine action
        Player p = e.getPlayer();

        if (CommandMine.getCreateMap().containsKey(e.getPlayer().getUniqueId())) {
            MutableTriple<String, Location, Location> tuple = CommandMine.getCreateMap().get(p.getUniqueId());
            if (e.getClickedBlock() != null) {
                Location loc = e.getClickedBlock().getLocation();
                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    tuple.setMiddle(e.getClickedBlock().getLocation());
                    p.sendMessage("Pos1 set at "+loc.getX()+","+loc.getY()+","+loc.getZ());

                }
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    tuple.setRight(e.getClickedBlock().getLocation());
                    p.sendMessage("Pos2 set at "+loc.getX()+","+loc.getY()+","+loc.getZ());
                }
            }

        }
    }

     */

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (PMineInvBlocks.getEditMap().containsKey(p.getUniqueId())) {
            try {
                double percentage = Double.parseDouble(ChatColor.stripColor(e.getMessage()));
                if (percentage < 0) {
                    percentage = 0;
                }
                if (percentage > 100) {
                    percentage = 100;
                }
                double newValue = (Math.floor(percentage)/100);
                PMine mine = GridManager.getPMine(p.getUniqueId());

                mine.getComposition().put(PMineInvBlocks.getEditMap().get(p.getUniqueId()),newValue);

            } catch (NumberFormatException ignored) {}
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineInvBlocks(p).getInv()));
            PMineInvBlocks.getEditMap().remove(p.getUniqueId());
        }

    }


}
