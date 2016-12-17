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

    public static synchronized BusStopDataManager getInstance() {
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
                if (ukBusStopsLocation.getStops().size() > 0) {
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
            GetBusDistanceTask getBusDistancesLongOperation = new GetBusDistanceTask(busStopLatLng);
            getBusDistancesLongOperation.execute(ukBusStopsLocation.getStops().get(i).getAtcocode());
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


    private void findClosestBusStopToCurrentLocation(LatLng busStopLatLng, String busCode) {

        double currentDistance;
        currentDistance = distFrom(mUserLatLng.latitude, mUserLatLng.longitude,
                busStopLatLng.latitude, busStopLatLng.longitude);

        if (currentDistance < mClosestDistance) {
            Log.i("MyCallAndParse", "currentDistance < mClosestDistance " + busCode);
            sClosestMarker = busCode;
            mClosestDistance = currentDistance;
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
