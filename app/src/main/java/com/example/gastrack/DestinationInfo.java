package com.example.gastrack;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.anastr.speedviewlib.DeluxeSpeedView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class DestinationInfo extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    DeluxeSpeedView deluxeSpeedView;
    SeekBar seekBar;
    Button ok;
    TextView textSpeed;
    EditText destinationEditText;
    //  CheckBox withTremble, withEffects;
    int distanceTotal, timeTotal;
    String apiKey = "&key=AIzaSyCweBWVyxQ26oCNARzpnDVeVlFm2qIMsHE";
    String latlongApi = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    String distanceApi = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    String startAddress, destinationAddress;
    float DTE, MPG, gallonsInTank;
    TextView popupText;
    String TAG = "DestinationInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_destination_info);
        deluxeSpeedView = findViewById(R.id.deluxeSpeedView);
        destinationEditText = findViewById(R.id.destinationEditText);
        seekBar = findViewById(R.id.seekBar);
        ok = findViewById(R.id.ok);
        popupText = findViewById(R.id.popupText);
        textSpeed = findViewById(R.id.textSpeed);
//        withTremble = findViewById(R.id.withTremble);
//        withEffects = findViewById(R.id.withEffects);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);




        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deluxeSpeedView.speedTo(seekBar.getProgress());
                try {
                    Log.d(TAG, "in try block");
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(DestinationInfo.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
//                            Log.d(TAG, "location is: " + location.toString());
                            Log.d(TAG, "location success");
                            if (location != null) {
                                Log.d(TAG, "location not null");
                                GetStartLocation(location.getLatitude(), location.getLongitude(), destinationEditText.getText().toString());
                            }
                        }
                    });
                } catch (SecurityException e) {
                    Log.d(TAG, "try block failed");
                    e.printStackTrace();
                }
            }
        });

