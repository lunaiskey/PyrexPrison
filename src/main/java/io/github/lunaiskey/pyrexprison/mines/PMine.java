package io.github.lunaiskey.pyrexprison.mines;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.generator.PMineWorld;
import io.github.lunaiskey.pyrexprison.nms.NMSBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PMine {

    private int chunkX;
    private int chunkZ;
    private UUID owner;
    //mine coordinatess.
    private World world;
    private Location center;
    private Location min;
    private Location max;
    private int mineSize;

    public PMine(UUID owner, int chunkX, int chunkZ) {
        this.owner = owner;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.mineSize = 12;
        world = Bukkit.getWorld(PMineWorld.getWorldName());
        this.center = new Location(world, (225 * chunkX), 100, (225 * chunkZ));
        min = new Location(center.getWorld(), center.getBlockX() - mineSize, 50, center.getBlockZ() - mineSize);
        max = new Location(center.getWorld(), center.getBlockX() + mineSize, 100, center.getBlockZ() + mineSize);
        genBedrock();
        reset();
    }

    public UUID getOwner() {
        return owner;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public Location getCenter() {
        return center.clone();
    }

    public void save() {
        File file = new File(PyrexPrison.getPlugin().getDataFolder() + "/pmines/" + owner + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("chunkX", chunkX);
        map.put("chunkZ", chunkZ);
        data.createSection("mine", map);
        try {
            data.save(file);
        } catch (IOException e) {
            PyrexPrison.getPlugin().getLogger().severe("Failed to save " + owner + "'s Pmine.");
            e.printStackTrace();
        }
    }

    private boolean resetting = false;

    public boolean reset() {
        if (resetting) {
            return false;
        }
        if (!resetting) {
            resetting = true;
        }

        Random rand = new Random();
        Player p = Bukkit.getPlayer(owner);
        NMSBlockChange NMSBlockChange = new NMSBlockChange(world, ((CraftWorld) world).getHandle());
        for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    Material mat = null;
                    switch (rand.nextInt(4) + 1) {
                        case 1 -> mat = Material.STONE;
                        case 2 -> mat = Material.GRANITE;
                        case 3 -> mat = Material.ANDESITE;
                        case 4 -> mat = Material.DIORITE;
                    }
                    NMSBlockChange.setBlock(x,y,z,mat);
                }
            }
        }
        NMSBlockChange.update();

        resetting = false;
        return true;
    }


    public void genBedrock() {
        Location wallMin = this.min.clone().subtract(1, 1, 1);
        Location walkMax = this.max.clone().add(1, 0, 1);
        for (int x = wallMin.getBlockX(); x <= walkMax.getBlockX(); ++x) {
            for (int y = wallMin.getBlockY(); y <= walkMax.getBlockY(); ++y) {
                for (int z = wallMin.getBlockZ(); z <= walkMax.getBlockZ(); ++z) {
                    Block b = world.getBlockAt(x, y, z);
                    boolean inX = true;
                    boolean inZ = true;
                    if (x < min.getBlockX() || x > max.getBlockX()) {
                        inX = false;
                    }
                    if (z < min.getBlockZ() || z > max.getBlockZ()) {
                        inZ = false;
                    }
                    if (inX && inZ) {
                        if (y != wallMin.getBlockY()) {
                            continue;
                        }
                    }
                    b.setType(Material.BEDROCK,false);
                }
            }
        }
    }
}