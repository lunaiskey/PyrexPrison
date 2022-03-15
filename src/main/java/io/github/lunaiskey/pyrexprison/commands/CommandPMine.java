package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.GridManager;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class CommandPMine implements CommandExecutor {

    private Logger log;

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        log = PyrexPrison.getPlugin().getLogger();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage("Pmine Commands:",
                        "/pmine reset",
                        "/pmine tp",
                        "/pmine fly",
                        "/pmine debug getposition",
                        "/pmine debug getgridposition",
                        "/pmine debug genbedrock (DOESNT NEED TO RUN.)"
                        );
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    PMine mine = GridManager.getPMine(p.getUniqueId());
                    if (mine != null) {
                        p.sendMessage("PMine resetting...");
                        mine.reset();
                    } else {
                        p.sendMessage("PMine not found. Please report this to an admin.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("fly")) {
                    if (p.getLocation().getWorld().getName().equalsIgnoreCase(PMineWorld.getWorldName())) {
                        if (p.getAllowFlight()) {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                            p.sendMessage("[MINE] Flight Disabled");
                        } else {
                            p.setAllowFlight(true);
                            p.setFlying(true);
                            p.sendMessage("[MINE] Flight Enabled");
                        }

                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("tp")) {
                    PMine pMine = GridManager.getPMine(p.getUniqueId());
                    if (pMine == null) {
                        PyrexPrison.getPlugin().getGridManager().newPMine(p.getUniqueId());
                        pMine = GridManager.getPMine(p.getUniqueId());
                        p.teleport(GridManager.getPMine(p.getUniqueId()).getCenter().add(0.5,1,0.5));
                    } else {
                        p.teleport(GridManager.getPMine(p.getUniqueId()).getCenter().add(0.5,1,0.5));
                    }
                    return true;
                }
            } else if (args.length == 2){
                if (args[0].equalsIgnoreCase("debug")) {
                    if (args[1].equalsIgnoreCase("getposition")) {
                        Location l = p.getLocation().clone();
                        p.sendMessage("you are in "+l.getWorld().getName()+" at "+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ());
                        return true;
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
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("genbedrock")) {
                        Location l = p.getLocation().clone();
                        Pair<Integer,Integer> loc = new GridManager().getGridLocation(l);
                        PMine pMine = GridManager.getPMine(loc.getLeft(),loc.getRight());
                        pMine.genBedrock();
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return true;
    }
}
