package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Efficiency extends PyrexEnchant {

    public Efficiency() {
        super("Efficiency", 100, 0, 0, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {
        if (pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED) != level) {
            pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED,level);
        }
    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {

    }
}
