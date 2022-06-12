package io.github.lunaiskey.pyrexprison.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface PyrexInventory {

    void init();

    Inventory getInv();

    void onClick(InventoryClickEvent e);
}
