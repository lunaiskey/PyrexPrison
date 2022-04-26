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

public class CommandPItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
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
                                item.setAmount(1);
                            } else item.setAmount(Math.min(amount, 64));
                        } catch (NumberFormatException e) {
                            p.sendMessage(StringUtil.color("&cValue has to be an integer."));
                            return true;
                        }
                    }
                    p.getInventory().addItem(item);
                    p.sendMessage(StringUtil.color("&aSuccessfully gave "+p.getName()+" &f"+itemID.name()+"&a."));
                } catch (Exception e){
                    p.sendMessage(StringUtil.color("&cInvalid ItemID."));
                }
            }

        }
        return true;
    }
}
