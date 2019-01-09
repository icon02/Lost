package com.lost.lost.friend;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Friend {

    private String name;
    private ArrayList<LatLng> pos;
    private String userID;


    public Friend(String userID, String name) {
        this.name = name;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public LatLng getCurrentPosition() {
        return pos.get(pos.size() -1);
    }
}
