package io.github.lunaiskey.lunixprisons.commands;

import io.github.lunaiskey.lunixprisons.mines.GridManager;
import io.github.lunaiskey.lunixprisons.mines.PMine;
import io.github.lunaiskey.lunixprisons.mines.generator.PMineWorld;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPMine implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("reset")) {
                PMine mine = GridManager.getPMine(p.getUniqueId());
                if (mine != null) {
                    p.sendMessage("PMine resetting...");
                    mine.reset();
                } else {
                    p.sendMessage("PMine not found. Please report this to an admin.");
                }
            }
            if (args[0].equalsIgnoreCase("fly")) {
                if (p.getLocation().getWorld().getName().equalsIgnoreCase(PMineWorld.getWorldName())) {
                    if (p.getAllowFlight()) {
                        p.setAllowFlight(false);
                        p.setFlying(false);
                    } else {
                        p.setAllowFlight(true);
                        p.setFlying(true);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("tp")) {
                p.teleport(GridManager.getPMine(p.getUniqueId()).getCenter().add(0.5,1,0.5));
            }
            if (args[0].equalsIgnoreCase("debug")) {
                if (args[1].equalsIgnoreCase("getposition")) {
                    Location l = p.getLocation().clone();
                    p.sendMessage("you are in "+l.getWorld().getName()+" at "+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ());
                }
                if (args[1].equalsIgnoreCase("getgridposition")) {
                    Location l = p.getLocation().clone();
                    Pair<Integer,Integer> loc = new GridManager().getGridLocation(l);
                    PMine pMine = GridManager.getPMine(loc.getLeft(),loc.getRight());
                    if (pMine != null && l.getWorld().getName().equalsIgnoreCase("mines")) {
                        p.sendMessage("Owner: "+pMine.getOwner());
                    } else {
                        p.sendMessage("Owner: No-One");
                    }
                    p.sendMessage(loc.getLeft()+","+loc.getRight());
                }
                if (args[1].equalsIgnoreCase("genbedrock")) {
                    Location l = p.getLocation().clone();
                    Pair<Integer,Integer> loc = new GridManager().getGridLocation(l);
                    PMine pMine = GridManager.getPMine(loc.getLeft(),loc.getRight());
                    pMine.genBedrock();
                }
            }
        }
        return true;
    }
}
