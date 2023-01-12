package io.github.lunaiskey.pyrexprison.modules.player.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.modules.player.ViewPlayerHolder;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayerGUI implements PyrexInventory {

    private String name;
    private int size = 45;
    private Inventory inv;
    private Player viewing;
    private PyrexPlayer pyrexPlayer;

    public ViewPlayerGUI(Player toView) {
        this.viewing = toView;
        this.name = viewing.getName()+"'s Profile";
        this.inv = new ViewPlayerHolder(name,size, PyrexInvType.VIEW_PLAYER,viewing).getInventory();
        this.pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(viewing.getUniqueId());
    }

    @Override
    public void init() {
        for(int i = 0;i<size;i++) {
            switch(i) {
                case 10 -> inv.setItem(i,pyrexPlayer.getHelmet().getItemStack());
                case 12 -> inv.setItem(i,pyrexPlayer.getChestplate().getItemStack());
                case 14 -> inv.setItem(i,pyrexPlayer.getLeggings().getItemStack());
                case 16 -> inv.setItem(i,pyrexPlayer.getBoots().getItemStack());
                case 30 -> inv.setItem(i,pyrexPlayer.getPickaxe().getItemStack());
                case 32 -> inv.setItem(i,getPlayerStats());
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    private ItemStack getPlayerStats() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&ePlayer Stats"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&eTokens: &f"+ Numbers.formattedNumber(pyrexPlayer.getTokens())));
        lore.add(StringUtil.color("&aGems: &f"+ Numbers.formattedNumber(pyrexPlayer.getGems())));
        lore.add(StringUtil.color("&dPyrexPoints: &f"+ Numbers.formattedNumber(pyrexPlayer.getPyrexPoints())));
        lore.add(StringUtil.color("&bRank: &f"+pyrexPlayer.getRank()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
