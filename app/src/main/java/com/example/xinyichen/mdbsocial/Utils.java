package com.example.xinyichen.mdbsocial;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by xinyichen on 9/30/17.
 */

public class Utils {

    static DatabaseReference genDRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mdbsocial-39ef5.firebaseio.com").child("event");
    static Query query = FirebaseDatabase.getInstance().getReference().child("event").orderByChild("timestamp");
    static StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-39ef5.appspot.com/");

    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
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

}
