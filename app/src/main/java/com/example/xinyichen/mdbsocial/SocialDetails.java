package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.example.xinyichen.mdbsocial.R.id.interestedButton;

public class SocialDetails extends AppCompatActivity {
    boolean clickedButton;
    Social currSocial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_details);

        final Button interestedButton = (Button) findViewById(R.id.interestedButton);
        clickedButton = false;

        Intent intent = getIntent();

        currSocial = (Social) intent.getSerializableExtra("social");
        StorageReference sRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-39ef5.appspot.com").child("/" + currSocial.key + ".png");
        final DatabaseReference dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mdbsocial-39ef5.firebaseio.com").child("event");

        ImageView eventImg = (ImageView) findViewById(R.id.eventImg);
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(sRef)
                .into(eventImg);
        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText(currSocial.title);
        TextView date = (TextView) findViewById(R.id.dateText);
        date.setText(currSocial.date);
        TextView details = (TextView) findViewById(R.id.detailText);
        details.setText(currSocial.description);
        TextView email = (TextView) findViewById(R.id.emailText);
        email.setText(currSocial.hostEmail);


        final TextView numInterest = (TextView) findViewById(R.id.numInterested);
        numInterest.setText(currSocial.numInterested + " people are interested");

        interestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = !clickedButton;
                 if(clickedButton) {
                    interestedButton.setText("Yay! Have Fun :)");
                    onIntClicked(dRef);
                    numInterest.setText(currSocial.numInterested + " people are interested");

                } else {
                    interestedButton.setText("Interested?");
                     onIntClicked(dRef);
                    numInterest.setText(currSocial.numInterested + " people are interested");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SocialDetails.this, ListActivity2.class);
        startActivity(intent);
        super.onBackPressed();
    }

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
                    p.numInterested = p.numInterested - 1;
                } else {
                    // Star the post and add self to stars
                    p.numInterested = p.numInterested + 1;
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
