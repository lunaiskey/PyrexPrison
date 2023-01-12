package io.github.lunaiskey.pyrexprison.modules.player;

import org.bukkit.ChatColor;

public enum CurrencyType {
    TOKENS,
    GEMS,
    PYREX_POINTS,
    ;

    public String getUnicode() {
        return switch (this) {
            case TOKENS -> "⛁";
            case GEMS -> "❈";
            case PYREX_POINTS -> "☀";
        };
    }

    public ChatColor getColorCode() {
        return switch (this) {
            case TOKENS -> ChatColor.YELLOW;
            case GEMS -> ChatColor.GREEN;
            case PYREX_POINTS -> ChatColor.LIGHT_PURPLE;
        };
    }
}
