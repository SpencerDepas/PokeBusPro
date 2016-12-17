package DataAccess;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import Manager.BusStopDataManager;

/**
 * Created by SpencerDepas on 12/17/16.
 */

public class GetBusDistanceTask extends AsyncTask<String, Void, Void> {
    private LatLng busStopLatLng;
    private String busCode;

    public GetBusDistanceTask(LatLng busStopLatLng) {
        this.busStopLatLng = busStopLatLng;
    }

    @Override
    protected Void doInBackground(String... params) {
        busCode = params[0];
        BusStopDataManager.getInstance().getBusStopInformation(busCode, busStopLatLng);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }

}
