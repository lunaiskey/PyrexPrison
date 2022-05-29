package io.github.lunaiskey.pyrexprison.mines.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.mines.PMine;
import io.github.lunaiskey.pyrexprison.mines.PMineManager;
import io.github.lunaiskey.pyrexprison.mines.upgrades.PMineUpgrade;
import io.github.lunaiskey.pyrexprison.mines.upgrades.PMineUpgradeType;
import io.github.lunaiskey.pyrexprison.mines.upgrades.upgrades.Size;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PMineUpgradesGUI implements PyrexInventory {

    private final Player player;
    private final String name = "Upgrades";
    private final int size = 36;
    private final PyrexPlayer pyrexPlayer;
    private final PMine mine;
    private final Map<PMineUpgradeType,PMineUpgrade> upgradeMap = PMineUpgradeType.getUpgradeMap();
    private final Map<PMineUpgradeType,Integer> upgradeLevelMap;
    private static Set<UUID> pendingSizeReset = new HashSet<>();

    private final Map<Integer, PMineUpgradeType> typeMap = new HashMap<>();

    private Inventory inv = new PyrexHolder(name,size, PyrexInvType.PMINE_UPGRADES).getInventory();

    public PMineUpgradesGUI(Player player) {
        this.player = player;
        this.pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        this.mine = PyrexPrison.getPlugin().getPmineManager().getPMine(player.getUniqueId());
        this.upgradeLevelMap = mine.getUpgradeMap();
        typeMap.put(20,PMineUpgradeType.SIZE);
        typeMap.put(22,PMineUpgradeType.SELL_PRICE);
        typeMap.put(24,PMineUpgradeType.MAX_PLAYERS);
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 13 -> inv.setItem(i,getPlayerSkull(player));
                case 20,22,24 -> inv.setItem(i,getUpgradeIcon(typeMap.get(i)));
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ",Material.PURPLE_STAINED_GLASS_PANE,null));
                default -> inv.setItem(i, ItemBuilder.createItem(" ",Material.BLACK_STAINED_GLASS_PANE,null));
            }
        }
    }

    @Override
    public Inventory getInv() {
        init();
        return inv;
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (pendingSizeReset.contains(p.getUniqueId())) {
            mine.genBedrock();
            mine.reset();
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        PMine mine = PyrexPrison.getPlugin().getPmineManager().getPMine(p.getUniqueId());
        PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getWhoClicked().getUniqueId());
        switch (slot) {
            case 20 -> {
                int level = upgradeLevelMap.get(PMineUpgradeType.SIZE);
                Size size = (Size) upgradeMap.get(PMineUpgradeType.SIZE);
                if (level < size.getMaxLevel()) {
                    if (pyrexPlayer.getGems() >= size.getCost(level+1)) {
                        pyrexPlayer.takeGems(size.getCost(level+1));
                        mine.setRadius(12 + size.getRadiusIncrease(level+1));
                        upgradeLevelMap.put(PMineUpgradeType.SIZE,level+1);
                        e.getClickedInventory().setItem(slot,getUpgradeIcon(typeMap.get(slot)));
                        pendingSizeReset.add(p.getUniqueId());
                    } else {
                        p.sendMessage(StringUtil.color("You can't afford this upgrade."));
                    }
                } else {
                    p.sendMessage(StringUtil.color("You've already maxed out this upgrade."));
                }
            }
            case 22,24 -> p.sendMessage(StringUtil.color("&cThis upgrade is currently unavailable."));
        }
    }

    private ItemStack getUpgradeIcon(PMineUpgradeType type) {
        CurrencyType currencyType = CurrencyType.GEMS;
        ItemStack item = new ItemStack(getMaterial(type));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(getName(type)));
        List<String> lore = new ArrayList<>();

        PMineUpgrade upgrade = PMineUpgradeType.getUpgradeMap().get(type);
        int level = upgradeLevelMap.get(type);
        for (String desc : upgrade.getDescription()) {
            lore.add(StringUtil.color("&7"+desc));
        }
        lore.add(" ");
        lore.add(StringUtil.color("&7Upgrade: &b"+upgrade.getUpgradeLore(level+1)));
        lore.add(StringUtil.color("&7Cost: &a"+CurrencyType.getUnicode(currencyType)+"&f"+(upgrade.getCost(level+1))));
        lore.add(" ");
        lore.add(StringUtil.color("&eClick to upgrade!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlayerSkull(Player p) {
        ItemStack item = ItemBuilder.getPlayerSkull(p);
        return item;
    }

    private Material getMaterial(PMineUpgradeType type) {
        return switch (type) {
            case SIZE -> Material.CHEST;
            case SELL_PRICE -> Material.SUNFLOWER;
            case MAX_PLAYERS -> Material.PLAYER_HEAD;
        };
    }

    private String getName(PMineUpgradeType type) {
        return switch (type) {
            case SIZE -> "&eSize";
            case SELL_PRICE -> "&eSell Price";
            case MAX_PLAYERS -> "&eMax Players";
        };
    }
}
