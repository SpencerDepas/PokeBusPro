package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by spencer on 2/21/2015.
 */
public class GetBusStopJSON {


   /* static String testLat = "40.6455520";
    static String testLng = "-73.9829084";*/

    int stopRadius;
    BusInfo tempBusInfo;
    ArrayList<BusInfo> busInfo;

    static Hashtable<String, BusInfo> busInfoHashtable =
            new Hashtable<String, BusInfo>();
    public volatile boolean parsingComplete = true;


    @SuppressLint("NewApi")
    public void readAndParseJSON(String in, ArrayList<BusInfo> busInfo) {


        int totalBuses = 0;

        try {

            Log.i("MyGetBusStopJSON", "inside readAndParseJSON");
            JSONObject reader = new JSONObject(in);
            //Log.i("MyGetBusStopJSON", "after JSONObject reader = new JSONObject(in);" + reader.toString());

            JSONObject sys = reader.getJSONObject("data");
            JSONArray stopsArray= (JSONArray) sys.get("stops");

            tempBusInfo = new BusInfo();
            Log.i("MyGetBusStopJSON", "reader" + reader.length());
            Log.i("MyGetBusStopJSON", "sys " + sys.length());
            Log.i("MyGetBusStopJSON", "stopsArray " + stopsArray.length());


            Log.i("MyGetBusStopJSON", "stopsArray.length()   " + stopsArray.length());

            if(stopsArray.length() != 0) {

                for (int i = 0; i < stopsArray.length(); i++) {

                    JSONObject tlt = stopsArray.getJSONObject(i);
                    Log.i("MyGetBusStopJSON", stopsArray.getJSONObject(i).toString());
                    Log.i("MyGetBusStopJSON", "tlt" + tlt.getJSONArray("routes")
                            .getJSONObject(0)
                            .getString("shortName"));
                    tlt.getJSONArray("routes")
                            .getJSONObject(0);
                    //Log.i("MyGetBusStopJSONy", "tlt.getJSONArray(\"routes\") " + tlt.getJSONArray("routes") );


                    for(int y = 0 ; y < tlt.getJSONArray("routes").length(); y++){
                        totalBuses ++;
                        Log.i("MyGetBusStopJSONy", "tlt.getJSONArray(\"routes\").getJSONObject(y) " + tlt.getJSONArray("routes").getJSONObject(y) );

                        Log.i("MyGetBusStopJSONy", "BusName longName" + tlt.getJSONArray("routes")
                                .getJSONObject(y)
                                .getString("longName") + " y = " + y);


                        tempBusInfo = new BusInfo();
                        tempBusInfo.setBusName(tlt.getJSONArray("routes")
                                .getJSONObject(y)
                                .getString("shortName"));

                        tempBusInfo.setBusCode(tlt.getString("code"));
                        tempBusInfo.setBusId(tlt.getString("id"));
                        tempBusInfo.setBusStopLat(tlt.getString("lat"));
                        tempBusInfo.setBusStopLng(tlt.getString("lon"));


                        tempBusInfo.setHashMapKey(tempBusInfo.getBusCode() + tempBusInfo.getBusName());
                        Log.i("MyGetBusStopJSONy", " tempBusInfo.getHashMapKey() " + tempBusInfo.getHashMapKey());
                        busInfo.add(tempBusInfo);
                        int lastIndex = busInfo.lastIndexOf(tempBusInfo);
                        Log.i("MyGetBusStopJSONy", " tempBusInfo.getHashMapKey() " + busInfo.get(lastIndex).getHashMapKey());
                        busInfoHashtable.put(tempBusInfo.getHashMapKey(), busInfo.get(lastIndex));

                    }

                    Log.i("MyGetBusStopJSONy", " number of shortnames " + tlt.getJSONArray("routes"));






                    Log.i("MyGetBusStopJSON", " tempBusInfo.getBusName()::  " + tempBusInfo.getBusName());
                }

            }else{

                new ToastMessageTask().execute("No Bus Stops in range");
            }
            Log.i("MyGetBusStopJSONy", " number of totalBuses " + totalBuses );


            tempBusInfo = null;
            parsingComplete = false;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyGetBusStopJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }

    }

    String busStopURL;
    public void fetchBusStop(ArrayList<BusInfo> busInfoIn) {
        busInfo = busInfoIn;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MapsActivity.mContext);



        String radius= sharedPrefs.getString(MapsActivity.mContext.getString(R.string.radius_key), "300");
        stopRadius = Integer.parseInt(radius);
        Log.i("MyGetBusStopJSON", "stopRadius:" + stopRadius);
        Log.i("MyGetBusStopJSON", "prefradius:" + PrefsFragment.radius);
        Log.i("MyGetBusStopJSON", "busInfoIn:" + busInfo.size());


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //this is because we want the service to work independently from each other. so depending on what starts this class depends on how we get api key
                    Log.i("MyGetBusStopJSON", "before decides which URL  ");
                    Log.i("MyGetBusStopJSON", "before busInfo.size()  " + busInfo.size());


                    Log.i("MyGetBusStopJSON", "Correct for MAPZS ACTIVTY ");

                    busStopURL = "http://pokebuspro-api.herokuapp.com/bus_time/where/stops-for-location.json?radius=" + stopRadius + "&lat=" +
                            MapsActivity.latitude + "&lon=" + MapsActivity.longitude;



                    Log.i("MyGetBusStopJSON", "after decides which URL  ");

                    Log.i("MyHandleJSON", "inside fetchBusStop");

                    URL url = new URL(busStopURL);
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




