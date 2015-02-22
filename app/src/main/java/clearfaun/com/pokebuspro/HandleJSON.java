package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by spencer on 2/21/2015.
 */
public class HandleJSON {

    final static public String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28";
    static String testLat = "40.6455520";
    static String testLng = "-73.9829084";

    int stopRadius = 200;

    String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.json?key=" + API_KEY + "&radius=" + stopRadius + "&lat=" +
            MapsActivity.latitude + "&lon=" + MapsActivity.longitude;
    //private String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.json?key=05a5c2c8-432a-47bd-8f50-ece9382b4b28&lat=40.6455520&lon=-73.9829084&radius=130";

    static BusInfo[] busInfo;



    public volatile boolean parsingComplete = true;







    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {




        try {

            Log.i("MyHandleJSON", "inside readAndParseJSON");
            JSONObject reader = new JSONObject(in);
            Log.i("MyHandleJSON", "after JSONObject reader = new JSONObject(in);");

            JSONObject sys = reader.getJSONObject("data");
            JSONArray stopsArray= (JSONArray) sys.get("stops");

            busInfo = new BusInfo[stopsArray.length()];


            Log.i("MyHandleJSON", "stopsArray.length()   " + stopsArray.length());


            for(int i = 0; i < stopsArray.length(); i++){
                JSONObject tlt = stopsArray.getJSONObject(i);
                Log.i("MyHandleJSON", stopsArray.getJSONObject(i).toString());

                busInfo[i] =  new BusInfo();
                busInfo[i].busCode(tlt.getString("code"));
                busInfo[i].busId(tlt.getString("id"));
                busInfo[i].busStopLat(tlt.getString("lat"));
                busInfo[i].busStopLng(tlt.getString("lon"));



            }





            parsingComplete = false;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyHandleJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }

    }


    public void fetchJSON() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i("MyHandleJSON", "inside fetchJSON");

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

                    readAndParseJSON(data);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("MyHandleJSON", "inside fetchJSON inside exception");
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




