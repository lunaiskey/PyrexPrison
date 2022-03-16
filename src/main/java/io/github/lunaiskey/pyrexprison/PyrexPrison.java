package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.commands.CommandMine;
import io.github.lunaiskey.pyrexprison.commands.CommandPMine;
import io.github.lunaiskey.pyrexprison.listeners.PlayerEvents;
import io.github.lunaiskey.pyrexprison.mines.GlobalMine;
import io.github.lunaiskey.pyrexprison.mines.GridManager;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.mines.PMine;
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
import java.util.UUID;

public final class PyrexPrison extends JavaPlugin {

    private static PyrexPrison plugin;
    private GridManager gridManager;

    private static Map<String, GlobalMine> mines;
    private int saveTaskID = -1;

    private static class IsMineFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".mine.yml");
        }
    }

    private static class IsPMineFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".yml");
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

        new PMineWorld().generateWorld();
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
        gridManager = new GridManager();
        loadPMines();

        //Bukkit.getPluginCommand("mine").setExecutor(new CommandMine());
        Bukkit.getPluginCommand("pmine").setExecutor(new CommandPMine());
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this),this);
        this.getLogger().severe("MINES AND PMINES ARE CURRENTLY IN AN UNSAFE STATE.");
        this.getLogger().severe("MINES NEED TO BE SECURED, CURRENTLY ANYONE CAN CREATE MINES");
        this.getLogger().severe("Mines and PMines commands arent arg length checked. will be fixed later.");
        this.getLogger().info(" version " + getDescription().getVersion() + " enabled!");

    }

    @Override
    public void onDisable() {
        save();
        this.getLogger().info("LunixPrisons disabled");
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
        for (PMine pMine : GridManager.getPMinesMap().values()) {
            pMine.save();
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
        File pmineFolder = new File(getDataFolder(), "pmines");
        if (!pmineFolder.exists() && !pmineFolder.mkdir()) {
            this.getLogger().severe("Could not make pmine folder.");
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

    private void loadPMines() {
        File[] pmineFiles = new File(getDataFolder(), "pmines").listFiles(new IsPMineFile());
        assert pmineFiles != null;
        for (File file : pmineFiles) {
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            Map<String,Object> map = fileConf.getConfigurationSection("mine").getValues(false);
            UUID owner = UUID.fromString(file.getName().replace(".yml",""));
            int chunkX = (int) map.get("chunkX");
            int chunkZ = (int) map.get("chunkZ");
            GridManager.newPMine(owner,chunkX,chunkZ);
            if (Bukkit.getPlayer(owner) == null || !Bukkit.getPlayer(owner).isOnline()) {
                continue;
            } else {
                GridManager.getPMine(owner).reset();
            }
        }
    }

    public static Map<String, GlobalMine> getMines() {
        return mines;
    }

    public GridManager getGridManager() {
        return gridManager;
    }
}
