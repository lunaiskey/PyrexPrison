package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.persistence.CraftPersistentDataAdapterContext;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static Map<UUID, PyrexPlayer> playerMap = new HashMap<>();

    public Map<UUID, PyrexPlayer> getPlayerMap() {
        return playerMap;
    }

    public void createPyrexPlayer(UUID pUUID) {
        playerMap.put(pUUID,new PyrexPlayer(pUUID,0,0,0,0));
    }




}
