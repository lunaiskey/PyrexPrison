package io.github.lunaiskey.pyrexprison.nms;

import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NBTTags {

    public static CompoundTag getBaseTagContainer(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        return nmsStack.getOrCreateTag();
    }

    public static ItemStack addCustomTagContainer(ItemStack item, String containerName, CompoundTag tag) {
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

    public static ItemStack addPyrexDataContainer(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag pyrexDataTag = new CompoundTag();
        if (itemTag.getAllKeys().contains("PyrexData")) {
            return item;
        }
        itemTag.put("PyrexData",pyrexDataTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static ItemStack addPyrexData(ItemStack item, String identifier, Object value) {
        item = addPyrexDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        CompoundTag pyrexDataTag = itemTag.getCompound("PyrexData");
        if (value instanceof Integer) {
            pyrexDataTag.putInt(identifier,(int)value);
        }
        if (value instanceof Short) {
            pyrexDataTag.putShort(identifier,(short)value);
        }
        if (value instanceof Long) {
            pyrexDataTag.putLong(identifier,(long)value);
        }
        if (value instanceof Double) {
            pyrexDataTag.putDouble(identifier,(double)value);
        }
        if (value instanceof String) {
            pyrexDataTag.putString(identifier,(String) value);
        }
        if (value instanceof Boolean) {
            pyrexDataTag.putBoolean(identifier,(boolean)value);
        }
        if (value instanceof Byte) {
            pyrexDataTag.putByte(identifier,(byte) value);
        }
        if (value instanceof Float) {
            pyrexDataTag.putFloat(identifier,(float) value);
        }
        if (value instanceof UUID) {
            pyrexDataTag.putUUID(identifier,(UUID) value);
        }
        itemTag.put("PyrexData",pyrexDataTag);
        nmsStack.setTag(itemTag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static CompoundTag getPyrexDataMap(ItemStack item) {
        item = addPyrexDataContainer(item);
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        CompoundTag itemTag = nmsStack.getOrCreateTag();
        if (itemTag.contains("PyrexData")) {
            return itemTag.getCompound("PyrexData");
        }
        return new CompoundTag();
    }
}
