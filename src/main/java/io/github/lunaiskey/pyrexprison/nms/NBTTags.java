package io.github.lunaiskey.pyrexprison.nms;

import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTTags {

    public CompoundTag getBaseTagContainer(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        return nmsStack.getOrCreateTag();
    }

    public ItemStack addCustomTagContainer(ItemStack item, String containerName, CompoundTag tag) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        itemTag.put(containerName,tag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static boolean hasCustomTagContainer(ItemStack item, String containerName) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        if (!nmsStack.hasTag()) {
            return false;
        }
        CompoundTag itemTag = nmsStack.getTag();
        assert itemTag != null;
        return itemTag.contains(containerName);
    }

    public static ItemStack setCurrencyVoucherTags(ItemStack item, long amount, CurrencyType type) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag voucherTag = new CompoundTag();
        voucherTag.putLong(type.name(),amount);
        itemTag.put("voucher",voucherTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static Pair<CurrencyType,Long> getVoucherValue(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag voucherTag = itemTag.getCompound("voucher");
        if (voucherTag.getAllKeys().size() == 1) {
            for (String str : voucherTag.getAllKeys()) {
                return new ImmutablePair<>(CurrencyType.valueOf(str), voucherTag.getLong(str));
            }
        }
        return null;
    }


}
