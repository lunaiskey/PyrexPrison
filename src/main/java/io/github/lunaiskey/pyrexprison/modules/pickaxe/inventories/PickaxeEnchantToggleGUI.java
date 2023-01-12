package io.github.lunaiskey.pyrexprison.modules.pickaxe.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PickaxeEnchantToggleGUI implements PyrexInventory {

    private String name = "Toggle Enchants";
    private int size = 54;
    private Player p;
    private PyrexPlayer pyrexPlayer;
    private PyrexPickaxe pyrexPickaxe;
    private Set<EnchantType> disabledEnchants;

    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.PICKAXE_ENCHANTS_TOGGLE).getInventory();

    private Map<Integer, EnchantType> enchantLocation = new HashMap<>();

    public PickaxeEnchantToggleGUI(Player p) {
        this.p = p;
        this.pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        this.pyrexPickaxe = pyrexPlayer.getPickaxe();
        this.disabledEnchants = pyrexPickaxe.getDisabledEnchants();
        enchantLocation.put(11,EnchantType.EFFICIENCY);
        enchantLocation.put(12,EnchantType.HASTE);
        enchantLocation.put(13,EnchantType.SPEED);
        enchantLocation.put(14,EnchantType.JUMP_BOOST);
        enchantLocation.put(15,EnchantType.NIGHT_VISION);
        enchantLocation.put(20,EnchantType.FORTUNE);
        enchantLocation.put(21,EnchantType.JACK_HAMMER);
        enchantLocation.put(22,EnchantType.STRIKE);
        enchantLocation.put(23,EnchantType.EXPLOSIVE);
        enchantLocation.put(24,EnchantType.MINE_BOMB);
        enchantLocation.put(29,EnchantType.NUKE);
        enchantLocation.put(30,EnchantType.GEM_FINDER);
        enchantLocation.put(31,EnchantType.KEY_FINDER);
        enchantLocation.put(32,EnchantType.LOOT_FINDER);
        enchantLocation.put(33,EnchantType.XP_BOOST);
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 11,12,13,14,15,20,21,22,23,24,29,30,31,32,33 -> inv.setItem(i, getEnchantPlaceholder(enchantLocation.get(i)));
                case 0 -> inv.setItem(i,ItemBuilder.getGoBack());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    private ItemStack getEnchantPlaceholder(EnchantType type) {
        PyrexEnchant enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        String name = StringUtil.color("&b"+enchant.getName());
        String status = disabledEnchants.contains(type) ? "&cDisabled" : "&aEnabled";
        Material mat = disabledEnchants.contains(type) ? Material.GRAY_DYE : Material.LIME_DYE;
        List<String> lore = new ArrayList<>();
        if (enchant.getDescription() != null && !enchant.getDescription().isEmpty()) {
            for (String desc : enchant.getDescription()) {
                lore.add(StringUtil.color("&7"+desc));
            }
            lore.add(" ");
        }
        lore.add(StringUtil.color("&7Status: "+status));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to toggle!"));
        return ItemBuilder.createItem(name,mat,lore);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Inventory inv = e.getClickedInventory();
        if (slot == 0) {
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantGUI(p).getInv()));
            return;
        }
        if (enchantLocation.containsKey(slot)) {
            EnchantType enchantType = enchantLocation.get(slot);
            if (disabledEnchants.contains(enchantType)) {
                disabledEnchants.remove(enchantType);
            } else {
                disabledEnchants.add(enchantType);
            }
            inv.setItem(slot,getEnchantPlaceholder(enchantType));
        }
    }
}
