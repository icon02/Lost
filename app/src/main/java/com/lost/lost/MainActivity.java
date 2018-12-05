package com.lost.lost;

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
import android.view.View;

import com.lost.lost.fragments.AddFriendFragment;
import com.lost.lost.fragments.EmergencyFragment;
import com.lost.lost.fragments.FriendsFragment;
import com.lost.lost.fragments.MapsFragment;
import com.lost.lost.fragments.PersProfileFragment;
import com.lost.lost.javaRes.mainApp.MainApp;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainApp app;
    private FragmentManager fragmentManager;
    private EmergencyFragment emergencyFragment;
    private AddFriendFragment addFriendFragment;
    private PersProfileFragment persProfileFragment;

    MenuItem addFriend_MenuItem;


    MapsFragment mapsFragment;
    FriendsFragment friendsFragment;

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


        fragmentManager.beginTransaction().replace(R.id.fragment_container, mapsFragment).commit();
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

        } else if (id == R.id.nav_aboutUs) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_personal){
            fragmentManager.beginTransaction().replace(R.id.fragment_container, persProfileFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
