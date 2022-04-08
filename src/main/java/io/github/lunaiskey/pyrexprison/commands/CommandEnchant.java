package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.pickaxe.PickaxeHandler;
import io.github.lunaiskey.pyrexprison.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandEnchant implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
            PyrexPickaxe pickaxe = pyrexPlayer.getPickaxe();
            CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(p.getInventory().getItemInMainHand());
            if (p.getName().equals("Lunaiskey")) {
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("set")) {
                        EnchantType type = EnchantType.valueOf(args[1]);
                        int newLevel = Integer.parseInt(args[2]);
                        if (pyrexDataMap.contains("id")) {
                            // is custom pickaxe
                            if (pyrexDataMap.getString("id").equals(PickaxeHandler.getId())) {
                                pickaxe.getEnchants().put(type,newLevel);
                                PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                            }
                        }
                    }
                }
            } else {
                p.sendMessage(StringUtil.color("&cThis feature is currently unavailable."));
            }
        }
        return true;
    }
}
