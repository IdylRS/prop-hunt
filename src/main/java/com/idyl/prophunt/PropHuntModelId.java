package com.idyl.prophunt;

public enum PropHuntModelId {
    DEAD_TREE(11006),
    BUSH(1669),
    TREE(1637),
    CRATE(15402),
    ROCK_PILE(1391),
    HAT_STAND(1185);

    private final int value;

    private PropHuntModelId(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
}
