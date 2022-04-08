package io.github.lunaiskey.pyrexprison.mines;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PMineInvBlocks {

    private static Map<UUID,Material> editMap = new HashMap<>();

    private String name = "Blocks";
    private int size = 27;
    private Player p;

    public PMineInvBlocks(Player p) {
        this.p = p;
    }

    private final Inventory inv = new PyrexHolder(name,size, PyrexInvType.PMINE_BLOCKS).getInventory();

    private void init() {
        int counter = 0;
        PMine mine = PMineManager.getPMine(p.getUniqueId());
        if (mine != null) {
            Map<Material,Double> map = mine.getComposition();
            for (Material mat : map.keySet()) {
                inv.setItem(counter,getBlockItem(mat,map.get(mat)));
                counter++;
            }
        }
    }

    public Inventory getInv() {
        init();
        return inv;
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (e.getClickedInventory() == e.getView().getTopInventory()) {
            if (item != null || item.getType() != Material.AIR) {
                editMap.put(p.getUniqueId(),item.getType());
                Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),p::closeInventory);
                e.getWhoClicked().sendMessage("Type a number into chat to set the percentage.");
            }
        }
    }

    private ItemStack getBlockItem(Material material, double chance) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(List.of(ChatColor.GRAY + "Chance: " + ChatColor.LIGHT_PURPLE + chance*100 + "%"," ",ChatColor.YELLOW + "Click to modify!"));
        item.setItemMeta(meta);
        return item;
    }

    public static Map<UUID, Material> getEditMap() {
        return editMap;
    }
}
