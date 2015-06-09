package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by spencer on 2/22/2015.
 */
public class GetBusDistanceJSON {

    boolean fromService =  false;
    int stopCode;
    ArrayList<BusInfo> busInfo;
    JSONObject busNameObject;

    long timeForParsing;



    @SuppressLint("NewApi")
    public void readAndParseJSON(ArrayList<String> in) {

        //for each stop


        //in is the stream of data withdistance informatioon. it will be as large as the amount of buis stops in the area
        Log.i("MyGetBusDistanceJSONt", "inside readAndParseJSON");
        timeForParsing =  System.currentTimeMillis() ;

        try {

            Log.i("MyGetBusDistanceJSONt", "in.length " + in.size());

            for (int z = 0 ; z < in.size(); z ++) {

                JSONObject reader = new JSONObject(in.get(z));
                //z changes the bus stop we are choosing.

                Log.i("MyGetBusDistanceJSONttt", "z is :  " + reader.toString());


                int maxNumberOfBusDistancesAvailable = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                        .getJSONObject(0)
                        .getJSONArray("MonitoredStopVisit").length();


                String[] tempDistance = new String[maxNumberOfBusDistancesAvailable];
                // Max return of three buses
                for (int i = 0; i < maxNumberOfBusDistancesAvailable; i++) {
                    // i gets the distances for each stop, up to three
                    Log.i("MyGetBusDistanceJSONt", "z is :  " + z);
                    Log.i("MyGetBusDistanceJSONt", "i is :  " + i);
                    Log.i("MyGetBusDistanceJSONt", " busInfo.get(z).getBusCode():  " + busInfo.get(z).getBusCode());
                    try {


                        Log.i("MyGetBusDistanceJSONt", "maxNumberOfBusDistancesAvailable :" + maxNumberOfBusDistancesAvailable);

                        int sysLength = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                                .getJSONObject(0).length();

                        Log.i("MyGetBusDistanceJSONt", "maxNumberOfBusDistancesAvailable :" + sysLength);


                        String destinationName = reader.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery")
                                .getJSONObject(0)
                                .getJSONArray("MonitoredStopVisit")
                                .getJSONObject(i)
                                .getJSONObject("MonitoredVehicleJourney")
                                .get("DestinationName").toString();

                        Log.i("MyGetBusDistanceJSONt", "destinationName :" + destinationName);

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


                        if (fromService && busInfo.get(0).getBusName().equals(busNameObject)) {
                            busInfo.get(0).setBusDistance(sys.get("PresentableDistance").toString());
                            Log.i("MyGetBusStopJSONy", "sys.get(\"PresentableDistance\").toString() " + sys.get("PresentableDistance").toString());
                            Log.i("MyGetBusStopJSONy", "busInfo.get(0)." + busInfo.get(0).getDistance()[0]);
                        } else if (busInfo.get(z).getBusCode().equals(busCodeObject.substring(4))) {
                            //if the bus code is the same
                            //if the bus name is the same

                            if (busInfo.get(z).getBusName().equals(busNameObject)) {
                                Log.i("MyGetBusStopJSONy", "busInfo.get(z).getBusName() " + busInfo.get(z).getBusName());
                                Log.i("MyGetBusStopJSONy", "busNameObject " + busNameObject);
                                busInfo.get(z).setLongName(destinationName);
                                Log.i("MyGetBusStopJSONy", "z: ) " + z);
                                Log.i("MyGetBusStopJSONy", "i: busInfo.get(q).getBusCode() " + busInfo.get(z).getBusCode());
                                Log.i("MyGetBusStopJSONy", "IN " + busNameObject);
                                Log.i("MyGetBusStopJSONy", "PresentableDistance " + sys.get("PresentableDistance").toString());


                                busInfo.get(z).setBusDistance(sys.get("PresentableDistance").toString());

                            }
                        }


                        //tempDistance[i] = sys.get("PresentableDistance").toString();
                        Log.i("MyGetBusDistanceJSONt", " sys  " + sys.get("PresentableDistance").toString() + ". i is : " + i);

                    } catch (Exception e) {
                        Log.i("MyGetBusDistanceJSONt", " iner try catch Exception e  " + e.toString());
                        tempDistance[0] = "flofpfjfik";
                        tempDistance[1] = "flofpfjfik";
                        tempDistance[2] = "flofpfjfik";

                    }
                }


                Log.i("MyGetBusDistanceJSONt", " tempBusInfo  " + busInfo.get(z).getBusCode());
                Log.i("MyGetBusDistanceJSONt", " busInfo.size()  " + busInfo.size());

            }


            //just to read data
            //
            //
            //
           /* for(int i = 0 ; i < busInfo.size(); i ++){
                Log.i("MyGetBusDistanceJSONo", " name  " + busInfo.get(i).getBusName());
                Log.i("MyGetBusDistanceJSONo", " bus code  " + busInfo.get(i).getBusCode());
                Log.i("MyGetBusDistanceJSONo", " distance 1:  " + busInfo.get(i).getDistance()[0]);
                Log.i("MyGetBusDistanceJSONo", " distance 2:  " + busInfo.get(i).getDistance()[1]);
                Log.i("MyGetBusDistanceJSONo", " distance 3:  " + busInfo.get(i).getDistance()[2]);
                Log.i("MyGetBusDistanceJSONo", "  \n  ");
            }*/


        }catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("MyGetBusDistanceJSON", "inside readAndParseJSON  Exception  " + e.toString());
        }

