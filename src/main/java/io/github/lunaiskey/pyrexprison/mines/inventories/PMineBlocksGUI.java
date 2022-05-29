package io.github.lunaiskey.pyrexprison.mines.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PMineBlocksGUI implements PyrexInventory {

    private static Map<UUID,Material> editMap = new HashMap<>();
    private static Map<UUID,Integer> pageMap = new HashMap<>();

    private String name = "Blocks";
    private int size = 54;
    private Player p;
    private PMine mine;
    private List<Material> materialList;
    private Map<Material,Double> mineComposition;
    private int totalPages;


    public PMineBlocksGUI(Player p) {
        this.p = p;
        this.mine = PyrexPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId());
        materialList = new ArrayList<>(mine.getComposition().keySet());
        mineComposition = mine.getComposition();
        totalPages = (((materialList.size() - materialList.size()%45)/45)+1);
    }

    private final Inventory inv = new PyrexHolder(name,size, PyrexInvType.PMINE_BLOCKS).getInventory();

    @Override
    public void init() {
        if (mine != null) {
            for (int i = 0;i<size;i++) {
                switch (i) {
                    case 1,2,3,5,6,7 -> inv.setItem(i,ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
                    case 0 -> inv.setItem(i,getPreviousPage(0));
                    case 8 -> {
                        int page = 2;
                        if (page > totalPages) {
                            inv.setItem(i,ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
                        } else {
                            inv.setItem(i,getNextPage(2));
                        }
                    }
                    case 4 -> inv.setItem(i,ItemBuilder.createItem(StringUtil.color("&atotal: "+(((materialList.size() - materialList.size()%45)/45)+1)),Material.OBSIDIAN,null));
                    default -> {
                        if (getIndex(i) < materialList.size()) {
                            inv.setItem(i,getBlockItem(materialList.get(getIndex(i)),mineComposition.get(materialList.get(getIndex(i)))));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    private void updateGUI(Player p) {
        Inventory inv = p.getOpenInventory().getTopInventory();
        int page = pageMap.get(p.getUniqueId());
        int pageOffset = page*45;
        for (int i = 0;i<45;i++) {
            if (pageOffset+i < materialList.size()) {
                Material mat = materialList.get(pageOffset+i);
                inv.setItem(getSlot(i),getBlockItem(mat,mineComposition.get(mat)));
            } else {
                inv.setItem(getSlot(i),new ItemStack(Material.AIR));
            }
        }
        inv.setItem(0,getPreviousPage(page));
        inv.setItem(8,getNextPage(page+2));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        int slot = e.getRawSlot();
        int page = pageMap.get(p.getUniqueId());
        int totalPages = ((materialList.size() - materialList.size()%45)/45)+1;
        if (e.getClickedInventory() == e.getView().getTopInventory()) {
            if (slot == 0) {
                if (page > 0) {
                    pageMap.put(p.getUniqueId(),page-1);
                    updateGUI(p);
                } else {
                    Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->p.openInventory(new PMineGUI(p).getInv()));
                }
            }
            if (slot == 8) {
                if (totalPages-1 > page) {
                    pageMap.put(p.getUniqueId(),page+1);
                    updateGUI(p);
                }
            }
            if (slot >= 9) {
                if (item != null && item.getType() != Material.AIR) {
                    editMap.put(p.getUniqueId(),item.getType());
                    Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),p::closeInventory);
                    e.getWhoClicked().sendMessage("Type a number into chat to set the percentage.");
                }
            }
        }
    }

    public void onOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        pageMap.put(player.getUniqueId(),0);
    }

    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        pageMap.remove(player.getUniqueId());
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

    private int getSlot(int index) {
        return index+9;
    }
    private int getIndex(int slot) {
        return slot-9;
    }

    private ItemStack getPreviousPage(int slot) {
        return slot <= 0 ? ItemBuilder.getGoBack() : ItemBuilder.getPreviousPage(slot);
    }

    private ItemStack getNextPage(int slot) {
        if (slot > totalPages) {
            return ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null);
        } else {
            return ItemBuilder.getNextPage(slot);
        }
    }
}