//        withTremble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                deluxeSpeedView.setWithTremble(isChecked);
//            }
//        });
//
//        withEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                deluxeSpeedView.setWithEffects(isChecked);
//            }
//        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void GetDistanceTime() {
        startAddress = startAddress.replaceAll("\\s+","+");
        startAddress = startAddress.replaceAll(",","");
        destinationAddress = destinationAddress.replaceAll("\\s+","+");
        destinationAddress = destinationAddress.replaceAll(",","");
        new DestinationInfo.GetDistanceTask().execute(createURLDistance(startAddress, destinationAddress));

    }

    protected void GetStartLocation(double startLatitude, double startLongitude, String destination) {
        /*
        jsonObject jsonAddress = HttpRequest(latlongApi + startLatitude + startLongitude);
        String startAddress = jsonAddress.find();
        jsonObject jsonDistance = HttpRequest(distanceApi + startAddress + "&destination=" + destination);
        distance = jsonDistance.find();
        time = jsonDistance.find();
         */
        startAddress = "";
        Log.d(TAG, "in GetStartLocation");
        new DestinationInfo.GetAddressTask().execute(createURLLatLng(Double.toString(startLatitude), Double.toString(startLongitude)));
        destinationAddress = destination;
    }

    private class GetAddressTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
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
                    if (jsonText.toString().startsWith("{ \"items\":")) {
                        // Google Cloud can't return a raw JSON list, so we had to add this "items" field;
                        // remove it now.
                        String jsonItemsText = new JSONObject(jsonText.toString()).get("items").toString();
                        result = new JSONArray(jsonItemsText);
                    } else if (jsonText.toString().equals("null")) {
                        result = new JSONArray();
                    } else {
                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
                    }
                    Log.d(TAG, result.toString());
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
            return result;
        }


        @Override
        protected void onPostExecute(JSONArray Address) {
            if (Address == null) {
                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
            } else if (Address.length() == 0) {
                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoAddress(Address);
            }
        }

    }

    /**
     * Converts the JSON ride data to an arraylist
     *
     * @param AddressArray JSON array of player objects
     */
    private void convertJSONtoAddress(JSONArray AddressArray) {
        try{
            Log.d(TAG, "First index: " + AddressArray.getString(0));
            JSONObject addressObject = (JSONObject) AddressArray.get(0);
            AddressArray = (JSONArray) addressObject.get("results");
            addressObject = (JSONObject) AddressArray.get(0);
            String address = (String) addressObject.get("formatted_address");
            Log.d(TAG, "address hopefully: " + address);
            startAddress = address;
            Log.d(TAG, "Starting getDistanceTime");
            GetDistanceTime();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param lat string version of the latitude
     * @param lng string version of the longitude
     * @return URL formatted for the ride server
     */
    private URL createURLLatLng(String lat, String lng) {
        try {
            String urlString = latlongApi;
            urlString = urlString + lat + "," + lng + apiKey;
            Log.d(TAG, "url string: {" + urlString + "}");
            return new URL(urlString);
        } catch (Exception e) {
            Log.d(TAG, "createURLLatLng failed");
//            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
        }

        return null;
    }


    private class GetDistanceTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... params) {
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
                    if (jsonText.toString().startsWith("{ \"items\":")) {
                        // Google Cloud can't return a raw JSON list, so we had to add this "items" field;
                        // remove it now.
                        String jsonItemsText = new JSONObject(jsonText.toString()).get("items").toString();
                        result = new JSONArray(jsonItemsText);
                    } else if (jsonText.toString().equals("null")) {
                        result = new JSONArray();
                    } else {
                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
                    }
                    Log.d(TAG, result.toString());
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
            return result;
        }


        @Override
        protected void onPostExecute(JSONArray Distance) {
            if (Distance == null) {
                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
            } else if (Distance.length() == 0) {
                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
            } else {
                convertJSONtoDistance(Distance);
            }
        }

    }

    /**
     * Converts the JSON ride data to an arraylist
     *
     * @param DistanceArray JSON array of player objects
     */
    private void convertJSONtoDistance(JSONArray DistanceArray) {
        try{
            //Whittles down the JSON Array to just the data we want: the time and distance of trip
            JSONObject DistanceObject = (JSONObject) DistanceArray.get(0);
            DistanceArray = (JSONArray) DistanceObject.get("routes");
            DistanceObject = (JSONObject) DistanceArray.get(0);
            DistanceArray = (JSONArray) DistanceObject.get("legs");
            DistanceObject = (JSONObject) DistanceArray.get(0);
            JSONObject TimeObject = (JSONObject) DistanceObject.get("duration");
            DistanceObject = (JSONObject) DistanceObject.get("distance");
            String time = (String) TimeObject.get("text");
            String distance = (String) DistanceObject.get("text");

            Log.d(TAG, "distance hopefully: " + distance);
            Log.d(TAG, "time hopefully: " + time);

            distance = distance.substring(0, distance.length() - 3);
            distanceTotal = Integer.parseInt(distance);

            String [] timeArray = time.split(" ");

            //Calculate the duration of the trip in hours
            int timeResult = 0;
            for (int i = 0; i < timeArray.length; i++) {
                if (timeArray[i] == "years" || timeArray[i] == "year") {
                    timeResult += Integer.parseInt(timeArray[i-1]) * 8760;
                } else if (timeArray[i] == "months" || timeArray[i] == "month") {
                    timeResult += Integer.parseInt(timeArray[i-1]) * 730;
                } else if (timeArray[i] == "days" || timeArray[i] == "day") {
                    timeResult += Integer.parseInt(timeArray[i-1]) * 24;
                } else if (timeArray[i] == "hours" || timeArray[i] == "hour") {
                    timeResult += Integer.parseInt(timeArray[i-1]);
                } else if (timeArray[i] == "minutes" || timeArray[i] == "minute") {
                    timeResult += Integer.parseInt(timeArray[i-1]) / 60;
                }
            }
            timeTotal = timeResult;
            CalculateDTE();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param start string version of the latitude
     * @param finish string version of the longitude
     * @return URL formatted for the ride server
     */
    private URL createURLDistance(String start, String finish) {
        try {
            String urlString = distanceApi;
            urlString = urlString + start + "&destination=" + finish + apiKey;
            Log.d(TAG, "url string: {" + urlString + "}");
            return new URL(urlString);
        } catch (Exception e) {
            Log.d(TAG, "createURLDistance failed");
            return null;
//            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
        }
    }

        private void CalculateDTE() {

        // fractionGas comes from gas needle, avgTankSize comes from radio buttons and mpgTotal
        // comes from database
        DTE = MPG * deluxeSpeedView.getSpeed() / 100 * gallonsInTank;

        if (DTE < distanceTotal) {
            popupText.setText(getString(R.string.notEnoughGas));
        } else {
            popupText.setText(getString(R.string.EnoughGas));
        }
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0,0);

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
    }
}


