package io.github.lunaiskey.pyrexprison.modules.pickaxe.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.EnchantPyrexHolder;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickaxeAddLevelsGUI {

    private String name;
    private int size = 9;
    private Player p;
    private Inventory inv;
    private EnchantType type;
    private CurrencyType currencyType;
    private PyrexPickaxe pickaxe;
    private PyrexPlayer player;
    private PyrexEnchant enchant;
    private Map<Integer,Integer> amountLoc = new HashMap<>();

    public PickaxeAddLevelsGUI(Player p, EnchantType type) {
        this.p = p;
        this.type = type;
        this.player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        pickaxe = player.getPickaxe();
        enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        name = "Add "+enchant.getName()+" Levels";
        inv = new EnchantPyrexHolder(name,size, PyrexInvType.PICKAXE_ENCHANTS_ADD_LEVELS, type).getInventory();
        amountLoc.put(0,1);
        amountLoc.put(1,10);
        amountLoc.put(2,100);
        amountLoc.put(3,1000);
        amountLoc.put(4,10000);
        amountLoc.put(5,Integer.MAX_VALUE);
        currencyType = enchant.getCurrencyType();
    }

    private void init() {
        for (int i = 0; i<size;i++) {
            switch(i) {
                case 0,1,2,3,4,5 -> inv.setItem(i,getAddLevelButton(amountLoc.get(i)));
                case 6,8 -> inv.setItem(i,ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
                case 7 -> inv.setItem(i,pickaxe.getItemStack());
            }
        }
    }

    public Inventory getInv() {
        init();
        return inv;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        switch (e.getRawSlot()) {
            case 0, 1, 2, 3, 4, 5 -> {
                int level = amountLoc.get(e.getRawSlot());
                int start = pickaxe.getEnchants().getOrDefault(type, 0);
                int end = Math.min(start + level, enchant.getMaxLevel());
                if (level == Integer.MAX_VALUE) {
                    Pair<Integer, BigInteger> pair = enchant.getMaxLevelFromAmount(start, player.getTokens());
                    // afford or not
                    if (player.getCurrency(currencyType).compareTo(pair.getRight()) >= 0) {
                        // pickaxe level is max or not
                        if (start < enchant.getMaxLevel()) {
                            if (start != pair.getLeft()) {
                                player.takeCurrency(currencyType,pair.getRight());
                                pickaxe.getEnchants().put(type, pair.getLeft());
                                PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                                p.sendMessage("Purchased " + (pair.getLeft() - start) + " Levels of " + enchant.getName() + ".");
                                Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), () -> p.openInventory(getInv()));
                            }
                        } else {
                            p.sendMessage(StringUtil.color("&cYou have the max level of this enchantment."));
                        }
                    //pair.getRight(); cost
                    //pair.getLeft(); level
                    } else {
                        p.sendMessage(StringUtil.color("&cYou cannot afford any more levels of this enchantment."));
                    }
                } else {
                    BigInteger cost = enchant.getCostBetweenLevels(start, end);
                    if (start < enchant.getMaxLevel()) {
                        if (player.getCurrency(currencyType).compareTo(cost) >= 0) {
                            player.takeCurrency(currencyType,cost);
                            pickaxe.getEnchants().put(type, end);
                            p.sendMessage("Purchased " + (end - start) + " Levels of " + enchant.getName() + ".");
                            PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                            Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), () -> p.openInventory(getInv()));
                        } else {
                            p.sendMessage(StringUtil.color("&cYou cannot afford this level of enchantment."));
                        }
                    } else {
                        p.sendMessage(StringUtil.color("&cYou have the max level of this enchantment."));
                    }
                    //enchant.getCostBetweenLevels(start,end); cost
                    //end; level
                }
            }
        }
    }

    private ItemStack getAddLevelButton(int level) {
        Material mat;
        if (level <= 10) {
            mat = Material.LIME_STAINED_GLASS_PANE;
        } else if (level <= 1000) {
            mat = Material.YELLOW_STAINED_GLASS_PANE;
        } else {
            mat = Material.RED_STAINED_GLASS_PANE;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        String name = level == Integer.MAX_VALUE ? StringUtil.color("&a&l+MAX") : StringUtil.color("&a&l+"+level);
        int start = pickaxe.getEnchants().getOrDefault(type,0);
        int end = Math.min(start + level, enchant.getMaxLevel());
        List<String> lore = new ArrayList<>();
        if (level == Integer.MAX_VALUE) {
            Pair<Integer,BigInteger> pair = enchant.getMaxLevelFromAmount(start, player.getCurrency(currencyType));
            lore.add(StringUtil.color("&a&l| &7Cost: "+currencyType.getColorCode()+currencyType.getUnicode()+"&f"+Numbers.formattedNumber(pair.getRight())));
            lore.add(StringUtil.color("&a&l| &7New Level: &e"+pair.getLeft()));
        } else {
            lore.add(StringUtil.color("&a&l| &7Cost: "+currencyType.getColorCode()+currencyType.getUnicode()+"&f"+ Numbers.formattedNumber(enchant.getCostBetweenLevels(start,end))));
            lore.add(StringUtil.color("&a&l| &7New Level: &e"+end));
        }
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
