package io.github.lunaiskey.pyrexprison.player.armor;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorInv implements PyrexInventory {

    private final String name = "Armor";
    private final int size = 27;
    private final Inventory inv = new PyrexHolder(name,size, PyrexInvType.ARMOR).getInventory();
    private final Player p;
    private final Map<Integer,ArmorType> armorSlots = new HashMap<>();

    public ArmorInv(Player p) {
        this.p = p;
        armorSlots.put(10,ArmorType.HELMET);
        armorSlots.put(11,ArmorType.CHESTPLATE);
        armorSlots.put(12,ArmorType.LEGGINGS);
        armorSlots.put(13,ArmorType.BOOTS);
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    @Override
    public void init() {
        for(int i = 0;i<size;i++) {
            switch(i) {
                case 10,11,12,13 -> inv.setItem(i, PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getArmor().get(armorSlots.get(i)).getItemStack());
                case 16 -> inv.setItem(i,getToggleButton(false));
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    private ItemStack getToggleButton(boolean enabled) {
        if (enabled) {
            ItemStack item = new ItemStack(Material.TORCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtil.color("&e&lArmor Status &7| &a&lEQUIPED"));
            meta.setLore(List.of(StringUtil.color("&7Click to Unequip!")));
            item.setItemMeta(meta);
            return item;
        } else {
            ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtil.color("&e&lArmor Status &7| &c&lUNEQUIPED"));
            meta.setLore(List.of(StringUtil.color("&7Click to Equip!")));
            item.setItemMeta(meta);
            return item;
        }
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
