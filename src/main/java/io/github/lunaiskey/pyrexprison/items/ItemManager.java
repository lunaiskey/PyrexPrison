package io.github.lunaiskey.pyrexprison.items;

import io.github.lunaiskey.pyrexprison.items.pyrexitems.GemStone;
import io.github.lunaiskey.pyrexprison.items.pyrexitems.Geode;
import io.github.lunaiskey.pyrexprison.items.pyrexitems.PlayerMenu;

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
        itemMap.put(ItemID.DIAMOND_GEMSTONE,new GemStone(ItemID.DIAMOND_GEMSTONE));
        itemMap.put(ItemID.RUBY_GEMSTONE,new GemStone(ItemID.RUBY_GEMSTONE));
        itemMap.put(ItemID.EMERALD_GEMSTONE,new GemStone(ItemID.EMERALD_GEMSTONE));
        itemMap.put(ItemID.SAPPHIRE_GEMSTONE,new GemStone(ItemID.SAPPHIRE_GEMSTONE));
        itemMap.put(ItemID.AMBER_GEMSTONE,new GemStone(ItemID.AMBER_GEMSTONE));
        itemMap.put(ItemID.TOPAZ_GEMSTONE,new GemStone(ItemID.TOPAZ_GEMSTONE));
        itemMap.put(ItemID.JADE_GEMSTONE,new GemStone(ItemID.JADE_GEMSTONE));
        itemMap.put(ItemID.OPAL_GEMSTONE,new GemStone(ItemID.OPAL_GEMSTONE));
        itemMap.put(ItemID.JASPER_GEMSTONE,new GemStone(ItemID.JASPER_GEMSTONE));
        itemMap.put(ItemID.AMETHYST_GEMSTONE,new GemStone(ItemID.AMETHYST_GEMSTONE));
        itemMap.put(ItemID.PLAYER_MENU,new PlayerMenu());
    }

    public Map<ItemID, PyrexItem> getItemMap() {
        return itemMap;
    }
}
