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
    private static final String TAG = "AddMarkers";

    public static synchronized AddMarkers getInstance() {
        if (addMarkers == null) {
            addMarkers = new AddMarkers();
        }
        return addMarkers;
    }

    public void setInterface(AddMarkersCallback newfirstBusStopHasBeenDisplayed) {
        firstBusStopHasBeenDisplayed = newfirstBusStopHasBeenDisplayed;
    }

    public void addMarkerToMapWithBusDistances(BusStopDistances distancesExample, String currenBusCode,
                                               LatLng busStopLatLng, ArrayList<String> favBuses) {
        this.favBuses = favBuses;

        MarkerManager.getInstance().subtractOneBusStop();
        int incomingBusesSize = distancesExample.getDepartures().getAll().size();

        String busName = getBusName(distancesExample, incomingBusesSize);
        //String busName = busCode;


        String allbusNamesAndDistances = putMultiBusesInStackedString(distancesExample,
                incomingBusesSize);

        LatLng markerLocation;


        MarkerManager markerManager = MarkerManager.getInstance();
        markerHashTable = markerManager.getMarkerHashTable();

        Marker marker = markerHashTable.get(currenBusCode);
        if (marker != null) {
            //use old
            //we do not need to re add it to the screen
            //we do need to delete the old info
            marker.setTitle(currenBusCode);
            marker.setSnippet(allbusNamesAndDistances);
            Log.i(TAG, " addMarkerToMapWithBusDistances " + marker.getId().toString());

            markerHashTable.put(currenBusCode, marker);


        } else {
            //make new
            markerLocation = new LatLng(busStopLatLng.latitude, busStopLatLng.longitude);
            marker = MainActivity.googleMap.addMarker(new MarkerOptions().position(markerLocation));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
            marker.setTitle(currenBusCode + busName);
            marker.setSnippet(allbusNamesAndDistances);

            markerHashTable.put(currenBusCode, marker);


            //this does not scale
            //could make a hashtable for this
            String favBusCode = isAFavoriteBus(favBuses, currenBusCode);
            if(favBusCode != null){
                MarkerManager.getInstance().addPokeBusColor(currenBusCode);
            }

        }

        if (MarkerManager.getInstance().getBusStopCount() == 0) {
            //called once
            firstBusStopHasBeenDisplayed.removeLoadingIcon();
            openSnippet(PopupAdapterForMapMarkers.sMarkerCurrentKey,
                    MarkerManager.getInstance().getBusCodeOfClosetBusStopToUser());
            Log.i(TAG, "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ");
        }
    }

    private String isAFavoriteBus(ArrayList<String> favBuses, String busCode){
        for (int i = 0; i < favBuses.size(); i++) {
            if (favBuses.get(i).equals(busCode)) {
                return busCode;
            }
        }
        return null;
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String str = sdf.format(new Date());
        return str;
    }

    private String timeToMinuets(String incomingBusTime) {
        int timeTillBus = 0;
        if (incomingBusTime != null) {
            currentTime = currentTime();
            Log.i(TAG, "currentTime" + currentTime);

            currentTime = currentTime.replaceAll(":", "");
            int incomingBusInt = Integer.parseInt(incomingBusTime.replaceAll(":", ""));
            int timeInt = Integer.parseInt(currentTime);

            timeTillBus = incomingBusInt - timeInt;
            if (timeTillBus < 0) {
                return incomingBusTime;
            }

        }
        return (timeTillBus + "");
    }

    private String putMultiBusesInStackedString(BusStopDistances distancesExample, int incomingBusesSize) {
        //still for one stop
        String allbusNamesAndDistances = "";
        for (int i = 0; i < incomingBusesSize; i++) {


            String busNamec = distancesExample.getDepartures().getAll().get(i).getLineName();


            int indexOfChar = busNamec.indexOf('_') + 1;
            busNamec = busNamec.substring(indexOfChar);

            String time = timeToMinuets(distancesExample.getDepartures().getAll().get(i)
                    .getBestDepartureEstimate());
            Log.i(TAG, "LLL time" + time);

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
        if (!lastOpenSnippetKey.equals("")) {
            openAfterItsBeenOpenedSnippet(lastOpenSnippetKey, closestMarkerToUser);
        } else {
            firstTimeOpenSnippet(closestMarkerToUser);
        }
    }

    private void firstTimeOpenSnippet(String closestMarkerToUser) {
        if (favBuses.size() > 0) {
            boolean hasFavBusStopBeenDisplayed = showFavBusStopSnippet();
            if (hasFavBusStopBeenDisplayed) {
                displayClosestMarker(closestMarkerToUser);
            }
        } else {
            //if no fav buses we need a snippet to open
            displayClosestMarker(closestMarkerToUser);
        }
    }

    private void displayClosestMarker(String closestMarkerToUser) {
        Marker marker = markerHashTable.get(closestMarkerToUser);
        displayMarker(marker);
    }

    private void openAfterItsBeenOpenedSnippet(String lastOpenSnippetKey, String closestMarkerToUser) {
        //on first open is different(maybe) after you have opened  another snippet
        Marker marker = markerHashTable.get(lastOpenSnippetKey);
        if (marker != null) {
            if (marker.isInfoWindowShown()) {
                refreshMarkerSnippet(marker);

            } else {
                //here we go
                marker.hideInfoWindow();
                marker.showInfoWindow();
                firstBusStopHasBeenDisplayed.animateCameraToMarker(marker);
            }
        } else {
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
        if (marker != null) {
            firstBusStopHasBeenDisplayed.animateCameraToMarker(marker);
            marker.showInfoWindow();
        }
    }

    private void refreshMarkerSnippet(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }

    public void addPokeBusColor(String busCode) {
        Marker marker = markerHashTable.get(busCode);
        if (marker != null) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_white_blue36dp));
        }
    }

    private boolean showFavBusStopSnippet() {
        for (int i = 0; i < favBuses.size(); i++) {
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
                Marker marker = markerHashTable.get(arrayList.get(i));
                if (marker != null) {
                    //this is because you may delete an icon you can not see
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
                    refreshMarkerSnippet(marker);
                }
            }
        }
    }

}
