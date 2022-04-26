package io.github.lunaiskey.pyrexprison.items.pyrexitems;

import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Geode extends PyrexItem {

    public Geode(ItemID id) {
        super(id, "&7Geode", null, Material.PLAYER_HEAD);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NmQ3YjJjZjlhMjE3ODRmMzZmNDU5MjE1MWRmYzU1YWE3YzZlZjlhZjRhNDMwYWJkODZkZTBmMDhiMjI0ZCJ9fX0=", UUID.fromString("a2f2daf5-0ffd-4ae5-acf8-4e7193e7228d"));
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        ItemMeta meta = item.getItemMeta();
        switch (getItemID()) {
            case COMMON_GEODE -> meta.setDisplayName(StringUtil.color("&fCommon "+getName()));
            case UNCOMMON_GEODE -> meta.setDisplayName(StringUtil.color("&aUncommon "+getName()));
            case RARE_GEODE -> meta.setDisplayName(StringUtil.color("&9Rare "+getName()));
            case EPIC_GEODE -> meta.setDisplayName(StringUtil.color("&5Epic "+getName()));
            case LEGENDARY_GEODE -> meta.setDisplayName(StringUtil.color("&6Legendary "+getName()));
        }
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7A small rock found while mining."));
        lore.add("");
        lore.add(StringUtil.color("&eHold and R-Click to crack open!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        e.getPlayer().sendMessage(StringUtil.color("&cThis feature is currently unavailable."));
    }
}
