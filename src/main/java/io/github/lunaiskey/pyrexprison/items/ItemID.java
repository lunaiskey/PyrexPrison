package io.github.lunaiskey.pyrexprison.items;

import java.util.ArrayList;
import java.util.List;

public enum ItemID {

    VOUCHER(false),
    COMMON_GEODE,
    UNCOMMON_GEODE,
    RARE_GEODE,
    EPIC_GEODE,
    LEGENDARY_GEODE,
    ;

    ItemID() {
        this.visible = true;
    }

    ItemID(boolean visible) {
        this.visible = visible;
    }

    private final boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public static List<String> getVisibleIDs() {
        List<String> list = new ArrayList<>();
        for (ItemID id : ItemID.values()) {
            if (id.isVisible()) {
                list.add(id.name());
            }
        }
        return list;
    }
}
