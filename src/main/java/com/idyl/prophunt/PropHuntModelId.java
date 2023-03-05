package com.idyl.prophunt;

import lombok.Getter;

import java.util.LinkedHashMap;

public class PropHuntModelId {
    public static LinkedHashMap<String, PropHuntModelId> map = new LinkedHashMap<>();

    @Getter
    private int id;
    @Getter
    private String name;

    public PropHuntModelId(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public static PropHuntModelId[] values() {
        return map.values().toArray(PropHuntModelId[]::new).clone();
    }

    public static PropHuntModelId valueOf(String name) {
        PropHuntModelId model = map.get(name);
        if (model == null) {
            throw new IllegalArgumentException("No model by the name " + name + " found");
        }
        return model;
    }

    public static String[] keys() {
        return map.keySet().toArray(String[]::new);
    }

    public static void add(String name, int modelID) {
        map.put(name, new PropHuntModelId(name, modelID));
    }
}
