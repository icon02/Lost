package com.lost.lost.fragments;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lost.lost.R;


public class EmergencyFragment extends FragmentPassObject {

    private TextView lon;
    private TextView lat;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference ref = database.child("Users/");

    private String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String pos;
    StringBuffer buffer = new StringBuffer();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emergency, container, false);

        lat = v.findViewById(R.id.textView4);
        lon = v.findViewById(R.id.textView3);

        ref.child(uID).child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double longitude = dataSnapshot.getValue(Double.class);
                lon.setText(longitude.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child(uID).child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double latitude = dataSnapshot.getValue(Double.class);
                lat.setText(latitude.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


}
