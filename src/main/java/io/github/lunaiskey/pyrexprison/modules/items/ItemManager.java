package io.github.lunaiskey.pyrexprison.modules.items;

import io.github.lunaiskey.pyrexprison.modules.items.items.*;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {

    private Map<ItemID,PyrexItem> itemMap = new LinkedHashMap<>();

    public ItemManager() {
        registerItems();
    }

    private void registerItems() {
        addPyrexItem(new Geode(Geode.GeodeRarity.COMMON));
        addPyrexItem(new Geode(Geode.GeodeRarity.UNCOMMON));
        addPyrexItem(new Geode(Geode.GeodeRarity.RARE));
        addPyrexItem(new Geode(Geode.GeodeRarity.EPIC));
        addPyrexItem(new Geode(Geode.GeodeRarity.LEGENDARY));
        addPyrexItem(new GemStone(GemStone.GemstoneType.DIAMOND));
        addPyrexItem(new GemStone(GemStone.GemstoneType.RUBY));
        addPyrexItem(new GemStone(GemStone.GemstoneType.EMERALD));
        addPyrexItem(new GemStone(GemStone.GemstoneType.SAPPHIRE));
        addPyrexItem(new GemStone(GemStone.GemstoneType.AMBER));
        addPyrexItem(new GemStone(GemStone.GemstoneType.TOPAZ));
        addPyrexItem(new GemStone(GemStone.GemstoneType.JADE));
        addPyrexItem(new GemStone(GemStone.GemstoneType.OPAL));
        addPyrexItem(new GemStone(GemStone.GemstoneType.JASPER));
        addPyrexItem(new GemStone(GemStone.GemstoneType.AMETHYST));
        addPyrexItem(new PlayerMenu());

        addPyrexItem(new TokenPouch(ItemID.COMMON_TOKEN_POUCH));
        addPyrexItem(new TokenPouch(ItemID.UNCOMMON_TOKEN_POUCH));
        addPyrexItem(new TokenPouch(ItemID.RARE_TOKEN_POUCH));
        addPyrexItem(new TokenPouch(ItemID.EPIC_TOKEN_POUCH));
        addPyrexItem(new TokenPouch(ItemID.LEGENDARY_TOKEN_POUCH));

        addPyrexItem(new GemPouch(ItemID.COMMON_GEM_POUCH));
        addPyrexItem(new GemPouch(ItemID.UNCOMMON_GEM_POUCH));
        addPyrexItem(new GemPouch(ItemID.RARE_GEM_POUCH));
        addPyrexItem(new GemPouch(ItemID.EPIC_GEM_POUCH));
        addPyrexItem(new GemPouch(ItemID.LEGENDARY_GEM_POUCH));

        //itemMap.put(ItemID.SEX_ITEM,new SexItem());
    }

    private void addPyrexItem(PyrexItem item) {
        itemMap.put(item.getItemID(),item);
    }

    public PyrexItem getPyrexItem(ItemID itemID) {
        return itemMap.get(itemID);
    }

    public Map<ItemID, PyrexItem> getItemMap() {
        return itemMap;
    }
}
