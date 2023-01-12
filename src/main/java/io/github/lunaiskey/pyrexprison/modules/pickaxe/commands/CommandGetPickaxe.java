package io.github.lunaiskey.pyrexprison.modules.pickaxe.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandGetPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (PyrexPrison.getPlugin().getPickaxeHandler().hasOrGivePickaxe(p)) {
                p.sendMessage(StringUtil.color("&cYou already have a pickaxe."));
            }
        }
        return true;
    }
}
