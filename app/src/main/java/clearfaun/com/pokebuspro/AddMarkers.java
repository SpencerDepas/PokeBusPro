package clearfaun.com.pokebuspro;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by spencer on 2/21/2015.
 */
public class AddMarkers {

    public static void addMarkersToMap() {

        LatLng[] markerLocation = new LatLng[HandleJSON.busInfo.length];
        Marker[] marker = new Marker[HandleJSON.busInfo.length];

        Log.i("MyAddMarkers", "addMarkersToMap() HandleJSON.busInfo.length : " + HandleJSON.busInfo.length);
    /*    for(int i = 0; i < HandleJSON.busInfo.length; i ++){

            markerLocation[0] = new LatLng(HandleJSON.busInfo[0].getBusStopLat(), HandleJSON.busInfo[0].getBusStopLng());
            marker[0] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[0]));
            marker[0].setTitle(HandleJSON.busInfo[0].getBusId());


        }*/



        Marker markerTwo = MapsActivity.mMap.addMarker(
                new MarkerOptions().position(new LatLng(HandleJSON.busInfo[3].getBusStopLat(), HandleJSON.busInfo[3].getBusStopLng())));
                markerTwo.setVisible(true);

    }



}
