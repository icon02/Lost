package com.lost.lost.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    private ListView friendsList;
    private FriendListAdapter friendListAdapter;

    private String uid = FirebaseAuth.getInstance().getUid();

    private Switch aSwitch;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("Users/").child(uid).child("Friends/");

    private ArrayList<Friend> friendlist;

    private boolean checked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        friendsList = v.findViewById(R.id.friends_ListView);
        friendListAdapter = new FriendListAdapter(getActivity(), getFriendsList());
        friendsList.setAdapter(friendListAdapter);

        aSwitch = v.findViewById(R.id.switch1);

        /*
        //TODO: Nullpointer on switch
        for (Friend f : getFriendsList()) {
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                       checked = true;
                    } else {
                        checked = false;
                    }
                }
            });
            f.setEnabled(checked);
        }
        */

        return v;
    }

    public ArrayList<Friend> getFriendsList() {
        final ArrayList<Friend> output = new ArrayList<>();

        //testing
        //output.add(new Friend("a73pkdrfy8X2KIQJRRNWrplk9ox1", "AlexTest"));
        output.add(new Friend("1IP8AjnmFJb6lGeO9sXwiyJNPEk1", "Test"));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: iterate over all friends in child "Friend" and add each one to output
                for (DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    Friend friend = snapshot.getValue(Friend.class);
                    output.add(new Friend(friend.getUserID(), friend.getName()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return output;
    }

}
