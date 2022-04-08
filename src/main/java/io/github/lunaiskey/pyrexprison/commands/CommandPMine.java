package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.PMineInv;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import io.github.lunaiskey.pyrexprison.util.TimeUtil;
import org.bukkit.Bukkit;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class CommandPMine implements CommandExecutor {

    private PyrexPrison plugin;
    private Logger log;
    private final boolean debug = false;
    private static Map<UUID,Long> resetCooldown = new HashMap<>();

    public CommandPMine(PyrexPrison plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.openInventory(new PMineInv().getInv());
                return true;
            }
            if (args[0].equalsIgnoreCase("reset")) {
                PMine mine = PMineManager.getPMine(p.getUniqueId());
                if (p.hasPermission("pyrex.resetcooldown.bypass") || !resetCooldown.containsKey(p.getUniqueId()) || System.currentTimeMillis() >= resetCooldown.get(p.getUniqueId())+300000) {
                    if (mine != null) {
                        p.sendMessage("PMine resetting...");
                        mine.reset();
                        resetCooldown.put(p.getUniqueId(),System.currentTimeMillis());
                    } else {
                        p.sendMessage("PMine not found. Please report this to an admin.");
                    }
                } else {
                    p.sendMessage("Reset is on Cooldown: "+ TimeUtil.countdown(resetCooldown.get(p.getUniqueId())+300000));
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
                p.teleport(PMineManager.getPMine(p.getUniqueId()).getCenter().add(0.5,1,0.5));
                p.sendMessage("Teleporting to mine...");
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage("PMine Commands:",
                        "| /pmine reset",
                        "| /pmine tp",
                        "| /pmine fly",
                        "| /pmine menu");
                if (p.hasPermission("pyrex.debug")) {
                    p.sendMessage("| /pmine debug getposition",
                            "| /pmine debug getgridposition"
                            //"| /pmine debug genbedrock (DOESNT NEED TO RUN.)"
                    );
                }
                return true;
            }
            if (p.hasPermission("pyrex.debug")) {
                if (args[0].equalsIgnoreCase("debug")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("getposition")) {
                            Location l = p.getLocation().clone();
                            p.sendMessage("you are in " + l.getWorld().getName() + " at " + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("getgridposition")) {
                            Location l = p.getLocation().clone();
                            Pair<Integer, Integer> loc = new PMineManager().getGridLocation(l);
                            PMine pMine = PMineManager.getPMine(loc.getLeft(), loc.getRight());
                            if (pMine != null && l.getWorld().getName().equalsIgnoreCase("mines")) {
                                p.sendMessage("Owner: " + Bukkit.getOfflinePlayer(pMine.getOwner()).getName());
                            } else {
                                p.sendMessage("Owner: No-One");
                            }
                            p.sendMessage(loc.getLeft() + "," + loc.getRight());
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("genbedrock")) {
                            if (debug) {
                                Location l = p.getLocation().clone();
                                Pair<Integer, Integer> loc = new PMineManager().getGridLocation(l);
                                PMine pMine = PMineManager.getPMine(loc.getLeft(), loc.getRight());
                                pMine.genBedrock();
                            }
                            return true;
                        }
                    }
                }
            } else {
                p.sendMessage(StringUtil.color("&cNo Permission."));
                return true;
            }
            p.sendMessage(StringUtil.color("Invalid Arguments."));
            return true;

        }
        return true;
    }

    public static Map<UUID, Long> getResetCooldown() {
        return resetCooldown;
    }
}
