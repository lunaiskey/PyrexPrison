package io.github.lunaiskey.pyrexprison.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PyrexHolder implements InventoryHolder {

    private final Inventory inventory;
    private final PyrexInvType invType;

    public PyrexHolder(String name, int size, PyrexInvType invType) {
        this.inventory = Bukkit.createInventory(this,size,name);
        this.invType = invType;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public PyrexInvType getInvType() { return invType; }


}
