package com.lost.lost.javaRes.mainApp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.SystemClock;

import com.google.firebase.database.DatabaseReference;
import com.lost.lost.MainActivity;
import com.lost.lost.javaRes.friend.Friend;


import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class MainApp {

    private static final int REFRESH_RATE = 2000; //2sek

    private static final int TIME_UNTIL_EMERGENCY = 15000; //15sek
    private long timeOutOfInternet;
    private boolean ermergencyRunning;

    private WifiManager wifiManager;
    private WifiConfiguration wifiCofnig;

    private final String myUserID;
    private Timer timer;
    private TimerTask refreshTasks;
    private MainActivity mainActivity;


    private HashSet<Friend> friends;


    private boolean isRunning;
    private boolean isLoading;


    public MainApp(String userID, MainActivity mainActivity) {
        myUserID = userID;
        timer = new Timer();
        isRunning = false;
        isLoading = false;
        timer = new Timer();
        this.mainActivity = mainActivity;
        friends = new HashSet<>();
        timeOutOfInternet = 0;
        ermergencyRunning = false;
        wifiManager = (WifiManager)mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiCofnig = new WifiConfiguration();


        refreshTasks = new TimerTask() {
            @Override
            public void run() {
                //TODO insert all tasks that need to refresh
                if(!hasInternetConnection()) {
                    //no internet connection, count
                    if(timeOutOfInternet == 0) {
                        timeOutOfInternet = SystemClock.currentThreadTimeMillis();
                    } else {
                        if(SystemClock.currentThreadTimeMillis() - timeOutOfInternet >= TIME_UNTIL_EMERGENCY) {
                            //TODO stop/interrupt wifi hotspot

                        }
                    }
                } else {
                    timeOutOfInternet = 0;
                    //sync friendlist
                    syncMapsMarkers();
                }




            }


        };

    }

    private boolean hasInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void run() {
        isLoading = true;
        isRunning = true;


        isLoading = false;
        timer.schedule(refreshTasks, REFRESH_RATE);
        isRunning = false;
    }

    /* get the distance between to positions
    public int distance (LatLng a, LatLng b) {
        double lat_a = a.latitude;
        double lng_a = a.longitude;
        double lat_b = b.latitude;
        double lng_b = b.longitude;

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        float dist = (float) (distance * meterConversion);

        return Math.round(dist);
    }
    */

    public boolean isRunning() {
        return isRunning;
    }
    public boolean isLoading() { return isLoading; }
    public String MyUserID() { return myUserID; }
    public void setMainActivity(MainActivity mainActivity) { this.mainActivity = mainActivity; }

    /**************************************************************/
    /**********************Refresh Tasks***************************/
    /**************************************************************/



    public synchronized void syncMapsMarkers() {
        //TODO
        /*
        for(Friend f : friends) {
            mainActivity.mapsFragment().removeMarker(f.getName());
            mainActivity.mapsFragment().addMarker(f.getLastPosition(), f.getName());
        }
        mainActivity.mapsFragment().refreshMarkers();
        */
    }

    private static void loadAppWifiHotspotConfiguration() {

    }

    private static void loadDefaultWifiHostspotConfiguration() {

    }



}
