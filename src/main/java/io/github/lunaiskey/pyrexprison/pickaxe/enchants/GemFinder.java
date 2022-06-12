package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class GemFinder extends PyrexEnchant {
    public GemFinder() {
        super("Gemstone Finder", List.of("&c[WIP]","Increases your chances of finding Gemstones."),0, CurrencyType.TOKENS,  false);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        //PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).giveGems(0);
    }

    @Override
    public void onDrop(PlayerDropItemEvent e, int level) {

    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }

    @Override
    public BigInteger getEquation(int n) {
        return BigInteger.ZERO;
    }
}
