package com.lost.lost.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lost.lost.R;
import com.lost.lost.javaRes.friend.Friend;
import com.lost.lost.javaRes.friend.FriendListAdapter;

import java.util.ArrayList;


public class FriendsFragment extends FragmentPassObject {

    ListView friendsList;
    FriendListAdapter friendListAdapter;

    private String uid = FirebaseAuth.getInstance().getUid();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("Users/").child(uid).child("Friends/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        friendsList = v.findViewById(R.id.friends_ListView);
        friendListAdapter = new FriendListAdapter(getActivity(), getFriendsList());
        friendsList.setAdapter(friendListAdapter);

        return v;
    }

    public ArrayList<Friend> getFriendsList() {
        final ArrayList<Friend> output = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: iterate over all friends in child "Friend" and add each one to output
                for (DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    Friend friend = snapshot.getValue(Friend.class);
                    output.add(friend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return output;
    }



}
