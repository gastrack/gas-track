package com.example.gastrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DestinationInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_destination_info);
    }
}
