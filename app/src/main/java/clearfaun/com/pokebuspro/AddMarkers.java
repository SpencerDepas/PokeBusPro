package clearfaun.com.pokebuspro;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {



    public static void addMarkersToMap(BusInfo[] busInfo) {


        LatLng[] markerLocation = new LatLng[busInfo.length];
        Marker[] marker = new Marker[busInfo.length];

        Log.i("MyAddMarkers", "addMarkersToMap() HandleJSON.busInfo.length : " + busInfo.length);
        for(int i = 0; i < busInfo.length; i ++){

            markerLocation[i] = new LatLng(busInfo[i].getBusStopLat(), busInfo[i].getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));
            marker[i].setTitle(busInfo[i].getBusId());
            marker[i].setSnippet(busInfo[i].getDistance());
            Log.i("MyAddMarkers", "addMarkersToMap() GetBusStopJSON.busInfo[i].getDistance() : " + busInfo[i].getDistance());
        }





    }



}
