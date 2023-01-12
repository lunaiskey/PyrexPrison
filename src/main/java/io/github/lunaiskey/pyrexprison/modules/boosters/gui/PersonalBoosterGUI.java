package io.github.lunaiskey.pyrexprison.modules.boosters.gui;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.modules.boosters.Booster;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import io.github.lunaiskey.pyrexprison.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PersonalBoosterGUI implements PyrexInventory {

    private final String name = "Personal Boosters";
    private static Map<UUID,Info> infoMap = new HashMap<>();
    private final int size = 36;
    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.PERSONAL_BOOSTER).getInventory();
    private Player player;

    public PersonalBoosterGUI(Player player) {
        this.player = player;
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 10,11,12,13,14,15,16,19,20,21,22,23,24,25 -> {
                    inv.setItem(i,getBoosterItem(getIndex(i)));
                }
                default -> inv.setItem(i, ItemBuilder.getDefaultFiller());
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

    public void onOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        infoMap.put(p.getUniqueId(),new Info(0));
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Info info = infoMap.get(p.getUniqueId());
        infoMap.remove(p.getUniqueId());
    }

    private ItemStack getBoosterItem(int index) {
        PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        List<Booster> boosters = pyrexPlayer.getBoosters();
        if (index < boosters.size()) {
            Booster booster = boosters.get(index);
            Material material = Material.BEACON;
            String name = booster.getType().getName();
            List<String> lore = new ArrayList<>();
            lore.add(StringUtil.color("&7Multiplier: &f"+booster.getMultiplier()+"x"));
            lore.add(StringUtil.color("&7Ending in: &f"+TimeUtil.countdown(booster.getEndTime())));
            return ItemBuilder.createItem(name,material,lore);
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    public void updateGUI(Player p) {
        Info info = infoMap.get(p.getUniqueId());
        int page = info.getPage();
        Inventory inv = p.getOpenInventory().getTopInventory();
        if (inv.getHolder() instanceof PyrexHolder) {
            PyrexHolder holder = (PyrexHolder) inv.getHolder();
            if (holder.getInvType() == PyrexInvType.PERSONAL_BOOSTER) {
                for (int i = 0;i<14;i++) {
                    int slot = getSlot(i);
                    int index = (page*18)+i;
                    inv.setItem(slot,getBoosterItem(index));
                }
            }
        }
    }

    private int getIndex(int slot) {
        return switch(slot) {
            case 10,11,12,13,14,15,16 -> slot-10;
            case 19,20,21,22,23,24,25 -> slot-12;
            default -> -1;
        };
    }

    private int getSlot(int index) {
        return switch(index) {
            case 0,1,2,3,4,5,6 -> index+10;
            case 7,8,9,10,11,12,13 -> index+12;
            default -> -1;
        };
    }

    public static class Info {
        private int page;

        public Info(int page) {
            this.page = page;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }

}
