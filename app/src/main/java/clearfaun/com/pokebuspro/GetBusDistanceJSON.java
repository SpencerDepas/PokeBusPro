package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.internal.in;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by spencer on 2/22/2015.
 */
public class GetBusDistanceJSON {

    final static public String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28";
    int stopCode;
    int busInfoIndex;
    int index;
    ArrayList<BusInfo> busInfo;
    public volatile boolean parsingComplete = true;

    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {




        try {

            Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON");
            JSONObject reader = new JSONObject(in);

            JSONObject sys = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                    .getJSONObject(0)
                    .getJSONArray("MonitoredStopVisit")
                    .getJSONObject(0)
                    .getJSONObject("MonitoredVehicleJourney")
                    .getJSONObject("MonitoredCall")
                    .getJSONObject("Extensions")
                    .getJSONObject("Distances");



            MapsActivity.addDistance(sys.get("PresentableDistance").toString(), index);

            //busInfo.setBusDistance(sys.get("PresentableDistance").toString());
            tempBusInfo.setBusDistance(sys.get("PresentableDistance").toString());
            Log.i("MyGetBusDistanceJSON", " tempBusInfo  " + tempBusInfo.getBusCode());
            busInfo.get(index).setBusDistance(sys.get("PresentableDistance").toString());
            //busInfo.add(index, tempBusInfo);
            //busInfo.remove(index);

            Log.i("MyGetBusDistanceJSON", " tempDistance  " + sys.get("PresentableDistance").toString());

            Log.i("MyGetBusDistanceJSON", " busInfo.size()  " + busInfo.size());

            Log.i("MyGetBusDistanceJSON", " busInfo  index   " + index );

                Log.i("MyGetBusDistanceJSON", " parsingComplete = false;  " );
            parsingComplete = false;




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }


        Log.i("MyGetBusDistanceJSON", "inside GetBusStopJSON.busInfo[busInfoIndex].getDistance()  " + busInfo.get(index).getDistance());




    }

    static BusInfo tempBusInfo;

    public void fetchBusDistanceJson(ArrayList<BusInfo> busInfoIn, int index) {
        busInfo = busInfoIn;
        tempBusInfo = new BusInfo();
        tempBusInfo = busInfoIn.get(index);
        this.index = index;



        stopCode = Integer.parseInt(busInfoIn.get(index).getBusCode());
        busInfoIndex = index;
        final String downloadURLTwo = "http://bustime.mta.info/api/siri/stop-monitoring.json?key=05a5c2c8-432a-47bd-8f50-ece9382b4b28&MonitoringRef=MTA_" + stopCode + "&MaximumStopVisits=1";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i("MyGetBusDistanceJSON", "inside fetchBusStop");

                    URL url = new URL(downloadURLTwo);
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
                    Log.i("MyGetBusDistanceJSON", "inside fetchBusStop inside exception");
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
