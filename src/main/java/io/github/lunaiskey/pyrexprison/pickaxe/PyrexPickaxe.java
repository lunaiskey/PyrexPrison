package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class PyrexPickaxe {

    private Map<EnchantType,Integer> enchants = new HashMap<>();
    private long blocksBroken;

    public PyrexPickaxe() {
        enchants.put(EnchantType.EFFICIENCY, 100);
        enchants.put(EnchantType.HASTE,3);
        enchants.put(EnchantType.SPEED,3);
        enchants.put(EnchantType.JUMP_BOOST,3);
        enchants.put(EnchantType.JACK_HAMMER,10000);
        blocksBroken = 0;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        CompoundTag tag = NBTTags.getPyrexDataMap(item);
        tag.putString("id","PyrexPickaxe");
        item = NBTTags.addCustomTagContainer(item,"PyrexData",tag);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public long getBlocksBroken() {
        return blocksBroken;
    }

    public Map<EnchantType, Integer> getEnchants() {
        return enchants;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }
}
