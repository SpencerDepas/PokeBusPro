package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
    int index;
    ArrayList<BusInfo> busInfo;
    public volatile boolean parsingComplete = true;

    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {

        Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON");


        try {

            Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON");
            JSONObject reader = new JSONObject(in);






            int distanceArrayLength = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                    .getJSONObject(0)
                    .getJSONArray("MonitoredStopVisit").length();

            String[] tempDistance = new String[distanceArrayLength];

            for (int i = 0 ; i < distanceArrayLength; i ++){

                JSONObject sys = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                        .getJSONObject(0)
                        .getJSONArray("MonitoredStopVisit")
                        .getJSONObject(i)
                        .getJSONObject("MonitoredVehicleJourney")
                        .getJSONObject("MonitoredCall")
                        .getJSONObject("Extensions")
                        .getJSONObject("Distances");

                tempDistance[i] = sys.get("PresentableDistance").toString();
                Log.i("MyGetBusDistanceJSON", " sys  " + sys.get("PresentableDistance").toString());

            }


            //Log.i("MyGetBusDistanceJSON", " sys  " + sys.get("PresentableDistance").toString());
            //Log.i("MyGetBusDistanceJSON", " sysTwo  " + sysTwo.get("PresentableDistance").toString());





            MapsActivity.addDistance(tempDistance, index);



            Log.i("MyGetBusDistanceJSON", " tempBusInfo  " + tempBusInfo.getBusCode());
            Log.i("MyGetBusDistanceJSON", " busInfo.size()  " + busInfo.size());
            Log.i("MyGetBusDistanceJSON", " busInfo  index   " + index );




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }


        Log.i("MyGetBusDistanceJSON", "inside GetBusStopJSON.busInfo[busInfoIndex].getDistance()  " + busInfo.get(index).getDistance() + " index : " + index);


        Log.i("MyAsyncTask", "end11:"  );

    }

    static BusInfo tempBusInfo;
    static int onPostExecuteCount = 0;

    public void fetchBusDistanceJson(ArrayList<BusInfo> busInfoIn, int index) {
        busInfo = busInfoIn;
        tempBusInfo = new BusInfo();
        tempBusInfo = busInfoIn.get(index);
        this.index = index;


        stopCode = Integer.parseInt(busInfoIn.get(index).getBusCode());

        new LongOperation().execute("");



    }

    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String trackHowManyBuses = "3";
            final String downloadURLTwo = "http://bustime.mta.info/api/siri/stop-monitoring.json?key=05a5c2c8-432a-47bd-8f50-ece9382b4b28&MonitoringRef=MTA_"
                    + stopCode + "&MaximumStopVisits=" + trackHowManyBuses;

            Log.i("MyAsyncTask", "inside AsyncTask");


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
                Log.i("MyGetBusDistanceJSON", " inside exception" +  e.toString());
            }




            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            onPostExecuteCount++;
            Log.i("MyAsyncTask", "onPostExecute AsyncTask onPostExecuteCount:" + onPostExecuteCount );

            if(onPostExecuteCount == busInfo.size()){
                onPostExecuteCount = 0;
                Log.i("MyAsyncTask", " AsyncTask  onPostExecute MapsActivity.obtainedAllDistances = true;");

                if(AddMarkers.markersAdded) {
                    //update marker
                    AddMarkers.updateMarkersToMap(busInfo);

                }else{
                    //to make markers on first run
                    AddMarkers.addMarkersToMap(busInfo);
                }
            }

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
