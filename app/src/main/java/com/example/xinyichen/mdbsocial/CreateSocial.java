package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateSocial extends AppCompatActivity {
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private EditText title, date, details;
    private Button submitButton, uploadButton;
    ImageButton imgButton;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    DatabaseReference dbReference;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_social);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-39ef5.appspot.com/");
        dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mdbsocial-39ef5.firebaseio.com/");

        submitButton = (Button)findViewById(R.id.submitButton);
        imgButton = (ImageButton)findViewById(R.id.imageButton);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        title = (EditText) findViewById(R.id.titleText);
        date = (EditText) findViewById(R.id.dateText);
        details = (EditText) findViewById(R.id.detailText);
        submitButton = (Button) findViewById(R.id.submitButton);

        key = dbReference.child("event").push().getKey();


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null) {
                    StorageReference childRef = storageReference.child(key + ".png");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CreateSocial.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateSocial.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(CreateSocial.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent, 10);
                StorageReference fileRef = storageReference.child(key + ".png");
                Social event = new Social(title.getText().toString(), fileRef.toString(),
                        date.getText().toString(), details.getText().toString());
                event.setHostEmail(firebaseAuth.getCurrentUser().getEmail());
                event.setKey(key);
                dbReference.child("event").child(key).setValue(event);
                Intent intent = new Intent(CreateSocial.this, ListActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //switch(requestCode){
            //case 111:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        imgButton.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //break;
            /* case 10:
                StorageReference fileRef = storageReference.child(key + ".png");
                Social event = new Social(title.getText().toString(), fileRef.toString(),
                        date.getText().toString(), details.getText().toString());
                event.setHostEmail(firebaseAuth.getCurrentUser().getEmail());
                event.setKey(key);
                dbReference.child("event").child(key).setValue(event);
                Intent intent = new Intent(CreateSocial.this, ListActivity2.class);
                startActivity(intent);
                break; */

        }
    }