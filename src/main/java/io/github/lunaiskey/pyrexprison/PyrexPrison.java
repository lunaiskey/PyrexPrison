package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.gangs.Gang;
import io.github.lunaiskey.pyrexprison.gangs.GangManager;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.items.ItemManager;
import io.github.lunaiskey.pyrexprison.leaderboards.LeaderboardStorage;
import io.github.lunaiskey.pyrexprison.listeners.PlayerEvents;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.pickaxe.PickaxeHandler;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.boosters.Boosters;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.*;

public final class PyrexPrison extends JavaPlugin {

    private static PyrexPrison plugin;
    private PMineManager pmineManager;
    private PlayerManager playerManager;
    private PickaxeHandler pickaxeHandler;
    private ItemManager itemManager;
    private GangManager gangManager;
    private LeaderboardStorage leaderboardStorage;
    private Random rand = new Random();
    private final Set<UUID> savePending = new HashSet<>();

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
        itemManager = new ItemManager();
        leaderboardStorage = new LeaderboardStorage();
        gangManager = new GangManager();

        playerManager.loadPlayers();
        pmineManager.loadPMines();
        gangManager.loadGangs();
        itemManager.registerItems();
        new CommandManager().registerCommands();
        new Boosters().scheduleTask();

        checkPlayerData();
        leaderboardStorage.startTasks();
        buffSave();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PyrexExpansion(this).register();
        }

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this),this);
        this.getLogger().info("version " + getDescription().getVersion() + " enabled!");

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(saveTaskID);
        closeAllPyrexInvs();
        saveAll();
        this.getLogger().info("Saved "+savePending.size()+" Players.");
    }

    public static PyrexPrison getPlugin() {
        return plugin;
    }

    private void closeAllPyrexInvs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof PyrexHolder) {
                player.closeInventory();
            }
        }
    }

    public void buffSave() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        if (saveTaskID != -1) {
            //Cancel old task
            scheduler.cancelTask(saveTaskID);
        }
        //Schedule save
        saveTaskID = scheduler.scheduleSyncRepeatingTask(PyrexPrison.getPlugin(), this::saveAll, 5 * 60 * 20L,5 * 60 * 20L);
        this.getLogger().info("Buffered a save task to happen in 5 minutes.");

    }

    private void save(UUID player, boolean slient) {
        if (PyrexPrison.getPlugin().getPmineManager().getPMine(player) != null) {
            PyrexPrison.getPlugin().getPmineManager().getPMine(player).save();
        }
        if (playerManager.getPlayerMap().get(player) != null) {
            playerManager.getPlayerMap().get(player).save();
        }
    }

    private void saveAll() {
        for (UUID uuid : savePending) {
            save(uuid,true);
            savePending.removeIf(n -> (Bukkit.getPlayer(n) == null));
        }
        for (Gang gang : gangManager.getGangMap().values()) {
            gang.save();
        }
        this.getLogger().info("Saving Player, Mine and Gang data...");
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
        File gangFolder = new File(getDataFolder(), "gangdata");
        if (!gangFolder.exists() && !gangFolder.mkdir()) {
            this.getLogger().severe("Could not make GangData folder.");
            return false;
        }
        return true;
    }

    private void checkPlayerData() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!playerManager.getPlayerMap().containsKey(p.getUniqueId())) {
                playerManager.createPyrexPlayer(p.getUniqueId());
            } else {
                playerManager.getPlayerMap().get(p.getUniqueId()).setName(p.getName());
            }
            if (PyrexPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId()) == null) {
                pmineManager.newPMine(p.getUniqueId());
            }
            savePending.add(p.getUniqueId());
        }
    }


    public PMineManager getPmineManager() {
        return pmineManager;
    }

    public GangManager getGangManager() { return gangManager;}

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PickaxeHandler getPickaxeHandler() {
        return pickaxeHandler;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public Random getRand() {
        return rand;
    }

    public Set<UUID> getSavePending() {
        return savePending;
    }

    public LeaderboardStorage getLeaderboardStorage() {
        return leaderboardStorage;
    }
}
