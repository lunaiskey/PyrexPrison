package io.github.lunaiskey.pyrexprison.player.armor;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.EnchantmentProc;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.SalesBoost;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.abilitys.XPBoost;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Armor {

    private final ArmorType type;
    private Color customColor;
    private int tier;
    private Map<AbilityType, Ability> abilties = new HashMap<>();


    public Armor(ArmorType type, int tier,Color customColor, Map<AbilityType,Ability> abilityMap) {
        this.type = type;
        this.customColor = customColor;
        this.tier = tier;
        if (abilityMap != null) {
            abilties.put(AbilityType.SALES_BOOST,abilityMap.getOrDefault(AbilityType.SALES_BOOST,new SalesBoost(0)));
            abilties.put(AbilityType.ENCHANTMENT_PROC,abilityMap.getOrDefault(AbilityType.ENCHANTMENT_PROC,new EnchantmentProc(0)));
            abilties.put(AbilityType.XP_BOOST,abilityMap.getOrDefault(AbilityType.XP_BOOST,new XPBoost(0)));
        } else {
            abilties.put(AbilityType.SALES_BOOST,new SalesBoost(0));
            abilties.put(AbilityType.ENCHANTMENT_PROC,new EnchantmentProc(0));
            abilties.put(AbilityType.XP_BOOST,new XPBoost(0));
        }

    }

    public Armor(ArmorType type) {
        this(type,0,null,null);
    }

    public Armor(ArmorType type, int tier) {
        this(type,tier,null,null);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial(type));
        ItemMeta meta = item.getItemMeta();
        if (tier == 0) {
            meta.setDisplayName(StringUtil.color("&fStarter "+type.getName()));
        } else {
            meta.setDisplayName(PyrexPrison.getPlugin().getPlayerManager().getGemstonesMap().get(getGemstone(tier)).getName()+" "+type.getName());
        }
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&f&lPiece Abilities:"));
        boolean hasAbilities = false;
        for (AbilityType type : AbilityType.getSortedList()) {
            Ability ability = abilties.get(type);
            if (ability.getLevel() > 0) {
                hasAbilities = true;
                lore.add(" "+ability.getLoreTitle()+ability.getLoreAddon());
            }
        }
        if (!hasAbilities) {
            lore.add(StringUtil.color(" &7&oNo abilities found..."));
        }

        lore.add(" ");
    lore.add(ChatColor.of(getAWTTierColor(tier))+""+ChatColor.BOLD+"TIER "+tier+" "+type.name());
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DYE);
        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
            leatherMeta.setColor(getTierColor(tier));
            item.setItemMeta(leatherMeta);
        } else {
            item.setItemMeta(meta);
        }
        item = NBTTags.addPyrexData(item,"id","PYREX_ARMOR_"+type.name());
        return item;
    }

    public Material getMaterial(ArmorType type) {
        Material mat = Material.AIR;
        switch(type) {
            case HELMET -> mat = Material.LEATHER_HELMET;
            case CHESTPLATE -> mat = Material.LEATHER_CHESTPLATE;
            case LEGGINGS -> mat = Material.LEATHER_LEGGINGS;
            case BOOTS -> mat = Material.LEATHER_BOOTS;
        }
        return mat;
    }

    public GemStoneType getGemstone(int tier) {
        GemStoneType type;
        switch (tier) {
            case 1 -> type = GemStoneType.AMETHYST;
            case 2 -> type = GemStoneType.JASPER;
            case 3 -> type = GemStoneType.OPAL;
            case 4 -> type = GemStoneType.JADE;
            case 5 -> type = GemStoneType.TOPAZ;
            case 6 -> type = GemStoneType.AMBER;
            case 7 -> type = GemStoneType.SAPPHIRE;
            case 8 -> type = GemStoneType.EMERALD;
            case 9 -> type = GemStoneType.RUBY;
            case 10 -> type = GemStoneType.DIAMOND;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }
        return type;
    }

    public int getCostAmount(int tier) {
        int cost;
        switch(tier) {
            case 1 -> cost = 64;
            case 2 -> cost = 96;
            case 3 -> cost = 128;
            case 4 -> cost = 192;
            case 5 -> cost = 256;
            case 6 -> cost = 384;
            case 7 -> cost = 512;
            case 8 -> cost = 768;
            case 9 -> cost = 1024;
            case 10 -> cost = 1536;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }
        return cost;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getTierMax() {
        return 10;
    }

    public ArmorType getType() {
        return type;
    }

    public Map<AbilityType, Ability> getAbilties() {
        return abilties;
    }

    public Color getTierColor(int tier) {
        if (tier == 0) {
            return Color.WHITE;
        } else if (tier <= getTierMax()) {
            return Color.fromRGB(PyrexPrison.getPlugin().getPlayerManager().getGemstonesMap().get(getGemstone(tier)).getIntFromHex());
        } else {
            return Color.BLACK;
        }
    }

    public java.awt.Color getAWTTierColor(int tier) {
        return new java.awt.Color(getTierColor(tier).asRGB());
    }

    public Color getCustomColor() {
        if (customColor != null) {
            return customColor;
        } else {
            return getTierColor(tier);
        }
    }

    public boolean hasCustomColor() {
        return customColor != null;
    }

    public java.awt.Color getAWTCustomColor() {
        return new java.awt.Color(getCustomColor().asRGB());
    }
}
