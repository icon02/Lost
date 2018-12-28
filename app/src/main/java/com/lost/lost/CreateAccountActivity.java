package com.lost.lost;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        mEmailField = findViewById(R.id.create_email);
        mPasswordField = findViewById(R.id.create_password);

        findViewById(R.id.create_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


    }
    /**
     @Override
     protected void onStart(){
     super.onStart();

     FirebaseUser currentUser = mAuth.getCurrentUser();
     }
     */
    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount:"+email);
        if(!validForm()) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(CreateAccountActivity.this, "Please confirm email and then log in!", Toast.LENGTH_SHORT).show();

                            /**
                             //sleep to show TOAST
                             try{
                             TimeUnit.SECONDS.sleep(10);
                             } catch (InterruptedException e){
                             return;
                             }
                             **/

                            createdAccount();
                        } else {
                            Log.w(TAG, "createWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentification failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private void createdAccount(){
        FirebaseUser createdAccount = mAuth.getCurrentUser();

        createdAccount.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Email sent.");
                    }
                });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.create_button){
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}
