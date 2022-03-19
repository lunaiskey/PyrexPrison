package io.github.lunaiskey.pyrexprison.util;

import org.bukkit.ChatColor;

public class StringUtil {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }
}
