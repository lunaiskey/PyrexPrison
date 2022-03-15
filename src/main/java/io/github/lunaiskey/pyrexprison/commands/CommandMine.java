package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.GlobalMine;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Deprecated
public class CommandMine implements CommandExecutor {
    private static Map<UUID, MutableTriple<String,Location,Location>> createMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                return true;
            }
            if (args[0].equalsIgnoreCase("createtest")) {
                sender.sendMessage("Creating test mine");
                PyrexPrison.getMines().put("test",new GlobalMine(0,125,0,50,175,50,"test", Bukkit.getWorld("world")));
            }
            if (args[0].equalsIgnoreCase("create")) {
                sender.sendMessage("Creating '"+args[1]+"' mine");
                createMap.put(p.getUniqueId(),new MutableTriple<>(args[1],null,null));
                p.sendMessage("Please select the opposite positions of the mine by left and then right clicking.");
            }
            if (args[0].equalsIgnoreCase("reset")) {
                GlobalMine globalMine = PyrexPrison.getMines().get(args[1]);
                if (globalMine != null) {
                    if (globalMine.reset()) {
                        sender.sendMessage("Mine '"+ globalMine.getName()+"' has been successfully reset.");
                    } else {
                        sender.sendMessage("Mine '"+ globalMine.getName()+"' is still resetting...");
                    }

                } else {
                    sender.sendMessage("Mine '"+args[1]+"' doesn't exist.");
                }
            }
        } else if (sender instanceof ConsoleCommandSender){
            sender.sendMessage("Console isn't currently supported. try again ingame.");
        }
        return true;
    }

    public static Map<UUID, MutableTriple<String,Location,Location>> getCreateMap() {
        return createMap;
    }

    public static boolean checkValidMineCoords(Location pos1, Location pos2) {
        if (pos1 != null && pos2 != null) {
            return pos1.getWorld() == pos2.getWorld();
        }
        return false;
    }
}
