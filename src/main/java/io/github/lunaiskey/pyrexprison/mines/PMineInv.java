package io.github.lunaiskey.pyrexprison.mines;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
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
        for (int i = 0; i < size;i++) {
            switch(i) {
                case 0,9,18,8,17,26 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 11 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aTeleport to Mine"),Material.COMPASS, List.of(ChatColor.YELLOW+"Click to teleport!")));
                case 12 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aReset Mine"),Material.CLOCK, List.of(ChatColor.YELLOW+"Click to reset!")));
                case 14 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aChange Mine Blocks"),Material.GRASS_BLOCK, List.of(ChatColor.YELLOW+"Click to view menu!")));
                case 15 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&aMine Upgrades"),Material.COMMAND_BLOCK,List.of(StringUtil.color("&eClick to view!"))));
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
            }
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
                case 11 -> {mine.teleportToCenter(p,false,false);Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), p::closeInventory);}
                case 12 -> {mine.reset();Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), p::closeInventory);}
                case 14 -> Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),() -> p.openInventory(new PMineInvBlocks(p).getInv()));
                case 15 -> {p.sendMessage(StringUtil.color("&cThis feature is currently unavailable."));Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), p::closeInventory);}
            }
        } else {
            p.sendMessage(ChatColor.RED+"Your Mine hasn't loaded correctly, Please contact an administrator.");
        }
        //p.sendMessage(ChatColor.LIGHT_PURPLE + "This feature is currently a work in progress, Please try again later.");
    }
}
