package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStone;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneType;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static Map<UUID, PyrexPlayer> playerMap = new HashMap<>();
    private static Map<GemStoneType, GemStone> gemstonesMap = new HashMap();

    public Map<UUID, PyrexPlayer> getPlayerMap() {
        return playerMap;
    }

    public void createPyrexPlayer(UUID pUUID) {
        playerMap.put(pUUID,new PyrexPlayer(pUUID));
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
            Map<String,Object> armorData = new HashMap<>();
            try {
                armorData = fileConf.getConfigurationSection("armor").getValues(false);
            } catch (Exception ignored) {}
            boolean isArmorEquiped = (boolean) armorData.getOrDefault("isArmorEquiped",false);

            getPlayerMap().put(pUUID,new PyrexPlayer(pUUID,tokens,gems,pyrexPoints,rank,pickaxe,isArmorEquiped));
        }
    }

    public void initGemstoneMap() {
        gemstonesMap.put(GemStoneType.DIAMOND,new GemStone(GemStoneType.DIAMOND,10));
        gemstonesMap.put(GemStoneType.RUBY,new GemStone(GemStoneType.RUBY,9));
        gemstonesMap.put(GemStoneType.EMERALD,new GemStone(GemStoneType.EMERALD,8));
        gemstonesMap.put(GemStoneType.SAPPHIRE,new GemStone(GemStoneType.SAPPHIRE,7));
        gemstonesMap.put(GemStoneType.AMBER,new GemStone(GemStoneType.AMBER,6));
        gemstonesMap.put(GemStoneType.TOPAZ,new GemStone(GemStoneType.TOPAZ,5));
        gemstonesMap.put(GemStoneType.JADE,new GemStone(GemStoneType.JADE,4));
        gemstonesMap.put(GemStoneType.OPAL,new GemStone(GemStoneType.OPAL,3));
        gemstonesMap.put(GemStoneType.JASPER,new GemStone(GemStoneType.JASPER,2));
        gemstonesMap.put(GemStoneType.AMETHYST,new GemStone(GemStoneType.AMETHYST,1));
    }

    public Map<GemStoneType, GemStone> getGemstonesMap() {
        return gemstonesMap;
    }

    public int getGemstoneCount(Player p, GemStoneType type) {
        int count = 0;
        GemStone gemStone = getGemstonesMap().get(type);
        for (ItemStack item : p.getInventory().getContents()) {
            CompoundTag tag = NBTTags.getPyrexDataMap(item);
            if (tag.contains("id")) {
                if (tag.getString("id").equals(gemStone.getId())) {
                    count += item.getAmount();
                }
            }
        }
        return count;
    }

    public void removeGemstones(Player p, GemStoneType type, int amount) {
        GemStone gemStone = getGemstonesMap().get(type);
        ItemStack[] inv = p.getInventory().getContents();
        for (int i = 0;i<inv.length;i++) {
            ItemStack item = inv[i];
            CompoundTag tag = NBTTags.getPyrexDataMap(item);
            if (tag.contains("id")) {
                if (tag.getString("id").equals(gemStone.getId())) {
                    if (amount > 64) {
                        inv[i] = null;
                        amount -= 64;
                    } else if (amount > 0){
                        item.setAmount(item.getAmount()-amount);
                        amount = 0;
                    } else {
                        break;
                    }
                }
            }
        }
        p.getInventory().setContents(inv);
    }
}
