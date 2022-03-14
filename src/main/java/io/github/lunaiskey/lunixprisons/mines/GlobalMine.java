package io.github.lunaiskey.lunixprisons.mines;

import java.util.*;
import java.util.logging.Logger;

import io.github.lunaiskey.lunixprisons.LunixPrison;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class GlobalMine implements ConfigurationSerializable {

    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;
    private String name;
    private World world;

    public GlobalMine(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, String name, World world) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.name = name;
        this.world = world;
    }



    public GlobalMine(Map<String,Object> me) {
        try {
            minX = (Integer) me.get("minX");
            minY = (Integer) me.get("minY");
            minZ = (Integer) me.get("minZ");
            maxX = (Integer) me.get("maxX");
            maxY = (Integer) me.get("maxY");
            maxZ = (Integer) me.get("maxZ");
        } catch (Throwable t){
            throw new IllegalArgumentException("Error deserializing coordinate pairs");
        }
        try {
            world = Bukkit.getServer().getWorld((String) me.get("world"));
        } catch (Throwable t) {
            throw new IllegalArgumentException("Error finding world");
        }
        if (world == null) {
            Logger l = LunixPrison.getPlugin().getLogger();
            l.severe("Unable to find a world!");
            l.severe("Attempted to load world named: " + me.get("world"));
            l.severe("Worlds listed: " + Bukkit.getWorlds());
            throw new IllegalArgumentException("World was null!");
        }
        name = (String) me.get("name");
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public void redefine(int minX,int minY,int minZ,int maxX,int maxY, int maxZ,String name, World world) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.name = name;
        this.world = world;
    }

    private transient boolean resetting = false;

    public boolean reset() {
        if (resetting) {
            return false;
        }
        if (!resetting) {
            resetting = true;
        }
        Bukkit.getScheduler().runTask(LunixPrison.getPlugin(),() -> {
            boolean test = false;
            int temp;
            if (minX > maxX) {temp = maxX;maxX=minX;minX=temp;}
            if (minY > maxY) {temp = maxY;maxY=minY;minY=temp;}
            if (minZ > maxZ) {temp = maxZ;maxZ=minZ;minZ=temp;}
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        Block b = world.getBlockAt(x,y,z);
                        switch (new Random().nextInt(2)+1) {
                            case 1 -> b.setType(Material.STONE);
                            case 2 -> b.setType(Material.DIORITE);
                        }
                    }
                }
            }

        });
        resetting = false;
        return true;

    }

    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> me = new LinkedHashMap<>();
        me.put("minX",minX);
        me.put("minY",minY);
        me.put("minZ",minZ);
        me.put("maxX",maxX);
        me.put("maxY",maxY);
        me.put("maxZ",maxZ);
        me.put("name",name);
        me.put("world", world.getName());
        return me;
    }
}
