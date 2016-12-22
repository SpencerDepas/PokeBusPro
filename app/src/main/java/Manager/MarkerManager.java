package Manager;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Hashtable;

import clearfaun.com.pokebuspro.R;
import ui.component.AddMarkers;
import utils.DistanceFormula;

/**
 * Created by SpencerDepas on 4/15/16.
 */
public class MarkerManager {

    private static MarkerManager sMarkerManager;
    private static double mClosestDistance = 9999999;
    private static String sClosestMarker;
    private static int sBusStopsSize = 0;
    private String mMarkerCurrentKey = "";

    private ArrayList<String> mFavBusStops;
     private ArrayList<String> favBuses;
    private String randomMarkeyKey = "";
    private static AddMarkers addMarkers;
    private static String currentTime;

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

    public void setmFavBusStops(ArrayList<String> mFavBusStops) {
        this.mFavBusStops = mFavBusStops;
    }

    public String getCurrentKey() {
        return mMarkerCurrentKey;
    }

    public void setCurrentKey(String sMarkerCurrentKey) {
        mMarkerCurrentKey = sMarkerCurrentKey;
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

    public void subtractOneBusStop(){
        //this is so we know when we have loaded all bus stops
        //and can stop a loading bar
        sBusStopsSize--;
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

    public void addPokeBusColor(String busCode) {
        Marker marker = markerHashTable.get(busCode);
        if (marker != null) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_white_blue36dp));
        }
    }

    public void removePokeBusColor(ArrayList<String> arrayList) {
        MarkerManager markerManager = MarkerManager.getInstance();
        Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                Marker marker = markerHashTable.get(arrayList.get(i));
                if (marker != null) {
                    //this is because you may delete an icon you can not see
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
                    refreshMarkerSnippet(marker);
                }
            }
        }
    }

    private void refreshMarkerSnippet(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }

}
