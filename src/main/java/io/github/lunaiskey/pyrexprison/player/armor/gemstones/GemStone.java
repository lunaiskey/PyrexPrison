package io.github.lunaiskey.pyrexprison.player.armor.gemstones;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.WordUtils;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GemStone {

    private String id;
    private List<String> description;
    private GemStoneType type;
    private int tier;

    public GemStone(GemStoneType type, int tier) {
        this.type = type;
        this.id = type.name()+"_GEMSTONE";
        this.tier = tier;
        List<String> desc = new ArrayList<>();
        desc.add(StringUtil.color("&7This Gemstone is used to upgrade"));
        desc.add(StringUtil.color("&7your Armor to Tier "+tier));
        this.description = desc;
    }

    public ItemStack getItemStack() {
        ItemStack item = ItemBuilder.getSkull(StringUtil.color("&f" + getName() + " Gemstone"),description,type.getSkullTexture(),type.getSkullUUID());
        item = NBTTags.addPyrexData(item,"id",id);
        return item;
    }

    public void onPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public String getId() {
        return id;
    }

    public List<String> getDescription() {
        return description;
    }

    public GemStoneType getType() {
        return type;
    }

    public int getTier() {
        return tier;
    }

    public String getName() {
        String name = WordUtils.capitalizeFully(type.name());
        return getColor()+name;
    }

    public ChatColor getColor() {
        return ChatColor.of("#"+getHex());
    }

    public String getHex() {
        String color = "FFFFFF";
        switch(type) {
            case AMETHYST -> color = "A12FFA";
            case JASPER -> color = "A30071";
            case OPAL -> color = "EEEEEE";
            case JADE -> color = "49FCAA";
            case TOPAZ -> color = "E6D48A";
            case AMBER -> color = "FA7D33";
            case SAPPHIRE -> color = "2441FA";
            case EMERALD -> color = "39AD39";
            case RUBY -> color = "A1000B";
            case DIAMOND -> color = "4DF5FC";
        }
        return color;
    }

    public int getIntFromHex() {
        return Integer.decode("#"+getHex());
    }

}
