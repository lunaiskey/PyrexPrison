package io.github.lunaiskey.pyrexprison.modules.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class XPBoost extends PyrexEnchant {

    public XPBoost() {
        super("XP Boost", List.of("&c[WIP]","Boosts the XP gained per block."), 100, CurrencyType.TOKENS, false);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {

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
    public BigInteger getCost(int n) {
        return BigInteger.ZERO;
    }
}
