package io.github.lunaiskey.pyrexprison.player;

import org.bukkit.ChatColor;

public enum CurrencyType {
    TOKENS,
    GEMS,
    PYREX_POINTS,
    ;

    public static String getUnicode(CurrencyType type) {
        String unicode;
        switch (type) {
            case TOKENS -> unicode = "⛁";
            case GEMS -> unicode = "❈";
            case PYREX_POINTS -> unicode = "☀";
            default -> unicode = "#";
        }
        return unicode;
    }

    public static ChatColor getColorCode(CurrencyType type) {
        ChatColor color;
        switch (type) {
            case TOKENS -> color = ChatColor.YELLOW;
            case GEMS -> color = ChatColor.GREEN;
            case PYREX_POINTS -> color = ChatColor.LIGHT_PURPLE;
            default -> color = ChatColor.GRAY;
        }
        return color;
    }
}
