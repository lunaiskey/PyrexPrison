package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
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

public class EnchantInv {

    private String name = "Enchantments";
    private int size = 54;
    private Player p;
    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.ENCHANTS).getInventory();
    private PyrexPickaxe pickaxe;
    private Map<Integer,EnchantType> enchantLocation = new HashMap<>();

    public EnchantInv(Player p) {
        this.p = p;
        pickaxe = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId()).getPickaxe();
        enchantLocation.put(20,EnchantType.FORTUNE);
        enchantLocation.put(21,EnchantType.JACK_HAMMER);
        enchantLocation.put(23,EnchantType.GEM_FINDER);
        enchantLocation.put(24,EnchantType.KEY_FINDER);
        enchantLocation.put(22,EnchantType.STRIKE);
    }

    private void init() {
        for(int i = 0; i < size;i++) {
            switch(i) {
                case 20,21,22,23,24,29,30,31,32,33,38,39,40,41,42 -> {
                    if (enchantLocation.containsKey(i)) {
                        inv.setItem(i,getEnchantPlaceholder(enchantLocation.get(i)));
                    } else {
                        inv.setItem(i, ItemBuilder.createItem("&c&lTBA", Material.BARRIER,null));
                    }
                }
                case 13 -> inv.setItem(i, pickaxe.getItemStack());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    public Inventory getInv() {
        init();
        return inv;
    }

    private ItemStack getEnchantPlaceholder(EnchantType type) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        PyrexEnchant enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
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


        if (level == enchant.getMaxLevel()) {
            lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
            lore.add(" ");
            lore.add(StringUtil.color("&7Enchant is max level!"));
            lore.add(" ");
        } else {
            lore.add(StringUtil.color("&7Cost: "+CurrencyType.getColorCode(currencyType)+ CurrencyType.getUnicode(currencyType)+"&f"+enchant.getCostBetweenLevels(level,level+1)));
            lore.add(StringUtil.color("&7Max Level: &f"+enchant.getMaxLevel()));
            lore.add(" ");
        }
        lore.add(StringUtil.color("&eL-Click to purchase levels."));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (enchantLocation.containsKey(e.getRawSlot())) {
            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->p.openInventory(new AddLevelsInv(p,enchantLocation.get(e.getRawSlot())).getInv()));
        }
    }
}
