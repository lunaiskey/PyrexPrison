package io.github.lunaiskey.pyrexprison.gangs;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.upgrades.PMineUpgradeType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;


public class Gang {

    private UUID uuid;
    private UUID owner;
    private String name;
    private Map<UUID,GangRankType> members;
    private long trophies;

    private final int maxMembers = 10;

    public Gang(UUID uuid, UUID owner, String name, Map<UUID,GangRankType> members, long trophies){
        this.uuid = uuid;
        this.owner = owner;
        this.name = name;
        this.members = Objects.requireNonNullElseGet(members, HashMap::new);;
        this.trophies = trophies;
    }
    public Gang(UUID uuid, UUID owner, String name){
        this(uuid,owner,name,null,0);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public Map<UUID, GangRankType> getMembers() {
        return members;
    }

    public boolean removeMember(UUID uuid) {
        if (owner != uuid) {
            getMembers().remove(uuid);
            PyrexPrison.getPlugin().getGangManager().getPlayerGangMap().remove(uuid);
            return true;
        } else {
            return false;
        }
    }

    public boolean addMember(UUID uuid) {
        if (!getMembers().containsKey(uuid)) {
            getMembers().put(uuid,GangRankType.MEMBER);
            PyrexPrison.getPlugin().getGangManager().getPlayerGangMap().put(uuid,this.uuid);
            return true;
        } else {
            return false;
        }
    }

    public void setName(String name) {
        PyrexPrison.getPlugin().getGangManager().getGangNameMap().remove(this.name);
        this.name = name.replace(" ","");
        PyrexPrison.getPlugin().getGangManager().getGangNameMap().put(this.name,this.uuid);
    }

    public boolean isFull() {
        return getMembers().size() >= maxMembers;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public long getTrophies() {
        return trophies;
    }

    public void save() {
        File file = new File(PyrexPrison.getPlugin().getDataFolder() + "/gangs/" + uuid + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> gangMap = new LinkedHashMap<>();
        gangMap.put("owner", owner);
        gangMap.put("name", name);
        Map<String, Object> memberMap = new LinkedHashMap<>();
        for (UUID pUUID : members.keySet()) {
            memberMap.put(pUUID.toString(),members.get(pUUID).name());
        }
        gangMap.put("members", members);
        gangMap.put("trophies", trophies);
        data.createSection("gangData", gangMap);
        data.createSection("members",memberMap);
        try {
            data.save(file);
        } catch (IOException e) {
            PyrexPrison.getPlugin().getLogger().severe("Failed to save the " + name + " gang.");
            e.printStackTrace();
        }
    }


}
