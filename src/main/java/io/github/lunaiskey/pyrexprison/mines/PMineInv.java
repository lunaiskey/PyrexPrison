package io.github.lunaiskey.pyrexprison.mines;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class PMineInv {

    private String name = "Personal Mine";
    private int size = 27;

    private final Inventory inv = new PyrexHolder(name,size, PyrexInvType.PMINE_MAIN).getInventory();

    private void init() {
        for (int i = 0; i < 9;i++) {
            inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
        }
        inv.setItem(11,ItemBuilder.createItem(ChatColor.GREEN+"Teleport to Mine",Material.NETHER_STAR, List.of("",ChatColor.YELLOW+"Click to teleport!")));
        inv.setItem(13,ItemBuilder.createItem(ChatColor.GREEN+"Reset Mine",Material.CLOCK, List.of("", ChatColor.YELLOW+"Click to reset!")));
        inv.setItem(15,ItemBuilder.createItem(ChatColor.GREEN+"Change Mine Blocks",Material.GRASS_BLOCK, List.of("",ChatColor.YELLOW+"Click to view menu!")));

        for (int i = 18; i < 27;i++) {
            inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
        }
    }

    public Inventory getInv() {
        init();
        return inv;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();
        if (slot <= 9 || slot >= 18) {
            return;
        }
        PMine mine = PMineManager.getPMine(p.getUniqueId());
        if (mine != null) {
            switch (slot) {
                case 11 -> {mine.teleportToCenter(p);Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), p::closeInventory);}
                case 13 -> {mine.reset();Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), p::closeInventory);}
                case 15 -> Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineInvBlocks(p).getInv()));

            }
        } else {
            p.sendMessage(ChatColor.RED+"Your Mine hasn't loaded correctly, Please contact an administrator.");
        }
        //p.sendMessage(ChatColor.LIGHT_PURPLE + "This feature is currently a work in progress, Please try again later.");
    }
}
