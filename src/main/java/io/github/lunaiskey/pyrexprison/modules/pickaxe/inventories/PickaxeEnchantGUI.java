package io.github.lunaiskey.pyrexprison.modules.pickaxe.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickaxeEnchantGUI {

    private String name = "Enchantments";
    private int size = 54;
    private Player p;
    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.PICKAXE_ENCHANTS).getInventory();
    private PyrexPickaxe pickaxe;
    private static Map<Integer, EnchantType> enchantLocation = new HashMap<>();

    static {
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
    public PickaxeEnchantGUI(Player p) {
        this.p = p;
        pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxe();
    }

    private void init() {
        for(int i = 0; i < size;i++) {
            switch(i) {
                case 20,21,22,23,24,29,30,31,32,33,38,39,40,41,42 -> {
                    if (enchantLocation.containsKey(i)) {
                        inv.setItem(i,getEnchantPlaceholder(enchantLocation.get(i)));
                    } else {
                        inv.setItem(i, ItemBuilder.createItem("&c&lCOMING SOON", Material.BEDROCK,null));
                    }
                }
                case 11 -> inv.setItem(i,getEnchantToggleIcon());
                case 13 -> inv.setItem(i, pickaxe.getItemStack());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    public Inventory getInv() {
        init();
        return inv;
    }

    private ItemStack getEnchantToggleIcon() {
        String name = "&aToggle Enchants";
        Material mat = Material.GREEN_DYE;
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&eClick to view!"));
        return ItemBuilder.createItem(name,mat,lore);
    }

    private ItemStack getEnchantPlaceholder(EnchantType type) {
        PyrexEnchant enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        Material mat = Material.ENCHANTED_BOOK;
        if (!enchant.isEnabled()) {
            mat = Material.BARRIER;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        CurrencyType currencyType = enchant.getCurrencyType();
        int level = pickaxe.getEnchants().getOrDefault(type, 0);
        meta.setDisplayName(StringUtil.color("&b"+enchant.getName()+" &8[&7"+level+" -> "+(level+1)+"&8]"));
        List<String> lore = new ArrayList<>();
        if (enchant.getDescription() != null && !enchant.getDescription().isEmpty()) {
            for (String desc : enchant.getDescription()) {
                lore.add(StringUtil.color("&7"+desc));
            }
        }
        lore.add(" ");
        if (enchant.isEnabled()) {
            if (level >= enchant.getMaxLevel()) {
                lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
                lore.add(" ");
                lore.add(StringUtil.color("&7Enchant is max level!"));
            } else {
                lore.add(StringUtil.color("&7Cost: "+currencyType.getColorCode()+currencyType.getUnicode()+"&f"+ Numbers.formattedNumber(enchant.getCostBetweenLevels(level,level+1))));
                lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
                lore.add(" ");
                lore.add(StringUtil.color("&eL-Click to purchase levels."));
            }
        } else {
            lore.add(StringUtil.color("&cEnchantment is currently disabled."));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        if (slot == 11) {
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->p.openInventory(new PickaxeEnchantToggleGUI(p).getInv()));
            return;
        }
        if (enchantLocation.containsKey(slot)) {
            PyrexEnchant enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(enchantLocation.get(slot));
            int level = pickaxe.getEnchants().getOrDefault(enchantLocation.get(slot), 0);
            if (enchant.isEnabled()) {
                if (level < enchant.getMaxLevel()) {
                    Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->p.openInventory(new PickaxeAddLevelsGUI(p,enchantLocation.get(slot)).getInv()));
                } else {
                    p.sendMessage(StringUtil.color("&cYou have maxed out this enchantment."));
                }
            } else {
                p.sendMessage(StringUtil.color("&cThis enchantment is currently unavailable."));
            }
        }
    }
}
