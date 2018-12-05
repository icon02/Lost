package com.lost.lost.fragments;

import android.support.v4.app.Fragment;

import com.lost.lost.javaRes.mainApp.MainApp;

public abstract class FragmentPassObject extends Fragment {
    MainApp app;

    public void setApp(MainApp app) {
        this.app = app;
    }
}