            Log.i("MyAsyncTask", "end11:");
    }

    long startTime;

    public void fetchBusDistanceJson(ArrayList<BusInfo> busInfoIn) {

        busInfo = busInfoIn;
        Log.i("fetchBusDistanceJson", "busInfo" + busInfo.size());
        startTime =  System.currentTimeMillis() ;
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




            ArrayList<String> data = new ArrayList<>();
            Log.i("MyGetBusDistanceJSONn", "inside AsyncTask");
            try {

                BusInfo.clearJson();

                for(int i = 0, t = 0; i < busInfo.size(); i ++) {




                    Log.i("MyGetBusDistanceJSONn", "busInfo.size(): " + busInfo.size());
                    Log.i("MyGetBusDistanceJSONn", "i is : " + i );
                    Log.i("MyGetBusDistanceJSONn", "data.length is : " + data.size() );
                    Log.i("MyGetBusDistanceJSONn", "busInfo.get(i).getBusCode(): " + busInfo.get(i).getBusCode());
                    stopCode = Integer.parseInt(busInfo.get(i).getBusCode());
                    //Log.i("MyGetBusDistanceJSONnn", "stopCode " + stopCode);


                    if(BusInfo.hasBusCodeBeenCalledJson(busInfo.get(i).getBusCode())){

                        //we only want one request for each stop
                        //Log.i("MyGetBusDistanceJSONnn", "!hasBusCodeBeenCalledJson " + busInfo.get(i).getBusCode() );
                        //Log.i("MyGetBusDistanceJSONnn", "i is : " + i );

                    }else {
                        Log.i("MyGetBusDistanceJSONnn", "hasBusCodeBeenCalledJson "  + busInfo.get(i).getBusCode());

                        BusInfo.addBusCodeBeenCalledJson(busInfo.get(i).getBusCode());
                        Log.i("MyGetBusDistanceJSONnn", "stopCode " + stopCode);

                        if (fromService) {

                            busDistanceURL = "http://pokebuspro-api.herokuapp.com/bus_time/siri/stop-monitoring.json?MonitoringRef=MTA_"
                                    + stopCode + "&MaximumStopVisits=" + howManyBusesPerStop;

                            URL url = new URL(busDistanceURL);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(10000 /* milliseconds */);
                            conn.setConnectTimeout(15000 /* milliseconds */);
                            conn.setRequestMethod("GET");
                            conn.setDoInput(true);
                            // Starts the query
                            conn.connect();
                            InputStream stream = conn.getInputStream();

                            data.add(i, convertStreamToString(stream));
                            stream.close();

                            break;
                        }

                        Log.i("MyGetBusDistanceJSONnn", "data length "  + data.size());

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

                        data.add(t, convertStreamToString(stream));
                        t++;
                        stream.close();
                    }
                }
                Log.i("MyGetBusDistanceJSONn", "data size: " + data.size());





            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyGetBusDistanceJSONn", " inside exception" +  e.toString());
            }

           /* for(int g = 0 ; g < data.size(); g ++){

                if(data.get(g).length() > 2){
                    Log.i("MyGetBusDistanceJSONn", " whats the count on data " +  g);
                }

            }*/

            for(int i = 0 ; i < busInfo.size(); i++){
                //each distance is put in one at a time
                //this resets the counter
                //must be done in order to put new distances in
                busInfo.get(i).resetbusDistanceCounter();
            }

            //long endTimeTT =  (System.currentTimeMillis() );
            Log.i("MyMapsActivityTime", "getBusDistance(busInfo) This is the time it takes to connect with all the data : " + ((System.currentTimeMillis()  - startTime)));

            readAndParseJSON(data);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("MyAsyncTask", " onPostExecute");

            long endTime =  (System.currentTimeMillis() );

            Log.i("MyMapsActivityTime", "getBusDistance(busInfo) endTime for parsing : " + ((endTime - timeForParsing)));

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
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
            Log.i("MyAsyncTask", " onPreExecute" );
            fromService =  false;
            try{
                fromService =  busInfo.get(0).forNoUIToast;
            }catch(Exception e){
                Log.i("MyAsyncTask", " Exception e " + e);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
