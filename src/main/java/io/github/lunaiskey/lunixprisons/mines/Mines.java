package io.github.lunaiskey.lunixprisons.mines;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class Mines {

    private final static String worldName = "mines";

    public void generateWorld() {
        WorldCreator wc = new WorldCreator(worldName);
        wc.biomeProvider(new MineBiomeProvider());
        wc.generator(new MineWorldGenerator());
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.environment(World.Environment.NORMAL);
    }

    public static String getWorldName() {
        return worldName;
    }

    public static void addMine(String name, GlobalMine globalMine) {

    }
}
