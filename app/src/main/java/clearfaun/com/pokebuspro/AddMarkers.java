package clearfaun.com.pokebuspro;


import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    static LatLng[] markerLocation;
    static Marker[] marker;

    public static void addMarkersToMap(ArrayList<BusInfo> busInfo) {


        markerLocation = new LatLng[busInfo.size()];
        marker = new Marker[busInfo.size()];
        Log.i("AddMarkers", "markerLocation: " + markerLocation.length );
        Log.i("AddMarkers", "marker " + marker.length );
        Log.i("AddMarkers", "pointList " + MapsActivity.pointList.size() );

        Log.i("AddMarkers", "befire int i = 0; i < busInfo.size() " );
        Log.i("AddMarkers", "busInfo.size() " + busInfo.size() );




        for (int i = 0; i < busInfo.size(); i++) {
            Log.i("AddMarkers", "in befire int i = 0; i < busInfo.size()");

            markerLocation[i] = new LatLng(busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));
            marker[i].setTitle(busInfo.get(i).getBusCode());
            Log.i("AddMarkers", "addMarkersToMap() " + busInfo.get(i).getBusCode() + " i is " + i);


            //distance
            if (busInfo.get(i).getDistance()[1].equals("Not available")) {

                marker[i].setSnippet(busInfo.get(i).getDistance()[0]);

            } else if (busInfo.get(i).getDistance()[2].equals("Not available")) {

                marker[i].setSnippet(busInfo.get(i).getDistance()[0]
                        + "\n" + busInfo.get(i).getDistance()[1]);

            } else {

                marker[i].setSnippet(busInfo.get(i).getDistance()[0]
                        + "\n" + busInfo.get(i).getDistance()[1]
                        + "\n" + busInfo.get(i).getDistance()[2]);
            }

            marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blueba));
            Log.i("AddMarkers", "pre color poke bus : ");









            Log.i("AddMarkers", "addMarkersToMap() GetBusStopJSON.busInfo[i].getDistance() : " + busInfo.get(i).getDistance()[0]);


            //this is for saving latlng for onRoatate
            MapsActivity.pointList.add(markerLocation[i]);

        }

        addPokeBusColor();
        //to find closest marker to you
        int busInfoIndex = 0;
        double closestSnippet = 0;

        //first onpons the closet snippet
        for(int i = 0 ; i < busInfo.size(); i ++){
            if(closestSnippet == 0 || closestSnippet > MapsActivity.distFrom(MapsActivity.latLng.latitude, MapsActivity.latLng.longitude,
                    busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng())){

                closestSnippet = MapsActivity.distFrom(MapsActivity.latLng.latitude, MapsActivity.latLng.longitude,
                        busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
                busInfoIndex = i;

            }
        }

        //if pokebus is in ranneg then opons pokebus snippet
        for(int i = 0 ; i < busInfo.size(); i ++){

            for(int z = 0 ; z < MapsActivity.listPokeBusCode.size(); z ++){

                if(busInfo.get(i).getBusCode().equals(MapsActivity.listPokeBusCode.get(z) +"")){
                    //if a poke bus is in range then it will opon in stead of the closest bus stop
                    busInfoIndex = i;
                    break;
                }

            }
        }

        marker[busInfoIndex].showInfoWindow();

        Log.i("AddMarkers", "  Remove spinner " );
        MapsActivity.spinner.setVisibility(View.INVISIBLE);

        Log.i("AddMarkers", "  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " );
    }

    public static void addPokeBusColor(){
        if(MapsActivity.listPokeBusCode != null){

            for(int i = 0 ; i < MapsActivity.busInfo.size(); i ++) {
                for (int q = 0; q < MapsActivity.listPokeBusCode.size(); q++) {

                    if (MapsActivity.busInfo.get(i).getBusCode().equals(MapsActivity.listPokeBusCode.get(q) + "")) {
                        //to distinqush a pokebus
                        marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blueab));
                    }
                }
            }
        }
    }

    public static void removePokeBusColor(){
        if(MapsActivity.listPokeBusCode != null){

            for(int i = 0 ; i < MapsActivity.busInfo.size(); i ++) {
                marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blueba));
            }
        }
    }

    public static void updateMarkersToMap(ArrayList<BusInfo> busInfo) {




        for (int i = 0; i < busInfo.size() && i < marker.length; i++) {
            Log.i("MyAddMarkers", "updateMarkersToMap :  marker[i].getSnippet(): " + marker[i].getSnippet());
            Log.i("MyAddMarkers", "updateMarkersToMap :   marker.length: " + marker.length);
            Log.i("MyAddMarkers", "updateMarkersToMap :  i < busInfo.size(): " + busInfo.size());
            Log.i("MyAddMarkers", "updateMarkersToMap :  i  " + i);


            marker[i].setSnippet(busInfo.get(i).getDistance()[0]
                        + "\n" + busInfo.get(i).getDistance()[1]
                        + "\n" + busInfo.get(i).getDistance()[2]
                    );

            if(marker[i].isInfoWindowShown()) {
                marker[i].hideInfoWindow();
                marker[i].showInfoWindow();
            }

        }
        //+ "\n" + busInfo.get(i).getDistance()[2])
        //Log.i("MyAddMarkers", " after updateMarkersToMap : " + busInfo.get(0).getDistance()[0]);
        Log.i("MyAddMarkers", "updateMarkersToMap  DOIBNEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " );
    }


}
