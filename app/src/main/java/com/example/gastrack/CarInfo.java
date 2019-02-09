package com.example.gastrack;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CarInfo extends AppCompatActivity {

    int mpgTotal, year;
    String make, modelFinal;
    String modelApi = "https://www.fueleconomy.gov/ws/rest/vehicle/menu/model?";
    String modelNumberApi = "https://www.fueleconomy.gov/ws/rest/vehicle/menu/options?year=";
    String mpgApi = "https://www.fueleconomy.gov/ws/rest/vehicle/";
    String TAG = "MainActivity";
    List<String> modelList;
    int modelNumber, DTE;
    Button button;
    EditText mCaryear;
    EditText mModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_car_info);
        setContentView(R.layout.activity_car_info);
    }

    public void sendMessage1(View view) {
        Intent newActivity = new Intent(this, DestinationInfo.class);

        startActivity(newActivity);
    }

    private class GetModelTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder jsonText = new StringBuilder();
            JSONArray result = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonText.append(line);
                    }
                    Log.d(TAG, jsonText.toString());
//                    if (jsonText.toString().startsWith("{ \"items\":")) {
//                        // Google Cloud can't return a raw JSON list, so we had to add this "items" field;
//                        // remove it now.
//                        String jsonItemsText = new JSONObject(jsonText.toString()).get("items").toString();
//                        result = new JSONArray(jsonItemsText);
//                    } else if (jsonText.toString().equals("null")) {
//                        result = new JSONArray();
//                    } else {
//                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
//                    }

//                    Log.d(TAG, result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return jsonText.toString();
        }


    }

    private URL createURLModel(int carYear, String carMake) {
        try {
            String urlString = modelApi;
            urlString = urlString + "year=" + Integer.toString(carYear) + "&make=" + carMake;
            Log.d(TAG, "url string: {" + urlString + "}");
            return new URL(urlString);
        } catch (Exception e) {
            Log.d(TAG, "createURLLatLng failed");
//            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    protected void GetModel(int carYear, String carMake) {
        new CarInfo.GetModelTask().execute(createURLModel(carYear, carMake));
    }


//    button.setOnClickListener(new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            mCaryear = (EditText)findViewById(R.id.car_year);
//            mModel = (EditText)findViewById(R.id.edit_text);
//
//            String model = mModel.getText().toString();
//            int year = Integer.parseInt(mCaryear.getText().toString());
//            GetModel(year,model);
//        }
//
//    }


}
