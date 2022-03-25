package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPyrexPoints implements CommandExecutor {

    private PyrexPrison plugin;
    private PlayerManager playerManager;
    private CurrencyType currencyType = CurrencyType.PYREX_POINTS;
    private String unicode = CurrencyType.getUnicode(currencyType);

    public CommandPyrexPoints(PyrexPrison plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            long pyrexPoints = playerManager.getPlayerMap().get(p.getUniqueId()).getPyrexPoints();
            if (args.length == 0) {
                p.sendMessage(StringUtil.color("&dYou have "+unicode+"&f"+ Numbers.formattedNumber(pyrexPoints)+"&d Pyrex Points."));
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&d/pyrexpoints give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    p.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    p.sendMessage(StringUtil.color("&cAmount can't be empty"));
                    return true;
                }
                try {
                    long amount = (long) Double.parseDouble(args[2]);
                    Currency.giveCurrency(givePlayer.getUniqueId(), currencyType,amount);
                    if (amount <= 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0"));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&dGiven "+unicode+"&f"+Numbers.formattedNumber(amount)+"&d Pyrex Points to &f"+givePlayer.getName()+"&d."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("take")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&d/pyrexpoints take <player> <amount>"));
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
                    long amount = (long) Double.parseDouble(args[2]);
                    Currency.takeCurrency(givePlayer.getUniqueId(), currencyType,amount);
                    if (amount <= 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&dTaken "+unicode+"&f"+Numbers.formattedNumber(amount)+"&d Pyrex Points from &f"+givePlayer.getName()+"&d."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&d/pyrexpoints set <player> <amount>"));
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
                    long amount = (long) Double.parseDouble(args[2]);
                    Currency.setCurrency(givePlayer.getUniqueId(), currencyType,amount);
                    if (amount < 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be positive."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&dSet &f"+givePlayer.getName()+"'s&d Pyrex Points to &f"+Numbers.formattedNumber(amount)+"&d."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("pay")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&d/pyrexpoints pay <player> <amount>"));
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
                    long amount = (long) Double.parseDouble(args[2]);
                    long balance = playerManager.getPlayerMap().get(p.getUniqueId()).getPyrexPoints();

                    if (balance < amount) {
                        p.sendMessage(StringUtil.color("&cInsufficient Pyrex Points."));
                        return true;
                    }
                    Currency.giveCurrency(givePlayer.getUniqueId(), currencyType,amount);
                    Currency.takeCurrency(p.getUniqueId(), currencyType,amount);
                    if (amount == 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    p.sendMessage(StringUtil.color("&dPaid "+unicode+"&f"+Numbers.formattedNumber(amount)+"&d Pyrex Points to &f"+givePlayer.getName()+"&d."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color("&d/pyrexpoints withdraw <amount>"));
                    return true;
                }
                try {
                    long amount = (long) Double.parseDouble(args[1]);
                    long balance = playerManager.getPlayerMap().get(p.getUniqueId()).getPyrexPoints();

                    if (amount > balance) {
                        p.sendMessage(StringUtil.color("&cInsufficient Pyrex Points."));
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
                    p.sendMessage(StringUtil.color("&dWithdrawn "+unicode+"&f"+Numbers.formattedNumber(amount)+"&d Pyrex Points."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
        }
        return true;
    }
}
