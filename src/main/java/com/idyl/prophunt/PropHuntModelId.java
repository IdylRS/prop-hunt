package com.idyl.prophunt;

public enum PropHuntModelId {
    DEAD_TREE(11006),
    BUSH(1669),
    TREE(1637),
    CRATE(15402),
    ROCK_PILE(1391),
    HAT_STAND(1185),
    CHEST(11204),
    BED(1147),
    STOOL(10089),
    UNDOR(10321),
    BANK(10647),
    POTTED_PLANT(1632),
    LIGHT(10941),
    ROPE(11442),
    SPADE(9556),
    SIGN(9555);

    private final int value;

    private PropHuntModelId(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
}
