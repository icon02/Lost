package com.lost.lost;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lost.lost.fragments.AddFriendFragment;
import com.lost.lost.fragments.FriendsFragment;
import com.lost.lost.fragments.MapsFragment;
import com.lost.lost.fragments.PersProfileFragment;
import com.lost.lost.fragments.SettingsFragment;
import com.lost.lost.javaRes.account.LogInActivity;
import com.lost.lost.javaRes.mainApp.MainApp;
import com.lost.lost.javaRes.services.SplashActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REFRESH_RATE = 2000; //2 sec
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    public MainApp app;
    public FragmentManager fragmentManager;
    public static AddFriendFragment addFriendFragment;
    public static PersProfileFragment persProfileFragment;
    public static SettingsFragment settingsFragment;

    private ImageView persProfile;

    private MenuItem addFriend_MenuItem;

    public MapsFragment mapsFragment;
    public FriendsFragment friendsFragment;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uID;

    private GoogleApiClient mGoogleApiClient;

    //private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(uID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        fragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);


        //set persistence
        //ref.keepSynced(true);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initGoogleAPIClient();

        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //showGPSDisabledAlert();
            showSettingDialog();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        uID = currentUser.getUid();

        /*
        persProfile = findViewById(R.id.persProfile_Button);
        persProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new PersProfileFragment()).commit();
            }
        });
        */
        fragmentManager.beginTransaction().replace(R.id.fragment_container, mapsFragment).commit();
        /*
        timer = new Timer();
        refreshTasks = new TimerTask() {
            @Override
            public void run() {
                //TODO add all sync-Tasks

            }
        };
        timer.schedule(refreshTasks, REFRESH_RATE);
        */
    }



    private void init() {
        app = new MainApp(uID, this);
        mapsFragment = new MapsFragment();
        mapsFragment.setApp(app);
        friendsFragment = new FriendsFragment();
        friendsFragment.setApp(app);
        addFriendFragment = new AddFriendFragment();
        addFriendFragment.setApp(app);
        persProfileFragment = new PersProfileFragment();
        persProfileFragment.setApp(app);
        settingsFragment = new SettingsFragment();
        settingsFragment.setApp(app);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.gc();
        //TODO disable AccessPoint and reset/write old AccessPoint-Settings
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
        //TODO disable AccessPoint and reset/write old AccessPoint-Settings
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
            return true;
        } else if(id == R.id.addFriend_MenuItem) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, addFriendFragment).commit();
            return true;
        } else if(id == R.id.action_logOut){
            FirebaseAuth.getInstance().signOut();
            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            //TODO change nav_header_Title to fragment title
            setTitle("Maps");
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mapsFragment).commit();
        } else if (id == R.id.nav_friends) {
            setTitle("Friends");
            fragmentManager.beginTransaction().replace(R.id.fragment_container, friendsFragment).commit();
        } else if (id == R.id.nav_emergency) {
            //TODO enter Emergency Activity
            Intent emergency = new Intent(this, EmergencyActivity.class);
            startActivity(emergency);
            //fragmentManager.beginTransaction().replace(R.id.fragment_container, deviceListFragment).commit();
        } else if (id == R.id.nav_manage) {
            setTitle("Settings");
            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
        } else if (id == R.id.nav_aboutUs) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_personal){
            setTitle("Profile");
            fragmentManager.beginTransaction().replace(R.id.fragment_container, persProfileFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public MapsFragment mapsFragment() { return mapsFragment; }

    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
}


