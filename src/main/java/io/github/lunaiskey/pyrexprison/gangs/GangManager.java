package io.github.lunaiskey.pyrexprison.gangs;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class GangManager {

    private Map<UUID, Gang> gangMap = new HashMap<>();
    private Map<String, UUID> gangNameMap = new HashMap<>();

    private Map<UUID, UUID> playerGangMap = new HashMap<>();
    private List<Gang> trophyTop = new ArrayList<>();

    public void loadGangs(){
        File[] gangFiles = new File(PyrexPrison.getPlugin().getDataFolder(), "gangs").listFiles(new IsGangFile());
        assert gangFiles != null;

        for (File file : gangFiles){
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            Map<String,Object> gangMap = fileConf.getConfigurationSection("gangData").getValues(false);
            Map<String,Object> rawMemberMap = fileConf.getConfigurationSection("members").getValues(false);
            Map<UUID,GangRankType> memberMap = new LinkedHashMap<>();

            UUID gangUUID = UUID.fromString(file.getName().replace(".yml",""));
            UUID owner = UUID.fromString((String) gangMap.get("uuid"));
            String name = (String) gangMap.get("name");

            for (String str : rawMemberMap.keySet()) {
                try {
                    UUID memberUUID = UUID.fromString(str);
                    GangRankType rankType = GangRankType.valueOf((String) rawMemberMap.get(str));
                    memberMap.put(memberUUID,rankType);
                } catch (Exception ignored) {}
            }

            long trophies = (long) gangMap.get("trophies");
            addGang(gangUUID, owner, name, memberMap, trophies);
        }
        trophyTop = getTrophyTop();
    }

    public Map<UUID, Gang> getGangMap() {
        return gangMap;
    }

    public Map<String, UUID> getGangNameMap() {
        return gangNameMap;
    }

    public Map<UUID, UUID> getPlayerGangMap() {
        return playerGangMap;
    }

    public List<Gang> getTrophyTop(){
        List<Gang> sortedList = new ArrayList<>(gangMap.values());
        sortedList.sort(Collections.reverseOrder(Comparator.comparingLong(Gang::getTrophies)));
        return sortedList;
    }

    public void addGang(UUID uuid, UUID owner, String name, Map<UUID,GangRankType> members, long trophies) {
        if (members == null) {
            members = new LinkedHashMap<>();
        }
        members.put(owner,GangRankType.OWNER);
        for (UUID member : members.keySet()) {
            playerGangMap.put(member,uuid);
        }
        gangMap.put(uuid,new Gang(uuid,owner,name,members,trophies));
        gangNameMap.put(name,uuid);
    }

    public void addGang(UUID uuid, UUID owner, String name) {
        addGang(uuid,owner,name,new LinkedHashMap<>(),0);
    }

    public void createGang(UUID owner, String name) {
        UUID newUUID;
        do {
            newUUID = UUID.randomUUID();
        } while (gangMap.containsKey(newUUID));
        addGang(newUUID,owner,name);
    }

    public boolean removeGang(UUID uuid) {
        if (gangMap.containsKey(uuid)) {
            Gang gang = gangMap.get(uuid);
            String name = gang.getName();
            gangMap.remove(uuid);
            gangNameMap.remove(name);
            return true;
        } else {
            return false;
        }
    }
    public static class IsGangFile implements FilenameFilter {
        public boolean accept(File file, String s) {
            return s.contains(".yml");
        }
    }

}
