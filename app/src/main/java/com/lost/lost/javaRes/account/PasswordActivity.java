package com.lost.lost.javaRes.account;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lost.lost.R;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SendPasswordReset";

    private EditText mEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mEmail = findViewById(R.id.pass_email);

        findViewById(R.id.newPassword_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.newPassword_button){
            //TODO: Nullpointer on email!
            sendPassword(mEmail.getText().toString());
        }
    }

    private void sendPassword(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }
}
