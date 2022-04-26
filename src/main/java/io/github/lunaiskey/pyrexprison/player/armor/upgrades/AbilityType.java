package io.github.lunaiskey.pyrexprison.player.armor.upgrades;

import java.util.ArrayList;
import java.util.List;

public enum AbilityType {
    SALES_BOOST,
    ENCHANTMENT_PROC,
    XP_BOOST,
    ;

    public static List<AbilityType> getSortedList() {
        List<AbilityType> list = new ArrayList<>();
        list.add(SALES_BOOST);
        list.add(ENCHANTMENT_PROC);
        list.add(XP_BOOST);
        return list;
    }
}
