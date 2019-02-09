//package com.example.gastrack;
//
//import android.app.DownloadManager;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.Xml;
//import android.webkit.WebView;
//import android.widget.Toast;
//
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//
//    int mpgTotal, year;
//    String make, modelFinal;
//    String modelApi = "https://www.fueleconomy.gov/ws/rest/vehicle/menu/model?";
//    String modelNumberApi = "https://www.fueleconomy.gov/ws/rest/vehicle/menu/options?year=";
//    String mpgApi = "https://www.fueleconomy.gov/ws/rest/vehicle/";
//    String TAG = "MainActivity";
//    List<String> modelList;
//    int modelNumber, DTE;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //Get year and make from user textEdits
//        year = 2012;
//        make = "Honda";
//        GetModel(year, make);
//        // On click method for button once model has been selected
//        modelFinal = "Fit";
//        GetModelNumber();
//
//        final String httpUpdateRoutines = "";
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        StringRequest request = new StringRequest(DownloadManager.Request.Method.GET, httpUpdateRoutines, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "No new room added! " + error, Toast.LENGTH_SHORT).show();
//
//            }
//        } )
//        {
//
//            @Override
//            protected Map<String, String> getParams () throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//
//                return map;
//
//            }
//
//
//        };
//        queue.add(request);
//
//    }
//
//
//
//    protected void GetModel(int carYear, String carMake) {
//        new MainActivity.GetModelTask().execute(createURLModel(carYear, carMake));
//    }
//
//
//    private class GetModelTask extends AsyncTask<URL, Void, String> {
//
//        @Override
//        protected String doInBackground(URL... params) {
//            HttpURLConnection connection = null;
//            StringBuilder jsonText = new StringBuilder();
//            JSONArray result = null;
//            try {
//                connection = (HttpURLConnection) params[0].openConnection();
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(connection.getInputStream()));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        jsonText.append(line);
//                    }
//                    Log.d(TAG, jsonText.toString());
////                    if (jsonText.toString().startsWith("{ \"items\":")) {
////                        // Google Cloud can't return a raw JSON list, so we had to add this "items" field;
////                        // remove it now.
////                        String jsonItemsText = new JSONObject(jsonText.toString()).get("items").toString();
////                        result = new JSONArray(jsonItemsText);
////                    } else if (jsonText.toString().equals("null")) {
////                        result = new JSONArray();
////                    } else {
////                        result = new JSONArray().put(new JSONObject(jsonText.toString()));
////                    }
//
////                    Log.d(TAG, result.toString());
//                } else {
//                    throw new Exception();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//            return jsonText.toString();
//        }
//
//
//        @Override
//        protected void onPostExecute(String model) {
//            if (model == null) {
//                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
//            } else if (model.length() == 0) {
//                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
//            } else {
//                convertJSONtoModel(model);
//            }
//        }
//
//    }
//
//    /**
//     * Converts the JSON ride data to an arraylist
//     *
//     * @param ModelXml JSON array of player objects
//     */
//    private void convertJSONtoModel(String ModelXml) {
//        Log.d(TAG, "Initial: " + ModelXml);
//        String[] Models = ModelXml.split("<menuItem>");
//        Log.d(TAG, "Models array is of size " + Models.length);
//        List<String> result = new ArrayList<>();
//        for (int i = 0; i < Models.length; i++) {
//            if (Models[i].startsWith("<text>")) {
//                String[] temp = Models[i].split("<text>|</text>");
//                result.add(temp[1]);
//            }
//        }
//        modelList = result;
//        //Populate spinner with modelList
//    }
//
//    /**
//     * Formats a URL for the webservice specified in the string resources.
//     *
//     * @param carYear int version of the year car was made
//     * @param carMake string version of the make of the car
//     * @return URL formatted for the ride server
//     */
//    private URL createURLModel(int carYear, String carMake) {
//        try {
//            String urlString = modelApi;
//            urlString = urlString + "year=" + Integer.toString(carYear) + "&make=" + carMake;
//            Log.d(TAG, "url string: {" + urlString + "}");
//            return new URL(urlString);
//        } catch (Exception e) {
//            Log.d(TAG, "createURLLatLng failed");
////            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
//        }
//
//        return null;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    protected void GetModelNumber() {
//        new MainActivity.GetModelNumberTask().execute(createURLModelNumber());
//    }
//
//
//    private class GetModelNumberTask extends AsyncTask<URL, Void, String> {
//
//        @Override
//        protected String doInBackground(URL... params) {
//            HttpURLConnection connection = null;
//            StringBuilder jsonText = new StringBuilder();
//            try {
//                connection = (HttpURLConnection) params[0].openConnection();
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(connection.getInputStream()));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        jsonText.append(line);
//                    }
//                    Log.d(TAG, jsonText.toString());
//                } else {
//                    throw new Exception();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//            return jsonText.toString();
//        }
//
//
//        @Override
//        protected void onPostExecute(String model) {
//            if (model == null) {
//                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
//            } else if (model.length() == 0) {
//                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
//            } else {
//                convertJSONtoModelNumber(model);
//            }
//        }
//
//    }
//
//    /**
//     * Converts the JSON ride data to an arraylist
//     *
//     * @param ModelXml JSON array of player objects
//     */
//    private void convertJSONtoModelNumber(String ModelXml) {
//        Log.d(TAG, "Initial Model Number: " + ModelXml);
//        String[] Models = ModelXml.split("<value>|</value");
//        Log.d(TAG, "Models array is of size " + Models.length);
//        Log.d(TAG, "Models[1] is: " + Models[1]);
//        modelNumber = Integer.parseInt(Models[1]);
//        GetMPG();
//    }
//
//    /**
//     * Formats a URL for the webservice specified in the string resources.
//     *
//     * @return URL formatted for the ride server
//     */
//    private URL createURLModelNumber() {
//        try {
//            String urlString = modelNumberApi;
//            urlString = urlString + year + "&make=" + make + "&model=" + modelFinal;
//            Log.d(TAG, "url string: {" + urlString + "}");
//            return new URL(urlString);
//        } catch (Exception e) {
//            Log.d(TAG, "createURLModelNumber failed");
////            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
//        }
//
//        return null;
//    }
//
//
//
//
//
//
//
//
//    protected void GetMPG() {
//        new MainActivity.GetMPGTask().execute(createURLMPG());
//    }
//
//
//    private class GetMPGTask extends AsyncTask<URL, Void, String> {
//
//        @Override
//        protected String doInBackground(URL... params) {
//            HttpURLConnection connection = null;
//            StringBuilder jsonText = new StringBuilder();
//            try {
//                connection = (HttpURLConnection) params[0].openConnection();
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(connection.getInputStream()));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        jsonText.append(line);
//                    }
//                    Log.d(TAG, jsonText.toString());
//                } else {
//                    throw new Exception();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//            return jsonText.toString();
//        }
//
//
//        @Override
//        protected void onPostExecute(String model) {
//            if (model == null) {
//                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
//            } else if (model.length() == 0) {
//                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
//            } else {
//                convertJSONtoMPG(model);
//            }
//        }
//
//    }
//
//    /**
//     * Converts the JSON ride data to an arraylist
//     *
//     * @param mpg JSON array of player objects
//     */
//    private void convertJSONtoMPG(String mpg) {
//        Log.d(TAG, "Initial MPG: " + mpg);
//        String[] MPG = mpg.split("<comb08U>|</comb08U");
//        Log.d(TAG, "MPG array is of size " + MPG.length);
//        Log.d(TAG, "MPG[1] is: " + MPG[1]);
//        mpgTotal = Integer.parseInt(MPG[1]);
//        CalculateDTE();
//    }
//
//    /**
//     * Formats a URL for the webservice specified in the string resources.
//     *
//     * @return URL formatted for the ride server
//     */
//    private URL createURLMPG() {
//        try {
//            String urlString = mpgApi;
//            urlString = urlString + Integer.toString(modelNumber);
//            Log.d(TAG, "mpg url string: {" + urlString + "}");
//            return new URL(urlString);
//        } catch (Exception e) {
//            Log.d(TAG, "createURLMPG failed");
////            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
//        }
//
//        return null;
//    }
//
//
//
//
//    private void CalculateDTE() {
//        int avgTankSize;
//        switch (radioButton) {
//            case "Compact" :
//                avgTankSize = 14.5;
//                break;
//            case "Sedan" :
//                avgTankSize = 18.5;
//                break;
//            case "SUV" :
//                avgTankSize = 25;
//                break;
//            case "Truck" :
//                avgTankSize = 30;
//                break;
//            default:
//                avgTankSize = 20;
//        }
//        // fractionGas comes from gas needle, avgTankSize comes from radio buttons and mpgTotal
//        // comes from database
//        DTE = mpgTotal * fractionGas * avgTankSize;
//    }
//}
