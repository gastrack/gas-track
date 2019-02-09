package com.example.gastrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CarInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_car_info);
        setContentView(R.layout.activity_car_info);
    }

    public void sendMessage1 (View view){
        Intent newActivity = new Intent(this,DestinationInfo.class);

        startActivity(newActivity);
    }
}
