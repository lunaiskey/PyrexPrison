package io.github.lunaiskey.pyrexprison.pickaxe;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PyrexPickaxe {

    private UUID player;
    private Map<EnchantType,Integer> enchants;
    private Set<EnchantType> disabledEnchants;
    private long blocksBroken;

    public PyrexPickaxe(UUID player, Map<EnchantType,Integer> enchants,Set<EnchantType> disabledEnchants, long blocksBroken) {
        this.player = player;
        if (enchants == null) {
            enchants = new HashMap<>();
        }
        for (EnchantType type : EnchantType.getDefaultMap().keySet()) {
            if (enchants.containsKey(type)) {
                if (enchants.get(type) < EnchantType.getDefaultMap().get(type)) {
                    enchants.put(type,EnchantType.getDefaultMap().get(type));
                }
            } else {
                enchants.put(type,EnchantType.getDefaultMap().get(type));
            }
        }
        this.enchants = enchants;
        if (disabledEnchants == null) {
            this.disabledEnchants = new HashSet<>();
        } else {
            this.disabledEnchants = disabledEnchants;
        }
        this.blocksBroken = blocksBroken;
    }

    public PyrexPickaxe(UUID player) {
        this(player,null,null,0);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        CompoundTag tag = NBTTags.getPyrexDataMap(item);
        tag.putString("id","PYREX_PICKAXE");
        item = NBTTags.addCustomTagContainer(item,"PyrexData",tag);
        return PyrexPrison.getPlugin().getPickaxeHandler().updatePickaxe(item,player);
    }

    /*
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        CompoundTag tag = NBTTags.getPyrexDataMap(item);
        tag.putString("id",PickaxeHandler.getId());
        item = NBTTags.addCustomTagContainer(item,"PyrexData",tag);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color("&dYour Pickaxe"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&d&lEnchants"));
        for (EnchantType enchantType : EnchantType.getSortedSet()) {
            PyrexEnchant pyrexEnchant = PyrexPrison.getPlugin().getPickaxeHandler().getEnchantments().get(enchantType);
            if (enchants.containsKey(enchantType) && enchants.get(enchantType) > 0) {
                lore.add(StringUtil.color("&d&l| &f"+pyrexEnchant.getName()+" "+enchants.get(enchantType)));
                if (enchantType == EnchantType.EFFICIENCY) {
                    meta.addEnchant(Enchantment.DIG_SPEED,enchants.get(enchantType),true);
                }
            }
        }
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

     */

    public long getBlocksBroken() {
        return blocksBroken;
    }

    public Map<EnchantType, Integer> getEnchants() {
        return enchants;
    }

    public Set<EnchantType> getDisabledEnchants() {
        return disabledEnchants;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }
}
