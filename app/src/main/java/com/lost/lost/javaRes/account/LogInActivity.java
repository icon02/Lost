package com.lost.lost.javaRes.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.lost.lost.MainActivity;
import com.lost.lost.R;
import com.lost.lost.javaRes.services.SplashActivity;
import com.lost.lost.javaRes.mainApp.MainApp;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mTextView;

    private FirebaseAuth mAuth;

    MainApp mainApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.lost.lost.R.layout.activity_log_in);


        //fields
        mEmailField = findViewById(com.lost.lost.R.id.auth_email);
        mPasswordField = findViewById(com.lost.lost.R.id.auth_password);

        //buttons
        findViewById(com.lost.lost.R.id.auth_create_button).setOnClickListener(this);
        findViewById(com.lost.lost.R.id.auth_logIn_button).setOnClickListener(this);
        findViewById(com.lost.lost.R.id.auth_password_reset).setOnClickListener(this);

        //init auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn(String email, String password){
        Log.d(TAG, "signIn" + email);
        if(!validForm()) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //final boolean emailVerified = mAuth.getCurrentUser().isEmailVerified();
                        //TODO: add "&& emailVerified" in if!
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LogInActivity.this, "Authentification succeded!", Toast.LENGTH_SHORT).show();
                            logedIn();
                        //} else if (!emailVerified){
                        //    Log.w(TAG, "SignInWithEmail:failed -> no verification", task.getException());
                        //    Toast.makeText(LogInActivity.this, "Please verify your mail adress!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "SignInWithEmail:failed", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentification failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logedIn(){
        Intent splash = new Intent(this, SplashActivity.class);
        startActivity(splash);
    }

    private boolean validForm(){
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)){
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)){
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == com.lost.lost.R.id.auth_create_button){
            Intent create = new Intent(this, CreateAccountActivity.class);
            startActivity(create);
        } else if (i == com.lost.lost.R.id.auth_logIn_button){
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == com.lost.lost.R.id.auth_password_reset){
            Intent reset = new Intent(this, PasswordActivity.class);
            startActivity(reset);
        }
    }
}
