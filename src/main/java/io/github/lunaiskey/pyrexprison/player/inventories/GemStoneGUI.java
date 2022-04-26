package io.github.lunaiskey.pyrexprison.player.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStone;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GemStoneGUI implements PyrexInventory {

    private String name = "Gemstones";
    private int size = 36;
    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.GEMSTONES).getInventory();
    private Map<Integer, GemStoneType> gemStoneTypeMap = new HashMap<>();

    public GemStoneGUI() {
        gemStoneTypeMap.put(11,GemStoneType.AMETHYST);
        gemStoneTypeMap.put(12,GemStoneType.JASPER);
        gemStoneTypeMap.put(13,GemStoneType.OPAL);
        gemStoneTypeMap.put(14,GemStoneType.JADE);
        gemStoneTypeMap.put(15,GemStoneType.TOPAZ);
        gemStoneTypeMap.put(20,GemStoneType.AMBER);
        gemStoneTypeMap.put(21,GemStoneType.SAPPHIRE);
        gemStoneTypeMap.put(22,GemStoneType.EMERALD);
        gemStoneTypeMap.put(23,GemStoneType.RUBY);
        gemStoneTypeMap.put(24,GemStoneType.DIAMOND);
    }

    @Override
    public void init() {
        Map<GemStoneType, GemStone> gemStoneMap = PyrexPrison.getPlugin().getPlayerManager().getGemstonesMap();
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 11,12,13,14,15,20,21,22,23,24 -> inv.setItem(i, gemStoneMap.get(gemStoneTypeMap.get(i)).getItemStack());
                default -> inv.setItem(i,ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
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
        Player p = (Player) e.getWhoClicked();
        switch (e.getRawSlot()) {
            case 11,12,13,14,15,20,21,22,23,24 -> {
                GemStone gemStone = PyrexPrison.getPlugin().getPlayerManager().getGemstonesMap().get(gemStoneTypeMap.get(e.getRawSlot()));
                if (p.getUniqueId().equals(UUID.fromString("cc94070d-11cb-4590-ac72-efbd84d0d282"))) {
                    ItemStack gem = gemStone.getItemStack();
                    if (e.getClick() == ClickType.LEFT) {
                        if (!p.getInventory().addItem(gem).isEmpty()) {
                            p.sendMessage(StringUtil.color("&cYour inventory is full!"));
                        }
                    } else if (e.getClick() == ClickType.RIGHT) {
                        PyrexPrison.getPlugin().getPlayerManager().removeGemstones(p,gemStone.getType(),1);
                    } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                        gem.setAmount(64);
                        if (!p.getInventory().addItem(gem).isEmpty()) {
                            p.sendMessage(StringUtil.color("&cYour inventory is full!"));
                        }
                    } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                        PyrexPrison.getPlugin().getPlayerManager().removeGemstones(p,gemStone.getType(),64);
                    }
                }
            }
        }
    }
}
