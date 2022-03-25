package io.github.lunaiskey.pyrexprison.player;

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
            default -> unicode = "$";
        }
        return unicode;
    }
}
