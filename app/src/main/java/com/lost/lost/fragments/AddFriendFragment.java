package com.lost.lost.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lost.lost.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import static com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES;


public class AddFriendFragment extends FragmentPassObject implements View.OnClickListener {

    private Button scanButton;
    private Button addButton;

    private TextView idText;

    private TextInputEditText nameText;

    private IntentIntegrator qrScann;

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_friend, container, false);

        //init();
        scanButton = v.findViewById(R.id.scan_button);
        addButton = v.findViewById(R.id.button2);
        scanButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        idText = v.findViewById(R.id.textView6);

        initIntegrator();

        return v;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.scan_button){
            //intent switch to qr scanner
            qrScann.setDesiredBarcodeFormats(QR_CODE_TYPES);
            qrScann.initiateScan();
        } else if (id == R.id.button2){
            // get string from scanner and textField and put in friendslist
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,
        //        data.putExtra("SCAN_FORMATS", "QR_CODE "));

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){

            if (result.getContents() == null){
                Toast.makeText(this.getActivity(), "ID not found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity(), "Scan successful", Toast.LENGTH_SHORT).show();
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    // create new Friend with id and name

                    //TODO: just for testing purpose. DELETE after test successful!!
                    updateText(result.getContents());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(this.getActivity(), "NO DATA", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        scanButton = getView().findViewById(R.id.scan_button);
        addButton = getView().findViewById(R.id.button2);
        scanButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        qrScann = new IntentIntegrator(this.getActivity());

        idText = getView().findViewById(R.id.textView6);

        //nameText = getView().findViewById(R.id.friendNameInput_TextInputLayout);
    }

    private void initIntegrator(){
        qrScann = new IntentIntegrator(this.getActivity());
        qrScann.setPrompt("Scan a qr code");
        qrScann.setDesiredBarcodeFormats(qrScann.QR_CODE_TYPES);
        qrScann.setCameraId(0);
        qrScann.setOrientationLocked(false);
        qrScann.setBeepEnabled(true);

    }

    private void updateText(String scanCode){
        idText.setText(scanCode);
    }


}
