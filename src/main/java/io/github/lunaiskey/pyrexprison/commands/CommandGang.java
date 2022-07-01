package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gangs.GangManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class CommandGang implements CommandExecutor {

    private PyrexPrison plugin;
    private Logger log;
    private final boolean debug = false;

    public CommandGang(PyrexPrison plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 2){
                switch (args[0].toLowerCase()){
                    //gang create <name>
                    case "create":
                        if (!GangManager.getGangsMap().containsKey(p.getUniqueId())) {
                            if (!args[1].contains(" ")) {
                                    GangManager.newGang(p.getUniqueId(), args[1]);
                            }
                        }
                    case "delete":
                        if (GangManager.getGangsMap().containsKey(p.getUniqueId())){

                        }

                }
            }
        }
        return true;
    }
}
