package io.github.lunaiskey.pyrexprison.listeners;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.commands.CommandMine;
import io.github.lunaiskey.pyrexprison.mines.GlobalMine;
import io.github.lunaiskey.pyrexprison.mines.GridManager;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
                pMine.addMineBlocks(1);
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (ChatColor.stripColor(e.getMessage()).equalsIgnoreCase("confirm")) {
            //confirming create mine
            if (CommandMine.getCreateMap().containsKey(p.getUniqueId())) {
                MutableTriple<String,Location,Location> mine = CommandMine.getCreateMap().get(p.getUniqueId());
                // locations are both in the same world
                if (CommandMine.checkValidMineCoords(mine.getMiddle(),mine.getRight())) {
                    p.sendMessage("Created '"+mine.getLeft()+"' mine");
                    PyrexPrison.getMines().put(mine.getLeft(),new GlobalMine(mine.getMiddle().getBlockX(),mine.getMiddle().getBlockY(),mine.getMiddle().getBlockZ(),mine.getRight().getBlockX(),mine.getRight().getBlockY(),mine.getRight().getBlockZ(),mine.getLeft(),mine.getRight().getWorld()));

                    CommandMine.getCreateMap().remove(p.getUniqueId());
                } else {
                    p.sendMessage("No or Invalid coordinates given, Exiting...");
                    CommandMine.getCreateMap().remove(p.getUniqueId());
                }
            }
            e.setCancelled(true);
        }
    }
     */

}
