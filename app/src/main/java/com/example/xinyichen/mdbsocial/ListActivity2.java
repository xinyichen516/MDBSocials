package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity2 extends AppCompatActivity {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/event");
    ArrayList<Social> socials;
    RecyclerView recyclerAdapter;
    // the database reference, gives you the fir-demo node, getReference - goes to messages directory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        recyclerAdapter = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager(this));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                socials.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Social artist = postSnapshot.getValue(Social.class);
                    //adding artist to the list
                    socials.add(artist);
                }

                //creating adapter
                ListAdapter artistAdapter = new ListAdapter(ListActivity2.this, socials);
                //attaching adapter to the listview
                recyclerAdapter.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //socials = getList();
        //ListAdapter adapter = new ListAdapter(getApplicationContext(), socials);
        //recyclerAdapter.setAdapter(adapter);


        //Question 2: initialize the messages based on what is in the database
        //adapter.notifyDataSetChanged();
        // Read from the database
        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    //String message = dataSnapshot.child("message").getValue(String.class);
                    //String firebaseImageURL = dataSnapshot2.child("firebaseImageURL").getValue(String.class);
                    //messages.add(new Message(message, firebaseImageURL));
                    socials.add(dataSnapshot2.getValue(Social.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Cancelled", "Failed to read value.", error.toException());
            }
        });  */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateSocial.class);
                startActivity(intent);
            }
        });

        //Question 3: add an event listener for the children of the ref, and make it such that
        // every time a message is added, it creates a new message, adds it to messages and updates
        // the UI

        //Next part in NewMessageActivity
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                socials.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Social artist = postSnapshot.getValue(Social.class);
                    //adding artist to the list
                    socials.add(artist);
                }

                //creating adapter
                ListAdapter artistAdapter = new ListAdapter(ListActivity2.this, socials);
                //attaching adapter to the listview
                recyclerAdapter.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Social> getList() {
        final ArrayList<Social> events = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                Social currSocial = new Social(dataSnapshot.child("title").getValue(String.class),
                        dataSnapshot.child("firebaseImageUrl").getValue(String.class),
                        dataSnapshot.child("date").getValue(String.class),
                        dataSnapshot.child("description").getValue(String.class));
                currSocial.setHostEmail(dataSnapshot.child("hostEmail").getValue(String.class));
                events.add(currSocial);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return events;
    }
}
