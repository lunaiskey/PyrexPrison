package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.commands.*;
import org.bukkit.Bukkit;

public class FunctionManager {
    private static final PyrexPrison plug = PyrexPrison.getPlugin();

    public void registerCommands() {
        //Bukkit.getPluginCommand("mine").setExecutor(new CommandMine());
        Bukkit.getPluginCommand("pmine").setExecutor(new CommandPMine(plug));
        Bukkit.getPluginCommand("tokens").setExecutor(new CommandTokens(plug));
        Bukkit.getPluginCommand("gems").setExecutor(new CommandGems(plug));
        Bukkit.getPluginCommand("pyrexpoints").setExecutor(new CommandPyrexPoints(plug));
        Bukkit.getPluginCommand("enchants").setExecutor(new CommandEnchant());
        Bukkit.getPluginCommand("rankup").setExecutor(new CommandRankup());
        Bukkit.getPluginCommand("armor").setExecutor(new CommandArmor());
        Bukkit.getPluginCommand("getpickaxe").setExecutor(new CommandGetPickaxe());
        Bukkit.getPluginCommand("multiplier").setExecutor(new CommandMultiplier(plug));
        Bukkit.getPluginCommand("gemstones").setExecutor(new CommandGemstones());
        Bukkit.getPluginCommand("pitem").setExecutor(new CommandPItem());
        Bukkit.getPluginCommand("viewplayer").setExecutor(new CommandViewPlayer());
    }
}
