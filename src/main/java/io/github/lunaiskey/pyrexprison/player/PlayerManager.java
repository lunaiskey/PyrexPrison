package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.player.armor.Armor;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorType;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStone;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.EnchantmentProc;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.XPBoost;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
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
    private static Map<Integer, GemStoneType> reversedGemstoneMap = new HashMap<>();

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

            //Load Currencies from map
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

            //Load Pyrex Data from map
            Map<String,Object> playerData = new HashMap<>();
            try {
                playerData = fileConf.getConfigurationSection("pyrexData").getValues(false);
            } catch (Exception ignored) {}
            int rank = ((Number) playerData.getOrDefault("rank",0)).intValue();

            //Load Pickaxe Data from file.
            Map<String,Object> pickaxeMap = fileConf.getConfigurationSection("pickaxeData").getValues(false);
            Map<String,Object> pickaxeEnchant = fileConf.getConfigurationSection("pickaxeData.enchants").getValues(false);
            Map<EnchantType,Integer> pickaxeEnchantMap = new HashMap<>();
            for (String enchant : pickaxeEnchant.keySet()) {
                pickaxeEnchantMap.put(EnchantType.valueOf(enchant),(int)pickaxeEnchant.get(enchant));
            }
            long blocksBroken = ((Number) pickaxeMap.getOrDefault("blocksBroken",0L)).longValue();
            PyrexPickaxe pickaxe = new PyrexPickaxe(pUUID,pickaxeEnchantMap,blocksBroken);

            //Load Armor data from file.
            Map<String,Object> armorData = new HashMap<>();
            try {
                armorData = fileConf.getConfigurationSection("armor").getValues(true);
            } catch (Exception ignored) {}
            boolean isArmorEquiped = (boolean) armorData.getOrDefault("isArmorEquiped",false);
            Map<ArmorType, Armor> armorMap = new HashMap<>();
            for (ArmorType type : ArmorType.values()) {
                Color color = armorData.get(type.getName()+".customColor") != null ? Color.fromRGB((Integer) armorData.get(type.getName()+".customColor")) : null;
                int tier = (int) armorData.getOrDefault(type.getName()+".tier",0);
                Map<AbilityType, Ability> abilityMap = new HashMap<>();
                Map<String,Object> abilities;
                try {
                    abilities = fileConf.getConfigurationSection("armor."+type.getName()+".abilities").getValues(false);
                    for (String str : abilities.keySet()) {
                        PyrexPrison.getPlugin().getLogger().info("loaddata: "+str);
                        try {
                            int level = (Integer) abilities.get(str);
                            AbilityType abilityType = AbilityType.valueOf(str);
                            switch (abilityType) {
                                case SALES_BOOST -> abilityMap.put(abilityType,new SalesBoost(level));
                                case ENCHANTMENT_PROC -> abilityMap.put(abilityType,new EnchantmentProc(level));
                                case XP_BOOST -> abilityMap.put(abilityType,new XPBoost(level));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    Armor piece = new Armor(type,tier,color,abilityMap);
                    armorMap.put(type,piece);
                } catch (Exception ignored) {}
            }
            //Finished Loading
            getPlayerMap().put(pUUID,new PyrexPlayer(pUUID,tokens,gems,pyrexPoints,rank,pickaxe,isArmorEquiped,armorMap));
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
        for (GemStone gemstone : gemstonesMap.values()) {
            reversedGemstoneMap.put(gemstone.getTier(),gemstone.getType());
        }
    }

    public Map<GemStoneType, GemStone> getGemstonesMap() {
        return gemstonesMap;
    }

    public Map<Integer, GemStoneType> getReversedGemstoneMap() {
        return reversedGemstoneMap;
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
                    if (amount > 0) {
                        if (amount > item.getAmount()) {
                            amount -= item.getAmount();
                            inv[i] = null;
                        } else {
                            item.setAmount(item.getAmount()-amount);
                            amount = 0;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        p.getInventory().setContents(inv);
    }
}
