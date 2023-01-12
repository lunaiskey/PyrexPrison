package io.github.lunaiskey.pyrexprison.modules.pickaxe.enchants;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.items.ItemID;
import io.github.lunaiskey.pyrexprison.modules.items.ItemManager;
import io.github.lunaiskey.pyrexprison.modules.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexEnchant;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LootFinder extends PyrexEnchant {
    public LootFinder() {
        super("Loot Finder", List.of("Gives a chance to find Geodes."), 250, CurrencyType.TOKENS, true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        Random rand = PyrexPrison.getPlugin().getRand();
        Player p = e.getPlayer();
        if (rand.nextDouble()*100 <= getChance(level)) {
            if (level > getMaxLevel()) {
                level = getMaxLevel();
            }
            int bound = (((level-1) - ((level-1)%(getMaxLevel()/5)))/(getMaxLevel()/5))+1;
            p.getInventory().addItem(getGeode(rand.nextInt(bound)));
        }
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

    private double getChance(int level) {
        return 0.001*level;
    }

    private ItemStack getGeode(int rarity) {
        ItemManager itemManager = PyrexPrison.getPlugin().getItemManager();
        Map<ItemID, PyrexItem> itemMap = itemManager.getItemMap();
        return switch(rarity) {
            case 0 -> itemMap.get(ItemID.COMMON_GEODE).getItemStack();
            case 1 -> itemMap.get(ItemID.UNCOMMON_GEODE).getItemStack();
            case 2 -> itemMap.get(ItemID.RARE_GEODE).getItemStack();
            case 3 -> itemMap.get(ItemID.EPIC_GEODE).getItemStack();
            case 4 -> itemMap.get(ItemID.LEGENDARY_GEODE).getItemStack();
            default -> throw new IllegalStateException("Unexpected value: " + rarity);
        };
    }
}
