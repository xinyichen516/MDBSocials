package com.example.xinyichen.mdbsocial;

/**
 * Created by xinyichen on 9/27/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewMessageActivity extends AppCompatActivity {
    StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Part 3: Here we will get the image and the code from the switch and create a new
            // message while also storing the image
            //Question 1: add Firebase Storage to your project
            //Question 2: create a DatabaseReference below
            final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();


            //Question 3: generate a key below to use as a unique identifier for the message, and
            // for the image filename
            final String key = dbReference.child("messages").push().getKey();
            //final String key = dbReference.child("messages").push().getKey();

            //Question 4: create a StorageReference below (hint: the url you need can be found in
            // your console at firebase.google.com

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-demo-master-70479.appspot.com");
            StorageReference fileRef = storageReference.child(key + ".png");


            //Question 5: add a png file to the storage using the key as the filename. If it fails,
            // write a toast. If it works, add the message. Get the value of the switch using this line:
            // (((Switch) findViewById(R.id.switch1)).isChecked() ? "1" : "0")
            // Then go back to the ListActivity2

            fileRef.putFile(data.getData()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewMessageActivity.this, "aw shucks", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String msg = (((Switch) findViewById(R.id.switch1)).isChecked() ? "1" : "0");
                    Message message = new Message(msg, key);
                    dbReference.child("messages").child(key).setValue(message);
                    startActivity(new Intent(NewMessageActivity.this, ListActivity2.class));
                }
            });
            //Last part in ListAdapter
        }


    }
}

