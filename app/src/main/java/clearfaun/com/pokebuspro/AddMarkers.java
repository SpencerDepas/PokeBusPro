package clearfaun.com.pokebuspro;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    static boolean markersAdded;
    static LatLng[] markerLocation;
    static Marker[] marker;

    public static void addMarkersToMap(ArrayList<BusInfo> busInfo) {

        markersAdded = true;

        markerLocation = new LatLng[busInfo.size()];
        marker = new Marker[busInfo.size()];

        Log.i("MyAddMarkers", "addMarkersToMap() HandleJSON.busInfo.length : " + busInfo.size());


        for (int i = 0; i < busInfo.size(); i++) {

            markerLocation[i] = new LatLng(busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));
            marker[i].setTitle(busInfo.get(i).getBusName());
            marker[i].setSnippet(busInfo.get(i).getDistance()[0] +
                    "\n" + busInfo.get(i).getDistance()[1]
                    );


            marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.smallbuspost));
            Log.i("MyAddMarkers", "addMarkersToMap() GetBusStopJSON.busInfo[i].getDistance() : " + busInfo.get(i).getDistance()[0]);

            
        }






    }


    public static void updateMarkersToMap(ArrayList<BusInfo> busInfo) {

        Log.i("MyAddMarkers", "updateMarkersToMap : " + busInfo.get(0).getDistance()[0]);


        for (int i = 0; i < busInfo.size(); i++) {
            Log.i("MyAddMarkers", "updateMarkersToMap :  marker[i].getSnippet(): " + marker[i].getSnippet());
            marker[i].setSnippet(busInfo.get(i).getDistance()[0] +
                    "\n" + busInfo.get(i).getDistance()[1]);
            ;
        }
        //+ "\n" + busInfo.get(i).getDistance()[2])
        Log.i("MyAddMarkers", " after updateMarkersToMap : " + busInfo.get(0).getDistance()[0]);

    }


}
