package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;

public class EnchantPyrexHolder extends PyrexHolder {

    private final EnchantType type;

    public EnchantPyrexHolder(String name, int size, PyrexInvType invType, EnchantType type) {
        super(name, size, invType);
        this.type = type;
    }

    public EnchantType getType() {
        return type;
    }
}
