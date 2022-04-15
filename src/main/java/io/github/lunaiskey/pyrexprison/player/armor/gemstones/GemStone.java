package io.github.lunaiskey.pyrexprison.player.armor.gemstones;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
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

    public GemStone(GemStoneType type, int tier) {
        this.type = type;
        this.id = type.name()+"_GEMSTONE";
        List<String> desc = new ArrayList<>();
        desc.add(StringUtil.color("&7This gemstone is used to upgrade"));
        desc.add(StringUtil.color("&7your Armor to Tier "+tier));
        this.description = desc;
    }

    public ItemStack getItemStack() {
        String name = WordUtils.capitalizeFully(type.name());
        ItemStack item = ItemBuilder.getSkull(StringUtil.color("&f" + name + " Gemstone"),description,type.getSkullTexture(),type.getSkullUUID());
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

}
