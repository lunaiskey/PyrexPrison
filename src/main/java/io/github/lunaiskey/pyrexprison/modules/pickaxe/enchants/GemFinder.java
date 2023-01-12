package io.github.lunaiskey.pyrexprison.modules.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.items.ItemID;
import io.github.lunaiskey.pyrexprison.modules.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
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
            if (level > getMaxLevel()) {
                level = getMaxLevel();
            }
            int j = getMaxLevel()/10;
            int gemNum = rand.nextInt((((level-1)-((level-1) % j))/j)+1)+1;
            //int gemNum = (int) (Math.random() * (level / 100)) + 1;
            PyrexItem gemstone =  PyrexPrison.getPlugin().getItemManager().getItemMap().get(getGemstone(gemNum));
            ItemStack gemstoneItem = gemstone.getItemStack();
            player.getInventory().addItem(gemstoneItem);
            //player.sendMessage("GEM_FINDER: "+gemstone.getItemID());
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
        if (n <= 500) {
            return BigInteger.valueOf(12500+(12500L*(n)));
        } else {
            return getCost(500).add(BigInteger.valueOf(25000+(25000L*(n-500))));
        }
    }

    public ItemID getGemstone(int num){
        return switch (num){
            case 1 -> ItemID.AMETHYST_GEMSTONE;
            case 2 -> ItemID.JASPER_GEMSTONE;
            case 3 -> ItemID.OPAL_GEMSTONE;
            case 4 -> ItemID.JADE_GEMSTONE;
            case 5 -> ItemID.TOPAZ_GEMSTONE;
            case 6 -> ItemID.AMBER_GEMSTONE;
            case 7 -> ItemID.SAPPHIRE_GEMSTONE;
            case 8 -> ItemID.EMERALD_GEMSTONE;
            case 9 -> ItemID.RUBY_GEMSTONE;
            case 10 -> ItemID.DIAMOND_GEMSTONE;
            default -> ItemID.AMETHYST_GEMSTONE;
        };
    }
}
