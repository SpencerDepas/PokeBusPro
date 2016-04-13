package clearfaun.com.pokebuspro;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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



    private RestAdapter restAdapter;

    private Gson gson;

        final private String MTA_BUS_STOP_API = "http://pokebuspro-api.herokuapp.com/bus_time";
    final private String MTA_BUS_DISTANCE_API = "http://pokebuspro-api.herokuapp.com/bus_time/siri/stop-monitoring.json?MonitoringRef=MTA_301649&MaximumStopVisits=3";





    public CallAndParse(){
        Log.i("MyCallAndParse", "CallAndParse");
        gson = new GsonBuilder()
                .create();
    }


    public void getBusStopsInVicinity(String state) {
        Log.i("MyCallAndParse", "getBusStopsInVicinity");

        String lat = MapsActivity.latitude + "";
        String lng = MapsActivity.longitude + "";
        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MTA_BUS_STOP_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);


        String radius = "200000";
        //String lat = "40.6453205";
        //String lng = "-73.9838934";




        bussStopInterface.getBusSTop(radius, "40.6453205", "-73.9838934", new Callback<BusStopExample>() {


            @Override
            public void success(BusStopExample busStopExample, Response response) {
                Log.i("MyCallAndParse", "get bus stops success");


                Log.i("MyCallAndParse", "get bus stops local : " + busStopExample.getData().toString());

                Log.i("MyCallAndParse", "get bus stops size : " + busStopExample.getData().getStops().size());

                //civicInterface.gotCivicLocalInfo(local);
                getBusStopsDistances("fart");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                //zipAPIResponse.processFailed();
            }


        });


    }


    public void getBusStopsDistances(String state) {
        Log.i("MyCallAndParse", " getBusStopsDistances   ");

        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MTA_BUS_DISTANCE_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();


        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);






        bussStopInterface.getBusDistancesFromStop("MTA_301649","5",  new Callback<DistancesExample>() {


            @Override
            public void success(DistancesExample distancesExample, Response response) {
                Log.i("MyCallAndParse", "get bus stops success");


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

                //Log.i("MyCallAndParse", "get bus stops local : " + distancesExample.getData().getStops().size());

                //civicInterface.gotCivicLocalInfo(local);


            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("MyCallAndParse", "get bus stops  error  " + error.toString());
                //zipAPIResponse.processFailed();
            }


        });


    }






}
