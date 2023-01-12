package io.github.lunaiskey.pyrexprison.modules.armor;

import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;

public class ArmorPyrexHolder extends PyrexHolder {

    private ArmorType type;

    public ArmorPyrexHolder(String name, int size, PyrexInvType invType, ArmorType type) {
        super(name, size, invType);
        this.type = type;
    }

    public ArmorType getType() {
        return type;
    }
}
