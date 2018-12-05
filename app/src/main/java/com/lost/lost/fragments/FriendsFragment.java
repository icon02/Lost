package com.lost.lost.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.lost.lost.R;
import com.lost.lost.friend.Friend;
import com.lost.lost.friend.FriendListAdapter;

import java.util.ArrayList;


public class FriendsFragment extends FragmentPassObject {

    ListView friendsList;
    FriendListAdapter friendListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        friendsList = v.findViewById(R.id.friends_ListView);
        friendListAdapter = new FriendListAdapter(getActivity(), getFriendsList());
        friendsList.setAdapter(friendListAdapter);

        return v;
    }

    public ArrayList<Friend> getFriendsList() {
        ArrayList<Friend> output = new ArrayList<>();
        output.add(new Friend("Alex", new LatLng(48.44986, 14.323724)));
        output.add(new Friend("Nico", new LatLng(48.24986, 14.321784)));
        output.add(new Friend("Marco", new LatLng(48.134986, 14.323484)));
        output.add(new Friend("Lisa", new LatLng(48.344986, 14.323584)));
        output.add(new Friend("Ines", new LatLng(48.334186, 14.321784)));
        output.add(new Friend("Franz", new LatLng(48.334946, 14.323584)));
        output.add(new Friend("Alban", new LatLng(48.324986, 14.322784)));

        return output;
    }

}
