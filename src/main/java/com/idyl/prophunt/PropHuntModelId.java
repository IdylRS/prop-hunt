package com.idyl.prophunt;

public enum PropHuntModelId {
    BUSH(1565),
    CRATE(12152),
    ROCK_PILE(1391),
    HAT_STAND(1185),
    CHEST(1249),
    STOOL(10089),
    POTTED_PLANT(1620),
    MUSHROOM(11985),
    SKELETON(1078),
    SKELETON2(1080),
    PIPE(12472),
    BOULDER(15624),
    BOULDER2(20031),
    CANNONBALLS(1550),
    CACTUS(1580),
    FERN(1618),
    DEAD_TREE(1719),
    DAGGER(2672),
    BONES(2674);


    private final int value;

    private PropHuntModelId(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
}
