package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gangs.Gang;
import io.github.lunaiskey.pyrexprison.gangs.GangManager;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

    private static final String ALREADY_IN_GANG = StringUtil.color("&cYou're already in a gang.");

    //private final boolean debug = false; //

    public CommandGang(PyrexPrison plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GangManager gangManager = plugin.getGangManager();
        PlayerManager playerManager = plugin.getPlayerManager();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(StringUtil.color(
                        "&3&lGang Commands:",
                        "&3&l| &f/gang create <name>",
                        "&3&l| &f/gang rename <name>",
                        "&3&l| &f/gang info [name]",
                        "&3&l| &f/gang disband",
                        "&3&l| &f/gang invite <player>",
                        "&3&l| &f/gang join <name>",
                        "&3&l| &f/gang kick <player>",
                        "&3&l| &f/gang leave"
                ));
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (!gangManager.getPlayerGangMap().containsKey(p.getUniqueId())) {
                    if (args.length >= 2) {
                        String name = args[1];
                        int nameLength = name.length();
                        if (nameLength > 3 && nameLength <= 15) {
                            if (name.chars().allMatch(Character::isLetterOrDigit)) {
                                gangManager.createGang(p.getUniqueId(),name);
                            } else {
                                p.sendMessage(StringUtil.color("&cGang name has to be alphanumeric."));
                            }
                        } else {
                            p.sendMessage(StringUtil.color("&cGang name has to be between 3 and 20 characters."));
                        }
                    }
                } else {
                    p.sendMessage(ALREADY_IN_GANG);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("rename")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("info")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("disband")) {

                return true;
            }
            if (args[0].equalsIgnoreCase("invite")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("join")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                Gang gang = gangManager.getGangMap().get(gangManager.getPlayerGangMap().get(p.getUniqueId()));
                if (gang != null) {
                    if (gang.getOwner() == p.getUniqueId()) {
                        p.sendMessage(StringUtil.color("&cPlease either disband your gang or transfer it to another player."));
                    } else {
                        gang
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("kick")) {
                if (args.length >= 2) {
                    String otherPlayerName = args[1];
                    Player otherPlayer =  Bukkit.getPlayer(otherPlayerName);
                    UUID playerUUID;
                    if (otherPlayer == null) {
                        playerUUID = PyrexPrison.getPlugin().getPlayerManager().getPlayerUUID(otherPlayerName);
                    } else {
                        playerUUID = otherPlayer.getUniqueId();
                    }
                    if (playerUUID == null) {
                        p.sendMessage(StringUtil.color("&cCouldn't find player..."));
                        return true;
                    }
                }
                return true;
            }

        }
        return true;
    }
}
