package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.player.armor.ArmorInv;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandArmor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(new ArmorInv(p).getInv());
        }
        return true;
    }
}
