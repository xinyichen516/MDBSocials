package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class LoginActivity extends AppCompatActivity {

    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //instance variable for FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.mobiledevsberkeley.org/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        //instance variable to listen for the auth state. Log when the auth state changes
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("SignedIn", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("SignedOut", "onAuthStateChanged:signed_out");
                }
            }
        };

        (findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        (findViewById(R.id.signupButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
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
            mAuth.signInWithEmailAndPassword(email, password)
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
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("CreatedUserWithEmail", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign up failed, have you already signed up?",
                                        Toast.LENGTH_LONG).show();
                            }

                            else {
                                startActivity(new Intent(LoginActivity.this, ListActivity2.class));
                            }
                        }
                    });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

