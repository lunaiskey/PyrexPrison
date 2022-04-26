package io.github.lunaiskey.pyrexprison.player.inventories;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.gui.PyrexInventory;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.armor.Armor;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorPyrexHolder;
import io.github.lunaiskey.pyrexprison.player.armor.ArmorType;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStone;
import io.github.lunaiskey.pyrexprison.player.armor.gemstones.GemStoneType;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.Ability;
import io.github.lunaiskey.pyrexprison.player.armor.upgrades.AbilityType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorUpgradeGUI implements PyrexInventory {

    private String name;
    private int size = 36;
    private final Player player;
    private final PyrexPlayer pyrexPlayer;
    private final Inventory inv;
    private ArmorType type;
    private Map<Integer,AbilityType> abilitySlots = new HashMap<>();

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
        Armor armor = pyrexPlayer.getArmor().get(type);
        switch (e.getRawSlot()) {
            case 21,22,23 -> {
                Ability ability = armor.getAbilties().get(abilitySlots.get(e.getRawSlot()));
                if (ability.getLevel() < ability.getMaxLevel()) {
                    if (pyrexPlayer.getGems() >= ability.getNextCost()) {
                        ability.setLevel(ability.getLevel()+1);
                        pyrexPlayer.takeGems(ability.getNextCost());
                        player.sendMessage(StringUtil.color("&aUpgraded "+abilitySlots.get(e.getRawSlot()).name()+" to level "+ability.getLevel()+"."));
                        p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                        Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()-> player.openInventory(new ArmorUpgradeGUI(player,type).getInv()));
                    } else {
                        player.sendMessage(StringUtil.color("&cYou cannot afford this upgrade."));
                    }
                }
            }
            //case 22,23,24 -> e.getWhoClicked().sendMessage(StringUtil.color("&cThis upgrade isn't implemented yet, try again later."));
            case 11 -> {
                if (armor.getTier() < armor.getTierMax()) {
                    int cost = armor.getCostAmount(armor.getTier()+1);
                    GemStoneType gemStoneType = armor.getGemstone(armor.getTier()+1);
                    if (PyrexPrison.getPlugin().getPlayerManager().getGemstoneCount(player,gemStoneType) >= cost) {
                        PyrexPrison.getPlugin().getPlayerManager().removeGemstones(player,gemStoneType,cost);
                        armor.setTier(armor.getTier()+1);
                        player.sendMessage(StringUtil.color("&aSuccessfully upgraded armor to Tier "+armor.getTier()+"."));
                        p.getInventory().setItem(type.getSlot(),armor.getItemStack());
                        Bukkit.getScheduler().runTask(PyrexPrison.getPlugin(),()->player.openInventory(new ArmorUpgradeGUI(player,type).getInv()));
                    } else {
                        player.sendMessage(StringUtil.color("&cYou don't have enough of this type of Gemstone."));
                    }
                }
            }
        }
    }

    private ItemStack getUpgradeButton(AbilityType abilityType) {
        Material mat = Material.IRON_BARS;
        String name = "Set the name in getUpgradeButton stupid...";
        Armor armor = pyrexPlayer.getArmor().get(type);
        int level = armor.getAbilties().get(abilityType).getLevel();
        int maxLevel = armor.getAbilties().get(abilityType).getMaxLevel();
        long cost = armor.getAbilties().get(abilityType).getCost();
        switch (abilityType) {
            case SALES_BOOST -> {
                mat = Material.SUNFLOWER;
                name = "&eSales Boost";
            }
            case ENCHANTMENT_PROC -> {
                mat = Material.NETHER_STAR;
                name = "&eEnchantment Proc";
            }
            case XP_BOOST -> {
                mat = Material.EXPERIENCE_BOTTLE;
                name = "&eXP Boost";
            }
        }
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
            GemStone gemStone = PyrexPrison.getPlugin().getPlayerManager().getGemstonesMap().get(armor.getGemstone(armor.getTier()+1));
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
        lore.add(StringUtil.color("&eClick to change!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
