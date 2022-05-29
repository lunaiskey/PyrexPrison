package io.github.lunaiskey.pyrexprison.items.pyrexitems;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Geode extends PyrexItem {

    public Geode(ItemID id) {
        super(id, "&7Geode", null, Material.PLAYER_HEAD);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = ItemBuilder.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NmQ3YjJjZjlhMjE3ODRmMzZmNDU5MjE1MWRmYzU1YWE3YzZlZjlhZjRhNDMwYWJkODZkZTBmMDhiMjI0ZCJ9fX0=", UUID.fromString("a2f2daf5-0ffd-4ae5-acf8-4e7193e7228d"));
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
        ItemMeta meta = item.getItemMeta();
        switch (getItemID()) {
            case COMMON_GEODE -> meta.setDisplayName(StringUtil.color("&fCommon "+getName()));
            case UNCOMMON_GEODE -> meta.setDisplayName(StringUtil.color("&aUncommon "+getName()));
            case RARE_GEODE -> meta.setDisplayName(StringUtil.color("&9Rare "+getName()));
            case EPIC_GEODE -> meta.setDisplayName(StringUtil.color("&5Epic "+getName()));
            case LEGENDARY_GEODE -> meta.setDisplayName(StringUtil.color("&6Legendary "+getName()));
        }
        List<String> lore = new ArrayList<>();
        lore.add(StringUtil.color("&7A small rock found while mining."));
        lore.add("");
        lore.add(StringUtil.color("&eHold and R-Click to crack open!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Random rand = PyrexPrison.getPlugin().getRand();
        PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId());
        switch (getItemID()) {
            case COMMON_GEODE -> {
                pyrexPlayer.giveTokens(BigInteger.valueOf(5000));
                pyrexPlayer.giveGems(50);
                p.sendMessage(ChatColor.AQUA+"You found 5000 tokens and 50 gems from the geode.");
            }
            case UNCOMMON_GEODE -> {
                pyrexPlayer.giveTokens(BigInteger.valueOf(10000));
                pyrexPlayer.giveGems(75);
                p.sendMessage(ChatColor.AQUA+"You found 10000 tokens and 75 gems from the geode.");
            }
            case RARE_GEODE -> {
                pyrexPlayer.giveTokens(BigInteger.valueOf(15000));
                pyrexPlayer.giveGems(100);
                p.sendMessage(ChatColor.AQUA+"You found 15000 tokens and 100 gems from the geode.");
            }
            case EPIC_GEODE -> {
                pyrexPlayer.giveTokens(BigInteger.valueOf(20000));
                pyrexPlayer.giveGems(125);
                p.sendMessage(ChatColor.AQUA+"You found 20000 tokens and 125 gems from the geode.");
            }
            case LEGENDARY_GEODE -> {
                pyrexPlayer.giveTokens(BigInteger.valueOf(30000));
                pyrexPlayer.giveGems(150);
                p.sendMessage(ChatColor.AQUA+"You found 30000 tokens and 150 gems from the geode.");
            }
        }
        e.getItem().setAmount(e.getItem().getAmount()-1);
    }
}
