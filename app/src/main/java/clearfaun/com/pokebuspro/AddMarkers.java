package clearfaun.com.pokebuspro;


import android.app.AlertDialog;
import android.content.DialogInterface;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Hashtable;

import POJO.DistancesExample;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

//    static LatLng[] markerLocation;
//    static Marker[] marker;
    static boolean dialogOpon = false;
    static ArrayList<String> overlappingMarkersIndex;

    DialogPopupListner popupListner = null;

    public AddMarkers(){

    }


    public void addMarkerToMapWithBusDistances(DistancesExample distancesExample, String busCode,  LatLng busStopLatLng) {

        Log.i("AddMarkers", "addMarkerToMapWithBusDistances" );



        String allbusNamesAndDistances = putBusStopsInStackedString(distancesExample);


        MarkerManager markerManager = MarkerManager.getInstance();

        Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();






        String busName = distancesExample.getSiri().getServiceDelivery().getStopMonitoringDelivery().get(0)
                .getMonitoredStopVisit()
                .get(0)
                .getMonitoredVehicleJourney()
                .getLineRef();



        int indexOfChar = busName.indexOf('_') + 1;
        busName = busName.substring(indexOfChar);

        Log.i("AddMarkers", "busName : " + busName);

        LatLng markerLocation;



        Marker marker = markerHashTable.get(busCode);
        if(marker != null){
            Log.i("AddMarkers", "markerHashTable.containsKey(hash) use old marker : " + busName);
            //use old
            //we do not need to re add it to the screen
            //we do need to delete the old info

            marker.setTitle(busCode + busName);
            marker.setSnippet(allbusNamesAndDistances);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));


        }else{
            Log.i("AddMarkers", "markerHashTable.containsKey(hash) make new marker  : " + busName);
            //make new
            markerLocation = new LatLng(busStopLatLng.latitude, busStopLatLng.longitude);
            marker = MapsActivity.googleMap.addMarker(new MarkerOptions().position(markerLocation));
            marker.setTitle(busCode + busName);
            marker.setSnippet(allbusNamesAndDistances);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
        }




        marker.showInfoWindow();
        marker.hideInfoWindow();


        markerHashTable.put(busCode, marker);



        final String fBusCode = busCode;
        MapsActivity.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker Pin) {

                popupListner.displayDialog(Pin.getTitle(), Pin.getId(), fBusCode);
                dialogOpon = true;

            }
        });



        if(MapsActivity.spinner.getVisibility() == View.VISIBLE){
            MapsActivity.spinner.setVisibility(View.INVISIBLE);
        }

        Log.i("AddMarkers", "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " );
    }

    private String putBusStopsInStackedString(DistancesExample distancesExample){
        Log.i("AddMarkers", "putBusStopsInStackedString" );


        int busNameL = distancesExample.getSiri().getServiceDelivery().getStopMonitoringDelivery().get(0)
                .getMonitoredStopVisit()
                .size();

        String allbusNamesAndDistances = "";

        long startTime = System.nanoTime();
        for(int i = 0; i < busNameL; i ++){



            String busNamec = distancesExample.getSiri().getServiceDelivery().getStopMonitoringDelivery().get(0)
                    .getMonitoredStopVisit()
                    .get(i)
                    .getMonitoredVehicleJourney()
                    .getLineRef();


            int indexOfChar = busNamec.indexOf('_') + 1;
            busNamec = busNamec.substring(indexOfChar);


            Log.i("AddMarkerss", "busNameL : "  + busNameL );
            Log.i("AddMarkerss", "busNamec : "  + busNamec );

            Log.i("AddMarkerss", "i : "  + i );


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
            Log.i("AddMarkerss", "distance : "  + distance );
        }


        // Log.i("AddMarkerss", "allbusNamesAndDistances : "  + allbusNamesAndDistances  + " buscode : " + busCode);


        long estimatedTime = System.nanoTime() - startTime;

        Log.i("AddMarkerss", "estimatedTime : " +   estimatedTime);

        return allbusNamesAndDistances;
    }

    public void addDistancesToMarkers(DistancesExample distancesExample, Marker marker ){
        Log.i("AddMarkers", "  addDistancesToMarkers " );

        Log.i("AddMarkers", "  distence length : " +  distancesExample.getSiri().getServiceDelivery()
                .getStopMonitoringDelivery()
                .get(0)
                .getMonitoredStopVisit()
        .size());


        if(distancesExample.getSiri().getServiceDelivery()
                .getStopMonitoringDelivery()
                .get(0)
                .getMonitoredStopVisit()
                .size() > 0){
            //make sure we have a distance



            //distance

            if(distancesExample.getSiri().getServiceDelivery()
                    .getStopMonitoringDelivery()
                    .get(0)
                    .getMonitoredStopVisit()
                    .size() == 1){
                //returns one distances
                marker.setSnippet(distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery().get(0)
                        .getMonitoredStopVisit()
                        .get(0)
                        .getMonitoredVehicleJourney()
                        .getMonitoredCall()
                        .getExtensions()
                        .getDistances()
                        .getPresentableDistance()
                );


            }else if (distancesExample.getSiri().getServiceDelivery()
                    .getStopMonitoringDelivery()
                    .get(0)
                    .getMonitoredStopVisit()
                    .size() == 2){

                //returns two distances

                marker.setSnippet(distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery().get(0)
                        .getMonitoredStopVisit()
                        .get(0)
                        .getMonitoredVehicleJourney()
                        .getMonitoredCall()
                        .getExtensions()
                        .getDistances()
                        .getPresentableDistance()

                        + "\n" + distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery().get(0)
                        .getMonitoredStopVisit()
                        .get(1)
                        .getMonitoredVehicleJourney()
                        .getMonitoredCall()
                        .getExtensions()
                        .getDistances()
                        .getPresentableDistance()
                );

            } else {
                //returns three distances
                marker.setSnippet(distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery().get(0)
                        .getMonitoredStopVisit()
                        .get(0)
                        .getMonitoredVehicleJourney()
                        .getMonitoredCall()
                        .getExtensions()
                        .getDistances()
                        .getPresentableDistance()

                        + "\n" + distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery().get(0)
                        .getMonitoredStopVisit()
                        .get(1)
                        .getMonitoredVehicleJourney()
                        .getMonitoredCall()
                        .getExtensions()
                        .getDistances()
                        .getPresentableDistance()

                        + "\n" + distancesExample.getSiri().getServiceDelivery()
                        .getStopMonitoringDelivery().get(0)
                        .getMonitoredStopVisit()
                        .get(2)
                        .getMonitoredVehicleJourney()
                        .getMonitoredCall()
                        .getExtensions()
                        .getDistances()
                        .getPresentableDistance()
                );
            }

        }else{
            marker.setSnippet("Not available");
        }

    }

    static String lastOpenSnippet;

    public static void whatSnippetIsOpen(){
        Log.i("AddMarkers", "  whatSnippetIsOpen() ");

//        lastOpenSnippet = null;
//        if(marker != null) {
//            Log.i("AddMarkers", "  AddMarkers.marker() length" + AddMarkers.marker.length );
//            for (Marker marker : AddMarkers.marker) {
//
//                if (marker.isInfoWindowShown()) {
//                    lastOpenSnippet = marker.getTitle();
//                    Log.i("AddMarkers", "  lastOpenSnippet " + lastOpenSnippet);
//                    break;
//                }
//            }
//        }

    }

    static boolean fromOpenSnippetWithIndex = false;

    public static void openSnippetWithIndex(int index ){
//        Log.i("AddMarkersa", "  MapsActivity.busInfoIndexForBusName  " + index);
//        Log.i("AddMarkersa", "  openSnippetWithIndex id " + marker[index].getId());
//
//        Log.i("AddMarkersa", "  openSnippetWithIndex  tittle" + marker[index].getTitle());
//        Log.i("AddMarkersa", "  openSnippetWithIndex  snippet" + marker[index].getSnippet());
//
//        fromOpenSnippetWithIndex = true;
//        marker[index].hideInfoWindow();
//        marker[index].showInfoWindow();



    }


    public static void openClosestSnippet(String testParam){
        //this should open
        //1 last open snippet
        //2 closest pokebus
        //3 closet snippet




        //marker[busInfoIndex].showInfoWindow();
        //busInfo.get(busInfoIndex).setAddedToPopup(true);

    }
    static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        Log.i("AddMarkerstz", " dLat = " + lat1);
        Log.i("AddMarkerstz", " dLng = " + lng1);
        Log.i("AddMarkerstz", " dLat = " + lat2);
        Log.i("AddMarkerstz", " dLng = " + lng2);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        Log.i("AddMarkerstz", " dist = " + dist);
        return dist;
    }


    public static void addPokeBusColor(){

    }

    public static void removePokeBusColor() {

//        for (int i = 0; i < MapsActivity.busInfo.size(); i++) {
//            //marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blueba));
//            marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
//        }

    }

