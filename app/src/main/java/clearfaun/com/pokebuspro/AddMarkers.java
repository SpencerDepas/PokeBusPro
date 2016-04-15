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


//    public void addMarkersToMap(final ArrayList<BusInfo> busInfo) {
//
//
//        overlappingMarkersIndex = new ArrayList<String>();
//
//        markerLocation = new LatLng[busInfo.size()];
//        marker = new Marker[busInfo.size()];
//        Log.i("AddMarkers", "markerLocation: " + markerLocation.length );
//        Log.i("AddMarkers", "marker " + marker.length );
//        Log.i("AddMarkers", "pointList " + MapsActivity.pointList.size() );
//
//        Log.i("AddMarkers", "befire int i = 0; i < busInfo.size() " );
//        Log.i("AddMarkers", "busInfo.size() " + busInfo.size() );
//
//
//
//
//        for (int i = 0; i < busInfo.size(); i++) {
//            Log.i("AddMarkers", "in befire int i = 0; i < busInfo.size()");
//
//            markerLocation[i] = new LatLng(busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
//            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions().position(markerLocation[i]));
//            marker[i].setTitle(busInfo.get(i).getBusCode());
//            //marker[i].setTitle(busInfo.get(i).getBusName());
//
//
//
//
//            MapsActivity.mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick(Marker Pin) {
//                    //busInfo.get(PopupAdapterForMapMarkers.currentPopUpIndex).setMarkerSet(false);
//                    popupListner.displayDialog(Pin.getTitle(), Pin.getId());
//
//
//                    dialogOpon = true;
////                    MapsActivity.popupForPokebus(MapsActivity.optionsButton, Pin.getTitle(), Pin.getId());
//
//
//                }
//            });
//            Log.i("AddMarkers", "addMarkersToMap() " + busInfo.get(i).getBusCode() + " i is " + i);
//
//
//            //distance
//            if (busInfo.get(i).getDistance()[1].equals("Not available")) {
//
//                marker[i].setSnippet(busInfo.get(i).getDistance()[0]);
//
//            } else if (busInfo.get(i).getDistance()[2].equals("Not available")) {
//
//                marker[i].setSnippet(busInfo.get(i).getDistance()[0]
//                        + "\n" + busInfo.get(i).getDistance()[1]);
//
//            } else {
//
//                marker[i].setSnippet(busInfo.get(i).getDistance()[0]
//                        + "\n" + busInfo.get(i).getDistance()[1]
//                        + "\n" + busInfo.get(i).getDistance()[2]);
//            }
//
//
//            marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
//            //marker[i].setIcon(BitmapDescriptorFactory.defaultMarker((float)355));
//            Log.i("AddMarkers", "pre color poke bus : ");
//
//
//            Log.i("AddMarkers", "addMarkersToMap() GetBusStopJSON.busInfo[i].getDistance() : " + busInfo.get(i).getDistance()[0]);
//
//
//            //this is for saving latlng for onRoatate
//            MapsActivity.pointList.add(markerLocation[i]);
//
//
//
//
//
//        }
//
//        addPokeBusColor();
//        //to find closest marker to you
//
//        openClosestSnippet(busInfo);
//
//        Log.i("AddMarkers", "  Remove spinner ");
//        Log.i("AddMarkers", "  MapsActivity.spinner.getVisibility(): " + MapsActivity.spinner.getVisibility());
//
//
//
//        if(MapsActivity.spinner.getVisibility() == View.VISIBLE){
//            MapsActivity.spinner.setVisibility(View.INVISIBLE);
//        }
//
//        Log.i("AddMarkers", "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " );
//    }

    LatLng markerLocation;
    //Marker marker;

    //private Hashtable<String, Marker> markerHashTable;

    public void addMarkerToMapWithBusDistances(DistancesExample distancesExample, String busCode,  LatLng busStopLatLng) {

        Log.i("AddMarkers", "addMarkerToMapWithBusDistances" );




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
        Marker marker;

        String hash = busCode + busName;
        if(markerHashTable.containsKey(hash)){
            //use old
            marker = markerHashTable.get(hash);
        }else{

            //make new
            markerLocation = new LatLng(busStopLatLng.latitude, busStopLatLng.longitude);
            marker = MapsActivity.mMap.addMarker(new MarkerOptions().position(markerLocation));
            marker.setTitle(busCode + busName);

            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
        }




        addDistancesToMarkers(distancesExample, marker);


        Log.i("AddMarkers", "markerHashTable size : " + markerHashTable.size());

        //marker[i].setTitle(busInfo.get(i).getBusName());




        markerHashTable.put(hash, marker);

        final String fBusCode = busCode;

        MapsActivity.mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker Pin) {
                //busInfo.get(PopupAdapterForMapMarkers.currentPopUpIndex).setMarkerSet(false);
                popupListner.displayDialog(Pin.getTitle(), Pin.getId(), fBusCode);


                dialogOpon = true;
//                    MapsActivity.popupForPokebus(MapsActivity.optionsButton, Pin.getTitle(), Pin.getId());


            }
        });


        marker.hideInfoWindow();
        marker.showInfoWindow();


        Log.i("AddMarkers", "  MapsActivity.spinner.getVisibility(): " + MapsActivity.spinner.getVisibility());



        if(MapsActivity.spinner.getVisibility() == View.VISIBLE){
            MapsActivity.spinner.setVisibility(View.INVISIBLE);
        }

        Log.i("AddMarkers", "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " );
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

    static int pokeBusMarkerIndex;
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
