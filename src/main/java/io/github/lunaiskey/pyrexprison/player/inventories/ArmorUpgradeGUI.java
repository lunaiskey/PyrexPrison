package io.github.lunaiskey.pyrexprison.player.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.armor.Armor;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorPyrexHolder;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorType;
import io.github.lunaiskey.pyrexprison.items.pyrexitems.GemStone;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ArmorUpgradeGUI implements PyrexInventory {

    private String name;
    private int size = 36;
    private final Player player;
    private final PyrexPlayer pyrexPlayer;
    private final Inventory inv;
    private ArmorType type;
    private Map<Integer,AbilityType> abilitySlots = new HashMap<>();
    private static Map<UUID,ArmorType> customColorMap = new HashMap<>();

    public ArmorUpgradeGUI(Player player, ArmorType type) {
        this.player = player;
        this.pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(player.getUniqueId());
        this.type = type;
        this.name = type.getName() + " Upgrades";
        this.inv = new ArmorPyrexHolder(name,size, PyrexInvType.ARMOR_UPGRADES,type).getInventory();
        this.abilitySlots.put(21,AbilityType.SALES_BOOST);
        this.abilitySlots.put(22,AbilityType.ENCHANTMENT_PROC);
        this.abilitySlots.put(23,AbilityType.XP_BOOST);
    }

    @Override
    public void init() {
        for (int i = 0;i<size;i++) {
            switch (i) {
                case 13 -> inv.setItem(i,pyrexPlayer.getArmor().get(type).getItemStack());
                case 21,22,23 -> inv.setItem(i,getUpgradeButton(abilitySlots.get(i)));
                //case 23 -> inv.setItem(i,ItemBuilder.createItem("&c&lTBD",Material.IRON_BARS,null));
                case 11 -> inv.setItem(i, getTierUpButton());
                case 15 -> inv.setItem(i, getColorButton());
                case 0,9,18,27,8,17,26,35 -> inv.setItem(i, ItemBuilder.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,null));
                default -> inv.setItem(i, ItemBuilder.createItem(" ", Material.BLACK_STAINED_GLASS_PANE,null));
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
        Inventory inv = e.getClickedInventory();
        Armor armor = pyrexPlayer.getArmor().get(type);
        boolean isEquiped = pyrexPlayer.isArmorEquiped();
        switch (e.getRawSlot()) {
            case 21,22,23 -> {
                Ability ability = armor.getAbilties().get(abilitySlots.get(e.getRawSlot()));
                if (ability.getLevel() < ability.getMaxLevel()) {
                    if (pyrexPlayer.getGems() >= ability.getCost()) {
                        pyrexPlayer.takeGems(ability.getCost());
                        ability.setLevel(ability.getLevel()+1);
                        player.sendMessage(StringUtil.color("&aUpgraded "+abilitySlots.get(e.getRawSlot()).name()+" to level "+ability.getLevel()+"."));
                        if (isEquiped) {
                            p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                        }

                        Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()-> player.openInventory(new ArmorUpgradeGUI(player,type).getInv()));
                    } else {
                        player.sendMessage(StringUtil.color("&cYou cannot afford this upgrade."));
                    }
                }
            }
            case 11 -> {
                if (armor.getTier() < armor.getTierMax()) {
                    int cost = armor.getCostAmount(armor.getTier()+1);
                    ItemID gemStoneType = armor.getGemstone(armor.getTier()+1);
                    if (PyrexPrison.getPlugin().getPlayerManager().getPyrexItemCount(player,gemStoneType) >= cost) {
                        PyrexPrison.getPlugin().getPlayerManager().removePyrexItem(player,gemStoneType,cost);
                        armor.setTier(armor.getTier()+1);
                        player.sendMessage(StringUtil.color("&aSuccessfully upgraded armor to Tier "+armor.getTier()+"."));
                        if (isEquiped) {
                            p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                        }
                        Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->player.openInventory(new ArmorUpgradeGUI(player,type).getInv()));
                    } else {
                        player.sendMessage(StringUtil.color("&cYou don't have enough of this type of Gemstone."));
                    }
                }
            }
            case 15 -> {
                switch (e.getClick()) {
                    case LEFT,SHIFT_LEFT -> {
                        Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(), p::closeInventory);
                        p.sendMessage("Type in the hex code you want for your piece.");
                        customColorMap.put(p.getUniqueId(),type);
                    }
                    case RIGHT,SHIFT_RIGHT -> {
                        if (armor.getCustomColor() != null) {
                            armor.setCustomColor(null);
                            if (isEquiped) {
                                p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                            }
                            inv.setItem(e.getRawSlot(),getColorButton());
                            inv.setItem(13,armor.getItemStack());
                        }
                    }
                }
            }
        }
    }

    private ItemStack getUpgradeButton(AbilityType abilityType) {
        Material mat = abilityType.getUpgradeMaterial();
        String name = abilityType.getUpgradeName();
        Armor armor = pyrexPlayer.getArmor().get(type);
        int level = armor.getAbilties().get(abilityType).getLevel();
        int maxLevel = armor.getAbilties().get(abilityType).getMaxLevel();
        long cost = armor.getAbilties().get(abilityType).getCost();
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        String levelStr = level > 0 ? ""+level : "";
        meta.setDisplayName(StringUtil.color(name+" "+levelStr));
        List<String> lore = new ArrayList<>();
        Ability ability = armor.getAbilties().get(abilityType);
        for (String str : ability.getDescription()) {
            lore.add(StringUtil.color("&7"+str));
        }
        if (level < maxLevel) {
            lore.add(" ");
            lore.add(StringUtil.color("&7Upgrade: "+armor.getAbilties().get(abilityType).getLoreAddon() +" -> "+armor.getAbilties().get(abilityType).getLoreAddon(level+1)));
            lore.add(StringUtil.color("&7Cost: &a"+ CurrencyType.getUnicode(CurrencyType.GEMS)+"&f"+cost));
            lore.add("");
            lore.add(StringUtil.color("&eClick to upgrade!"));
        } else {
            lore.add(" ");
            lore.add(StringUtil.color("&cYou have maxed this upgrade."));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getTierUpButton() {
        Armor armor = pyrexPlayer.getArmor().get(type);
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aTier Up Piece"));
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use Gemstones to upgrade your"));
        lore.add(StringUtil.color("&7Armor Piece's Tier."));
        lore.add(" ");
        if (armor.getTier() < armor.getTierMax()) {
            GemStone gemStone = (GemStone) PyrexPrison.getPlugin().getItemManager().getItemMap().get(armor.getGemstone(armor.getTier()+1));
            lore.add(StringUtil.color("&7Cost:"));
            lore.add(StringUtil.color("&8- "+gemStone.getName()+" &7x")+armor.getCostAmount(armor.getTier()+1));
            lore.add(" ");
            lore.add(StringUtil.color("&eClick to upgrade."));
        } else {
            lore.add(StringUtil.color("&cYou've reached Max Tier!"));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getColorButton() {
        ItemStack item = new ItemStack(Material.YELLOW_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&aChange "+type.getName()+" Color"));
        Color color = pyrexPlayer.getArmor().get(type).getAWTCustomColor();
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7Use this to update your"));
        lore.add(StringUtil.color("&7Armor Piece's color."));
        lore.add(" ");
        lore.add(StringUtil.color("&7Current Color: [#")+ChatColor.of(color)+Integer.toHexString(color.getRGB()).substring(2).toUpperCase()+ChatColor.GRAY+"]");
        lore.add(" ");
        lore.add(StringUtil.color("&eL-Click to change!"));
        lore.add(StringUtil.color("&eR-Click to reset!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static Map<UUID,ArmorType> getCustomColorMap() {
        return customColorMap;
    }
}
