package io.github.lunaiskey.pyrexprison.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    public static ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(name));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack replaceSkullTexture(ItemStack head, String base64, UUID profileUUID) {
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(profileUUID, "");
        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getSkull(String base64, UUID profileUUID) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(profileUUID, "");
        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getSkull(String name, List<String> lore,String base64, UUID profileUUID) {
        ItemStack item = getSkull(base64,profileUUID);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(name));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
