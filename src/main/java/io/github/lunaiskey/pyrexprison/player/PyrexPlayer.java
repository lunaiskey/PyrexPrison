package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PyrexPlayer {

    private double tokens;
    private double gems;
    private int rank;
    private final UUID pUUID;


    public PyrexPlayer(UUID pUUID, double tokens, double gems, int rank) {
        this.pUUID = pUUID;
        this.tokens = tokens;
        this.gems = gems;
        this.rank = rank;
        save();
    }

    public double getGems() {
        return gems;
    }

    public double getTokens() {
        return tokens;
    }

    public int getRank() {
        return rank;
    }

    public UUID getpUUID() {
        return pUUID;
    }

    public void setGems(double gems) {
        this.gems = gems;
    }

    public void setTokens(double tokens) {
        this.tokens = tokens;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void giveTokens(double tokens) {
        this.tokens += tokens;
    }

    public void giveGems(double gems) {
        this.gems += gems;
    }

    public void takeTokens(double tokens) {
        this.tokens -= tokens;
    }
    public void takeGems(double gems) {
        this.gems -= gems;
    }

    public void save() {
        File file = new File(PyrexPrison.getPlugin().getDataFolder() + "/playerdata/" + pUUID + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> currencyMap = new LinkedHashMap<>();
        currencyMap.put("tokens", tokens);
        currencyMap.put("gems", gems);
        data.createSection("currencies", currencyMap);
        try {
            data.save(file);
        } catch (IOException e) {
            PyrexPrison.getPlugin().getLogger().severe("Failed to save " + pUUID + "'s player data.");
            e.printStackTrace();
        }
    }
}
