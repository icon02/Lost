package com.lost.lost.javaRes.mainApp;

import android.net.wifi.WifiManager;
import android.os.SystemClock;

import com.google.firebase.database.DatabaseReference;

import java.util.Timer;
import java.util.TimerTask;

public class Emergency {

    private static final int RETRY_INTERVAL = 2000;

    private DatabaseReference myDataBase;

    private WifiManager wifiManager;

    public Emergency(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    private Timer timer;
    private TimerTask tasks = new TimerTask() {
        @Override
        public void run() {

        }
    };

    public synchronized void run() {
        timer = new Timer();
        timer.schedule(tasks, RETRY_INTERVAL);
    }

    public synchronized void stop() {
        timer = null;
        System.gc();
    }



}
