package io.github.lunaiskey.pyrexprison.modules.pickaxe.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandResetPickaxe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            pyrexPlayer.setPickaxe(new PyrexPickaxe(p.getUniqueId()));
            p.sendMessage("Reset your pickaxe.");
            PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
        }
        return true;
    }
}
