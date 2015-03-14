package clearfaun.com.pokebuspro;


import android.util.Log;
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



        for (int i = 0; i < busInfo.size() ; i++) {


            markerLocation[i] = new LatLng(busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));
            marker[i].setTitle(busInfo.get(i).getBusCode());
            Log.i("AddMarkers", "addMarkersToMap() " + busInfo.get(i).getBusCode() + " i is " + i);






            //distance
            if(busInfo.get(i).getDistance()[1].equals("Not available")){

                marker[i].setSnippet(busInfo.get(i).getDistance()[0]);

            }else if (busInfo.get(i).getDistance()[2].equals("Not available")){

                marker[i].setSnippet(busInfo.get(i).getDistance()[0]
                        + "\n" + busInfo.get(i).getDistance()[1]);

            }else{

                marker[i].setSnippet(busInfo.get(i).getDistance()[0]
                        + "\n" + busInfo.get(i).getDistance()[1]
                        + "\n" + busInfo.get(i).getDistance()[2]);
            }








            marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.smallbuspost));
            Log.i("MyAddMarkers", "addMarkersToMap() GetBusStopJSON.busInfo[i].getDistance() : " + busInfo.get(i).getDistance()[0]);


            //this is for saving latlng for onRoatate
            MapsActivity.pointList.add(markerLocation[i]);
            
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
        Log.i("MyAddMarkers", " after updateMarkersToMap : " + busInfo.get(0).getDistance()[0]);

    }


}
