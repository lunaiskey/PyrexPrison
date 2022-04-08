package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.listeners.PlayerEvents;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.pickaxe.PickaxeHandler;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public final class PyrexPrison extends JavaPlugin {

    private static PyrexPrison plugin;
    private PMineManager pmineManager;
    private PlayerManager playerManager;
    private PickaxeHandler pickaxeHandler;
    private Random rand = new Random();

    private int saveTaskID = -1;

    @Override
    public void onEnable() {
        plugin = this;

        if (!setupConfig()) {
            this.getLogger().severe("Directories failed to setup correctly, exiting...");
            return;
        }

        new PMineWorld().generateWorld();

        pmineManager = new PMineManager();
        playerManager = new PlayerManager();
        pickaxeHandler = new PickaxeHandler();
        pmineManager.loadPMines();
        playerManager.loadPlayers();

        new FunctionManager().registerCommands();

        checkPlayerData();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PyrexExpansion(this).register();
        }

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this),this);
        this.getLogger().info("version " + getDescription().getVersion() + " enabled!");


    }

    @Override
    public void onDisable() {
        save();
        this.getLogger().info("saving data...");
    }

    public static PyrexPrison getPlugin() {
        return plugin;
    }

    public void buffSave() {
        BukkitScheduler scheduler = getServer().getScheduler();
        if (saveTaskID!= -1) {
            //Cancel old task
            scheduler.cancelTask(saveTaskID);
        }
        //Schedule save
        scheduler.scheduleSyncDelayedTask(PyrexPrison.getPlugin(), this::save, 60 * 20L);
    }

    private void save() {
        for (PMine pMine : PMineManager.getPMinesMap().values()) {
            pMine.save();
        }
        for (PyrexPlayer player : playerManager.getPlayerMap().values()) {
            player.save();
        }

    }

    private boolean setupConfig() {
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists() && !pluginFolder.mkdir()) {
            this.getLogger().severe("Could not make plugin folder.");
            return false;
        }
        File pmineFolder = new File(getDataFolder(), "pmines");
        if (!pmineFolder.exists() && !pmineFolder.mkdir()) {
            this.getLogger().severe("Could not make PMine folder.");
            return false;
        }
        File playerFolder = new File(getDataFolder(), "playerdata");
        if (!playerFolder.exists() && !playerFolder.mkdir()) {
            this.getLogger().severe("Could not make PlayerData folder.");
            return false;
        }
        return true;
    }

    private void checkPlayerData() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!playerManager.getPlayerMap().containsKey(p.getUniqueId())) {
                playerManager.createPyrexPlayer(p.getUniqueId());
            }
            if (PMineManager.getPMine(p.getUniqueId()) == null) {
                pmineManager.newPMine(p.getUniqueId());
            }
        }
    }


    public PMineManager getPmineManager() {
        return pmineManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PickaxeHandler getPickaxeHandler() {
        return pickaxeHandler;
    }

    public Random getRand() {
        return rand;
    }
}
