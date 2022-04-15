package io.github.lunaiskey.pyrexprison.player.armor;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ArmorUpgradeInv implements PyrexInventory {

    private String name;
    private int size = 36;
    private final Player player;
    private final PyrexPlayer pyrexPlayer;
    private final Inventory inv;
    private ArmorType type;

    public ArmorUpgradeInv(Player player,ArmorType type) {
        this.player = player;
        this.pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        this.type = type;
        this.name = type.getName() + " Upgrades";
        this.inv = new ArmorPyrexHolder(name,size, PyrexInvType.ARMOR_UPGRADES,type).getInventory();
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 13 -> inv.setItem(i,pyrexPlayer.getArmor().get(type).getItemStack());
                case 20 -> inv.setItem(i,ItemBuilder.createItem("&eSales Boost Upgrade",Material.SUNFLOWER,null));
                case 21 -> inv.setItem(i,ItemBuilder.createItem("&eEnchantment Proc Upgrade",Material.NETHER_STAR,null));
                case 22 -> inv.setItem(i,ItemBuilder.createItem("&c&lTBD",Material.IRON_BARS,null));
                case 23 -> inv.setItem(i,ItemBuilder.createItem("&c&lTBD",Material.IRON_BARS,null));
                case 24 -> inv.setItem(i,ItemBuilder.createItem("&c&lTBD",Material.IRON_BARS,null));
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
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
        switch (e.getRawSlot()) {
            case 20,21,22,23,24 -> e.getWhoClicked().sendMessage(StringUtil.color("&cThis upgrade isn't implemented yet, try again later."));
        }
    }
}
