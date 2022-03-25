package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpBoost extends PyrexEnchant {


    public JumpBoost() {
        super("Jump Boost", 3, 0, 0, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {

    }

    @Override
    public void onEquip(Player player, ItemStack pickaxe, int level) {
        if (level <= 0) {
            this.onUnEquip(player, pickaxe, level);
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, level-1, true, true));
    }

    @Override
    public void onUnEquip(Player player, ItemStack pickaxe, int level) {
        player.removePotionEffect(PotionEffectType.JUMP);
    }
}
