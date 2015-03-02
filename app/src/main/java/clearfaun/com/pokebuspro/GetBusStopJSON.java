package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by spencer on 2/21/2015.
 */
public class GetBusStopJSON {

    final static public String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28";
   /* static String testLat = "40.6455520";
    static String testLng = "-73.9829084";*/

    int stopRadius = 200;

    String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.json?key=" + API_KEY + "&radius=" + stopRadius + "&lat=" +
            MapsActivity.latitude + "&lon=" + MapsActivity.longitude;

    /*String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.json?key=" + API_KEY + "&radius=" + stopRadius + "&lat=" +
            MapsActivity.testLat + "&lon=" + MapsActivity.testLng;*/

    //private String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.json?key=05a5c2c8-432a-47bd-8f50-ece9382b4b28&lat=40.6455520&lon=-73.9829084&radius=130";

    BusInfo tempBusInfo;
    ArrayList<BusInfo> busInfo;


    public volatile boolean parsingComplete = true;


    @SuppressLint("NewApi")
    public void readAndParseJSON(String in, ArrayList<BusInfo> busInfo) {




        try {

            Log.i("MyGetBusStopJSON", "inside readAndParseJSON");
            JSONObject reader = new JSONObject(in);
            Log.i("MyGetBusStopJSON", "after JSONObject reader = new JSONObject(in);" + reader.toString());



            JSONObject sys = reader.getJSONObject("data");
            JSONArray stopsArray= (JSONArray) sys.get("stops");

            tempBusInfo = new BusInfo();



            Log.i("MyGetBusStopJSON", "stopsArray.length()   " + stopsArray.length());


            for(int i = 0; i < stopsArray.length(); i++){
                JSONObject tlt = stopsArray.getJSONObject(i);
                Log.i("MyGetBusStopJSON", stopsArray.getJSONObject(i).toString());
                Log.i("MyGetBusStopJSON", "tlt" + tlt.getJSONArray("routes")
                        .getJSONObject(0)
                        .getString("shortName")
                        );
                /*Log.i("MyGetBusStopJSON", "tlt" + tlt.getJSONArray("routes")
                                .getJSONObject(0)

                );
                Log.i("MyGetBusStopJSON", "tlt plus i " + i + " " + tlt.getJSONArray("routes")
                        .getJSONObject(0)
                        .getString("shortname")

                        );*/



               // Log.i("MyGetBusStopJSON", "stopsArray.getJSONObject(i).getJSONObject(\"shortName\").toString()" + stopsArray.getJSONObject(i).getJSONObject("shortName").toString());

                tempBusInfo =  new BusInfo();
                tempBusInfo.setBusName(tlt.getJSONArray("routes")
                        .getJSONObject(0)
                        .getString("shortName"));

                tempBusInfo.setBusCode(tlt.getString("code"));
                tempBusInfo.setBusId(tlt.getString("id"));
                tempBusInfo.setBusStopLat(tlt.getString("lat"));
                tempBusInfo.setBusStopLng(tlt.getString("lon"));

                busInfo.add(tempBusInfo);

                Log.i("MyGetBusStopJSON", " tempBusInfo.getBusName()::  " +  tempBusInfo.getBusName());
            }





            parsingComplete = false;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyGetBusStopJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }

    }


    public void fetchBusStop(ArrayList<BusInfo> busInfoIn) {
        busInfo = busInfoIn;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i("MyHandleJSON", "inside fetchBusStop");

                    URL url = new URL(downloadURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    String data = convertStreamToString(stream);

                    readAndParseJSON(data, busInfo);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("MyGetBusStopJSON", "inside fetchBusStop inside exception " + e);
                }
            }
        });

        thread.start();
    }

    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}




