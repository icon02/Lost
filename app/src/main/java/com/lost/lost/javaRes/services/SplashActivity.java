package com.lost.lost.javaRes.services;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.lost.lost.MainActivity;
import com.lost.lost.R;

import java.util.Timer;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private static int splashTimeOut=2500;

    private static final int PERMISSION_REQUEST = 100;

    private TrackingService ts = new TrackingService();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=findViewById(R.id.logo);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashscreenanimation);
        logo.startAnimation(myanim);

        //check if GPS is enabled
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            finish();
        }

        //check if the app has access to the location permission
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED){
            startTrackingService();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while(ts.isCreating()) {

                }
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);
        */
        while(ts.isCreating()){

        }
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

        if (requestCode == PERMISSION_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackingService();
        } else {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackingService(){
        startService(new Intent(this, TrackingService.class));

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
    }
}
