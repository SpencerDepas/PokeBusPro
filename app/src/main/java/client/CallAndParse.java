package client;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;


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
    final private String MTA_BUS_STOP_API = "http://pokebuspro-api.herokuapp.com/bus_time";

    //private final String API_KEY = "AIzaSyAljUMfpi4WiIiLi7nHTWakvYz_PS23Pyw";
    //private final String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28"


    //final private String MTA_BUS_DISTANCE_API = "http://pokebuspro-api.herokuapp.com/bus_time/siri/stop-monitoring.json?MonitoringRef=MTA_301649&MaximumStopVisits=3";


    public CallAndParse(NoBusesInAreaInterface noBusINterFace) {


        this.mNoBusINterFace = noBusINterFace;
        Log.i("MyCallAndParse", "CallAndParse");
        mGson = new GsonBuilder()
                .create();
    }


    public void getBusStopsAndBusDistances(LatLng latLng, ArrayList<String> favBusStops, String prefRadius) {
        Log.i("MyCallAndParse", "getBusStopsAndBusDistances");

        this.mFavBusStops = favBusStops;


        Log.i("MyCallAndParse", "mFavBusStops. size : " + favBusStops.size());

        String lat = latLng.latitude + "";
        String lng = latLng.longitude + "";
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MTA_BUS_STOP_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);

        //MainActivity.prefs.getString("KEY1", "301");


        Log.i("MyCallAndParse", "prefRadius : " + prefRadius);

        //String radius = "500";
        //String lat = "40.6453205";
        //String lng = "-73.9838934";


        bussStopInterface.getBusStop(prefRadius, lat, lng, new Callback<BusStopExample>() {


            @Override
            public void success(BusStopExample busStopExample, Response response) {
                Log.i("MyCallAndParse", "get bus stops success");


                Log.i("MyCallAndParse", "get bus stops local : " + busStopExample.getData().toString());

                sBusStopsSize = busStopExample.getData().getStops().size();

                //only make a call if we have bus stops
                if (busStopExample.getData().getStops().size() > 0) {
                    makeBusDistanceThreads(busStopExample);
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

    private void makeBusDistanceThreads(BusStopExample busStopExample) {
        Log.i("MyCallAndParse", "makeBusDistanceThreads");


        for (int i = 0; i < busStopExample.getData().getStops().size(); i++) {
            Log.i("MyCallAndParse", "makeBusDistanceThreads buscode for loop " + busStopExample.getData().getStops().get(i).getCode());
            LatLng busStopLatLng = new LatLng(busStopExample.getData().getStops().get(i).getLat(), busStopExample.getData().getStops().get(i).getLon());
            GetBusDistancesLongOperation getBusDistancesLongOperation = new GetBusDistancesLongOperation(busStopLatLng);
            getBusDistancesLongOperation.execute(busStopExample.getData().getStops().get(i).getCode());
        }


    }


    public void getBusStopsDistances(String busCode, LatLng busStopLatLng) {
        Log.i("MyCallAndParse", " getBusStopsDistances   ");


        final String finalBusCode = busCode;
        final LatLng finalbusStopLatLng = busStopLatLng;
        Log.i("MyCallAndParse", " getBusStopsDistances   busCode  : " + busCode);
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MTA_BUS_STOP_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);

        bussStopInterface.getBusDistancesFromStop("MTA_" + busCode, mSetHowManyIncomingBuses, new Callback<DistancesExample>() {


            @Override
            public void success(DistancesExample distancesExample, Response response) {
                Log.i("MyCallAndParse", "get bus stops success");


                Log.i("MyCallAndParse", "get bus distances size : " + distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery()
                        .size()
                );

                Log.i("MyCallAndParse", "finalBusCode : " + finalBusCode);
                AddMarkers addMarkers = AddMarkers.getInstance();
                addMarkers.addMarkerToMapWithBusDistances(distancesExample, finalBusCode,
                        finalbusStopLatLng, mFavBusStops);


            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                //zipAPIResponse.processFailed();
            }


        });


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


}
