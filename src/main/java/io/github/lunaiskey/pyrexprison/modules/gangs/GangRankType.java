package io.github.lunaiskey.pyrexprison.modules.gangs;

public enum GangRankType {
    MEMBER(0),
    MOD(1),
    OWNER(2),
    ;

    GangRankType(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }
}
