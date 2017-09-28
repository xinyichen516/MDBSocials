package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateSocial extends AppCompatActivity {
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private EditText title, date, details;
    private Button submitButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_social);

        //initialize firebase auth obj
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(CreateSocial.this, LoginActivity.class));
        }

        title = (EditText) findViewById(R.id.titleText);
        date = (EditText) findViewById(R.id.dateText);
        details = (EditText) findViewById(R.id.detailText);
        submitButton = (Button) findViewById(R.id.insertPic);

        /*FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView nameGreeting = (TextView) findViewById(R.id.pageText);
        nameGreeting.setText("Create your event, " + user.getDisplayName());*/

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

            final String key = dbReference.child("event").push().getKey();
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-39ef5.appspot.com/");
            final StorageReference fileRef = mStorageRef.child(key + ".png");

            fileRef.putFile(data.getData()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateSocial.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Social event = new Social(title.getText().toString(), fileRef.toString(),
                            date.getText().toString(), details.getText().toString());
                    event.setHostEmail(firebaseAuth.getCurrentUser().getEmail());
                    dbReference.child("event").child(key).setValue(event);
                    Intent intent = new Intent(CreateSocial.this, ListActivity2.class);
                    startActivity(intent);
                }
            });
        }
    }
}
