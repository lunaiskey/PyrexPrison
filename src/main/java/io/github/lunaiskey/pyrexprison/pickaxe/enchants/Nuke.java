package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Nuke extends PyrexEnchant {
    public Nuke() {
        super("Nuke", List.of("&c[WIP]","Obliterates the entire mine."), 5000, CurrencyType.TOKENS, false);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {

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
