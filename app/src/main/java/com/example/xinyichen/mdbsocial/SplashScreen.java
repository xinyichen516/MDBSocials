package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;

    private static int WELCOME_TIMEOUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //instance variable to listen for the auth state. Log when the auth state changes
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("SignedIn", "onAuthStateChanged:signed_in:" + user.getUid());
                    LoggedIn();

                } else {
                    // User is signed out
                    Log.d("SignedOut", "onAuthStateChanged:signed_out");
                }
            }
        };


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcome = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(welcome);
                finish();

            }
        },WELCOME_TIMEOUT);
    }

    public void LoggedIn() {
        Intent intent = new Intent(this, ListActivity2.class);
        startActivity(intent);
    }
}
