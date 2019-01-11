package com.lost.lost.friend;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Friend {

    private String name;
   // private ArrayList<LatLng> pos;
    private String userID;
    private LatLng pos;


    public Friend(String userID, String name) {
        this.name = name;
        this.userID = userID;
    }

    public Friend(String name, LatLng pos){
        this.name = name;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    //public LatLng getCurrentPosition() {
      //  return pos.get(pos.size() -1);
    //}
}
