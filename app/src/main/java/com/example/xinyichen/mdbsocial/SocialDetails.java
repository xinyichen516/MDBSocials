package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class SocialDetails extends AppCompatActivity implements View.OnClickListener{
    boolean clickedButton;
    Social currSocial;
    Button interestedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_details);

        findViewById(R.id.interestedButton).setOnClickListener(this);
        clickedButton = false;

        Intent intent = getIntent();

        currSocial = (Social) intent.getSerializableExtra("social");
        StorageReference sRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-39ef5.appspot.com").child("/" + currSocial.key + ".png");

        Utils.mAuth.addAuthStateListener(Utils.mAuthListener);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(sRef)
                .into((ImageView) findViewById(R.id.eventImg));


        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText(currSocial.title);
        TextView date = (TextView) findViewById(R.id.dateText);
        date.setText(currSocial.date);
        TextView details = (TextView) findViewById(R.id.detailText);
        details.setText(currSocial.description);
        TextView email = (TextView) findViewById(R.id.emailText);
        email.setText(currSocial.hostEmail);


        final TextView numInterest = (TextView) findViewById(R.id.numInterested);
        numInterest.setText(this.getResources().getString(R.string.pplInt, currSocial.numInterested + ""));


        Utils.genDRef.child(currSocial.key).child("numInterested").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                numInterest.setText(numInterest.getResources().getString(R.string.pplInt, snapshot.getValue() + ""));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.interestedButton:
                clickedButton = !clickedButton;
                if(clickedButton) {
                    interestedButton.setText(view.getResources().getString(R.string.yay));
                    onIntClicked(Utils.genDRef.child(currSocial.key));

                } else {
                    interestedButton.setText(view.getResources().getString(R.string.inter));
                    onIntClicked(Utils.genDRef.child(currSocial.key));
                }
                break;
        }
    }

    /*
    updates the number of interested people and changes the button text
     */
    private void onIntClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Social p = mutableData.getValue(Social.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (clickedButton) {
                    // Unstar the post and remove self from stars
                    p.numInterested = p.numInterested + 1;
                } else {
                    // Star the post and add self to stars
                    p.numInterested = p.numInterested - 1;
                }
                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("postTransaction", "postTransaction:onComplete:" + databaseError);
            }
        });
    }
}
