package io.github.lunaiskey.pyrexprison.items.pyrexitems;

import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.inventories.PlayerMenuGUI;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerMenu extends PyrexItem {

    public PlayerMenu() {
        super(ItemID.PLAYER_MENU, "&aPlayer Menu", null, Material.NETHER_STAR);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = ItemBuilder.createItem(getName(),getMaterial(), getLore());
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        return item;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        p.openInventory(new PlayerMenuGUI().getInv());
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use this item to view the player menu!"));
        lore.add(StringUtil.color(" "));
        lore.add(StringUtil.color("&eClick to open!"));
        return lore;
    }
}
