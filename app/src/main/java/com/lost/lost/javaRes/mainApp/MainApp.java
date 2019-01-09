package com.lost.lost.javaRes.mainApp;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;

public class MainApp implements Runnable {

    private final String myUserID;
    private Timer timer;

    DatabaseReference database;
    DatabaseReference databaseMyData;


    private boolean isRunning;
    private boolean isLoading;




    public MainApp(String myUserID) {
        this.myUserID = myUserID;
        timer = new Timer();
        isRunning = false;
        isLoading = false;
    }

    public static String verifyEMail(String email) {
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference();


        return null;
    }

    @Override
    public void run() {
        isLoading = true;
        isRunning = true;

        isLoading = false;

        //TODO sync
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
    public boolean isLoading() { return isLoading; }

    public boolean syncMyLocation() {

        return false;
    }


    public String MyUserID() { return myUserID; }
}
