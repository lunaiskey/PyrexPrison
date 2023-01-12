package io.github.lunaiskey.pyrexprison.modules.pmines.upgrades.upgrades;

import io.github.lunaiskey.pyrexprison.modules.pmines.upgrades.PMineUpgrade;

import java.util.List;

public class SellPrice extends PMineUpgrade {
    public SellPrice() {
        super("Sell Price", List.of("&c[WIP]"), 4);
    }

    @Override
    public long getCost(int level) {
        return 0;
    }

    @Override
    public String getUpgradeLore(int level) {
        return "null";
    }
}
