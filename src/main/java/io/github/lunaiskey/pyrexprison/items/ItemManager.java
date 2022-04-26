package io.github.lunaiskey.pyrexprison.items;

import io.github.lunaiskey.pyrexprison.items.pyrexitems.Geode;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private Map<ItemID,PyrexItem> itemMap = new HashMap<>();

    public void registerItems() {
        itemMap.put(ItemID.COMMON_GEODE,new Geode(ItemID.COMMON_GEODE));
        itemMap.put(ItemID.UNCOMMON_GEODE,new Geode(ItemID.UNCOMMON_GEODE));
        itemMap.put(ItemID.RARE_GEODE,new Geode(ItemID.RARE_GEODE));
        itemMap.put(ItemID.EPIC_GEODE,new Geode(ItemID.EPIC_GEODE));
        itemMap.put(ItemID.LEGENDARY_GEODE,new Geode(ItemID.LEGENDARY_GEODE));
    }

    public Map<ItemID, PyrexItem> getItemMap() {
        return itemMap;
    }
}
