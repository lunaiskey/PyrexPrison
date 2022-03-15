package io.github.lunaiskey.pyrexprison.listeners;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.commands.CommandMine;
import io.github.lunaiskey.pyrexprison.mines.GlobalMine;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerEvents implements Listener {

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

}
