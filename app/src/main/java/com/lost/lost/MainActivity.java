package com.lost.lost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lost.lost.fragments.AddFriendFragment;
import com.lost.lost.fragments.EmergencyFragment;
import com.lost.lost.fragments.FriendsFragment;
import com.lost.lost.fragments.MapsFragment;
import com.lost.lost.fragments.PersProfileFragment;
import com.lost.lost.fragments.SettingsFragment;
import com.lost.lost.javaRes.mainApp.MainApp;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainApp app;
    private FragmentManager fragmentManager;
    private EmergencyFragment emergencyFragment;
    private AddFriendFragment addFriendFragment;
    private PersProfileFragment persProfileFragment;
    private SettingsFragment settingsFragment;


    ImageView persProfile;

    MenuItem addFriend_MenuItem;

    MapsFragment mapsFragment;
    FriendsFragment friendsFragment;

    private static final int PERMISSION_REQUEST = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        fragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        persProfile = findViewById(R.id.persProfile_Button);
        persProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new PersProfileFragment()).commit();
            }
        });
        */
        fragmentManager.beginTransaction().replace(R.id.fragment_container, mapsFragment).commit();
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

    private void init() {
        app = new MainApp();
        mapsFragment = new MapsFragment();
        mapsFragment.setApp(app);
        friendsFragment = new FriendsFragment();
        friendsFragment.setApp(app);
        emergencyFragment = new EmergencyFragment();
        emergencyFragment.setApp(app);
        addFriendFragment = new AddFriendFragment();
        addFriendFragment.setApp(app);
        persProfileFragment = new PersProfileFragment();
        persProfileFragment.setApp(app);
        settingsFragment = new SettingsFragment();
        settingsFragment.setApp(app);


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
            return true;
        } else if(id == R.id.addFriend_MenuItem) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, addFriendFragment).commit();
            return true;
        } else if(id == R.id.action_logOut){
            FirebaseAuth.getInstance().signOut();
            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
            //TODO: deactivate before presentation!!
        } else if (id == R.id.crash){
            Crashlytics.getInstance().crash(); //Force crash
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
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mapsFragment).commit();
        } else if (id == R.id.nav_friends) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, friendsFragment).commit();
        } else if (id == R.id.nav_emergency) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, emergencyFragment).commit();
        } else if (id == R.id.nav_manage) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
        } else if (id == R.id.nav_aboutUs) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_personal){
            fragmentManager.beginTransaction().replace(R.id.fragment_container, persProfileFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startTrackingService(){
        startService(new Intent(this, TrackingService.class));

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
    }
}
