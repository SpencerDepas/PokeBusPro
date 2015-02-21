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

        LatLng[] markerLocation = new LatLng[HandleJSON.busInfo.length];
        Marker[] marker = new Marker[HandleJSON.busInfo.length];

        Log.i("MyAddMarkers", "addMarkersToMap() HandleJSON.busInfo.length : " + HandleJSON.busInfo.length);
      /*  for(int i = 0; i < HandleJSON.busInfo.length; i ++){

            markerLocation[i] = new LatLng(HandleJSON.busInfo[i].getBusStopLat(), HandleJSON.busInfo[i].getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));

            marker[i].setTitle(HandleJSON.busInfo[i].busName);


        }*/


        //create marker


        LatLng MTA_301648LatLng1 = new LatLng(HandleJSON.busInfo[0].getBusStopLat(), HandleJSON.busInfo[0].getBusStopLng());
        Marker MTA_301648Marker1 = MapsActivity.mMap.addMarker(new MarkerOptions()
                .position(MTA_301648LatLng1));

        MTA_301648Marker1.setTitle(HandleJSON.busInfo[0].busName);
    }


}
