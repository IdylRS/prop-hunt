package com.idyl.prophunt;

public enum PropHuntModelId {
    DEAD_TREE(11006),
    BUSH(7826),
    YEW_TREE(12949),
    CRATE(15402),
    WOODEN_CRATE(12148),
    ROCK_PILE(1391),
    HAT_STAND(1185),
    CHEST(13975),
    STOOL(10089),
    BANK(10647),
    POTTED_PLANT(1620),
    SPADE(9556),
    MUSHROOM(11985),
    SKELETON(6186);

    private final int value;

    private PropHuntModelId(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
}
