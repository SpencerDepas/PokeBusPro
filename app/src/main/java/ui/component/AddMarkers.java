package ui.component;


import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Hashtable;

import clearfaun.com.pokebuspro.R;
import client.CallAndParse;
import model.DistancesExample;
import ui.activity.MainActivity;
import ui.activity.interfaces.FirstBusStopHasBeenDisplayed;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    FirstBusStopHasBeenDisplayed firstBusStopHasBeenDisplayed = null;


    private Hashtable<String, Marker> markerHashTable;
    private ArrayList<String> favBuses;
    private String randomMarkeyKey = "";
    private static AddMarkers addMarkers;


    public void setInterface(FirstBusStopHasBeenDisplayed newfirstBusStopHasBeenDisplayed) {
        firstBusStopHasBeenDisplayed = newfirstBusStopHasBeenDisplayed;
    }

    public static synchronized AddMarkers getInstance() {
        if (addMarkers == null) {
            addMarkers = new AddMarkers();
        }
        return addMarkers;
    }


    public void addMarkerToMapWithBusDistances(DistancesExample distancesExample, String busCode,
                                               LatLng busStopLatLng, ArrayList<String> favBuses) {

        Log.i("AddMarkers", "addMarkerToMapWithBusDistances");

        this.favBuses = favBuses;
        CallAndParse.busStopsSize--;

        int incomingBusesSize = distancesExample.getSiri().getServiceDelivery().getStopMonitoringDelivery().get(0)
                .getMonitoredStopVisit()
                .size();

        Log.i("AddMarkers", "addMarkerToMapWithBusDistances");


        String busName = getBusName(distancesExample, incomingBusesSize);


        String allbusNamesAndDistances = putMultiBusesInStackedString(distancesExample, incomingBusesSize);

        Log.i("AddMarkers", "busName : " + busName);

        LatLng markerLocation;


        MarkerManager markerManager = MarkerManager.getInstance();
        markerHashTable = markerManager.getMarkerHashTable();

        Marker marker = markerHashTable.get(busCode);
        randomMarkeyKey = busCode + busName;
        if (marker != null) {
            Log.i("AddMarkers", "markerHashTable.containsKey(hash) use old marker : " + busName);
            //use old
            //we do not need to re add it to the screen
            //we do need to delete the old info


            marker.setTitle(busCode + busName);
            marker.setSnippet(allbusNamesAndDistances);
            //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
            Log.i("AddMarkerstz", " addMarkerToMapWithBusDistances " + marker.getId().toString());

            //refreshMarkerSnippet(marker);

            markerHashTable.put(busCode, marker);


        } else {
            Log.i("AddMarkers", "markerHashTable.containsKey(hash) make new marker  : " + busName);
            //make new
            markerLocation = new LatLng(busStopLatLng.latitude, busStopLatLng.longitude);
            marker = MainActivity.googleMap.addMarker(new MarkerOptions().position(markerLocation));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
            marker.setTitle(busCode + busName);
            marker.setSnippet(allbusNamesAndDistances);

            markerHashTable.put(busCode, marker);


            //this does not scale
            //could make a hashtable for this
            for (int i = 0; i < favBuses.size(); i++) {

                if (favBuses.get(i).equals(busCode)) {
                    Log.i("AddMarkers", "  I R FAV BUS ");
                    addPokeBusColor(busCode);
                }
            }
        }


        Log.i("AddMarkers", "  favBuses.size :  " + favBuses.size());


        if (CallAndParse.busStopsSize == 0) {
            //called once
            Log.i("AddMarkerstz", "  called onece?  ");

            firstBusStopHasBeenDisplayed.removeLoadingIcon();

            openSnippet(PopupAdapterForMapMarkers.markerCurrentKey);

        }


        Log.i("AddMarkers", "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ");
    }

    private String getBusName(DistancesExample distancesExample, int incomingBusesSize) {
        //this is for when no buses are incoming


        String busName;

        if (incomingBusesSize == 0) {


            busName = "Not available";

        } else {

            busName = distancesExample.getSiri().getServiceDelivery().getStopMonitoringDelivery().get(0)
                    .getMonitoredStopVisit()
                    .get(0)
                    .getMonitoredVehicleJourney()
                    .getLineRef();


            int indexOfChar = busName.indexOf('_') + 1;
            busName = busName.substring(indexOfChar);
        }

        return busName;

    }

    private String putMultiBusesInStackedString(DistancesExample distancesExample, int incomingBusesSize) {
        //still for one stop
        Log.i("AddMarkers", "putMultiBusesInStackedString");


        String allbusNamesAndDistances = "";

        for (int i = 0; i < incomingBusesSize; i++) {


            String busNamec = distancesExample.getSiri().getServiceDelivery().getStopMonitoringDelivery().get(0)
                    .getMonitoredStopVisit()
                    .get(i)
                    .getMonitoredVehicleJourney()
                    .getLineRef();


            int indexOfChar = busNamec.indexOf('_') + 1;
            busNamec = busNamec.substring(indexOfChar);


            String distance = distancesExample.getSiri().getServiceDelivery()
                    .getStopMonitoringDelivery().get(0)
                    .getMonitoredStopVisit()
                    .get(i)
                    .getMonitoredVehicleJourney()
                    .getMonitoredCall()
                    .getExtensions()
                    .getDistances()
                    .getPresentableDistance();

            allbusNamesAndDistances += busNamec + ": " + distance + "\n";
            //Log.i("AddMarkerss", "distance : "  + distance );
        }

        if (allbusNamesAndDistances.length() < 1) {
            allbusNamesAndDistances = "No incoming busses";
        }

        return allbusNamesAndDistances;
    }


    public void openSnippet(String lastOpenSnippetKey) {


        if (!lastOpenSnippetKey.equals("")) {

            openAfterItsBeenOpenedSnippet(lastOpenSnippetKey);


        } else {

            firstTimeOpenSnippet();
        }


    }

    private void firstTimeOpenSnippet() {
        Log.i("AddMarkerstz", " firstTimeOpenSnippet ");

        if (favBuses.size() > 0) {
            showFavBusStopSnippet();

        } else {

            //if no fav buses we need a snippet to open
            Log.i("AddMarkers", " randomMarkeyKey " + randomMarkeyKey);

            Marker marker = markerHashTable.get(randomMarkeyKey);
            displayMarker(marker);


        }

    }

    private void openAfterItsBeenOpenedSnippet(String lastOpenSnippetKey) {
        //on first open is different(maybe) after you have opened  another snippet
        Log.i("AddMarkerstz", " openAfterItsBeenOpenedSnippet ");

        Marker marker = markerHashTable.get(lastOpenSnippetKey);


        if (marker != null) {
            if (marker.isInfoWindowShown()) {

                Log.i("AddMarkerstz", " openSnippet " + marker.getId().toString());

                refreshMarkerSnippet(marker);


            }
        }


    }

    private void displayMarker(Marker marker) {
        Log.i("AddMarkerstz", " displayMarker ");

        if (marker != null) {
            Log.i("AddMarkerstz", " displayMarker " + marker.getId().toString());
            marker.showInfoWindow();
        }
    }

    private void refreshMarkerSnippet(Marker marker) {
        Log.i("AddMarkerstz", " refreshMarkerSnippet " + marker.getTitle().toString());

        Log.i("AddMarkerstz", " marker.isInfoWindowShown() " + marker.isInfoWindowShown());

        if (marker.isInfoWindowShown()) {
            Log.i("AddMarkerstz", "this is the refresh refreshMarkerSnippet " + marker.getId().toString());

            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }


    public void addPokeBusColor(String busCode) {
        Log.i("AddMarkers", "addPokeBusColor");


        Marker marker = markerHashTable.get(busCode);

        if (marker != null) {
            Log.i("AddMarkers", "  I R FAV BUS ");


            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_white_blue36dp));

        }


    }

    private void showFavBusStopSnippet() {

        for (int i = 0; i < favBuses.size(); i++) {

            Marker marker = markerHashTable.get(favBuses.get(i));

            displayMarker(marker);
            break;
        }

    }

    public void removePokeBusColor(ArrayList<String> arrayList) {

        MarkerManager markerManager = MarkerManager.getInstance();
        Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();


        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {

                Log.i("AddMarkers", "arrayList.get(i) : " + arrayList.get(i));

                Marker marker = markerHashTable.get(arrayList.get(i));
                if (marker != null) {
                    //this is because you may delete an icon you can not see
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
                    Log.i("AddMarkers", " removePokeBusColor " + marker.getId().toString());
                    refreshMarkerSnippet(marker);
                }

            }
        }


    }


}
