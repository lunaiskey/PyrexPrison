package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.pickaxe.enchants.*;

import java.util.HashMap;
import java.util.Map;

public class PickaxeHandler {

    private String id = "PyrexPickaxe";
    private Map<EnchantType,PyrexEnchant> enchantments = new HashMap<>();

    public PickaxeHandler() {
        registerEnchants();
    }

    private void registerEnchants() {
        enchantments.put(EnchantType.EFFICIENCY,new Efficiency());
        enchantments.put(EnchantType.FORTUNE,new Fortune());
        enchantments.put(EnchantType.HASTE,new Haste());
        enchantments.put(EnchantType.JUMP_BOOST,new JumpBoost());
        enchantments.put(EnchantType.SPEED,new Speed());
        enchantments.put(EnchantType.JACK_HAMMER,new JackHammer());
    }

    public Map<EnchantType, PyrexEnchant> getEnchantments() {
        return enchantments;
    }
}
