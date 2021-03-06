package com.lost.lost.javaRes.friend;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.lost.lost.R;

import org.w3c.dom.Text;

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private CheckBox checkBox;

    public ViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView_name);
        checkBox = itemView.findViewById(R.id.checkBox);
    }

    public void setNameText(String name){
        textView.setText(name);
    }

    public CheckBox getCheckBox(){
        return checkBox;
    }
}
