package com.lost.lost.javaRes.friend;

import com.google.android.gms.maps.model.LatLng;


public class Friend {

    private String name;
   // private ArrayList<LatLng> pos;
    private String userID;
    private LatLng pos;
    private boolean enabled;


    public Friend(String userID, String name) {
        this.enabled = true;
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

    public LatLng getLastPosition() {
        return pos;
    }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
