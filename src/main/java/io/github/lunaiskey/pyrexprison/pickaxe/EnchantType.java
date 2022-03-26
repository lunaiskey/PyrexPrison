package io.github.lunaiskey.pyrexprison.pickaxe;

import org.bukkit.Material;

import java.util.LinkedHashSet;
import java.util.Set;

public enum EnchantType {
    EFFICIENCY,
    FORTUNE,
    HASTE,
    SPEED,
    JUMP_BOOST,
    JACK_HAMMER,
    ;

    public static Set<EnchantType> getSortedSet() {
        Set<EnchantType> type = new LinkedHashSet<>();
        type.add(EFFICIENCY);
        type.add(FORTUNE);
        type.add(HASTE);
        type.add(SPEED);
        type.add(JUMP_BOOST);
        type.add(JACK_HAMMER);
        return type;
    }
}
