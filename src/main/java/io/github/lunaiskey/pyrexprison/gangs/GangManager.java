package io.github.lunaiskey.pyrexprison.gangs;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.upgrades.PMineUpgradeType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigInteger;
import java.util.*;

public class GangManager {

    private static Map<UUID, Gang> gangsMap = new HashMap<>();
    private List<Gang> trophyTop = new ArrayList<>();
    private static Set<Gang> activeGangs = new HashSet<>();

    //This isn't done
    public void loadGangs(){
        File[] gangFiles = new File(PyrexPrison.getPlugin().getDataFolder(), "gangs").listFiles(new GangManager.IsGangFile());
        assert gangFiles != null;

        for (File file : gangFiles){
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            Map<String,Object> gangMap = fileConf.getConfigurationSection("gang").getValues(false);
            int ID = Integer.parseInt(file.getName().replace(".yml", ""));
            UUID owner = UUID.fromString(String.valueOf(gangMap.get("owner")));
            String name = gangMap.get("name").toString();
            Set<UUID> coOwners = Collections.singleton(UUID.fromString(String.valueOf(gangMap.get("coOwners"))));
            Set<UUID> mods = Collections.singleton(UUID.fromString(String.valueOf(gangMap.get("mods"))));
            Set<UUID> members = Collections.singleton(UUID.fromString(String.valueOf(gangMap.get("members"))));
            BigInteger trophies = (BigInteger) gangMap.get("trophies");

            newGang(owner, name, coOwners, mods, members, trophies, ID);
        }
        trophyTop = getTrophyTop();
    }

    public static Map<UUID, Gang> getGangsMap(){
        return gangsMap;
    }
    public static Set<Gang> getActiveGangs(){
        return activeGangs;
    }


    //This might work?
    public static List<Gang> getTrophyTop(){
        Map<UUID, PyrexPlayer> playerMap = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap();
        List<Gang> sortedList = new ArrayList<>(activeGangs);
        sortedList.sort(Collections.reverseOrder(Comparator.comparing(o -> gangsMap.get(o).getTrophies())));
        return sortedList;
    }


    public static void newGang(UUID owner, String name ){
        if (!gangsMap.containsKey(owner)){
            Gang nGang = new Gang(owner, name);
            gangsMap.put(owner, nGang);
            activeGangs.add(nGang);
        }
    }
    public static void newGang(UUID owner, String name, Set<UUID> gCoOwners, Set<UUID> gMods,
                        Set<UUID> gMembers, BigInteger gTrophies, int gID){
        if (!gangsMap.containsKey(owner)){
            Gang nGang = new Gang(owner, name, gCoOwners, gMods, gMembers, gTrophies, gID);
            gangsMap.put(owner, nGang);
            activeGangs.add(nGang);
        }

    }


    public void deleteGang(UUID owner){
        gangsMap.remove(owner);
        activeGangs.remove(gangsMap.get(owner));
    }

    public static class IsGangFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".yml");
        }
    }

}
