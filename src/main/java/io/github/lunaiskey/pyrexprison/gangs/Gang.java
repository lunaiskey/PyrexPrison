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


public class Gang{
    private UUID owner;
    private String name;
    private Set<UUID> coOwners;
    private Set<UUID> mods;
    private Set<UUID> members;
    private BigInteger trophies;
    private int nextID;
    private int ID;

    public Gang(UUID gOwner, String gName){
        ID = nextID;
        owner = gOwner;
        name = gName;
        coOwners = Objects.requireNonNullElseGet(coOwners, HashSet::new);
        mods = Objects.requireNonNullElseGet(mods, HashSet::new);
        members = Objects.requireNonNullElseGet(members, HashSet::new);
        trophies = BigInteger.valueOf(0);
        nextID++;
    }

    public Gang(UUID gOwner, String gName, Set<UUID> gCoOwners, Set<UUID> gMods, Set<UUID> gMembers,
                BigInteger gTrophies, int gID){
        ID = gID;
        owner = gOwner;
        name = gName;
        coOwners = gCoOwners;
        mods = gMods;
        members = gMembers;
        trophies = gTrophies;
        nextID++;

    }

    public UUID getOwner(){ return owner;}
    public String getName(){ return name; }
    public Set<UUID> getCoOwners(){ return coOwners; }
    public Set<UUID> getMods(){ return mods; }
    public Set<UUID> getMembers(){ return members; }
    public BigInteger getTrophies() { return trophies; }

    public boolean addMember(UUID player){
        return members.add(player);
    }
    public boolean addMod(UUID player){
        return mods.add(player);
    }
    public boolean addCoOwner(UUID player){
        addMod(player);
        return coOwners.add(player);
    }


    public boolean removeCoOwner(UUID player){
        return coOwners.remove(player);
    }
    public boolean removeMod(UUID player){
        removeCoOwner(player);
        return mods.remove(player);
    }
    public boolean removeMember(UUID player){
        removeMod(player);
        return members.remove(player);
    }

    //Might not work?
    public void save() {
        File file = new File(PyrexPrison.getPlugin().getDataFolder() + "/gangs/" + ID + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> gangMap = new LinkedHashMap<>();
        gangMap.put("owner", owner);
        gangMap.put("name", name);
        gangMap.put("coOwners", coOwners);
        gangMap.put("mods", mods);
        gangMap.put("members", members);
        gangMap.put("trophies", trophies);
        data.createSection("gang", gangMap);

        try {
            data.save(file);
        } catch (IOException e) {
            PyrexPrison.getPlugin().getLogger().severe("Failed to save the " + name + " gang.");
            e.printStackTrace();
        }
    }


}
