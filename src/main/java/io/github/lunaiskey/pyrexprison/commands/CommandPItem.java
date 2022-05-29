package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommandPItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("pyrex.pitem")) {
                if (args.length == 0) {
                    p.sendMessage(StringUtil.color("Usage: /pitem give <ItemID> [amount]"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("give")) {
                    try {
                        ItemID itemID = ItemID.valueOf(args[1].toUpperCase());
                        ItemStack item = PyrexPrison.getPlugin().getItemManager().getItemMap().get(itemID).getItemStack();
                        if (args.length == 3) {
                            try {
                                int amount = Integer.parseInt(args[2]);
                                if (amount <= 0) {
                                    p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                                } else {
                                    int counter = amount;
                                    while (counter > 0) {
                                        if (counter > 64) {
                                            item.setAmount(64);
                                            Map<Integer, ItemStack> leftOver = p.getInventory().addItem(item);
                                            if (leftOver.isEmpty()) {
                                                counter -= 64;
                                            } else {
                                                counter -= (64 - leftOver.get(0).getAmount());
                                                break;
                                            }
                                        } else {
                                            item.setAmount(counter);
                                            Map<Integer, ItemStack> leftOver = p.getInventory().addItem(item);
                                            if (leftOver.isEmpty()) {
                                                counter = 0;
                                            } else {
                                                counter = leftOver.get(0).getAmount();
                                                break;
                                            }
                                        }
                                    }
                                    amount -= counter;
                                    p.sendMessage(StringUtil.color("&aSuccessfully gave " + p.getName() + " &f" + amount + " &aof&f " + itemID.name() + "&a."));
                                }
                            } catch (NumberFormatException e) {
                                p.sendMessage(StringUtil.color("&cInvalid Amount."));
                                return true;
                            }

                        }

                    } catch (Exception e) {
                        p.sendMessage(StringUtil.color("&cInvalid ItemID."));
                    }
                }
            }

        }
        return true;
    }
}
