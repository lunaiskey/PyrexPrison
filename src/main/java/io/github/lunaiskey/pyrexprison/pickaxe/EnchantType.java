package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.pickaxe.enchants.NightVision;

import java.util.LinkedHashSet;
import java.util.Set;

public enum EnchantType {
    EFFICIENCY,
    FORTUNE,
    HASTE,
    SPEED,
    NIGHT_VISION,
    JUMP_BOOST,
    JACK_HAMMER,
    GEM_FINDER,
    KEY_FINDER,
    LOOT_FINDER,
    STRIKE,
    EXPLOSIVE,
    XP_BOOST,
    NUKE
    ;

    public static Set<EnchantType> getSortedSet() {
        Set<EnchantType> type = new LinkedHashSet<>();
        type.add(EFFICIENCY);
        type.add(FORTUNE);
        type.add(HASTE);
        type.add(SPEED);
        type.add(JUMP_BOOST);
        type.add(NIGHT_VISION);
        type.add(JACK_HAMMER);
        type.add(STRIKE);
        type.add(EXPLOSIVE);
        type.add(NUKE);
        type.add(GEM_FINDER);
        type.add(KEY_FINDER);
        type.add(LOOT_FINDER);
        type.add(XP_BOOST);
        return type;
    }
}
