package client;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import model.BusStopDistances;
import model.UkBusStopsLocation;
import ui.component.AddMarkers;
import model.BusStopExample;
import model.DistancesExample;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import ui.activity.interfaces.NoBusesInAreaInterface;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public class CallAndParse {

    private ArrayList<String> mFavBusStops;
    private NoBusesInAreaInterface mNoBusINterFace = null;
    private String mSetHowManyIncomingBuses = "12";
    private Gson mGson;
    public static int sBusStopsSize = 0;
    public static String sClosestMarker;
    private static double mClosestDistance = 9999999;
    LatLng mUserLatLng;
    final private String UK_BUS_API = "http://transportapi.com/v3/uk/bus";

    private final String APP_ID = "03bf8009";
    private final String API_KEY = "d9307fd91b0247c607e098d5effedc97";
    private final String NUMBER_OF_BUS_STOPS_TO_GET = "10";


    //private final String API_KEY = "AIzaSyAljUMfpi4WiIiLi7nHTWakvYz_PS23Pyw";
    //private final String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28"

    LatLng derbyRoad = new LatLng(52.1890001, 0.1406237);


    //final private String MTA_BUS_DISTANCE_API = "http://pokebuspro-api.herokuapp.com/bus_time/siri/stop-monitoring.json?MonitoringRef=MTA_301649&MaximumStopVisits=3";


    public CallAndParse(NoBusesInAreaInterface noBusINterFace) {
        //lets see if this shit works! and it does!
        this.mNoBusINterFace = noBusINterFace;
        Log.i("MyCallAndParse", "CallAndParse");
        mGson = new GsonBuilder()
                .create();
    }


    public void getBusStops(LatLng latLng, ArrayList<String> favBusStops, String prefRadius) {
        Log.i("MyCallAndParse", "getBusStops");

        this.mFavBusStops = favBusStops;
        mUserLatLng = latLng;

        mClosestDistance = 99999999;

        Log.i("MyCallAndParse", "mFavBusStops. size : " + favBusStops.size());

        String lat = latLng.latitude + "";
        String lng = latLng.longitude + "";
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(UK_BUS_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);

        //MainActivity.prefs.getString("KEY1", "301");


        Log.i("MyCallAndParse", "prefRadius : " + prefRadius);

        //String radius = "500";
        //String lat = "40.6453205";
        //String lng = "-73.9838934";


        bussStopInterface.getBusStop(lat,
                lng,
                APP_ID,
                API_KEY,
                NUMBER_OF_BUS_STOPS_TO_GET,
                new Callback<UkBusStopsLocation>() {


                    @Override
                    public void success(UkBusStopsLocation ukBusStopsLocation, Response response) {
                        Log.i("MyCallAndParse", "get bus stops success");


                        Log.i("MyCallAndParse", "get bus ukBusStopsLocation.getTotal() " + ukBusStopsLocation.getTotal());
                        Log.i("MyCallAndParse", "ukBusStopsLocation.getStops().size() : " + ukBusStopsLocation.getStops().size());
                        Log.i("MyCallAndParse", "ukBusStopsLocation.getStops(). getAtcocode(): " + ukBusStopsLocation.getStops().get(0).getAtcocode());

                        Log.i("MyCallAndParse", "ukBusStopsLocation.getStops(). getDistance( : " + ukBusStopsLocation.getStops().get(0).getDistance());

                        sBusStopsSize = ukBusStopsLocation.getStops().size();

                        //only make a call if we have bus stops
                        if (ukBusStopsLocation.getStops().size() > 0) {
                            makeBusDistanceThreads(ukBusStopsLocation);
                        } else {
                            mNoBusINterFace.noBusesFound();
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                        //zipAPIResponse.processFailed();
                    }
                });
    }

    private void makeBusDistanceThreads(UkBusStopsLocation ukBusStopsLocation) {
        Log.i("MyCallAndParse", "makeBusDistanceThreads");
        for (int i = 0; i < ukBusStopsLocation.getStops().size(); i++) {
            Log.i("MyCallAndParse", "makeBusDistanceThreads buscode for loop " + ukBusStopsLocation.getStops().get(i).getAtcocode());
            LatLng busStopLatLng = new LatLng(ukBusStopsLocation.getStops().get(i).getLatitude(),
                    ukBusStopsLocation.getStops().get(i).getLongitude());
            GetBusDistancesLongOperation getBusDistancesLongOperation = new GetBusDistancesLongOperation(busStopLatLng);
            getBusDistancesLongOperation.execute(ukBusStopsLocation.getStops().get(i).getAtcocode());
        }
    }


    public void getBusStopsDistances(String busCode, LatLng busStopLatLng) {
        Log.i("MyCallAndParse", " getBusStopsDistances   ");
        final String finalBusCode = busCode;
        final LatLng finalbusStopLatLng = busStopLatLng;
        Log.i("MyCallAndParse", " getBusStopsDistances   busCode  : " + busCode);
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(UK_BUS_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .build();


        findClosestBusStop(busStopLatLng, busCode);

        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);

        bussStopInterface.getBusDistancesFromStop(busCode, APP_ID, API_KEY, "no", "yes", "5",
                new Callback<BusStopDistances>() {


                    @Override
                    public void success(BusStopDistances distancesExample, Response response) {
                        Log.i("MyCallAndParse", "get bus stop distances success");


                        try {
                            Log.i("MyCallAndParse", "get getAtcocode : " + distancesExample.getAtcocode());
                            Log.i("MyCallAndParse", "LLLgetAimedDepartureTime" + distancesExample.getDepartures().getAll().size());

                            Log.i("MyCallAndParse", "finalBusCode : " + finalBusCode);

                            AddMarkers addMarkers = AddMarkers.getInstance();
                            addMarkers.addMarkerToMapWithBusDistances(distancesExample, finalBusCode,
                                    finalbusStopLatLng, mFavBusStops);

                        } catch (Exception e) {
                            Log.i("MyCallAndParse", "e : " + e);

                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                        //zipAPIResponse.processFailed();
                    }


                });


    }

    private void findClosestBusStop(LatLng busStopLatLng, String busCode) {


        double currentDistance;
        currentDistance = distFrom(mUserLatLng.latitude, mUserLatLng.longitude,
                busStopLatLng.latitude, busStopLatLng.longitude);

        if (currentDistance < mClosestDistance) {
            Log.i("MyCallAndParse", "currentDistance < mClosestDistance " + busCode);
            sClosestMarker = busCode;
            mClosestDistance = currentDistance;
        }


    }


    public class GetBusDistancesLongOperation extends AsyncTask<String, Void, Void> {
        private LatLng busStopLatLng;
        private String busCode;

        public GetBusDistancesLongOperation(LatLng busStopLatLng) {
            this.busStopLatLng = busStopLatLng;
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i("MyCallAndParse", "GetBusDistancesLongOperation");
            busCode = params[0];
            Log.i("MyCallAndParse", "GetBusDistancesLongOperation params.toString() : " + busCode);


            getBusStopsDistances(busCode, busStopLatLng);

            Log.i("MyCallAndParse", "post getBusStopsDistances" + busCode);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("MyCallAndParse", "onPostExecute");


            Log.i("MyCallAndParse", "onPostExecute busCode : " + busCode);
        }

    }


    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


}
