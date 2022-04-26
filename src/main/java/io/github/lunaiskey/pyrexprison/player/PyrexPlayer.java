package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.player.armor.Armor;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.SalesBoost;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class PyrexPlayer {

    private BigInteger tokens;
    private long gems;
    private long pyrexPoints;
    private int rank;
    private final UUID pUUID;
    private PyrexPickaxe pickaxe;
    private final Map<ArmorType,Armor> armor = new HashMap<>();
    private boolean isArmorEquiped;


    public PyrexPlayer(UUID pUUID, BigInteger tokens, long gems, long pyrexPoints, int rank, PyrexPickaxe pickaxe, boolean isArmorEquiped, Map<ArmorType,Armor> armor) {
        this.pUUID = pUUID;
        this.tokens = tokens;
        this.gems = gems;
        this.pyrexPoints = pyrexPoints;
        this.rank = rank;
        this.pickaxe = pickaxe;
        this.isArmorEquiped = isArmorEquiped;
        if (armor == null || armor.isEmpty()) {
            this.armor.put(ArmorType.HELMET,new Armor(ArmorType.HELMET));
            this.armor.put(ArmorType.CHESTPLATE,new Armor(ArmorType.CHESTPLATE));
            this.armor.put(ArmorType.LEGGINGS,new Armor(ArmorType.LEGGINGS));
            this.armor.put(ArmorType.BOOTS,new Armor(ArmorType.BOOTS));
        } else {
            this.armor.putAll(armor);
        }
        save();
    }

    public PyrexPlayer(UUID pUUID) {
        this(pUUID,BigInteger.ZERO,0,0,0,new PyrexPickaxe(pUUID),false,null);
    }

    public long getGems() {
        return gems;
    }

    public BigInteger getTokens() {
        return tokens;
    }

    public long getPyrexPoints() {
        return pyrexPoints;
    }

    public BigInteger getCurrency(CurrencyType type) {
        BigInteger amount = BigInteger.ZERO;
        switch (type) {
            case GEMS -> amount = BigInteger.valueOf(getGems()) ;
            case PYREX_POINTS -> amount = BigInteger.valueOf(getPyrexPoints());
            case TOKENS -> amount = getTokens();
        }
        return amount;
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

    public Map<ArmorType, Armor> getArmor() {
        return armor;
    }

    public Armor getHelmet() {
        return armor.get(ArmorType.HELMET);
    }

    public Armor getChestplate() {
        return armor.get(ArmorType.CHESTPLATE);
    }

    public Armor getLeggings() {
        return armor.get(ArmorType.LEGGINGS);
    }

    public Armor getBoots() {
        return armor.get(ArmorType.BOOTS);
    }

    public boolean isArmorEquiped() {
        return isArmorEquiped;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }

    public void setTokens(BigInteger tokens) {
        this.tokens = tokens;
    }

    public void setPyrexPoints(long pyrexPoints) {
        this.pyrexPoints = pyrexPoints;
    }

    public void setCurrency(CurrencyType type, long amount) {
        switch (type) {
            case GEMS -> setGems(amount);
            case PYREX_POINTS -> setPyrexPoints(amount);
        }
    }

    public void setCurrency(CurrencyType type, BigInteger amount) {
        switch (type) {
            case TOKENS -> setTokens(amount);
        }
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setArmorEquiped(boolean armorEquiped) {
        isArmorEquiped = armorEquiped;
    }

    public void giveTokens(BigInteger tokens) {this.tokens = this.tokens.add(tokens);}
    public void giveTokens(long tokens) {this.tokens = this.tokens.add(BigInteger.valueOf(tokens));}
    public void giveGems(long gems) {
        this.gems += gems;
    }
    public void givePyrexPoints(long pyrexPoints) {
        this.pyrexPoints += pyrexPoints;
    }

    public void giveCurrency(CurrencyType type, long amount) {
        giveCurrency(type, BigInteger.valueOf(amount));
    }

    public void giveCurrency(CurrencyType type, BigInteger amount) {
        switch (type) {
            case GEMS -> giveGems(amount.longValue());
            case PYREX_POINTS -> givePyrexPoints(amount.longValue());
            case TOKENS -> giveTokens(amount);
        }
    }

    public void takeTokens(BigInteger tokens) {
        this.tokens = this.tokens.subtract(tokens);
    }
    public void takeGems(long gems) {
        this.gems -= gems;
    }
    public void takePyrexPoints(long pyrexPoints) {
        this.pyrexPoints -= pyrexPoints;
    }

    public void takeCurrency(CurrencyType type, BigInteger amount) {
        switch (type) {
            case GEMS -> takeGems(amount.longValue());
            case PYREX_POINTS -> takePyrexPoints(amount.longValue());
            case TOKENS -> takeTokens(amount);
        }
    }

    public void payForBlocks(long amount) {
        double multiplier = getTotalMultiplier();
        int fortune = Math.max(pickaxe.getEnchants().getOrDefault(EnchantType.FORTUNE,0),5);
        BigInteger tokens = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(fortune)).multiply(BigDecimal.valueOf(multiplier+1)).toBigInteger();
        giveTokens(tokens);
    }

    public double getBaseMultiplier() {
        return 0;
    }

    public double getRankMultiplier() {
        double rank = Math.max(getRank(),0);
        if (rank <= 100) {
            return 0.1D*rank;
        } else {
            return (0.1*100D) + ((rank-100)*0.025);
        }
    }

    public double getArmorMultiplier() {
        double multiplier = 0;
        for (Armor armor : getArmor().values()) {
            SalesBoost boost = (SalesBoost) armor.getAbilties().getOrDefault(AbilityType.SALES_BOOST,new SalesBoost(0));
            multiplier += boost.getMultiplier(boost.getLevel());
        }
        return multiplier;
    }

    /**
     * All multipliers added together, don't multiply the original amount by this, do amount + amount*getTotalMultiplier because this can return 0.
     * @return Total Multiplier, can be 0.
     */
    public double getTotalMultiplier() {
        return getBaseMultiplier()+getRankMultiplier();
    }


    public void save() {
        File file = new File(PyrexPrison.getPlugin().getDataFolder() + "/playerdata/" + pUUID + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> currencyMap = new LinkedHashMap<>();
        currencyMap.put("tokens", tokens);
        currencyMap.put("gems", gems);
        currencyMap.put("pyrexpoints", pyrexPoints);
        data.createSection("currencies", currencyMap);
        Map<String, Object> playerData = new LinkedHashMap<>();
        playerData.put("rank",rank);
        data.createSection("pyrexData",playerData);
        Map<String, Object> armorData = new LinkedHashMap<>();
        armorData.put("isArmorEquiped",isArmorEquiped);
        data.createSection("armor",armorData);
        if (pickaxe != null) {
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, Object> enchantMap = new LinkedHashMap<>();
            map.put("blocksBroken",pickaxe.getBlocksBroken());
            for (EnchantType type : pickaxe.getEnchants().keySet()) {
                enchantMap.put(type.name(),pickaxe.getEnchants().get(type));
            }
            data.createSection("pickaxeData", map);
            data.createSection("pickaxeData.enchants",enchantMap);
        }
        for (Armor armor : armor.values()) {
            Map<String, Object> pieceData = new LinkedHashMap<>();
            pieceData.put("tier",armor.getTier());
            if (armor.hasCustomColor()) {
                pieceData.put("customColor",armor.getCustomColor().asRGB());
            }
            data.createSection("armor."+armor.getType().getName(),pieceData);
            Map<String,Object> abilityData = new LinkedHashMap<>();
            for(AbilityType type : armor.getAbilties().keySet()) {
                abilityData.put(type.name(),armor.getAbilties().get(type).getLevel());
            }
            data.createSection("armor."+armor.getType().getName()+".abilities",abilityData);
        }
        try {
            data.save(file);
        } catch (IOException e) {
            PyrexPrison.getPlugin().getLogger().severe("Failed to save " + pUUID + "'s player data.");
            e.printStackTrace();
        }
    }
}
