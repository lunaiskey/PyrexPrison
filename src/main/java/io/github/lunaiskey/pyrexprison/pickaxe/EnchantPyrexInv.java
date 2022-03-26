package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.gui.PyrexInv;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.pickaxe.EnchantType;

public class EnchantPyrexInv extends PyrexInv {

    private final EnchantType type;

    public EnchantPyrexInv(String name, int size, PyrexInvType invType, EnchantType type) {
        super(name, size, invType);
        this.type = type;
    }

    public EnchantType getType() {
        return type;
    }
}
