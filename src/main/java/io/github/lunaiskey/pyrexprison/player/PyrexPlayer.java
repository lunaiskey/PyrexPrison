package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexPickaxe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PyrexPlayer {

    private long tokens;
    private long gems;
    private long pyrexPoints;
    private int rank;
    private final UUID pUUID;
    private PyrexPickaxe pickaxe = new PyrexPickaxe();


    public PyrexPlayer(UUID pUUID, long tokens, long gems, long pyrexPoints, int rank) {
        this.pUUID = pUUID;
        this.tokens = tokens;
        this.gems = gems;
        this.pyrexPoints = pyrexPoints;
        this.rank = rank;
        save();
    }

    public long getGems() {
        return gems;
    }

    public long getTokens() {
        return tokens;
    }

    public long getPyrexPoints() {
        return pyrexPoints;
    }

    public int getRank() {
        return rank;
    }

    public UUID getpUUID() {
        return pUUID;
    }

    public PyrexPickaxe getPickaxe() {
        return pickaxe;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }

    public void setTokens(long tokens) {
        this.tokens = tokens;
    }

    public void setPyrexPoints(long pyrexPoints) {
        this.pyrexPoints = pyrexPoints;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void giveTokens(long tokens) {this.tokens += tokens;}
    public void giveGems(long gems) {
        this.gems += gems;
    }
    public void givePyrexPoints(long pyrexPoints) {
        this.pyrexPoints += pyrexPoints;
    }

    public void takeTokens(long tokens) {
        this.tokens -= tokens;
    }
    public void takeGems(long gems) {
        this.gems -= gems;
    }
    public void takePyrexPoints(long pyrexPoints) {
        this.pyrexPoints -= pyrexPoints;
    }

    public void save() {
        File file = new File(PyrexPrison.getPlugin().getDataFolder() + "/playerdata/" + pUUID + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> currencyMap = new LinkedHashMap<>();
        currencyMap.put("tokens", tokens);
        currencyMap.put("gems", gems);
        currencyMap.put("pyrexpoints", pyrexPoints);
        data.createSection("currencies", currencyMap);
        try {
            data.save(file);
        } catch (IOException e) {
            PyrexPrison.getPlugin().getLogger().severe("Failed to save " + pUUID + "'s player data.");
            e.printStackTrace();
        }
    }
}
