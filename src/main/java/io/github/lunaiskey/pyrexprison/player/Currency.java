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
import java.util.ArrayList;
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
    public static void giveCurrency(UUID pUUID, CurrencyType type, long amount) {
        if (amount < 0) {
            return;
        }
        PyrexPlayer pPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> pPlayer.giveTokens(amount);
            case GEMS -> pPlayer.giveGems(amount);
            case PYREX_POINTS -> pPlayer.givePyrexPoints(amount);
        }
    }

    /**
     * Takes a type of currency from a player, floored to the nearest 2 digits.
     * @param pUUID player who you want to take currency from
     * @param type type of currency to take
     * @param amount amount of currency to take
     * @return amount that was taken
     */
    public static void takeCurrency(UUID pUUID, CurrencyType type,  long amount) {
        if (amount < 0) {
            return;
        }
        PyrexPlayer pPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> {
                if (amount >= pPlayer.getTokens()) {
                    pPlayer.setTokens(0);
                    break;
                }
                pPlayer.takeTokens(amount);}
            case GEMS -> {
                if (amount >= pPlayer.getGems()) {
                    pPlayer.setGems(0);
                    break;
                }
                pPlayer.takeGems(amount);}
            case PYREX_POINTS -> {
                if (amount >= pPlayer.getPyrexPoints()) {
                    pPlayer.setPyrexPoints(0);
                    break;
                }
                pPlayer.takePyrexPoints(amount);}
        }
    }

    public static void setCurrency(UUID pUUID, CurrencyType type, long amount) {
        long newAmount = 0;
        if (amount > 0) {
            newAmount = amount;
        }
        PyrexPlayer pPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(pUUID);
        switch (type) {
            case TOKENS -> pPlayer.setTokens(newAmount);
            case GEMS -> pPlayer.setGems(newAmount);
            case PYREX_POINTS -> pPlayer.setPyrexPoints(newAmount);
        }
    }

    private static double getAcceptedAmount(double original) {
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
        meta.setDisplayName(StringUtil.color("   &7- &6&lBank Note &7-"));
        List<String> lore = new ArrayList<>(List.of(
                " ",
                "&7• Type: %type%",
                "&7• Amount: %amount%",
                " ",
                "&eRight Click to redeem!"));
        String strType = "";
        String strAmount = CurrencyType.getUnicode(type)+Numbers.formattedNumber(amount);
        switch (type) {
            case TOKENS -> {
                strType = "&eTokens";
                strAmount = "&e"+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount);
            }
            case GEMS -> {
                strType = "&aGems";
                strAmount = "&a"+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount);
            }
            case PYREX_POINTS -> {
                strType = "&dPyrex Points";
                strAmount = "&d"+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount);
            }
        }
        for (int i = 0;i<lore.size();i++) {
            String line = lore.get(i);
            if (line.contains("%type%")) {
                line = line.replace("%type%",strType);
            }
            if (line.contains("%amount%")) {
                line = line.replace("%amount%",strAmount);
            }
            lore.set(i,StringUtil.color(line));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        item = NBTTags.setCurrencyVoucherTags(item,amount,type);
        return item;
    }
}
