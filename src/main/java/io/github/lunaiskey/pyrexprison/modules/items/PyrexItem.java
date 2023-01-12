package io.github.lunaiskey.pyrexprison.modules.items;

import io.github.lunaiskey.pyrexprison.util.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PyrexItem {

    private ItemID id;
    private String name;
    private List<String> lore;
    private Material mat;
    private Rarity rarity;

    public PyrexItem(ItemID id,String name, List<String> lore, Rarity rarity, Material mat) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        if (rarity != null) {
            this.rarity = rarity;
        } else {
            this.rarity = Rarity.COMMON;
        }

        this.mat = mat;
    }

    public PyrexItem(ItemID id,String name, List<String> lore, Material mat) {
        this(id,name,lore, Rarity.COMMON,mat);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(mat);
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(StringUtil.color(name));
        }
        if (getLore() != null) {
            List<String> lore = new ArrayList<>();
            for (String str : getLore()) {
                lore.add(StringUtil.color(str));
            }
            lore.add(StringUtil.color(rarity.getColorCode()+rarity.name()));
            meta.setLore(lore);
        } else {
            meta.setLore(Collections.singletonList(StringUtil.color(rarity.getColorCode() + rarity.name())));
        }
        item.setItemMeta(meta);
        return item;
    }

    public abstract void onBlockBreak(BlockBreakEvent e);
    public abstract void onBlockPlace(BlockPlaceEvent e);
    public abstract void onInteract(PlayerInteractEvent e);

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemID getItemID() {
        return id;
    }

    public Material getMaterial() {
        return mat;
    }

    public Rarity getRarity() {
        return rarity;
    }
}
