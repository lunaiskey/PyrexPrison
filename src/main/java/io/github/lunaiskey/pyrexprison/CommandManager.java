package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.modules.armor.commands.CommandArmor;
import io.github.lunaiskey.pyrexprison.modules.boosters.commands.CommandBooster;
import io.github.lunaiskey.pyrexprison.modules.gangs.commands.CommandGang;
import io.github.lunaiskey.pyrexprison.modules.items.commands.CommandGemstones;
import io.github.lunaiskey.pyrexprison.modules.items.commands.CommandPItem;
import io.github.lunaiskey.pyrexprison.modules.leaderboards.commands.CommandLeaderboard;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.commands.CommandEnchant;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.commands.CommandGetPickaxe;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.commands.CommandResetPickaxe;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.modules.player.commands.*;
import io.github.lunaiskey.pyrexprison.modules.pmines.commands.CommandPMine;
import org.bukkit.Bukkit;

public class CommandManager {
    private static final PyrexPrison plug = PyrexPrison.getPlugin();

    public void registerCommands() {
        //Bukkit.getPluginCommand("mine").setExecutor(new CommandMine());
        Bukkit.getPluginCommand("pmine").setExecutor(new CommandPMine(plug));
        Bukkit.getPluginCommand("pmine").setTabCompleter(new CommandPMine(plug));

        Bukkit.getPluginCommand("tokens").setExecutor(new CommandCurrency(plug, CurrencyType.TOKENS));
        Bukkit.getPluginCommand("gems").setExecutor(new CommandCurrency(plug, CurrencyType.GEMS));
        Bukkit.getPluginCommand("pyrexpoints").setExecutor(new CommandCurrency(plug, CurrencyType.PYREX_POINTS));

        Bukkit.getPluginCommand("tokens").setTabCompleter(new CommandCurrency(plug, CurrencyType.TOKENS));
        Bukkit.getPluginCommand("gems").setTabCompleter(new CommandCurrency(plug, CurrencyType.GEMS));
        Bukkit.getPluginCommand("pyrexpoints").setTabCompleter(new CommandCurrency(plug, CurrencyType.PYREX_POINTS));

        Bukkit.getPluginCommand("enchants").setExecutor(new CommandEnchant());
        Bukkit.getPluginCommand("rankup").setExecutor(new CommandRankup());
        Bukkit.getPluginCommand("armor").setExecutor(new CommandArmor());
        Bukkit.getPluginCommand("getpickaxe").setExecutor(new CommandGetPickaxe());
        Bukkit.getPluginCommand("multiplier").setExecutor(new CommandMultiplier(plug));
        Bukkit.getPluginCommand("gemstones").setExecutor(new CommandGemstones());
        Bukkit.getPluginCommand("booster").setExecutor(new CommandBooster());

        Bukkit.getPluginCommand("pitem").setExecutor(new CommandPItem());
        Bukkit.getPluginCommand("pitem").setExecutor(new CommandPItem());

        Bukkit.getPluginCommand("viewplayer").setExecutor(new CommandViewPlayer());
        Bukkit.getPluginCommand("rank").setExecutor(new CommandRank());
        Bukkit.getPluginCommand("resetpickaxe").setExecutor(new CommandResetPickaxe());
        Bukkit.getPluginCommand("leaderboard").setExecutor(new CommandLeaderboard());

        Bukkit.getPluginCommand("gang").setExecutor(new CommandGang(plug));
    }
}
