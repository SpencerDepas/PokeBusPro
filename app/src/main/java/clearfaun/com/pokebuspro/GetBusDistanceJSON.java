package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by spencer on 2/22/2015.
 */
public class GetBusDistanceJSON {


    int stopCode;
    ArrayList<BusInfo> busInfo;
    JSONObject busNameObject;

    @SuppressLint("NewApi")
    public void readAndParseJSON(String[] in) {

        //in is the stream of data withdistance informatioon. it will be as large as the amount of buis stops in the area
        Log.i("MyGetBusDistanceJSONt", "inside readAndParseJSON");


        try {

            Log.i("MyGetBusDistanceJSONt", "in.length " + in.length);

            for (int z = 0 ; z < in.length; z ++) {
                JSONObject reader = new JSONObject(in[z]);
                //z changes the bus stop we are choosing.

                int maxNumberOfBusDistancesAvailable = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                        .getJSONObject(0)
                        .getJSONArray("MonitoredStopVisit").length();



                String[] tempDistance = new String[maxNumberOfBusDistancesAvailable];
                // Max return of three buses
                for (int i = 0 ;  i < maxNumberOfBusDistancesAvailable ; i++ ){
                    // i gets the distances for each stop, up to three
                    Log.i("MyGetBusDistanceJSONt", "z is :  " + z);
                    Log.i("MyGetBusDistanceJSONt", "i is :  " + i);
                    Log.i("MyGetBusDistanceJSONt", " busInfo.get(z).getBusCode():  " + busInfo.get(z).getBusCode());
                    try {


                        Log.i("MyGetBusDistanceJSONt", "maxNumberOfBusDistancesAvailable :" + maxNumberOfBusDistancesAvailable);

                        int sysLength = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                                .getJSONObject(0).length();

                        Log.i("MyGetBusDistanceJSONt", "maxNumberOfBusDistancesAvailable :" + sysLength);

                        JSONObject sys = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                                .getJSONObject(0)
                                .getJSONArray("MonitoredStopVisit")
                                .getJSONObject(i)
                                .getJSONObject("MonitoredVehicleJourney")
                                .getJSONObject("MonitoredCall")
                                .getJSONObject("Extensions")
                                .getJSONObject("Distances");
                        Log.i("MyGetBusDistanceJSONt", "sys :" + sys);
                        Log.i("MyGetBusDistanceJSONt", "sys :\n\n");


                        //z is for stop
                        String busNameObject = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                                .getJSONObject(0)
                                .getJSONArray("MonitoredStopVisit")
                                .getJSONObject(i)
                                .getJSONObject("MonitoredVehicleJourney")
                                .get("PublishedLineName").toString();

                        String busCodeObject = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                                .getJSONObject(0)
                                .getJSONArray("MonitoredStopVisit")
                                .getJSONObject(i)
                                .getJSONObject("MonitoredVehicleJourney")
                                .getJSONObject("MonitoredCall")
                                .getString("StopPointRef");



                        //String callBusName = busNameObject.get("PublishedLineName").toString();
                        //=Log.i("MyGetBusDistanceJSONt", "testForMultipleStops :" + callBusName);

                        /*Log.i("MyGetBusStopJSONy", "busNameObject " + busNameObject);
                        Log.i("MyGetBusStopJSONy", "busNameObject " + busCodeObject.substring(4));
                        Log.i("MyGetBusStopJSONy", "PresentableDistance " +sys.get("PresentableDistance").toString());
                        Log.i("MyGetBusStopJSONy", "i: " + i);
                        Log.i("MyGetBusStopJSONy", "z: " + z);*/



                            //if the bus code is the same
                        if(busInfo.get(z).getBusCode().equals(busCodeObject.substring(4))){
                            //if the bus name is the same

                            if(busInfo.get(z).getBusName().equals(busNameObject)){
                                Log.i("MyGetBusStopJSONy", "z: ) " + z );
                                Log.i("MyGetBusStopJSONy", "i: busInfo.get(q).getBusCode() " + busInfo.get(z).getBusCode() );
                                Log.i("MyGetBusStopJSONy", "IN " + busNameObject);
                                Log.i("MyGetBusStopJSONy", "PresentableDistance " +sys.get("PresentableDistance").toString());


                                busInfo.get(z).setBusDistance(sys.get("PresentableDistance").toString());

                            }
                        }




                        //tempDistance[i] = sys.get("PresentableDistance").toString();
                        Log.i("MyGetBusDistanceJSONt", " sys  " + sys.get("PresentableDistance").toString() + ". i is : " + i);

                    }catch(Exception e){
                        Log.i("MyGetBusDistanceJSONt", " iner try catch Exception e  " + e.toString());
                        tempDistance[0] = "flofpfjfik";
                        tempDistance[1] = "flofpfjfik";
                        tempDistance[2] = "flofpfjfik";

                    }
                }




                Log.i("MyGetBusDistanceJSONt", " tempBusInfo  " + busInfo.get(z).getBusCode());
                Log.i("MyGetBusDistanceJSONt", " busInfo.size()  " + busInfo.size());

            }

            for(int i = 0 ; i < busInfo.size(); i ++){
                Log.i("MyGetBusDistanceJSONo", " name  " + busInfo.get(i).getBusName());
                Log.i("MyGetBusDistanceJSONo", " bus code  " + busInfo.get(i).getBusCode());
                Log.i("MyGetBusDistanceJSONo", " distance 1:  " + busInfo.get(i).getDistance()[0]);
                Log.i("MyGetBusDistanceJSONo", " distance 2:  " + busInfo.get(i).getDistance()[1]);
                Log.i("MyGetBusDistanceJSONo", " distance 3:  " + busInfo.get(i).getDistance()[2]);
                Log.i("MyGetBusDistanceJSONo", "  \n  ");
            }


        }catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }

