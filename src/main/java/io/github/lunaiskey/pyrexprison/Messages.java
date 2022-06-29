package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.util.StringUtil;

public enum Messages {
    NO_PERMISSION("&cNo Permission."),
    ;


    private final String text;
    Messages(String text) {
        this.text = text;
    }

    public String getText() {
        return StringUtil.color(text);
    }

    public String getRawText() {
        return text;
    }
}
