package io.github.lunaiskey.pyrexprison.modules.pmines.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.modules.pmines.PMine;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PMineSettingsGUI implements PyrexInventory {

    private static Set<UUID> taxEditSet = new HashSet<>();
    private static Set<UUID> kickPlayerSet = new HashSet<>();

    private final String name = "Settings";
    private final int size = 27;
    private Player player;
    private PyrexPlayer pyrexPlayer;
    private PMine mine;
    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.PMINE_SETTINGS).getInventory();

    public PMineSettingsGUI(Player player) {
        this.player = player;
        this.pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        this.mine = PyrexPrison.getPlugin().getPmineManager().getPMine(player.getUniqueId());
    }

    public PMineSettingsGUI() {}


    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 0,9,18,8,17,26 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                case 11 -> inv.setItem(i,getManagePlayers());
                case 12 -> inv.setItem(i,getTogglePublic());
                case 13 -> inv.setItem(i,getMineTax());
                case 14 -> inv.setItem(i,getKickPlayer());
                case 15 -> inv.setItem(i,getKickAllPlayer());
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
        player = (Player) e.getWhoClicked();
        pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        mine = PyrexPrison.getPlugin().getPmineManager().getPMine(player.getUniqueId());
        int slot = e.getRawSlot();
        switch (slot) {
            case 11 -> {
                player.sendMessage(StringUtil.color("&cThis feature is currently unavailable."));
            }
            case 12 -> {
                mine.setPublic(!mine.isPublic());
                e.getClickedInventory().setItem(slot,getTogglePublic());
            }
            case 13 -> {
                getTaxEditSet().add(player.getUniqueId());
                Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->player.closeInventory());
                player.sendMessage(StringUtil.color("Type in your new tax value."));
            }
            case 14 -> {
                kickPlayerSet.add(player.getUniqueId());
                player.sendMessage(StringUtil.color("Type in the player's name that you want to kick."));
                Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()-> player.closeInventory());
            }
            case 15 -> {
                int counter = 0;
                for (Player kickPlayer : Bukkit.getOnlinePlayers()) {
                    if (mine.isInMineIsland(kickPlayer)) {
                        if (kickPlayer.getUniqueId() != mine.getOwner()) {
                            PyrexPrison.getPlugin().getPmineManager().getPMine(kickPlayer.getUniqueId()).teleportToCenter(kickPlayer,false,true);
                            kickPlayer.sendMessage(StringUtil.color("&eYou've been kicked from "+player.getName()+"'s mine. teleporting to your mine."));
                            counter++;
                        }
                    }
                }
                player.sendMessage(StringUtil.color("&aSuccessfully kicked "+counter+" Players."));
                Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()-> player.closeInventory());
            }
        }
    }

    private ItemStack getManagePlayers() {
        String name = StringUtil.color("&aManage Players");
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Modify all the players that can"));
        lore.add(StringUtil.color("&7join your mine while its private."));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to manage!"));
        return ItemBuilder.createItem(name,Material.IRON_DOOR,lore);
    }

    private ItemStack getTogglePublic() {
        String name = StringUtil.color("&aToggle Public");
        String isPublic = mine.isPublic() ? StringUtil.color("&aPublic") : StringUtil.color("&cPrivate");
        Material mat = mine.isPublic() ? Material.LIME_DYE : Material.RED_DYE;
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Change your mine to be either"));
        lore.add(StringUtil.color("&7Public or Private."));
        lore.add(" ");
        lore.add(StringUtil.color("&7Status: "+isPublic));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to toggle!"));
        return ItemBuilder.createItem(name,mat,lore);
    }

    private ItemStack getMineTax() {
        String name = StringUtil.color("&aMine Tax");
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Modify the tax that's placed"));
        lore.add(StringUtil.color("&7on players as they mine."));
        lore.add(" ");
        lore.add(StringUtil.color("&7Current Tax: &f"+mine.getMineTax()+"%"));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to change!"));
        return ItemBuilder.createItem(name,Material.SUNFLOWER,lore);
    }

    private ItemStack getKickPlayer() {
        String name = StringUtil.color("&aKick Player");
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Remove a specific player from"));
        lore.add(StringUtil.color("&7your mine."));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to kick!"));
        return ItemBuilder.createItem(name,Material.PLAYER_HEAD,lore);
    }

    private ItemStack getKickAllPlayer() {
        String name = StringUtil.color("&aKick All Player");
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Remove all players that"));
        lore.add(StringUtil.color("&7are currently in your mine."));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to kick all!"));
        return ItemBuilder.createItem(name,Material.PLAYER_HEAD,lore);
    }


    public static Set<UUID> getTaxEditSet() {
        return taxEditSet;
    }

    public static Set<UUID> getKickPlayerSet() {
        return kickPlayerSet;
    }
}
