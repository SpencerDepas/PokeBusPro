package ui.component;


import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import Manager.MarkerManager;
import clearfaun.com.pokebuspro.R;
import client.CallAndParse;
import model.BusStopDistances;
import ui.activity.MainActivity;
import ui.activity.interfaces.AddMarkersCallback;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    private AddMarkersCallback firstBusStopHasBeenDisplayed = null;


    private Hashtable<String, Marker> markerHashTable;
    private ArrayList<String> favBuses;
    private String randomMarkeyKey = "";
    private static AddMarkers addMarkers;
    private static String currentTime;


    public void setInterface(AddMarkersCallback newfirstBusStopHasBeenDisplayed) {
        firstBusStopHasBeenDisplayed = newfirstBusStopHasBeenDisplayed;
    }

    public static synchronized AddMarkers getInstance() {
        if (addMarkers == null) {
            addMarkers = new AddMarkers();
        }
        return addMarkers;
    }


    public void addMarkerToMapWithBusDistances(BusStopDistances distancesExample, String busCode,
                                               LatLng busStopLatLng, ArrayList<String> favBuses) {

        Log.i("AddMarkers", "addMarkerToMapWithBusDistances");

        Log.i("AddMarkers", "busCode : " + busCode);


        this.favBuses = favBuses;
        CallAndParse.sBusStopsSize--;

        int incomingBusesSize = distancesExample.getDepartures().getAll().size();


        Log.i("AddMarkers", "incomingBusesSize : " + incomingBusesSize);
        Log.i("AddMarkers", "addMarkerToMapWithBusDistances");


        String busName = getBusName(distancesExample, incomingBusesSize);
        //String busName = busCode;


        String allbusNamesAndDistances = putMultiBusesInStackedString(distancesExample, incomingBusesSize);

        Log.i("AddMarkers", "busName : " + busName);

        LatLng markerLocation;


        MarkerManager markerManager = MarkerManager.getInstance();
        markerHashTable = markerManager.getMarkerHashTable();

        Log.i("AddMarkers", "busCode : " + busCode);

        Marker marker = markerHashTable.get(busCode);
        if (marker != null) {
            Log.i("AddMarkers", "markerHashTable.containsKey(hash) use old marker : " + busName);
            //use old
            //we do not need to re add it to the screen
            //we do need to delete the old info


            marker.setTitle(busCode);
            marker.setSnippet(allbusNamesAndDistances);
            Log.i("AddMarkerstz", " addMarkerToMapWithBusDistances " + marker.getId().toString());

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


        if (CallAndParse.sBusStopsSize == 0) {
            //called once
            Log.i("AddMarkerstz", "  called onece?  ");

            firstBusStopHasBeenDisplayed.removeLoadingIcon();

            openSnippet(PopupAdapterForMapMarkers.sMarkerCurrentKey, CallAndParse.sClosestMarker);
            Log.i("AddMarkers", "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ");

        }


    }

    private String getBusName(BusStopDistances distancesExample, int incomingBusesSize) {
        //this is for when no buses are incoming


        String busName;

        if (incomingBusesSize == 0) {


            busName = "Not available";

        } else {

            busName = distancesExample.getDepartures().getAll().get(0).getLineName();


            int indexOfChar = busName.indexOf('_') + 1;
            busName = busName.substring(indexOfChar);
        }

        return busName;

    }

    private String currentTime() {
        Log.i("AddMarkers", "currentTime");


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String str = sdf.format(new Date());


        return str;
    }


    private String timeToMinuets(String incomingBusTime) {
        Log.i("AddMarkers", "timeToMinuets" + incomingBusTime);

        int timeTillBus = 0;
        if (incomingBusTime != null) {


            currentTime = currentTime();
            Log.i("AddMarkers", "currentTime" + currentTime);

            currentTime = currentTime.replaceAll(":", "");
            int incomingBusInt = Integer.parseInt(incomingBusTime.replaceAll(":", ""));
            int timeInt = Integer.parseInt(currentTime);

            timeTillBus = incomingBusInt - timeInt;
            if (timeTillBus < 0) {
                return incomingBusTime;
            }

            Log.i("AddMarkers", "timeTillBus" + timeTillBus);


        }


        return (timeTillBus + "");
    }

    private String putMultiBusesInStackedString(BusStopDistances distancesExample, int incomingBusesSize) {
        //still for one stop
        Log.i("AddMarkers", "putMultiBusesInStackedString");
        Log.i("AddMarkers", "incomingBusesSize : " + incomingBusesSize);
        Log.i("AddMarkers", "distancesExample.getDepartures().getAll().size() : " + distancesExample.getDepartures().getAll().size());


        String allbusNamesAndDistances = "";

        for (int i = 0; i < incomingBusesSize; i++) {


            String busNamec = distancesExample.getDepartures().getAll().get(i).getLineName();


            int indexOfChar = busNamec.indexOf('_') + 1;
            busNamec = busNamec.substring(indexOfChar);

            String time = timeToMinuets(distancesExample.getDepartures().getAll().get(i)
                    .getBestDepartureEstimate());
            Log.i("AddMarkers", "LLL time" + time);

            if(time.contains(":")){
                allbusNamesAndDistances += busNamec + ": " + time + " AM\n";

            }else{
                allbusNamesAndDistances += busNamec + ": " + time + "min\n";

            }

             //Log.i("AddMarkerss", "distance : "  + distance );
        }

        if (allbusNamesAndDistances.length() < 1) {
            allbusNamesAndDistances = "No incoming busses";
        }

        return allbusNamesAndDistances;
    }


    public void openSnippet(String lastOpenSnippetKey, String closestMarkerToUser) {


        Log.i("AddMarkerstz", " lastOpenSnippetKey :  " + lastOpenSnippetKey);

        if (!lastOpenSnippetKey.equals("")) {
            Log.i("AddMarkerstz", " !lastOpenSnippetKey.equals(\"\") :  ");


            openAfterItsBeenOpenedSnippet(lastOpenSnippetKey, closestMarkerToUser);


        } else {
            Log.i("AddMarkerstz", " else:  ");


            firstTimeOpenSnippet(closestMarkerToUser);
        }


    }

    private void firstTimeOpenSnippet(String closestMarkerToUser) {
        Log.i("AddMarkerstz", " firstTimeOpenSnippet ");

        if (favBuses.size() > 0) {

            boolean hasFavBusStopBeenDisplayed = showFavBusStopSnippet();

            if (hasFavBusStopBeenDisplayed) {
                Log.i("AddMarkerstz", " !hasFavBusStopBeenDisplayed :" + hasFavBusStopBeenDisplayed);

                displayClosestMarker(closestMarkerToUser);
            }

        } else {

            //if no fav buses we need a snippet to open
            displayClosestMarker(closestMarkerToUser);


        }

    }

    private void displayClosestMarker(String closestMarkerToUser) {
        Log.i("AddMarkers", " displayClosestMarker " + closestMarkerToUser);

        Marker marker = markerHashTable.get(closestMarkerToUser);
        displayMarker(marker);
    }

    private void openAfterItsBeenOpenedSnippet(String lastOpenSnippetKey, String closestMarkerToUser) {
        //on first open is different(maybe) after you have opened  another snippet
        Log.i("AddMarkerstz", " openAfterItsBeenOpenedSnippet ");

        Marker marker = markerHashTable.get(lastOpenSnippetKey);


        if (marker != null) {
            Log.i("AddMarkerstz", " marker != null ");

            if (marker.isInfoWindowShown()) {

                Log.i("AddMarkerstz", " openSnippet " + marker.getId().toString());

                refreshMarkerSnippet(marker);

            } else {

                Log.i("AddMarkerstz", " !!!! marker.isInfoWindowShown() ");

                //here we go
                marker.hideInfoWindow();
                marker.showInfoWindow();
                firstBusStopHasBeenDisplayed.animateCameraToMarker(marker);

            }
        } else {

            Log.i("AddMarkerstz", " else !!!! marker != null ");


            //if the last snippet is null
            //then lets open the closet snippet to you
            marker = markerHashTable.get(closestMarkerToUser);
            if (marker != null) {
                firstBusStopHasBeenDisplayed.animateCameraToMarker(marker);
                marker.showInfoWindow();

            }

        }


    }

    private void displayMarker(Marker marker) {
        Log.i("AddMarkerstz", " displayMarker ");

        if (marker != null) {
            Log.i("AddMarkerstz", " displayMarker " + marker.getId());
            firstBusStopHasBeenDisplayed.animateCameraToMarker(marker);
            marker.showInfoWindow();
        }
    }

    private void refreshMarkerSnippet(Marker marker) {
        Log.i("AddMarkerstz", " refreshMarkerSnippet " + marker.getTitle());

        Log.i("AddMarkerstz", " marker.isInfoWindowShown() " + marker.isInfoWindowShown());

        if (marker.isInfoWindowShown()) {
            Log.i("AddMarkerstz", "this is the refresh refreshMarkerSnippet " + marker.getId().toString());

            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }


    public void addPokeBusColor(String busCode) {
        Log.i("AddMarkers", "addPokeBusColor");

        Log.i("AddMarkers", "busCode : " + busCode);

        Marker marker = markerHashTable.get(busCode);

        if (marker != null) {
            Log.i("AddMarkers", "I R FAV BUS ");


            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_white_blue36dp));

        }


    }

    private boolean showFavBusStopSnippet() {
        Log.i("AddMarkers", "showFavBusStopSnippet ");


        for (int i = 0; i < favBuses.size(); i++) {

            Log.i("AddMarkers", "favBuses :  " + favBuses.get(i));


            Marker marker = markerHashTable.get(favBuses.get(i));

            displayMarker(marker);
            return true;
        }

        return false;

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
