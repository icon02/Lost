package com.lost.lost.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lost.lost.R;


public class PersProfileFragment extends FragmentPassObject {

    //UI
    private TextView mUid;

    //Firebase
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uID = currentUser.getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pers_profile, container, false);

        mUid = v.findViewById(R.id.persProfile_uid);
        mUid.setText(uID);



        return v;
    }

}
