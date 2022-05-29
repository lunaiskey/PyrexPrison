package io.github.lunaiskey.pyrexprison.commands;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.items.pyrexitems.Voucher;
import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PlayerManager;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CommandCurrency implements CommandExecutor, TabCompleter {

    private PyrexPrison plugin;
    private PlayerManager playerManager;
    private final CurrencyType type;
    private final String unicode;

    public CommandCurrency(PyrexPrison plugin, CurrencyType type) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.type = type;
        this.unicode = CurrencyType.getUnicode(type);
    }

    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                BigInteger currency = playerManager.getPlayerMap().get(p.getUniqueId()).getCurrency(type);
                sender.sendMessage(StringUtil.color(color()+"You have "+unicode + "&f" + Numbers.formattedNumber(currency) + color()+" "+name()+"."));
            }
            return true;
        }
        Player otherPlayer = Bukkit.getPlayer(args[0]);
        if (args[0].equalsIgnoreCase("give")) {
            if (sender.hasPermission(perm()+".give")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color(color()+"/"+label+" give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    sender.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(BigInteger.ZERO) < 0) {
                        sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    Currency.giveCurrency(givePlayer.getUniqueId(), type,amount);
                    sender.sendMessage(StringUtil.color(color()+"Given "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" to &f"+givePlayer.getName()+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've been given "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+"."));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            } else {
                sender.sendMessage(StringUtil.color("&cNo Permission."));
            }
        }
        if (args[0].equalsIgnoreCase("take")) {
            if (sender.hasPermission(perm()+".take")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color(color()+"/"+label+" take <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    sender.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(BigInteger.ZERO) < 0) {
                        sender.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    Currency.takeCurrency(givePlayer.getUniqueId(), type,amount);
                    sender.sendMessage(StringUtil.color(color()+"Taken "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" to &f"+givePlayer.getName()+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've had "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" taken from you."));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            } else {
                sender.sendMessage(StringUtil.color("&cNo Permission."));
            }
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (sender.hasPermission(perm()+".set")) {
                if (args.length == 1) {
                    sender.sendMessage(StringUtil.color(color()+"/"+label+" give <player> <amount>"));
                    return true;
                }
                Player givePlayer = Bukkit.getPlayer(args[1]);
                if (givePlayer == null) {
                    sender.sendMessage(StringUtil.color("&cPlayer is not online."));
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(StringUtil.color("&cAmount can't be empty."));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[2]);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(BigInteger.ZERO) < 0) {
                        sender.sendMessage(StringUtil.color("&cAmount has to be positive."));
                        return true;
                    }
                    Currency.setCurrency(givePlayer.getUniqueId(), type,amount);
                    sender.sendMessage(StringUtil.color(color()+"Set &f"+givePlayer.getName()+"'s"+color()+" "+name()+" to &f"+Numbers.formattedNumber(amount)+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've had your "+name()+" set to &f"+Numbers.formattedNumber(amount)+color()+"."));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            } else {
                sender.sendMessage(StringUtil.color("&cNo Permission."));
            }
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("pay")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color(color()+"/"+label+" pay <player> <amount>"));
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
                    BigInteger amount = new BigInteger(args[2]);
                    BigInteger playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getTokens();
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }
                    if (amount.compareTo(playerBalance) > 0) {
                        p.sendMessage(StringUtil.color("&cInsufficient "+name()+"."));
                        return true;
                    }
                    if (amount.compareTo(BigInteger.ZERO) <= 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    Currency.giveCurrency(givePlayer.getUniqueId(), type,amount);
                    Currency.takeCurrency(p.getUniqueId(), type,amount);
                    p.sendMessage(StringUtil.color(color()+"Paid "+unicode+"&f"+Numbers.formattedNumber(amount)+color()+" "+name()+" to &f"+givePlayer.getName()+color()+"."));
                    givePlayer.sendMessage(StringUtil.color(color()+"You've been paid "+unicode+"&f"+Numbers.formattedNumber(amount)+" "+color()+name()+" by &f"+p.getName()+color()+"."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (args.length == 1) {
                    p.sendMessage(StringUtil.color(color()+"/"+label+" withdraw <amount>"));
                    return true;
                }
                try {
                    BigInteger amount = new BigInteger(args[1]);
                    BigInteger playerBalance = playerManager.getPlayerMap().get(p.getUniqueId()).getCurrency(type);
                    if (type != CurrencyType.TOKENS) {
                        amount = BigInteger.valueOf(amount.longValue());
                    }

                    if (amount.compareTo(playerBalance) > 0 ) {
                        p.sendMessage(StringUtil.color("&cInsufficient "+name()+"."));
                        return true;
                    }
                    if (amount.compareTo(BigInteger.ZERO) <= 0) {
                        p.sendMessage(StringUtil.color("&cAmount has to be more then 0."));
                        return true;
                    }
                    if (!p.getInventory().addItem(new Voucher(amount, type).getItemStack()).isEmpty()) {
                        p.sendMessage(StringUtil.color("&cNot enough inventory space."));
                        return true;
                    }
                    Currency.takeCurrency(p.getUniqueId(), type,amount);
                    p.sendMessage(StringUtil.color(color()+"Withdrawn "+unicode+"&f"+Numbers.formattedNumber(amount)+" "+color()+name()+"."));
                } catch (NumberFormatException ex) {
                    p.sendMessage(StringUtil.color("&cAmount has to be a valid number."));
                }
                return true;
            }
            if (otherPlayer != null) {
                PyrexPlayer otherPyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(otherPlayer.getUniqueId());
                p.sendMessage(StringUtil.color(color()+otherPlayer.getName()+" has "+unicode+"&f"+Numbers.formattedNumber(otherPyrexPlayer.getCurrency(type))+" "+color()+name()+"."));
            } else {
                p.sendMessage(StringUtil.color("&c"+args[0]+" isn't online."));
            }
        } else {
            sender.sendMessage(StringUtil.color("&cYou have to be a player to run this command."));
        }
        return true;
    }

    private String perm() {
        return switch (type) {
            case TOKENS -> "pyrex.tokens";
            case GEMS -> "pyrex.gems";
            case PYREX_POINTS -> "pyrex.points";
        };
    }

    private String color() {
        return switch (type) {
            case TOKENS -> "&e";
            case GEMS -> "&a";
            case PYREX_POINTS -> "&d";
        };
    }

    private String name() {
        return switch (type) {
            case TOKENS -> "Tokens";
            case GEMS -> "Gems";
            case PYREX_POINTS -> "Pyrex Points";
        };
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        if (args.length == 1) {
            completion.add("pay");
            completion.add("withdraw");
            if (sender.hasPermission(perm()+".give")) {
                completion.add("give");
            }
            if (sender.hasPermission(perm()+".take")) {
                completion.add("take");
            }
            if (sender.hasPermission(perm()+".set")) {
                completion.add("set");
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                completion.add(player.getName());
            }
            return completion;
        }
        if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("set")) {
            if (args.length == 3) {
                return completion;
            }
        }
        return null;
    }
}
