package io.github.lunaiskey.pyrexprison.mines.upgrades;

import io.github.lunaiskey.pyrexprison.mines.upgrades.upgrades.MaxPlayers;
import io.github.lunaiskey.pyrexprison.mines.upgrades.upgrades.SellPrice;
import io.github.lunaiskey.pyrexprison.mines.upgrades.upgrades.Size;

import java.util.HashMap;
import java.util.Map;

public enum PMineUpgradeType {
    SIZE,
    SELL_PRICE,
    MAX_PLAYERS,
    ;

    private static Map<PMineUpgradeType,PMineUpgrade> upgradeMap = new HashMap<>();

    static {
        upgradeMap.put(SIZE,new Size());
        upgradeMap.put(SELL_PRICE,new SellPrice());
        upgradeMap.put(MAX_PLAYERS,new MaxPlayers());
    }

    public static Map<PMineUpgradeType, PMineUpgrade> getUpgradeMap() {
        return upgradeMap;
    }
}
