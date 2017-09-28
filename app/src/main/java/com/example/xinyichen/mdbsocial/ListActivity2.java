package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity2 extends AppCompatActivity {
    final ArrayList<Message> messages = new ArrayList<>();
    final ListAdapter adapter = new ListAdapter(this, messages);
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
    // the database reference, gives you the fir-demo node, getReference - goes to messages directory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        RecyclerView recyclerAdapter = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager(this));


        //Part 2: implement getList
        //Question 1: add Firebase Realtime Database to your project
        recyclerAdapter.setAdapter(adapter);



        //Question 2: initialize the messages based on what is in the database
        adapter.notifyDataSetChanged();
        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    /* String message = dataSnapshot.child("message").getValue(String.class);
                    String firebaseImageURL = dataSnapshot2.child("firebaseImageURL").getValue(String.class);
                    messages.add(new Message(message, firebaseImageURL)); */
                    messages.add(dataSnapshot2.getValue(Message.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Cancelled", "Failed to read value.", error.toException());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewMessageActivity.class);
                startActivity(intent);
            }
        });

        //Question 3: add an event listener for the children of the ref, and make it such that
        // every time a message is added, it creates a new message, adds it to messages and updates
        // the UI

        //Next part in NewMessageActivity
    }
}
