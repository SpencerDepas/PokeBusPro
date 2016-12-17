package Manager;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Hashtable;

import DataAccess.BusStopInformationDataAccess;
import DataAccess.BusStopLocationDataAccess;
import DataAccess.BusStopLocationDataAccess.OnBusStopLocationCallBack;
import DataAccess.GetBusDistanceTask;
import client.CallAndParse;
import model.BusStopDistances;
import model.UkBusStopsLocation;
import ui.activity.interfaces.NoBusesInAreaInterface;
import utils.DistanceFormula;

/**
 * Created by SpencerDepas on 12/17/16.
 */

public class BusStopDataManager {

    private static BusStopDataManager sBusStopDataManager;
    private ArrayList<String> mFavBusStops;
    public static int sBusStopsSize = 0;
    public static String sClosestMarker;
    private static double mClosestDistance = 9999999;
    LatLng mUserLatLng;
    private NoBusesInAreaInterface mNoBusINterFace = null;

    private void BusStopDataManager() {
    }

    public static BusStopDataManager getInstance() {
        if (sBusStopDataManager == null) {
            sBusStopDataManager = new BusStopDataManager();
        }
        return sBusStopDataManager;
    }

    private void getBusStops(final NoBusesInAreaInterface mNoBusINterFace) {
        this.mNoBusINterFace = mNoBusINterFace;

        BusStopLocationDataAccess mBusStopLocationDataAccess = new BusStopLocationDataAccess();
        mBusStopLocationDataAccess.getBusStops(new LatLng(11, 11), new OnBusStopLocationCallBack() {
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

    private void makeBusDistanceThreads(UkBusStopsLocation ukBusStopsLocation) {
        for (int i = 0; i < ukBusStopsLocation.getStops().size(); i++) {
            LatLng busStopLatLng = new LatLng(ukBusStopsLocation.getStops().get(i).getLatitude(),
                    ukBusStopsLocation.getStops().get(i).getLongitude());
            GetBusDistanceTask getBusDistanceTask = new GetBusDistanceTask(busStopLatLng);
            getBusDistanceTask.execute(ukBusStopsLocation.getStops().get(i).getAtcocode());


            findClosestBusStopToCurrentLocation(busStopLatLng, ukBusStopsLocation.getStops().get(i).getAtcocode());
        }
    }

    public void getBusStopInformation(String busCode, LatLng busStopLatLng) {

        BusStopInformationDataAccess busInformationForStopDataAccess = new BusStopInformationDataAccess();
        busInformationForStopDataAccess.getBusStopsInformation(busCode, busStopLatLng,
                new BusStopInformationDataAccess.OnBusStopInformationCallBack() {
                    @Override
                    public void onApiSuccess(BusStopDistances distancesExample) {
//                        try {
//
//                            AddMarkers addMarkers = AddMarkers.getInstance();
//                            addMarkers.addMarkerToMapWithBusDistances(distancesExample, busCode,
//                                    busStopLatLng, mFavBusStops);
//
//                        } catch (Exception e) {
//                            Log.i(TAG, "addMarkerToMapWithBusDistances : " + e);
//
//                        }
                    }

                    @Override
                    public void onApiError(String error) {

                    }
                });
    }

    //belongs in marker manager 
    private void findClosestBusStopToCurrentLocation(LatLng busStopLatLng, String busCode) {
        double currentDistance;
        currentDistance = DistanceFormula.distFrom(mUserLatLng.latitude, mUserLatLng.longitude,
                busStopLatLng.latitude, busStopLatLng.longitude);

        if (currentDistance < mClosestDistance) {
            sClosestMarker = busCode;
            mClosestDistance = currentDistance;
        }

    }


}
