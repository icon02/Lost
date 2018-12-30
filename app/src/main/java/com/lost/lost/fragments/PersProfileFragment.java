package com.lost.lost.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;
import com.lost.lost.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;



public class PersProfileFragment extends FragmentPassObject {

    //UI
    private TextView mUid;

    //Firebase
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uID = currentUser.getUid();

    //QR Code
    private static final String TAG = "GenerateQRCode";
    private ImageView qrImage;
    private Bitmap bitmap;
    private QRGEncoder encoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pers_profile, container, false);

        mUid = v.findViewById(R.id.persProfile_uid);
        mUid.setText(uID);

        qrImage = v.findViewById(R.id.qrCodeImage);

        createQRCode();

        return v;
    }

    private void createQRCode(){
        WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int heigth = point.y;
        int smallerDimension = width < heigth ? width : heigth;
        smallerDimension = smallerDimension * 3/4;

        encoder = new QRGEncoder(uID, null, QRGContents.Type.TEXT, smallerDimension);

        try{
            bitmap = encoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e){
            Log.v(TAG, e.toString());
        }
    }



}
