package io.github.lunaiskey.pyrexprison.modules.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.items.ItemID;
import io.github.lunaiskey.pyrexprison.modules.pmines.PMine;
import io.github.lunaiskey.pyrexprison.modules.pmines.PMineManager;
import io.github.lunaiskey.pyrexprison.util.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.modules.armor.Armor;
import io.github.lunaiskey.pyrexprison.modules.armor.ArmorType;
import io.github.lunaiskey.pyrexprison.modules.armor.upgrades.Ability;
import io.github.lunaiskey.pyrexprison.modules.armor.upgrades.AbilityType;
import io.github.lunaiskey.pyrexprison.modules.armor.upgrades.abilitys.EnchantmentProc;
import io.github.lunaiskey.pyrexprison.modules.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.pyrexprison.modules.armor.upgrades.abilitys.XPBoost;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class PlayerManager {

    private Map<UUID, PyrexPlayer> playerMap = new HashMap<>();
    private Map<String, UUID> playerNameMap = new HashMap<>();


    public Map<UUID, PyrexPlayer> getPlayerMap() {
        return playerMap;
    }

    public Map<String, UUID> getPlayerNameMap() {
        return playerNameMap;
    }
    public void createPyrexPlayer(UUID pUUID) {
        Player player = Bukkit.getPlayer(pUUID);
        if (player != null) {
            playerMap.put(pUUID,new PyrexPlayer(pUUID, player.getName()));
        } else {
            playerMap.put(pUUID,new PyrexPlayer(pUUID, Bukkit.getOfflinePlayer(pUUID).getName()));
        }
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
            String cachedName = (String) playerData.getOrDefault("name",Bukkit.getOfflinePlayer(pUUID).getName());
            if (cachedName.equals("null")) {
                cachedName = Bukkit.getOfflinePlayer(pUUID).getName();
            }
            int rank = ((Number) playerData.getOrDefault("rank",0)).intValue();
            ItemID selectedGemstone = ItemID.valueOf((String) playerData.getOrDefault("selectedGemstone",ItemID.AMETHYST_GEMSTONE.name()));
            int gemstoneCount = ((Number) playerData.getOrDefault("gemstoneCount",0)).intValue();

            //Load Pickaxe Data from file.
            Map<String,Object> pickaxeMap = fileConf.getConfigurationSection("pickaxeData").getValues(false);
            Map<String,Object> pickaxeEnchant = fileConf.getConfigurationSection("pickaxeData.enchants").getValues(false);
            Map<EnchantType,Integer> pickaxeEnchantMap = new HashMap<>();
            for (String enchant : pickaxeEnchant.keySet()) {
                pickaxeEnchantMap.put(EnchantType.valueOf(enchant),(int)pickaxeEnchant.get(enchant));
            }
            List<String> pickaxeDisabledEnchantList = fileConf.getConfigurationSection("pickaxeData").getStringList("disabledEnchants");
            Set<EnchantType> pickaxeDisabledEnchants = new HashSet<>();
            for (String str : pickaxeDisabledEnchantList) {
                try {
                    pickaxeDisabledEnchants.add(EnchantType.valueOf(str));
                } catch (Exception ignored) {}
            }
            long blocksBroken = ((Number) pickaxeMap.getOrDefault("blocksBroken",0L)).longValue();
            PyrexPickaxe pickaxe = new PyrexPickaxe(pUUID,pickaxeEnchantMap,pickaxeDisabledEnchants,blocksBroken);

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
            getPlayerMap().put(pUUID,new PyrexPlayer(pUUID,cachedName,tokens,gems,pyrexPoints,rank,pickaxe,isArmorEquiped,armorMap,selectedGemstone,gemstoneCount,null));
            playerNameMap.put(cachedName.toUpperCase(),pUUID);
        }
    }

    public int getPyrexItemCount(Player p, ItemID id) {
        int count = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            CompoundTag tag = NBTTags.getPyrexDataCompound(item);
            if (tag.contains("id")) {
                if (tag.getString("id").equals(id.name())) {
                    count += item.getAmount();
                }
            }
        }
        return count;
    }

    public void removePyrexItem(Player p, ItemID id, int amount) {
        ItemStack[] inv = p.getInventory().getContents();
        for (int i = 0;i<inv.length;i++) {
            ItemStack item = inv[i];
            CompoundTag tag = NBTTags.getPyrexDataCompound(item);
            if (tag.contains("id")) {
                if (tag.getString("id").equals(id.name())) {
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

    public int getGemstoneCountMax(Player p) {
        PyrexPlayer player = getPlayerMap().get(p.getUniqueId());
        return switch (player.getSelectedGemstone()) {
            case AMETHYST_GEMSTONE -> 200;
            case JASPER_GEMSTONE -> 225;
            case OPAL_GEMSTONE -> 250;
            case JADE_GEMSTONE -> 275;
            case TOPAZ_GEMSTONE -> 300;
            case AMBER_GEMSTONE -> 325;
            case SAPPHIRE_GEMSTONE -> 350;
            case EMERALD_GEMSTONE -> 400;
            case RUBY_GEMSTONE -> 450;
            case DIAMOND_GEMSTONE -> 500;
            default -> 100000;
        };

    }

    public void tickGemstoneCount(Player p) {
        tickGemstoneCount(p,1);
    }

    public void tickGemstoneCount(Player p, int amount) {
        PyrexPlayer player = getPlayerMap().get(p.getUniqueId());
        int combined = amount + player.getGemstoneCount();
        int ticks = (combined - combined%(getGemstoneCountMax(p)))/(getGemstoneCountMax(p));
        while (ticks > 0) {
            p.getInventory().addItem(PyrexPrison.getPlugin().getItemManager().getItemMap().get(player.getSelectedGemstone()).getItemStack());
            ticks--;
        }
        player.setGemstoneCount(combined%getGemstoneCountMax(p));
    }

    public List<UUID> getSortedByRank() {
        List<UUID> sortedValues = new ArrayList<>(playerMap.keySet());
        sortedValues.sort(Comparator.comparingInt(o -> playerMap.get(o).getRank()));
        return sortedValues;
    }

    @Nullable
    public UUID getPlayerUUID(String name) {
        return getPlayerNameMap().get(name.toUpperCase());
    }

    public void payForBlocks(Player p,long amount) {
        PyrexPlayer pyrexPlayer = playerMap.get(p.getUniqueId());

        Pair<Integer,Integer> pair = PyrexPrison.getPlugin().getPmineManager().getGridLocation(p.getLocation());
        PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(pair.getLeft(),pair.getRight());

        PyrexPickaxe pickaxe = pyrexPlayer.getPickaxe();
        double multiplier = pyrexPlayer.getTotalMultiplier();
        int fortune = Math.max(pickaxe.getEnchants().getOrDefault(EnchantType.FORTUNE,5),5);

        BigDecimal newAmount = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(mine.getSellPrice())).multiply(BigDecimal.valueOf(fortune).multiply(BigDecimal.valueOf(40)));
        BigInteger tokens = newAmount.multiply(BigDecimal.valueOf(multiplier+1)).toBigInteger();

        if (mine.getOwner() == pyrexPlayer.getpUUID()) {
            pyrexPlayer.giveTokens(tokens);
        } else {
            PyrexPlayer mineOwner = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(mine.getOwner());
            double tax = mine.getMineTax()/100D;
            double mineOwnerAmount = 1-tax;
            pyrexPlayer.giveTokens(new BigDecimal(tokens).multiply(BigDecimal.valueOf(mineOwnerAmount)).toBigInteger());
            mineOwner.giveTokens(new BigDecimal(tokens).multiply(BigDecimal.valueOf(tax)).toBigInteger());
            PyrexPrison.getPlugin().getSavePending().add(mineOwner.getpUUID());
        }

    }
}
