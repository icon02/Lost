package com.lost.lost.javaRes.services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lost.lost.R;

public class TrackingService extends Service {
    private static final int UPDATE_INTERVAL = 5000;

    private static final String TAG = TrackingService.class.getSimpleName();

    private FirebaseUser currentUser;
    private String uID;

    private boolean isCreating;

    public TrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    @Override
    public void onCreate(){
        isCreating = true;
        super.onCreate();
       // createNotification();
        createDatabaseReference();
        requestLocationUpdates();
        isCreating = false;
    }

    //creating persistent notification
    private void createNotification(){
        String stop = "stop";
        registerReceiver(stopReciver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.tracking_enabled);
        startForeground(1, builder.build());
    }


    protected BroadcastReceiver stopReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReciver);
            stopSelf();
        }
    };

    public boolean isCreating() {
        return isCreating;
    }

    private void createDatabaseReference(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();
    }

    private void requestLocationUpdates(){
        LocationRequest request = new LocationRequest();
        request.setInterval(UPDATE_INTERVAL);

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(request, new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) ref.child(uID).child("Location").setValue(location);
                }
            }, null);
        }
    }

}
