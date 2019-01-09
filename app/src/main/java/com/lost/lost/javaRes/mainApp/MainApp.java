package com.lost.lost.javaRes.mainApp;

import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;

public class MainApp implements Runnable {

    private final String myUserID;
    private Timer timer;

    DatabaseReference database;
    DatabaseReference databaseMyData;


    private boolean isRunning = false;




    public MainApp(String myUserID) {
        this.myUserID = myUserID;
        timer = new Timer();
    }

    public static String verifyEMail(String email) {
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = temp.getDatabase();

        return null;
    }

    @Override
    public void run() {
        isRunning = true;

        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean syncMyLocation() {

        return false;
    }


    public String MyUserID() { return myUserID; }
}
