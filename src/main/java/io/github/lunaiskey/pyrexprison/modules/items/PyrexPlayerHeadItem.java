package io.github.lunaiskey.pyrexprison.modules.items;

import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.nms.NBTTags;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class PyrexPlayerHeadItem extends PyrexItem {

    public PyrexPlayerHeadItem(ItemID id, String name, List<String> lore, Rarity rarity) {
        super(id, name, lore, rarity, Material.PLAYER_HEAD);
    }

    public PyrexPlayerHeadItem(ItemID id, String name, List<String> lore) {
        this(id,name,lore,Rarity.COMMON);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = super.getItemStack();
        return ItemBuilder.replaceSkullTexture(itemStack,getHeadBase64(),getHeadUUID());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {

    }

    public abstract UUID getHeadUUID();

    public abstract String getHeadBase64();
}
