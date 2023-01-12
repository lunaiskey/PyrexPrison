package io.github.lunaiskey.pyrexprison.modules.pickaxe.commands;

import io.github.lunaiskey.pyrexprison.Messages;
import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.EnchantType;
import io.github.lunaiskey.pyrexprison.modules.pickaxe.PyrexPickaxe;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
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
            //CompoundTag pyrexDataMap = NBTTags.getPyrexDataMap(p.getInventory().getItemInMainHand());
            if (p.hasPermission("pyrex.enchant")) {
                if (args.length == 0) {
                    p.sendMessage(
                        StringUtil.color("&bEnchant Commands:"),
                        StringUtil.color("&b|&f /enchant <player> <id> <level> "),
                        StringUtil.color("&b|&f /enchant <id> <level>")
                    );
                }
                if (args.length == 3) {
                    PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
                    PyrexPickaxe pickaxe = pyrexPlayer.getPickaxe();
                    if (args[0].equalsIgnoreCase("set")) {
                        EnchantType type = EnchantType.valueOf(args[1]);
                        int newLevel = Integer.parseInt(args[2]);
                        if (newLevel > 0) {
                            pickaxe.getEnchants().put(type,newLevel);
                        } else {
                            pickaxe.getEnchants().remove(type);
                        }
                        PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(p);
                    }
                } else if (args.length == 4) {
                    Player otherPlayer = Bukkit.getPlayer(args[0]);
                    if (otherPlayer != null) {
                        PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                        PyrexPickaxe pickaxe = pyrexPlayer.getPickaxe();

                        if (args[1].equalsIgnoreCase("set")) {
                            EnchantType type = EnchantType.valueOf(args[2]);
                            int newLevel = Integer.parseInt(args[3]);
                            if (newLevel > 0) {
                                pickaxe.getEnchants().put(type,newLevel);
                            } else {
                                pickaxe.getEnchants().remove(type);
                            }
                            PyrexPrison.getPlugin().getPickaxeHandler().updateInventoryPickaxe(otherPlayer);
                        }
                    }
                }
            } else {
                p.sendMessage(Messages.NO_PERMISSION.getText());
            }
        }
        return true;
    }
}
