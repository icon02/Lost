package com.lost.lost.javaRes.mainApp;

import android.widget.ImageView;

public class MainApp implements Runnable {

    private boolean isRunning = false;
    ImageView loadingIcon;

    public void setLoadingIcon(ImageView icon) {
        loadingIcon = icon;
    }

    @Override
    public void run() {
        isRunning = true;

        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
