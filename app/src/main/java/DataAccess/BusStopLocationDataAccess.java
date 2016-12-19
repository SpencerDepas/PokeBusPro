package DataAccess;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clearfaun.com.pokebuspro.R;
import model.UkBusStopsLocation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import ui.activity.PokeBusApplicationClass;

/**
 * Created by SpencerDepas on 12/17/16.
 */

public class BusStopLocationDataAccess {

    public void BusStopLocationDataAccess() {
    }

    public void getBusStops(LatLng latLng, final OnBusStopLocationCallBack onBusStopLocationCallBack) {

        String app_id = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_id);
        String app_key = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_key);
        String uk_bus_endpoint = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_endpoint);
        String numberOfBusStopsToReturn = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_number_of_bus_Stops_to_return);


        Gson mGson = new GsonBuilder()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(uk_bus_endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .build();

        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);
        bussStopInterface.getBusStop(latLng.latitude + "", latLng.longitude + "", app_id, app_key, numberOfBusStopsToReturn,
                new Callback<UkBusStopsLocation>() {
                    @Override
                    public void success(UkBusStopsLocation ukBusStopsLocation, Response response) {
                        onBusStopLocationCallBack.onApiSuccess(ukBusStopsLocation);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        onBusStopLocationCallBack.onApiError(error.toString());
                    }
                });

    }

    public interface OnBusStopLocationCallBack {
        void onApiSuccess(UkBusStopsLocation ukBusStopsLocation);
        void onApiError(String error);
    }

}
