package com.lost.lost.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lost.lost.R;
import com.lost.lost.javaRes.friend.Friend;

import net.sourceforge.zbar.Symbol;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES;


public class AddFriendFragment extends FragmentPassObject implements View.OnClickListener {

    private Button scanButton;
    private Button addButton;

    private TextView idText;

    private EditText nameText;


    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private static final int PERMISSION_REQUEST = 100;

    private String friendsName;
    private String friendsID;

    private String uid = FirebaseAuth.getInstance().getUid();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("Users/").child(uid).child("Friends");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_friend, container, false);

        init(v);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(this.getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                for(int i = 0; i < 200; i++) {
                    try {
                        Log.d("AddFriendFragment", "Try to acces cammera. Request code: " + i);
                    } catch(Exception e) {
                        Log.e("AddFriendFragment", "Error with RequestCode: " + i);
                    }
                }

                requestPermissions(new String[] {Manifest.permission.CAMERA}, 50);
            }
        }

        int permission = checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA);

        if (permission == PackageManager.PERMISSION_GRANTED){

        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }

        return v;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.scan_button){
            //intent switch to qr scanner
            launchScanner(v);
        } else if (id == R.id.button2){
            // get string from scanner and textField and put in friendslist
            addNewFriend(friendsID, nameText.getText().toString());
            Toast.makeText(this.getActivity(), "Added Friend: " + nameText.getText().toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK){
                    friendsID = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                    Toast.makeText(this.getActivity(), "Scan Result = " +friendsID, Toast.LENGTH_SHORT).show();
                    updateText(friendsID);
                } else if(resultCode == RESULT_CANCELED && data != null){
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if (!TextUtils.isEmpty(error)){
                        Toast.makeText(this.getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void init(View v){
        scanButton = v.findViewById(R.id.scan_button);
        addButton = v.findViewById(R.id.button2);
        scanButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        nameText = v.findViewById(R.id.editText);
        idText = v.findViewById(R.id.textView6);
    }

    private void launchScanner(View v){
        if (isCameraAvailable()){
            Intent intent = new Intent(this.getActivity(), ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this.getActivity(), "Rear facing camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateText(String scanCode){
        idText.setText(scanCode);
    }

    private boolean isCameraAvailable(){
        PackageManager pm = this.getActivity().getPackageManager();

        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void addNewFriend(String id, String name){
        Friend friend = new Friend(id, name);

        myRef.child(name).setValue(friend);
    }



}
