package DataAccess;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clearfaun.com.pokebuspro.R;
import model.BusStopDistances;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import ui.activity.PokeBusApplicationClass;

/**
 * Created by SpencerDepas on 12/17/16.
 */

public class BusStopInformationDataAccess {

    public void BusStopInformationDataAccess() {

    }

    public void getBusStopsInformation(final String busCode, final LatLng busStopLatLng, final OnBusStopInformationCallBack onBusStopInformationCallBack) {

        String app_id = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_id);
        String app_key = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_key);
        String uk_bus_endpoint = PokeBusApplicationClass.getContext().getString(R.string.uk_bus_api_endpoint);

        Gson mGson = new GsonBuilder()
                .create();

        //Retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(uk_bus_endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .build();

        //findClosestBusStop(busStopLatLng, busCode);

        GetBussStopInterface bussStopInterface = restAdapter.create(GetBussStopInterface.class);

        bussStopInterface.getBusDistancesFromStop(busCode, app_id, app_key, "no", "yes", "5",
                new Callback<BusStopDistances>() {
                    @Override
                    public void success(BusStopDistances distancesExample, Response response) {
                        onBusStopInformationCallBack.onApiSuccess(distancesExample);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        onBusStopInformationCallBack.onApiError(error.toString());
                    }
                });
    }

    public interface OnBusStopInformationCallBack {
        public void onApiSuccess(BusStopDistances distancesExample);
        public void onApiError(String error);
    }
}
