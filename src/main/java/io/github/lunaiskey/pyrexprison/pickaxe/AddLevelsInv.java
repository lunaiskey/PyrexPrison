package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AddLevelsInv {

    private String name;
    private int size = 9;
    private Player p;
    private Inventory inv;
    private EnchantType type;
    private PyrexPickaxe pickaxe;
    private PyrexPlayer player;
    private PyrexEnchant enchant;

    public AddLevelsInv(Player p, EnchantType type) {
        this.p = p;
        this.type = type;
        this.player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        pickaxe = player.getPickaxe();
        enchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(type);
        name = "Add "+enchant.getName()+" Levels";
        inv = new EnchantPyrexInv(name,size, PyrexInvType.ADD_LEVELS, type).getInventory();
    }

    private void init() {
        for (int i = 0; i<size;i++) {
            switch(i) {
                case 0 -> inv.setItem(i,getAddLevelButton(1));
                case 1 -> inv.setItem(i,getAddLevelButton(10));
                case 2 -> inv.setItem(i,getAddLevelButton(100));
                case 3 -> inv.setItem(i,getAddLevelButton(1000));
                case 4 -> inv.setItem(i,getAddLevelButton(10000));
                case 5 -> inv.setItem(i,getAddLevelButton(Integer.MAX_VALUE));
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
            Pair<Integer,Long> pair = enchant.getMaxLevelFromAmount(start, player.getTokens());
            lore.add(StringUtil.color("&a&l| &7Cost: &e"+ CurrencyType.getUnicode(CurrencyType.TOKENS)+"&f"+Numbers.formattedNumber(pair.getRight())));
            lore.add(StringUtil.color("&a&l| &7New Level: &e"+pair.getLeft()));
        } else {
            lore.add(StringUtil.color("&a&l| &7Cost: &e"+ CurrencyType.getUnicode(CurrencyType.TOKENS)+"&f"+ Numbers.formattedNumber(enchant.getCostBetweenLevels(start,end))));
            lore.add(StringUtil.color("&a&l| &7New Level: &e"+end));
        }
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
