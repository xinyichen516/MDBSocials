package com.example.xinyichen.mdbsocial;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity2 extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Social> socials;
    RecyclerView recyclerAdapter;
    ListAdapter adapter;

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

        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    /*
    contains the onValueEventListener to update the view as needed in order to fix the weird repeat bug
     */
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_list2);
        recyclerAdapter = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.addItemDecoration(new VerticalSpaceItemDecoration(3));
        socials = new ArrayList<>();

        adapter = new ListAdapter(getApplicationContext(), socials);
        recyclerAdapter.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Utils.query.addValueEventListener(new ValueEventListener() {
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

        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                finish();
                Intent intent = new Intent(getApplicationContext(), CreateSocial.class);
                startActivity(intent);
                break;
        }
    }

    /*
        creates a vertical space between each card view
         */
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

    /**
     * When the user presses the back button, a dialog pops up asking if they want to log out
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(ListActivity2.this);
        builder2.setMessage(R.string.logout);
        builder2.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.mAuth.signOut();
                finish();
                Intent intent = new Intent(ListActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder2.show();
    }

}