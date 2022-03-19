package io.github.lunaiskey.pyrexprison.player;

public enum CurrencyType {
    TOKENS,
    GEMS,
    CRYSTALS
    ;

    public static String getUnicode(CurrencyType type) {
        String unicode;
        switch (type) {
            case TOKENS -> unicode = "⛁";
            case GEMS -> unicode = "❈";
            default -> unicode = "$";
        }
        return unicode;
    }
}
