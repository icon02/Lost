package com.lost.lost.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lost.lost.R;

import net.sourceforge.zbar.Symbol;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES;


public class AddFriendFragment extends FragmentPassObject implements View.OnClickListener {

    private Button scanButton;
    private Button addButton;

    private TextView idText;

    private TextInputEditText nameText;


    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

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
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK){
                    Toast.makeText(this.getActivity(), "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                    updateText(data.getStringExtra(ZBarConstants.SCAN_RESULT));
                } else if(resultCode == RESULT_CANCELED && data != null){
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if (!TextUtils.isEmpty(error)){
                        Toast.makeText(this.getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
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




}
