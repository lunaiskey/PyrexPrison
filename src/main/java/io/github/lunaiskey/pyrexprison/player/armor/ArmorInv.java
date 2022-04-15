package io.github.lunaiskey.pyrexprison.player.armor;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
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
                case 0,9,18,8,17,26 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 10,11,12,13 -> inv.setItem(i, PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getArmor().get(armorSlots.get(i)).getItemStack());
                case 16 -> inv.setItem(i,getToggleButton(PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).isArmorEquiped()));
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    private ItemStack getToggleButton(boolean enabled) {
        ItemStack item;
        if (enabled) {
            item = new ItemStack(Material.REDSTONE_TORCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtil.color("&e&lArmor Status &7| &a&lEQUIPED"));
            meta.setLore(List.of(StringUtil.color("&7Click to Unequip!")));
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.TORCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtil.color("&e&lArmor Status &7| &c&lUNEQUIPED"));
            meta.setLore(List.of(StringUtil.color("&7Click to Equip!")));
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        boolean armorEquiped = pyrexPlayer.isArmorEquiped();
        if (e.getRawSlot() == 16) {
            if (armorEquiped) {
                p.getInventory().setHelmet(null);
                p.getInventory().setChestplate(null);
                p.getInventory().setLeggings(null);
                p.getInventory().setBoots(null);
                pyrexPlayer.setArmorEquiped(false);
                e.getInventory().setItem(16,getToggleButton(false));
            } else {
                p.getInventory().setHelmet(pyrexPlayer.getHelmet().getItemStack());
                p.getInventory().setChestplate(pyrexPlayer.getChestplate().getItemStack());
                p.getInventory().setLeggings(pyrexPlayer.getLeggings().getItemStack());
                p.getInventory().setBoots(pyrexPlayer.getBoots().getItemStack());
                pyrexPlayer.setArmorEquiped(true);
                e.getInventory().setItem(16,getToggleButton(true));
            }
        }
        switch (e.getRawSlot()) {
            case 10,11,12,13 -> Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new ArmorUpgradeInv(p,armorSlots.get(e.getRawSlot())).getInv()));
        }
    }
}
