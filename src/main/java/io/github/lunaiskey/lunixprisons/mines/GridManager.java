package io.github.lunaiskey.lunixprisons.mines;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.lunaiskey.lunixprisons.mines.generator.PMineWorld;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GridManager {

    private static Map<Pair<Integer,Integer>, PMine> pMines = new HashMap<>();
    private static Map<UUID, Pair<Integer,Integer>> ownerPMines = new HashMap<>();
    private final int gridIslandSize = 225;
    Location last = null;


    public Location getMinCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(PMineWorld.getWorldName()),-112+(gridIslandSize *chunkX),0,-112+(gridIslandSize *chunkZ));
    }

    public Location getMaxCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(PMineWorld.getWorldName()),112+(gridIslandSize *chunkX),256,112+(gridIslandSize *chunkZ));
    }

    public ImmutablePair<Integer,Integer> getGridLocation(Location loc) {
        double x = Math.floor((112D + loc.getBlockX()) / gridIslandSize);
        double z = Math.floor((112D + loc.getBlockZ()) / gridIslandSize);
        return new ImmutablePair<>((int) x,(int) z);
    }

    public static void newPMine(UUID owner, int chunkX, int chunkZ) {
        PMine mine = new PMine(owner, chunkX, chunkZ);
        Pair<Integer,Integer> pair = new ImmutablePair<>(chunkX,chunkZ);
        pMines.put(pair,mine);
        ownerPMines.put(owner,pair);
    }

    public static PMine getPMine(int chunkX,int chunkZ) {
        return pMines.get(new ImmutablePair<>(chunkX,chunkZ));
    }

    public static PMine getPMine(UUID owner) {
        Pair<Integer,Integer> pair = ownerPMines.get(owner);
        if (pair != null) {
            return getPMine(pair.getLeft(),pair.getRight());
        }
        return null;
    }

    public static Map<Pair<Integer, Integer>, PMine> getPMinesMap() {
        return pMines;
    }

    /*
    public Location getClosestIsland(Location location) {
        long x = Math.round((double) location.getBlockX() / Settings.islandDistance) * Settings.islandDistance + Settings.islandXOffset;
        long z = Math.round((double) location.getBlockZ() / Settings.islandDistance) * Settings.islandDistance + Settings.islandZOffset;
        long y = 100;
        return new Location(location.getWorld(), x, y, z);
    }

     */

    /*
    private Location getNextIsland(UUID playerUUID) {
        // See if there is a reserved spot
        if (islandSpot.containsKey(playerUUID)) {
            Location next = plugin.getGrid().getClosestIsland(islandSpot.get(playerUUID));
            // Single shot only
            islandSpot.remove(playerUUID);
            // Check if it is already occupied (shouldn't be)
            Island island = plugin.getGrid().getIslandAt(next);
            if (island == null || island.getOwner() == null) {
                // it's still free
                return next;
            }
            // Else, fall back to the random pick
        }
        // Find the next free spot
        if (last == null) {
            last = new Location(ASkyBlock.getIslandWorld(), Settings.islandXOffset + Settings.islandStartX, Settings.islandHeight, Settings.islandZOffset + Settings.islandStartZ);
        }
        Location next = last.clone();

        while (plugin.getGrid().islandAtLocation(next) || islandSpot.containsValue(next)) {
            next = nextGridLocation(next);
        }
        // Make the last next, last
        last = next.clone();
        return next;
    }

    private Location nextGridLocation(final Location lastIsland) {
        // plugin.getLogger().info("DEBUG nextIslandLocation");
        final int x = lastIsland.getBlockX();
        final int z = lastIsland.getBlockZ();
        final Location nextPos = lastIsland;
        if (x < z) {
            if (-1 * x < z) {
                nextPos.setX(nextPos.getX() + Settings.islandDistance);
                return nextPos;
            }
            nextPos.setZ(nextPos.getZ() + Settings.islandDistance);
            return nextPos;
        }
        if (x > z) {
            if (-1 * x >= z) {
                nextPos.setX(nextPos.getX() - Settings.islandDistance);
                return nextPos;
            }
            nextPos.setZ(nextPos.getZ() - Settings.islandDistance);
            return nextPos;
        }
        if (x <= 0) {
            nextPos.setZ(nextPos.getZ() + Settings.islandDistance);
            return nextPos;
        }
        nextPos.setZ(nextPos.getZ() - Settings.islandDistance);
        return nextPos;
    }

     */
}
