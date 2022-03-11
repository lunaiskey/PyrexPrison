package io.github.lunaiskey.lunixprisons.mines;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class GridManager {

    private static Map<ImmutablePair<Integer,Integer>, PersonalMine> personalMines = new HashMap();
    private final int diameter = 225;
    private final double half = diameter / 2D;

    public Location getMinCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(Mines.getWorldName()),-112+(diameter*chunkX),0,-112+(diameter*chunkZ));
    }

    public Location getMaxCorner(int chunkX,int chunkZ) {
        return new Location(Bukkit.getWorld(Mines.getWorldName()),112+(diameter*chunkX),256,112+(diameter*chunkZ));
    }

    public ImmutablePair<Integer,Integer> getGridLocation(Location loc) {
        return new ImmutablePair<>((int)Math.floor((Math.floor(loc.getX())+half)/diameter),(int)Math.floor((Math.floor(loc.getZ())+half)/diameter));
    }
}
