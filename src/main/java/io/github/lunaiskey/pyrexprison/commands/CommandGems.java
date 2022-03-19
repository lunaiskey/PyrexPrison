package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGems implements CommandExecutor {

    private PyrexPrison plugin;
    private PlayerManager playerManager;
    private CurrencyType currencyType = CurrencyType.GEMS;
    private String unicode = CurrencyType.getUnicode(currencyType);

    public CommandGems(PyrexPrison plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            double gems = playerManager.getPlayerMap().get(p.getUniqueId()).getGems();
            if (args.length == 0) {
                p.sendMessage(StringUtil.color("You have "+unicode + "&f" + Numbers.formattedNumber((long)gems) + "&a gems."));
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&a/gems give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    p.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    double amount = Currency.giveCurrency(givePlayer.getUniqueId(), currencyType,Double.parseDouble(args[2]));
                    if (amount == 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&aGiven "+unicode+"&f"+Numbers.formattedNumber((long)amount)+"&a Gems to &f"+givePlayer.getName()+"&a."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("take")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&a/gems take <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    p.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    double amount = Currency.takeCurrency(givePlayer.getUniqueId(), currencyType,Double.parseDouble(args[2]));
                    if (amount == 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&aTaken "+unicode+"&f"+Numbers.formattedNumber((long)amount)+"&a Gems to &f"+givePlayer.getName()+"&a."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&a/gems give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    p.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    double amount = Currency.setCurrency(givePlayer.getUniqueId(), currencyType,Double.parseDouble(args[2]));
                    if (amount < 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be positive."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&aSet &f"+givePlayer.getName()+"'s&a Gems to &f"+Numbers.formattedNumber((long)amount)+"&a."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("pay")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&e/tokens pay <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    p.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                if (givePlayer == p) {
                    p.sendMessage(StringUtil.color("&cYou can't pay yourself."));
                    return true;
                }
                try {
                    double giverAmount = Currency.getAcceptedAmount(Double.parseDouble(args[2]));
                    double giverBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getTokens();

                    if (giverBalance < giverAmount) {
                        p.sendMessage(StringUtil.color("&cInsufficient Gems."));
                        return true;
                    }
                    double amount = Currency.giveCurrency(givePlayer.getUniqueId(), currencyType,giverAmount);
                    double remove = Currency.takeCurrency(p.getUniqueId(), currencyType,giverAmount);
                    if (giverAmount == 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&aPaid "+unicode+"&f"+Numbers.formattedNumber((long)amount)+"&a Gems to &f"+givePlayer.getName()+"&a."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&a/gems withdraw <amount>"));
                    return true;
                }
                try {
                    long amount = (long) Currency.getAcceptedAmount(Double.parseDouble(args[1]));
                    double playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getGems();

                    if (playerBalance < amount) {
                        p.sendMessage(StringUtil.color("&cInsufficient Gems."));
                        return true;
                    }

                    if (amount == 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    if (!p.getInventory().addItem(Currency.getWithdrawVoucher(amount,currencyType)).isEmpty()) {
                        p.sendMessage(StringUtil.color("&cNot enough inventory space."));
                        return true;
                    }
                    Currency.takeCurrency(p.getUniqueId(), currencyType,amount);
                    p.sendMessage(StringUtil.color("&aWithdrawn "+unicode+"&f"+Numbers.formattedNumber(amount)+"&a Gems."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
        }
        return true;
    }
}
