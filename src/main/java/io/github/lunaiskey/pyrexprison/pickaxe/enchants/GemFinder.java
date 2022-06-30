package io.github.lunaiskey.pyrexprison.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.pyrexitems.GemStone;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;


public class GemFinder extends PyrexEnchant {
    public GemFinder() {
        super("Gemstone Finder", List.of("Increases your chances of finding Gemstones."),1000, CurrencyType.TOKENS,  true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Player player = e.getPlayer();
        Random rand = PyrexPrison.getPlugin().getRand();
        double roll = rand.nextDouble();
        if (roll*100 <= getChance(level)) {
            int gemNum;
            ItemID id;
            gemNum = (int) (Math.random() * (level / 100)) + 1;
            GemStone gem = getStone(gemNum);
            player.getInventory().addItem(gem.getItemStack());
        }
        //PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(e.getPlayer().getUniqueId()).giveGems(0);
    }

    private double getChance(int level) {
        return 0.001D * level;
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
        if (n <= 50) {
            return BigInteger.valueOf(12500+(12500L*(n)));
        } else if (n <= 100){
            return getCost(50).add(BigInteger.valueOf(25000+(25000L*(n-50))));
        } else {
            return getCost(100).add(BigInteger.valueOf(50000+(50000L*(n-100))));
        }
    }

    public GemStone getStone(int num){
        return switch (num){
            case 1 -> new GemStone(ItemID.AMETHYST_GEMSTONE);
            case 2 -> new GemStone(ItemID.JASPER_GEMSTONE);
            case 3 -> new GemStone(ItemID.OPAL_GEMSTONE);
            case 4 -> new GemStone(ItemID.JADE_GEMSTONE);
            case 5 -> new GemStone(ItemID.TOPAZ_GEMSTONE);
            case 6 -> new GemStone(ItemID.AMBER_GEMSTONE);
            case 7 -> new GemStone(ItemID.SAPPHIRE_GEMSTONE);
            case 8 -> new GemStone(ItemID.EMERALD_GEMSTONE);
            case 9 -> new GemStone(ItemID.RUBY_GEMSTONE);
            case 10 -> new GemStone(ItemID.DIAMOND_GEMSTONE);
            default -> new GemStone(ItemID.AMETHYST_GEMSTONE);
        };
    }
}
