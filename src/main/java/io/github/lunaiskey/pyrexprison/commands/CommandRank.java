package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandRank implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            if (args.length == 0) {
                p.sendMessage(StringUtil.color("&bYour rank is: &f"+pyrexPlayer.getRank()));
                return true;
            }
            Player otherPlayer = Bukkit.getPlayer(args[0]);
            if (args.length == 1) {
                if (otherPlayer != null) {
                    PyrexPlayer otherPyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                    p.sendMessage(StringUtil.color("&b"+otherPlayer.getName()+"'s rank is: &f"+otherPyrexPlayer.getRank()));
                } else {
                    p.sendMessage(StringUtil.color("&cPlayer "+args[0]+" isn't online."));
                }
                return true;
            }
            if (otherPlayer != null) {
                if (p.hasPermission("pyrex.rank.set")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        if (args.length >= 3) {
                            try {
                                int rank = Integer.parseInt(args[2]);
                                PyrexPlayer otherPyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                                otherPyrexPlayer.setRank(rank);
                                p.sendMessage(StringUtil.color("&aSuccessfully set "+otherPlayer.getName()+"'s rank to "+rank+"."));
                            } catch (Exception ignored) {
                                p.sendMessage(StringUtil.color("&cInvalid number."));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
