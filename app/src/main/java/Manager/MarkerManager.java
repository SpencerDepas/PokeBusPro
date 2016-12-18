package Manager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Hashtable;

import utils.DistanceFormula;

/**
 * Created by SpencerDepas on 4/15/16.
 */
public class MarkerManager {

    private static MarkerManager sMarkerManager;
    private static double mClosestDistance = 9999999;
    private static String sClosestMarker;
    public static int sBusStopsSize = 0;
    private ArrayList<String> mFavBusStops;

    //private List<Crime> mCrimes;
    private Hashtable<String, Marker> markerHashTable;

    private MarkerManager() {
    }

    public static synchronized MarkerManager getInstance() {
        if (sMarkerManager == null) {
            sMarkerManager = new MarkerManager();
        }
        return sMarkerManager;
    }

    public ArrayList<String> getFavoriteBusStops(){
        return mFavBusStops;
    }

    public void addFavoriteBusStop(String favBusCode){
        mFavBusStops.add(favBusCode);
    }

    public Hashtable<String, Marker> getMarkerHashTable() {
        if (markerHashTable == null) {
            markerHashTable = new Hashtable<String, Marker>();
        }

        return markerHashTable;
    }

    public void setBusStopCount(int bustStopCount){
        sBusStopsSize = bustStopCount;
    }

    public int getBusStopCount(){
        return sBusStopsSize;
    }


    public String getBusCodeOfClosetBusStopToUser(){
        return sClosestMarker;
    }

    public void findClosestBusStopToCurrentLocation(LatLng busStopLatLng, LatLng userLocation, String busCode) {
        double currentDistance;
        currentDistance = DistanceFormula.distFrom(userLocation.latitude, userLocation.longitude,
                busStopLatLng.latitude, busStopLatLng.longitude);

        if (currentDistance < mClosestDistance) {
            sClosestMarker = busCode;
            mClosestDistance = currentDistance;
        }
    }
}
