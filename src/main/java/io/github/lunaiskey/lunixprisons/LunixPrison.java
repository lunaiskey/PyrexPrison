package io.github.lunaiskey.lunixprisons;

import io.github.lunaiskey.lunixprisons.commands.CommandMine;
import io.github.lunaiskey.lunixprisons.listeners.PlayerEvents;
import io.github.lunaiskey.lunixprisons.mines.GlobalMine;
import io.github.lunaiskey.lunixprisons.mines.Mines;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class LunixPrison extends JavaPlugin {

    private static LunixPrison plugin;

    private static Map<String, GlobalMine> mines;
    private int saveTaskID = -1;

    private static class IsMineFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".mine.yml");
        }
    }

    static {
        ConfigurationSerialization.registerClass(GlobalMine.class);
    }

    @Override
    public void onEnable() {
        plugin = this;
        mines = new HashMap<>();

        if (!setupConfig()) {
            this.getLogger().severe("Config files failed to setup correctly, exiting...");
            return;
        }

        new Mines().generateWorld();
        File[] mineFiles = new File(getDataFolder(), "mines").listFiles(new IsMineFile());
        assert mineFiles != null;
        for (File file : mineFiles) {
            this.getLogger().info("Loading mine from file '" + file.getName() + "'...");
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            try {
                Object o = fileConf.get("mine");
                if (!(o instanceof GlobalMine)) {
                    this.getLogger().severe("Mine wasn't a mine object! Something is off with serialization!");
                    continue;
                }
                GlobalMine globalMine = (GlobalMine) o;
                mines.put(globalMine.getName(), globalMine);
                globalMine.reset();
            } catch (Throwable t) {
                t.printStackTrace();
                this.getLogger().severe("Unable to load mine!");
            }
        }
        Bukkit.getPluginCommand("mine").setExecutor(new CommandMine());
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(),this);
        this.getLogger().info(" version " + getDescription().getVersion() + " enabled!");

    }

    @Override
    public void onDisable() {
        save();
        this.getLogger().info("LunixPrisons disabled");
    }

    public static LunixPrison getPlugin() {
        return plugin;
    }

    public void buffSave() {
        BukkitScheduler scheduler = getServer().getScheduler();
        if (saveTaskID!= -1) {
            //Cancel old task
            scheduler.cancelTask(saveTaskID);
        }
        //Schedule save
        scheduler.scheduleSyncDelayedTask(LunixPrison.getPlugin(), this::save, 60 * 20L);
    }

    private void save() {
        for (GlobalMine globalMine : mines.values()) {
            File mineFile = getMineFile(globalMine);
            FileConfiguration mineConf = YamlConfiguration.loadConfiguration(mineFile);
            mineConf.set("mine", globalMine);
            try {
                mineConf.save(mineFile);
            } catch (IOException e) {
                this.getLogger().severe("Unable to serialize mine.");
                e.printStackTrace();
            }
        }
    }

    private File getMineFile(GlobalMine globalMine) {
        return new File(new File(getDataFolder(), "mines"), globalMine.getName().replace(" ", "") + ".mine.yml");
    }

    private boolean setupConfig() {
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists() && !pluginFolder.mkdir()) {
            this.getLogger().severe("Could not make plugin folder.");
            return false;
        }
        File mineFolder = new File(getDataFolder(), "mines");
        if (!mineFolder.exists() && !mineFolder.mkdir()) {
            this.getLogger().severe("Could not make mine folder.");
            return false;
        }
        /*
        try {
            Config.initConfig(getDataFolder());
        } catch (IOException e) {
            this.getLogger().severe("Could not make config file!");
            e.printStackTrace();
            return false;
        }

         */
        return true;
    }

    public static Map<String, GlobalMine> getMines() {
        return mines;
    }
}
