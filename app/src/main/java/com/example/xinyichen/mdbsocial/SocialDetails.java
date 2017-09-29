package com.example.xinyichen.mdbsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SocialDetails extends AppCompatActivity {
    boolean clickedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_details);

        final Button interestedButton = (Button) findViewById(R.id.interestedButton);
        clickedButton = false;

        interestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = !clickedButton;
                if(clickedButton) {
                    interestedButton.setText("Yay! Have Fun :)");
                } if (!clickedButton) {
                    interestedButton.setText("Interested?");
                }
            }
        });
    }
}
