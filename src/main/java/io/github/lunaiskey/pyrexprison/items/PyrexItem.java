package io.github.lunaiskey.pyrexprison.items;

import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class PyrexItem {

    private ItemID id;
    private String name;
    private List<String> description;
    private Material mat;

    public PyrexItem(ItemID id,String name, List<String> description, Material mat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mat = mat;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(mat);
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(name));
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }

    public abstract void onBlockBreak(BlockBreakEvent e);
    public abstract void onBlockPlace(BlockPlaceEvent e);
    public abstract void onInteract(PlayerInteractEvent e);

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public ItemID getItemID() {
        return id;
    }

    public Material getMaterial() {
        return mat;
    }
}