            Log.i("MyAsyncTask", "end11:");
    }



    public void fetchBusDistanceJson(ArrayList<BusInfo> busInfoIn) {

        busInfo = busInfoIn;
        Log.i("fetchBusDistanceJson", "busInfo" + busInfo.size());
        new LongOperation().execute("");

    }

    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private class LongOperation extends AsyncTask<String, Void, String> {


        String busDistanceURL;
        @Override
        protected String doInBackground(String... params) {
            Log.i("MyGetBusDistanceJSONn", "inside fetchBusStop" + params[0]);
            String howManyBusesPerStop = "25";

            String[] data = new String[busInfo.size()];

            Log.i("MyGetBusDistanceJSONn", "inside AsyncTask");
            try {


                for(int i = 0; i < busInfo.size(); i ++) {
                    Log.i("MyGetBusDistanceJSONn", "busInfo.size(): " + busInfo.size());
                    Log.i("MyGetBusDistanceJSONn", "i is : " + i );
                    Log.i("MyGetBusDistanceJSONn", "data.length is : " + data.length );
                    Log.i("MyGetBusDistanceJSONn", "busInfo.get(i).getBusCode(): " + busInfo.get(i).getBusCode());
                    stopCode = Integer.parseInt(busInfo.get(i).getBusCode());





                    busDistanceURL = "http://pokebuspro-api.herokuapp.com/bus_time/siri/stop-monitoring.json?MonitoringRef=MTA_"
                            + stopCode + "&MaximumStopVisits=" + howManyBusesPerStop;



                    Log.i("MyGetBusDistanceJSONn", "inside fetchBusStop");
                    Log.i("MyGetBusDistanceJSONn", "busDistanceURL:" + busDistanceURL);

                    URL url = new URL(busDistanceURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    data[i] = convertStreamToString(stream);
                    stream.close();
                }
                Log.i("MyGetBusDistanceJSONn", "Pre readAndParseJSON(data); ");

                for(int y = 0 ; y < data.length; y ++){
                    Log.i("MyGetBusDistanceJSONn", "Pre data[" + y + "]; " + data[y] );
                }



            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyGetBusDistanceJSONn", " inside exception" +  e.toString());
            }


            readAndParseJSON(data);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("MyAsyncTask", " onPostExecute");

            boolean fromService =  false;
            try{
                fromService =  busInfo.get(0).forNoUIToast;
            }catch(Exception e){
                Log.i("MyAsyncTask", " Exception e " + e);
            }

            Log.i("MyAsyncTask", " fromService " + fromService);
            if(fromService){
                Log.i("MyAsyncTask", " fromService");
                Service.displayToastDistance(busInfo);


            }else if(AddMarkers.marker == null){
                Log.i("MyAsyncTask", " AddMarkers.marker == null");
                //to make markers on first run
                AddMarkers.addMarkersToMap(busInfo);
            }else{
                Log.i("MyAsyncTask", " AsyncTask  AddMarkers.markersAdded");
                //update marker
                AddMarkers.updateMarkersToMap(busInfo);
            }


        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