//    public static void updateMarkersToMap(DistancesExample distancesExample) {
//
//        if(MapsActivity.spinner.getVisibility() == View.INVISIBLE){
//            MapsActivity.spinner.setVisibility(View.VISIBLE);
//        }
//
//
//        //remove loading
//
//
//
//        Log.i("MyAddMarkers", "updateMarkersToMap :  marker[i].getId(): " + marker[busIndexFormarker].getId());
//        Log.i("MyAddMarkers", "updateMarkersToMap :  marker[i].getSnippet(): " + marker[busIndexFormarker].getSnippet());
//        Log.i("MyAddMarkers", "updateMarkersToMap :   marker.length: " + marker.length);
//        Log.i("MyAddMarkers", "updateMarkersToMap :  getBusName " + busInfo.getBusName());
//        Log.i("MyAddMarkers", "updateMarkersToMap :  getBusCode " + busInfo.getBusCode());
//        Log.i("MyAddMarkers", "updateMarkersToMap :  i  " + busIndexFormarker);
//        Log.i("MyAddMarkerss", "marker[i].getId() " + marker[busIndexFormarker].getId());
//        Log.i("MyAddMarkerss", " marker[i].getTitle()" + marker[busIndexFormarker].getTitle());
//
//        marker[busIndexFormarker].setSnippet(busInfo.getDistance()[0]
//                + "\n" + busInfo.getDistance()[1]
//                + "\n" + busInfo.getDistance()[2]
//        );
//
//        if(marker[busIndexFormarker].isInfoWindowShown() && marker[busIndexFormarker].getTitle().equals(MapsActivity.busInfo.get(busIndexFormarker).getBusCode())) {
//            Log.i("MyAddMarkerss", "marker[i].getId() " + marker[busIndexFormarker].getId());
//            Log.i("MyAddMarkerss", " marker[i].getTitle()" + marker[busIndexFormarker].getTitle());
//            busInfo.setAddedToPopup(false);
//            marker[busIndexFormarker].hideInfoWindow();
//            marker[busIndexFormarker].showInfoWindow();
//        }
//
//
//
//
//
//
//        if(MapsActivity.spinner.getVisibility() == View.VISIBLE){
//            MapsActivity.spinner.setVisibility(View.INVISIBLE);
//        }
//        //+ "\n" + busInfo.get(i).getDistance()[2])
//        //Log.i("MyAddMarkers", " after updateMarkersToMap : " + busInfo.get(0).getDistance()[0]);
//
//
//        Log.i("MyAddMarkerst", "updateMarkersToMap  marker[pokeBusMarkerIndex].getSnippet() " + marker[pokeBusMarkerIndex].getSnippet() );
//
//
//
//
//        Log.i("MyAddMarkers", "updateMarkersToMap  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " );
//
//    }

    public static void updateMarkersToMap(String testParam) {


    }


}
