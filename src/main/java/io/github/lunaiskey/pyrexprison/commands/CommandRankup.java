package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.Rankup;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class CommandRankup implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PyrexPlayer player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            Rankup rankup = new Rankup();
            BigInteger cost = rankup.getLevelCost(player.getRank()+1);
            if (player.getTokens().compareTo(cost) >= 0) {
                int newLevel = rankup.rankup(p);
                int nextLevel = newLevel+1;
                p.sendMessage(
                        StringUtil.color("&b&lYou have ranked up to &f&l"+newLevel+"&b&l!"),
                        StringUtil.color(" &3&l- &bNext Rankup: &f"+nextLevel),
                        StringUtil.color(" &3&l- &bCost: "+CurrencyType.getColorCode(CurrencyType.TOKENS)+CurrencyType.getUnicode(CurrencyType.TOKENS)+"&f"+ Numbers.formattedNumber(rankup.getLevelCost(nextLevel)))
                );
            } else {
                p.sendMessage(StringUtil.color("&7You still need "+Numbers.formattedNumber(cost.subtract(player.getTokens()))+"to rankup."));
            }

        }


        return true;
    }
}
