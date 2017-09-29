package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity2 extends AppCompatActivity {
    ArrayList<Social> socials;
    RecyclerView recyclerAdapter;
    DatabaseReference ref;
    ListAdapter adapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        recyclerAdapter = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.addItemDecoration(new VerticalSpaceItemDecoration(3));
        socials = new ArrayList<>();


        adapter = new ListAdapter(getApplicationContext(), socials);
        recyclerAdapter.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        ref = FirebaseDatabase.getInstance().getReference("/event");
        query = FirebaseDatabase.getInstance().getReference().child("event").orderByChild("date");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    socials.add(dataSnapshot2.getValue(Social.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Cancelled", "Failed to read value.", error.toException());
            }
        });


        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateSocial.class);
                startActivity(intent);
            }
        });
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
    /* @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    socials.add(dataSnapshot2.getValue(Social.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Cancelled", "Failed to read value.", error.toException());
            }
        });
    } */
}
