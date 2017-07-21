package ui.component;


import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import Manager.MarkerManager;
import clearfaun.com.pokebuspro.R;
import model.BusStopDistances;
import ui.activity.MainActivity;
import ui.activity.PokeBusApplicationClass;
import ui.activity.interfaces.AddMarkersCallback;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    private static final String TAG = "AddMarkers";
    private AddMarkersCallback firstBusStopHasBeenDisplayed = null;
    private static AddMarkers addMarkers;
    private static String currentTime;

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
                                               LatLng busStopLatLng) {
        MarkerManager.getInstance().subtractOneBusStop();
        int incomingBusesSize = distancesExample.getDepartures().getAll().size();

        String allbusNamesAndDistances = putMultiBusesInStackedString(distancesExample,
                incomingBusesSize);


//        Marker marker = MarkerManager.getInstance().getMarkerHashTable().get(currenBusCode);
//        if (marker != null) {
//            updateMarker(marker, currenBusCode, allbusNamesAndDistances);
//            marker.isVisible();
//        } else {
//            makeNewMarker(currenBusCode, allbusNamesAndDistances, busStopLatLng);
//            //this does not scale
//            //could make a hashtable for this
//            Boolean isAFavoriteBus = isAFavoriteBusStop(currenBusCode);
//            if (isAFavoriteBus) {
//                MarkerManager.getInstance().addPokeBusColor(currenBusCode);
//            }
//        }

        makeNewMarker(currenBusCode, allbusNamesAndDistances, busStopLatLng);
        //this does not scale
        //could make a hashtable for this
        Boolean isAFavoriteBus = isAFavoriteBusStop(currenBusCode);
        if (isAFavoriteBus) {
            MarkerManager.getInstance().addPokeBusColor(currenBusCode);
        }

        checkIfIsLastBusStop();
    }

    private void checkIfIsLastBusStop(){
        if (MarkerManager.getInstance().getBusStopCount() == 0) {
            //called once
            firstBusStopHasBeenDisplayed.removeLoadingIcon();
            openSnippet(MarkerManager.getInstance().getCurrentKey(),
                    MarkerManager.getInstance().getBusCodeOfClosetBusStopToUser());
            Log.i(TAG, "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ");
        }
    }

    private void updateMarker(Marker marker, String currenBusCode, String busInfo) {
        marker.setTitle(currenBusCode);
        marker.setSnippet(busInfo);
        MarkerManager.getInstance().getMarkerHashTable().put(currenBusCode, marker);
//        if(!marker.isVisible()){
//              MainActivity.googleMap.addMarker(new MarkerOptions().position(marker.getPosition()));
//        }
    }

    private void makeNewMarker(String currenBusCode, String busInfo, LatLng busStopLatLng) {
        LatLng markerLocation = new LatLng(busStopLatLng.latitude, busStopLatLng.longitude);
        Marker marker = MainActivity.googleMap.addMarker(new MarkerOptions().position(markerLocation));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
        marker.setTitle(currenBusCode);
        marker.setSnippet(busInfo);

        MarkerManager.getInstance().getMarkerHashTable().put(currenBusCode, marker);

    }

    private boolean isAFavoriteBusStop(String busCode) {
        for (int i = 0; i < MarkerManager.getInstance().getFavoriteBusStops().size(); i++) {
            if (MarkerManager.getInstance().getFavoriteBusStops().get(i).equals(busCode)) {
                return true;
            }
        }
        return false;
    }

    private String getBusName(BusStopDistances distancesExample, int incomingBusesSize) {
        //this is for when no buses are incoming
        String busName;
        if (incomingBusesSize == 0) {
            busName = PokeBusApplicationClass.getContext().getString(R.string.no_incoming_buses);
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

            if (time.contains(":")) {
                allbusNamesAndDistances += busNamec + ": " + time + " AM\n";

            } else {
                allbusNamesAndDistances += busNamec + ": " + time + "min\n";

            }
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
        if (MarkerManager.getInstance().getFavoriteBusStops().size() > 0) {
            boolean hasFavBusStopBeenDisplayed = showFavBusStopSnippet();
            if (!hasFavBusStopBeenDisplayed) {
                displayClosestMarker(closestMarkerToUser);
            }
        } else {
            //if no fav buses we need a snippet to open
            displayClosestMarker(closestMarkerToUser);
        }
    }

    private void displayClosestMarker(String closestMarkerToUser) {
        Marker marker = MarkerManager.getInstance().getMarkerHashTable().get(closestMarkerToUser);
        displayMarker(marker);
    }

    private void openAfterItsBeenOpenedSnippet(String lastOpenSnippetKey, String closestMarkerToUser) {
        //on first open is different(maybe) after you have opened  another snippet
        Marker marker = MarkerManager.getInstance().getMarkerHashTable().get(lastOpenSnippetKey);
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
            marker = MarkerManager.getInstance().getMarkerHashTable().get(closestMarkerToUser);
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

    private boolean showFavBusStopSnippet() {
        for (int i = 0; i < MarkerManager.getInstance().getFavoriteBusStops().size(); i++) {
            Marker marker = MarkerManager.getInstance().getMarkerHashTable()
                    .get(MarkerManager.getInstance().getFavoriteBusStops().get(i));
            if (marker != null) {
                displayMarker(marker);
                return true;
            }
        }
        return false;
    }

}
