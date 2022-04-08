package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexPickaxe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static Map<UUID, PyrexPlayer> playerMap = new HashMap<>();

    public Map<UUID, PyrexPlayer> getPlayerMap() {
        return playerMap;
    }

    public void createPyrexPlayer(UUID pUUID) {
        playerMap.put(pUUID,new PyrexPlayer(pUUID, BigInteger.ZERO,0,0,0,new PyrexPickaxe(pUUID)));
    }

    public void loadPlayers() {
        File[] playerFiles = new File(PyrexPrison.getPlugin().getDataFolder(), "playerdata").listFiles(new PMineManager.IsPMineFile());
        assert playerFiles != null;
        for (File file : playerFiles) {
            FileConfiguration fileConf = YamlConfiguration.loadConfiguration(file);
            UUID pUUID = UUID.fromString(file.getName().replace(".yml",""));
            Map<String,Object> currencyMap = new HashMap<>();
            try {
                currencyMap = fileConf.getConfigurationSection("currencies").getValues(false);
            } catch (Exception ignored) {
                currencyMap.put("tokens",BigInteger.ZERO);
                currencyMap.put("gems",0);
                currencyMap.put("pyrexpoints",0);
            }
            BigInteger tokens = new BigInteger(currencyMap.getOrDefault("tokens",BigInteger.ZERO).toString());
            long gems = ((Number) currencyMap.getOrDefault("gems",0L)).longValue();
            long pyrexPoints = ((Number) currencyMap.getOrDefault("pyrexpoints",0L)).longValue();

            Map<String,Object> playerData = new HashMap<>();
            try {
                playerData = fileConf.getConfigurationSection("pyrexData").getValues(false);
            } catch (Exception ignored) {}
            int rank = ((Number) playerData.getOrDefault("rank",0)).intValue();
            Map<String,Object> pickaxeMap = fileConf.getConfigurationSection("pickaxeData").getValues(false);
            Map<String,Object> pickaxeEnchant = fileConf.getConfigurationSection("pickaxeData.enchants").getValues(false);
            Map<EnchantType,Integer> pickaxeEnchantMap = new HashMap<>();
            for (String enchant : pickaxeEnchant.keySet()) {
                pickaxeEnchantMap.put(EnchantType.valueOf(enchant),(int)pickaxeEnchant.get(enchant));
            }
            long blocksBroken = ((Number) pickaxeMap.getOrDefault("blocksBroken",0L)).longValue();
            PyrexPickaxe pickaxe = new PyrexPickaxe(pUUID,pickaxeEnchantMap,blocksBroken);

            getPlayerMap().put(pUUID,new PyrexPlayer(pUUID,tokens,gems,pyrexPoints,rank,pickaxe));
        }
    }




}
