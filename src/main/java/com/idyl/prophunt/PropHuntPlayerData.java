package com.idyl.prophunt;

public class PropHuntPlayerData {
    public int modelID;
    public String username;
    public boolean hiding;

    public PropHuntPlayerData(String username, boolean hiding, int modelID) {
        this.username = username;
        this.hiding = hiding;
        this.modelID = modelID;
    }

    @Override
    public String toString() {
        return "username: "+username+", hiding: "+hiding+", modelID: "+ modelID;
    }
}
