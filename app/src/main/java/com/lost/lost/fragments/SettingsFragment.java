package com.lost.lost.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lost.lost.R;


public class SettingsFragment extends FragmentPassObject {

    private Button ap_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        ap_btn = v.findViewById(R.id.ap_btn);
        ap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(ap_btn.getText().equals("OFF")) {
                    app.enableAp();
                    ap_btn.setText("ON");
                } else {
                    app.disableAp();
                    ap_btn.setText("OFF");
                }
                */
            }
        });

        return v;
    }

}
