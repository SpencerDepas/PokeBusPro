package Manager;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import DataAccess.BusStopInformationDataAccess;
import DataAccess.BusStopLocationDataAccess;
import DataAccess.BusStopLocationDataAccess.OnBusStopLocationCallBack;
import DataAccess.GetBusDistanceTask;
import model.BusStopDistances;
import model.UkBusStopsLocation;
import ui.activity.interfaces.NoBusesInAreaInterface;
import ui.component.AddMarkers;

/**
 * Created by SpencerDepas on 12/17/16.
 */

public class BusStopDataManager {

    private static BusStopDataManager sBusStopDataManager;
    private final String TAG = "BusStopDataManager";
    public static int sBusStopsSize = 0;

    private void BusStopDataManager() {
    }

    public static BusStopDataManager getInstance() {
        if (sBusStopDataManager == null) {
            sBusStopDataManager = new BusStopDataManager();
        }
        return sBusStopDataManager;
    }

    public void getBusStops(final NoBusesInAreaInterface mNoBusINterFace) {

        if (LocationManager.getInstance().getUserLatLng() != null) {
            BusStopLocationDataAccess mBusStopLocationDataAccess = new BusStopLocationDataAccess();
            mBusStopLocationDataAccess.getBusStops(LocationManager.getInstance().getUserLatLng(),
                    new OnBusStopLocationCallBack() {
                        @Override
                        public void onApiSuccess(UkBusStopsLocation ukBusStopsLocation) {
                            sBusStopsSize = ukBusStopsLocation.getStops().size();
                            if (sBusStopsSize > 0) {
                                makeBusDistanceThreads(ukBusStopsLocation);
                            } else {
                                mNoBusINterFace.noBusesFound();
                            }
                        }

                        @Override
                        public void onApiError(String error) {

                        }
                    });
        }

    }

    private void makeBusDistanceThreads(UkBusStopsLocation ukBusStopsLocation) {

        MarkerManager.getInstance().setBusStopCount(ukBusStopsLocation.getStops().size());

        for (int i = 0; i < ukBusStopsLocation.getStops().size(); i++) {
            LatLng busStopLatLng = new LatLng(ukBusStopsLocation.getStops().get(i).getLatitude(),
                    ukBusStopsLocation.getStops().get(i).getLongitude());
            GetBusDistanceTask getBusDistanceTask = new GetBusDistanceTask(busStopLatLng);
            getBusDistanceTask.execute(ukBusStopsLocation.getStops().get(i).getAtcocode());

            MarkerManager.getInstance().
                    findClosestBusStopToCurrentLocation(busStopLatLng,
                            LocationManager.getInstance().getUserLatLng(),
                            ukBusStopsLocation.getStops().get(i).getAtcocode());

        }
    }

    public void getBusStopInformation(final String busCode, final LatLng busStopLatLng) {
        BusStopInformationDataAccess busInformationForStopDataAccess = new BusStopInformationDataAccess();
        busInformationForStopDataAccess.getBusStopsInformation(busCode, busStopLatLng,
                new BusStopInformationDataAccess.OnBusStopInformationCallBack() {
                    @Override
                    public void onApiSuccess(BusStopDistances distancesExample) {
                        try {
                            AddMarkers addMarkers = AddMarkers.getInstance();
                            addMarkers.addMarkerToMapWithBusDistances(distancesExample, busCode,
                                    busStopLatLng);

                        } catch (Exception e) {
                            Log.i(TAG, "BusStopInformationDataAccess Exception: " + e);

                        }
                    }

                    @Override
                    public void onApiError(String error) {
                        Log.i(TAG, "BusStopInformationDataAccess onApiError: " + error.toString());

                    }
                });
    }
}
