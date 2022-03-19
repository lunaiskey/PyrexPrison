package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class Currency {

    /**
     * Give a type of currency to a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to give currency to
     * @param type type of currency to give
     * @param amount amount of currency to take
     * @return amount that was given
     */
    public static double giveCurrency(UUID pUUID, CurrencyType type,  double amount) {
        if (amount < 0) {
            return 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        double newAmount = Double.parseDouble(df.format(amount));
        PyrexPlayer pPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> pPlayer.giveTokens(newAmount);
            case GEMS -> pPlayer.giveGems(newAmount);
            default -> {return 0;}
        }
        return newAmount;
    }

    /**
     * Takes a type of currency from a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to take currency from
     * @param type type of currency to take
     * @param amount amount of currency to take
     * @return amount that was taken
     */
    public static double takeCurrency(UUID pUUID, CurrencyType type,  double amount) {
        if (amount < 0) {
            return 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        double newAmount = Double.parseDouble(df.format(amount));
        PyrexPlayer pPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> {
                if (newAmount >= pPlayer.getTokens()) {
                    pPlayer.setTokens(0);
                    break;
                }
                pPlayer.takeTokens(newAmount);}
            case GEMS -> {
                if (newAmount >= pPlayer.getGems()) {
                    pPlayer.setGems(0);
                    break;
                }
                pPlayer.takeTokens(newAmount);}
            default -> {return 0;}
        }
        return newAmount;
    }

    public static double setCurrency(UUID pUUID, CurrencyType type,  double amount) {
        double newAmount = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        if (amount > 0) {
            newAmount = Double.parseDouble(df.format(amount));
        }
        PyrexPlayer pPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> pPlayer.setTokens(newAmount);
            case GEMS -> pPlayer.setGems(newAmount);
            default -> {return 0;}
        }
        return newAmount;
    }

    public static double getAcceptedAmount(double original) {
        if (original < 0) {
            return 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(original));
    }

    public static ItemStack getWithdrawVoucher(long amount, CurrencyType type) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        switch (type) {
            case TOKENS -> meta.setDisplayName(StringUtil.color("&e⛁&f"+ Numbers.formattedNumber(amount)+" &e&lNote"));
            case GEMS -> meta.setDisplayName(StringUtil.color("&a❈&f"+Numbers.formattedNumber(amount)+" &a&lNote"));
        }
        meta.setLore(List.of(StringUtil.color("&eRight Click to redeem!")));
        item.setItemMeta(meta);
        item = NBTTags.setCurrencyVoucherTags(item,amount,type);
        return item;
    }
}
