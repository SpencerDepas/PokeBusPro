package clearfaun.com.pokebuspro;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import POJO.Agency;
import POJO.BusStopData;
import POJO.BusStopExample;
import POJO.DistancesExample;
import POJO.Route;
import POJO.Stop;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public class CallAndParse {




    //private final String API_KEY = "AIzaSyAljUMfpi4WiIiLi7nHTWakvYz_PS23Pyw";


    //private final String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28"


    private String returnHowManyIncomingBuses = "4";
    private RestAdapter restAdapter;

    private Gson gson;

    final private String MTA_BUS_STOP_API = "http://pokebuspro-api.herokuapp.com/bus_time";
    //final private String MTA_BUS_DISTANCE_API = "http://pokebuspro-api.herokuapp.com/bus_time/siri/stop-monitoring.json?MonitoringRef=MTA_301649&MaximumStopVisits=3";





    public CallAndParse(){
        Log.i("MyCallAndParse", "CallAndParse");
        gson = new GsonBuilder()
                .create();
    }


    public void getBusStopsAndBusDistances(String state) {
        Log.i("MyCallAndParse", "getBusStopsAndBusDistances");

        String lat = MapsActivity.latitude + "";
        String lng = MapsActivity.longitude + "";
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MTA_BUS_STOP_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);


        String radius = "500";
        //String lat = "40.6453205";
        //String lng = "-73.9838934";




        bussStopInterface.getBusSTop(radius, "40.6459", "-73.9831", new Callback<BusStopExample>() {


            @Override
            public void success(BusStopExample busStopExample, Response response) {
                Log.i("MyCallAndParse", "get bus stops success");


                Log.i("MyCallAndParse", "get bus stops local : " + busStopExample.getData().toString());

                Log.i("MyCallAndParse", "get bus stops size : " + busStopExample.getData().getStops().size());

                //civicInterface.gotCivicLocalInfo(local);
                //getBusStopsDistances("fart");

                makeBusDistanceThreads(busStopExample);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                //zipAPIResponse.processFailed();
            }


        });


    }

    private void makeBusDistanceThreads(BusStopExample busStopExample){
        Log.i("MyCallAndParse", "makeBusDistanceThreads" );




        for(int i = 0 ; i <  busStopExample.getData().getStops().size(); i ++){
            Log.i("MyCallAndParse", "makeBusDistanceThreads buscode for loop " +  busStopExample.getData().getStops().get(i).getCode());
            LatLng busStopLatLng = new LatLng(busStopExample.getData().getStops().get(i).getLat(), busStopExample.getData().getStops().get(i).getLon());
            GetBusDistancesLongOperation getBusDistancesLongOperation = new GetBusDistancesLongOperation(busStopLatLng);
            getBusDistancesLongOperation.execute(busStopExample.getData().getStops().get(i).getCode());
        }


    }

    public class GetBusDistancesLongOperation extends AsyncTask<String, Void, String> {
        LatLng busStopLatLng;

        public GetBusDistancesLongOperation(LatLng busStopLatLng){
            this.busStopLatLng = busStopLatLng;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyCallAndParse", "GetBusDistancesLongOperation" );
            String busCode = params[0];
            Log.i("MyCallAndParse", "GetBusDistancesLongOperation params.toString() : " +  busCode);

            CallAndParse callAndParse = new CallAndParse();

            callAndParse.getBusStopsDistances(busCode, busStopLatLng);

            return "Executed";
        }

    }

    public void getBusStopsDistances(String busCode,  LatLng busStopLatLng) {
        Log.i("MyCallAndParse", " getBusStopsDistances   ");


        final String finalBusCode = busCode;
        final LatLng finalbusStopLatLng =  busStopLatLng;
        Log.i("MyCallAndParse", " getBusStopsDistances   busCode  : " + busCode);
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MTA_BUS_STOP_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);

        bussStopInterface.getBusDistancesFromStop("MTA_" + busCode, returnHowManyIncomingBuses,  new Callback<DistancesExample>() {


            @Override
            public void success(DistancesExample distancesExample, Response response) {
                Log.i("MyCallAndParse", "get bus stops success");


                Log.i("MyCallAndParse", "get bus stops size : " + distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery()
                        .size()
                );


                Log.i("MyCallAndParse", "get bus stops local : " + distancesExample.getSiri().getServiceDelivery()
                .getStopMonitoringDelivery().get(0)
                .getMonitoredStopVisit()
                .get(0)
                .getMonitoredVehicleJourney()
                .getMonitoredCall()
                .getExtensions()
                .getDistances()
                .getPresentableDistance()

                );

                //AddMarkers.updateMarkersToMap(distancesExample );

                AddMarkers addMarkers = new AddMarkers();
                addMarkers.addMarkerToMapWithBusDistances(distancesExample, finalBusCode, finalbusStopLatLng);

                //Log.i("MyCallAndParse", "get bus stops local : " + distancesExample.getData().getStops().size());

                //civicInterface.gotCivicLocalInfo(local);


                //Log.i("MyAsyncTaskJJJJJ", " busInfo.get(i).getBusCode() " + busInfo.get(i).getBusCode());
                //AddMarkers.updateMarkersToMap(busInfo.get(i), i );





            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                //zipAPIResponse.processFailed();
            }


        });


    }






}
