package client;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import clearfaun.com.pokebuspro.BuildConfig;
import model.DistancesExample;
import okhttp3.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by SpencerDepas on 5/24/16.
 */
public class MTAClient {

    private Gson gson;
    private RestAdapter adapter;
    private GetBussStopInterface client;

    public MTAClient() {

        if (adapter == null || client == null) {
            OkHttpClient okHttpClient = new OkHttpClient();

            gson = new GsonBuilder()
                    .create();


            adapter = new RestAdapter.Builder()
                    .setEndpoint("horku")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setConverter(new GsonConverter(gson))
                    .build();

            client = adapter.create(GetBussStopInterface.class);

        }
    }

    public DistancesExample getBusDistances(String buscode, String returnHowManyBuses) {
        return client.getBusDistancesFromStopTwo(buscode, returnHowManyBuses);
    }


    public DistancesExample getBusStopsAndBusDistances(String buscode, String returnHowManyBuses) {
        return client.getBusDistancesFromStopTwo(buscode, returnHowManyBuses);
    }

}
