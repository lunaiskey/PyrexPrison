package io.github.lunaiskey.pyrexprison.player.inventories;

import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PlayerMenuGUI implements PyrexInventory {

    private final String name = "Player Menu [WIP]";
    private final int size = 54;
    private final Inventory inv = new PyrexHolder(name,size, PyrexInvType.PLAYER_MENU).getInventory();

    @Override
    public void init() {

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
}
