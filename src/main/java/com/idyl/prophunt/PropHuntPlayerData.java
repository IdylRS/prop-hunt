package com.idyl.prophunt;

public class PropHuntPlayerData {
    public int modelId;
    public String username;
    public boolean hiding;

    public PropHuntPlayerData(String username, boolean hiding, int modelId) {
        this.username = username;
        this.hiding = hiding;
        this.modelId = modelId;
    }
}
