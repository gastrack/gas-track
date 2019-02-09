//package com.example.gastrack;
//
//
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class Backend {
////    int distanceTotal, timeTotal;
////    String TAG = "MainActivity";
////    String apiKey = "&key=AIzaSyCweBWVyxQ26oCNARzpnDVeVlFm2qIMsHE";
////    String latlongApi = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
////    String distanceApi = "https://maps.googleapis.com/maps/api/directions/json?origin=";
////    String startAddress, destinationAddress;
//
//    @Override
//    protected void onClick(Bundle savedInstanceState) {
//        GetStartLocation(33.8,-117.9, "Calvin College");
//
//    }
//
////    protected void GetDistanceTime() {
////        startAddress = startAddress.replaceAll("\\s+","+");
////        startAddress = startAddress.replaceAll(",","");
////        destinationAddress = destinationAddress.replaceAll("\\s+","+");
////        destinationAddress = destinationAddress.replaceAll(",","");
////        new MainActivity.GetDistanceTask().execute(createURLDistance(startAddress, destinationAddress));
////
////    }
////
////    protected void GetStartLocation(double startLatitude, double startLongitude, String destination) {
////        /*
////        jsonObject jsonAddress = HttpRequest(latlongApi + startLatitude + startLongitude);
////        String startAddress = jsonAddress.find();
////        jsonObject jsonDistance = HttpRequest(distanceApi + startAddress + "&destination=" + destination);
////        distance = jsonDistance.find();
////        time = jsonDistance.find();
////         */
////        startAddress = "";
////        new MainActivity.GetAddressTask().execute(createURLLatLng(Double.toString(startLatitude), Double.toString(startLongitude)));
////        destinationAddress = destination;
////    }
////
////    private class GetAddressTask extends AsyncTask<URL, Void, JSONArray> {
////
////        @Override
////        protected JSONArray doInBackground(URL... params) {
////            HttpURLConnection connection = null;
////            StringBuilder jsonText = new StringBuilder();
////            JSONArray result = null;
////            try {
////                connection = (HttpURLConnection) params[0].openConnection();
////                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
////                    BufferedReader reader = new BufferedReader(
////                            new InputStreamReader(connection.getInputStream()));
////                    String line;
////                    while ((line = reader.readLine()) != null) {
////                        jsonText.append(line);
////                    }
////                    Log.d(TAG, jsonText.toString());
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
////                    Log.d(TAG, result.toString());
////                } else {
////                    throw new Exception();
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            } finally {
////                if (connection != null) {
////                    connection.disconnect();
////                }
////            }
////            return result;
////        }
////
////
////        @Override
////        protected void onPostExecute(JSONArray Address) {
////            if (Address == null) {
////                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
////            } else if (Address.length() == 0) {
////                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
////            } else {
////                convertJSONtoAddress(Address);
////            }
////        }
////
////    }
////
////    /**
////     * Converts the JSON ride data to an arraylist
////     *
////     * @param AddressArray JSON array of player objects
////     */
////    private void convertJSONtoAddress(JSONArray AddressArray) {
////        try{
////            Log.d(TAG, "First index: " + AddressArray.getString(0));
////            JSONObject addressObject = (JSONObject) AddressArray.get(0);
////            AddressArray = (JSONArray) addressObject.get("results");
////            addressObject = (JSONObject) AddressArray.get(0);
////            String address = (String) addressObject.get("formatted_address");
////            Log.d(TAG, "address hopefully: " + address);
////            startAddress = address;
////            GetDistanceTime();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////    }
////
////    /**
////     * Formats a URL for the webservice specified in the string resources.
////     *
////     * @param lat string version of the latitude
////     * @param lng string version of the longitude
////     * @return URL formatted for the ride server
////     */
////    private URL createURLLatLng(String lat, String lng) {
////        try {
////            String urlString = latlongApi;
////            urlString = urlString + lat + "," + lng + apiKey;
////            Log.d(TAG, "url string: {" + urlString + "}");
////            return new URL(urlString);
////        } catch (Exception e) {
////            Log.d(TAG, "createURLLatLng failed");
//////            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
////        }
////
////        return null;
////    }
////
////
////    private class GetDistanceTask extends AsyncTask<URL, Void, JSONArray> {
////
////        @Override
////        protected JSONArray doInBackground(URL... params) {
////            HttpURLConnection connection = null;
////            StringBuilder jsonText = new StringBuilder();
////            JSONArray result = null;
////            try {
////                connection = (HttpURLConnection) params[0].openConnection();
////                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
////                    BufferedReader reader = new BufferedReader(
////                            new InputStreamReader(connection.getInputStream()));
////                    String line;
////                    while ((line = reader.readLine()) != null) {
////                        jsonText.append(line);
////                    }
////                    Log.d(TAG, jsonText.toString());
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
////                    Log.d(TAG, result.toString());
////                } else {
////                    throw new Exception();
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            } finally {
////                if (connection != null) {
////                    connection.disconnect();
////                }
////            }
////            return result;
////        }
////
////
////        @Override
////        protected void onPostExecute(JSONArray Distance) {
////            if (Distance == null) {
////                // Toast.makeText(JoinRide, "It's not connecting", Toast.LENGTH_SHORT).show();
////            } else if (Distance.length() == 0) {
////                //   Toast.makeText(JoinRide, "there is no result", Toast.LENGTH_SHORT).show();
////            } else {
////                convertJSONtoDistance(Distance);
////            }
////        }
////
////    }
////
////    /**
////     * Converts the JSON ride data to an arraylist
////     *
////     * @param DistanceArray JSON array of player objects
////     */
////    private void convertJSONtoDistance(JSONArray DistanceArray) {
////        try{
////            //Whittles down the JSON Array to just the data we want: the time and distance of trip
////            JSONObject DistanceObject = (JSONObject) DistanceArray.get(0);
////            DistanceArray = (JSONArray) DistanceObject.get("routes");
////            DistanceObject = (JSONObject) DistanceArray.get(0);
////            DistanceArray = (JSONArray) DistanceObject.get("legs");
////            DistanceObject = (JSONObject) DistanceArray.get(0);
////            JSONObject TimeObject = (JSONObject) DistanceObject.get("duration");
////            DistanceObject = (JSONObject) DistanceObject.get("distance");
////            String time = (String) TimeObject.get("text");
////            String distance = (String) DistanceObject.get("text");
////
////            Log.d(TAG, "distance hopefully: " + distance);
////            Log.d(TAG, "time hopefully: " + time);
////
////            distance = distance.substring(0, distance.length() - 3);
////            distanceTotal = Integer.parseInt(distance);
////
////            String [] timeArray = time.split(" ");
////
////            //Calculate the duration of the trip in hours
////            int timeResult = 0;
////            for (int i = 0; i < timeArray.length; i++) {
////                if (timeArray[i] == "years" || timeArray[i] == "year") {
////                    timeResult += Integer.parseInt(timeArray[i-1]) * 8760;
////                } else if (timeArray[i] == "months" || timeArray[i] == "month") {
////                    timeResult += Integer.parseInt(timeArray[i-1]) * 730;
////                } else if (timeArray[i] == "days" || timeArray[i] == "day") {
////                    timeResult += Integer.parseInt(timeArray[i-1]) * 24;
////                } else if (timeArray[i] == "hours" || timeArray[i] == "hour") {
////                    timeResult += Integer.parseInt(timeArray[i-1]);
////                } else if (timeArray[i] == "minutes" || timeArray[i] == "minute") {
////                    timeResult += Integer.parseInt(timeArray[i-1]) / 60;
////                }
////            }
////            timeTotal = timeResult;
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////    }
////
////    /**
////     * Formats a URL for the webservice specified in the string resources.
////     *
////     * @param start string version of the latitude
////     * @param finish string version of the longitude
////     * @return URL formatted for the ride server
////     */
////    private URL createURLDistance(String start, String finish) {
////        try {
////            String urlString = distanceApi;
////            urlString = urlString + start + "&destination=" + finish + apiKey;
////            Log.d(TAG, "url string: {" + urlString + "}");
////            return new URL(urlString);
////        } catch (Exception e) {
////            Log.d(TAG, "createURLDistance failed");
////            return null;
//////            Toast.makeText(Backend.this, getString(R.string.networkFail),Toast.LENGTH_SHORT).show();
////        }
////    }
//
//}
//
