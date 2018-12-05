package com.lost.lost.friend;

import com.google.android.gms.maps.model.LatLng;

public class Friend {

    private String name;
    private LatLng pos;
    private int dataBaseId;

    public Friend(String name, LatLng pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public int getDataBaseId() {
        return dataBaseId;
    }

    public LatLng getCurrentPosition() {
        return pos;
    }
}
