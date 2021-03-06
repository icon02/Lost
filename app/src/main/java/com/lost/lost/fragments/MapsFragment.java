package com.lost.lost.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lost.lost.R;
import com.lost.lost.javaRes.friend.Friend;
import com.lost.lost.javaRes.friend.ViewHolder;

import java.util.ArrayList;
import java.util.Iterator;


public class MapsFragment extends FragmentPassObject implements OnMapReadyCallback {

    GoogleMap map;
    ArrayList<MarkerOptions> marker;

    private String uID = FirebaseAuth.getInstance().getUid();

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users/");
    private DatabaseReference myRef = database.child(uID).child("Location/");
    private DatabaseReference friendsList = database.child(uID).child("Friends/");

    private long lat, lng;

    private long fLat, fLng;

    private LatLng pos;

    private static final String TAG = "friends";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        myRef.keepSynced(true);
        database.keepSynced(true);
        friendsList.keepSynced(true);

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera TODO
        LatLng mySelf = getPosition();
        map.setMyLocationEnabled(true);
        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           //GPS permission granted
           // map.setMyLocationEnabled(true);
        } else {
            //GPS permission denied

        }

        //map.setMyLocationEnabled(true);
        //map.addMarker(new MarkerOptions().position(mySelf).title("my pos"));
        /*map.addMarker(new MarkerOptions().position(mySelf).title("My Position"));
        map.moveCamera(CameraUpdateFactory.newLatLng(mySelf));
        try {
            //Thread.sleep(3000);
        } catch(Exception e) {}
*/
/*
        friendsList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.getChildren()){
                    Friend f = s.getValue(Friend.class);
                    LatLng location = getFriendsPosition(f.getUserID());
                   // String checked = s.child(f.getName()).child("enabled").getValue(String.class);
                    //if (checked.equals("true")) {
                        map.addMarker(new MarkerOptions().position(location).title(s.getKey()));
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        //map.animateCamera(CameraUpdateFactory.zoomTo(16f));

        Query query = friendsList.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                ArrayList<Friend> list = new ArrayList<>();

                while (iterator.hasNext()){
                    DataSnapshot next = iterator.next();
                    Log.i(TAG, "Friend = " + next.getValue());
                    //list.add(next.getValue(Friend.class));
                    //for (Friend f : list){
                      //  String name = f.getName();
                      //  String uid = f.getUserID();

                        //createMarker(getFriendsPosition(uid), name);
                        //Log.i(TAG, f.getName() + " " + f.getUserID() + " " + f.getLastPosition());
                    //}
                    String name = next.child("name").getValue(String.class);
                    String uid = next.child("userID").getValue(String.class);
                    LatLng location = position(uid);
                    Log.i(TAG,name + " " + location);
                    if(location != null) map.addMarker(new MarkerOptions().position(location).title(name));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*
    private Marker createMarker(LatLng location, String title){
        return map.addMarker(new MarkerOptions().position(location).title(title));
    } */

    private LatLng position(String id){
        DatabaseReference ref = database;
        if (database.child(id) != null) {
            ref = database.child(id).child("Location/");
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pos = new LatLng(dataSnapshot.child("latitude").getValue(Long.class),
                                dataSnapshot.child("longitude").getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return pos;
    }


    public LatLng getPosition(){

        myRef.child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    fLat = dataSnapshot.getValue(Long.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    fLng = dataSnapshot.getValue(Long.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LatLng latLng = new LatLng(fLat, fLng);
        return latLng;
    }

    private LatLng getFriendsPosition(String id){
        DatabaseReference friendsRef = database.child(id).child("Location/");

        friendsRef.child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    lat = dataSnapshot.getValue(Long.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        friendsRef.child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    lng = dataSnapshot.getValue(Long.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LatLng latLng = new LatLng(lat, lng);
        return latLng;
    }

    /*
    public synchronized void addMarker(LatLng pos, String name) {
        MarkerOptions m = new MarkerOptions().position(pos).title(name);
        marker.add(m);

    } */

    public synchronized void removeMarker(String name) {
        for(MarkerOptions m : marker) {
            if(m.getTitle().equals(name)) marker.remove(m);
        }


    }

    public void refreshMarkers() {
        for(MarkerOptions m : marker) {
            map.addMarker(m);
        }
    }



}
