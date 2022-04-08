package io.github.lunaiskey.pyrexprison.player.armor;

import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class Armor {

    private ArmorType type;
    private Color customColor;

    public Armor(ArmorType type) {
        this.type = type;
        this.customColor = null;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial(type));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&fStarter "+type.getName()));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&f&lPiece Abilities:"));
        lore.add(StringUtil.color(" &7&oNo abilities found..."));
        lore.add(" ");
        lore.add(StringUtil.color("&f&lTIER 0 "+type.name()));
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DYE);
        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
            leatherMeta.setColor(getTierColor(0));
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

    public Color getTierColor(int tier) {
        Color color;
        switch(tier) {
            case 0 -> color = Color.WHITE;
            default -> color = null;
        }
        return color;
    }
}
