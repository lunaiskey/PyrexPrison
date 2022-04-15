package io.github.lunaiskey.pyrexprison.pickaxe;

import com.sun.jna.platform.unix.solaris.LibKstat;
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
    GEM_FINDER,
    KEY_FINDER,
    STRIKE,
    ;

    public static Set<EnchantType> getSortedSet() {
        Set<EnchantType> type = new LinkedHashSet<>();
        type.add(EFFICIENCY);
        type.add(FORTUNE);
        type.add(HASTE);
        type.add(SPEED);
        type.add(JUMP_BOOST);
        type.add(JACK_HAMMER);
        type.add(STRIKE);
        type.add(GEM_FINDER);
        type.add(KEY_FINDER);
        return type;
    }
}
