package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //instance variable for FirebaseAuth
        findViewById(R.id.logo).setOnClickListener(this);


        //instance variable to listen for the auth state. Log when the auth state changes

        (findViewById(R.id.loginButton)).setOnClickListener(this);

        (findViewById(R.id.signupButton)).setOnClickListener(this);
    }

    private void attemptLogin() {
        String email = ((EditText) findViewById(R.id.emailView)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_LONG).show();
            return;
        }

        if (!email.equals("") && !password.equals("")) {
            // Adds sign in capability. If successful, go to listactivity, else display a Toast
            Utils.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("SignInWithEmail", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign up fails, display a message to the user. If sign up succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("SignInWithEmailFailed", "signInWithEmail:failed", task.getException());
                                Toast.makeText(LoginActivity.this, "Sign in failed. Please try again!",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                finish();
                                startActivity(new Intent(LoginActivity.this, ListActivity2.class));
                            }
                        }
                    });
        }
    }

    private void attemptSignup() {
        String email = ((EditText) findViewById(R.id.emailView)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();

        if (!email.equals("") && !password.equals("")) {
            // adds sign up capability. Similar logic as login
            Utils.mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("CreatedUserWithEmail", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign up failed, have you already signed up?",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                finish();
                                startActivity(new Intent(LoginActivity.this, ListActivity2.class));
                            }
                        }
                    });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Utils.mAuth.addAuthStateListener(Utils.mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Utils.mAuthListener != null) {
            Utils.mAuth.removeAuthStateListener(Utils.mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signupButton:
                attemptSignup();
                break;
            case R.id.loginButton:
                attemptLogin();
                break;
            case R.id.logo:
                finish();
                Uri uri = Uri.parse("http://www.mobiledevsberkeley.org/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}

