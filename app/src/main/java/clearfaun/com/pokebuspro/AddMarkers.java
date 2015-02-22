package clearfaun.com.pokebuspro;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    public static void addMarkersToMap() {

        LatLng[] markerLocation = new LatLng[GetBusStopJSON.busInfo.length];
        Marker[] marker = new Marker[GetBusStopJSON.busInfo.length];

        Log.i("MyAddMarkers", "addMarkersToMap() HandleJSON.busInfo.length : " + GetBusStopJSON.busInfo.length);
        for(int i = 0; i < GetBusStopJSON.busInfo.length; i ++){

            markerLocation[i] = new LatLng(GetBusStopJSON.busInfo[i].getBusStopLat(), GetBusStopJSON.busInfo[i].getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));
            marker[i].setTitle(GetBusStopJSON.busInfo[i].getBusId());


        }



       /* Marker markerTwo = MapsActivity.mMap.addMarker(
                new MarkerOptions().position(new LatLng(HandleJSON.busInfo[3].getBusStopLat(), HandleJSON.busInfo[3].getBusStopLng())));
                markerTwo.setVisible(true);*/

    }



}
