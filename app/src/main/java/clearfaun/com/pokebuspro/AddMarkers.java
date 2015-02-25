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



    public static void addMarkersToMap(ArrayList<BusInfo> busInfo) {


        LatLng[] markerLocation = new LatLng[busInfo.size()];
        Marker[] marker = new Marker[busInfo.size()];

        Log.i("MyAddMarkers", "addMarkersToMap() HandleJSON.busInfo.length : " + busInfo.size());
        for(int i = 0; i < busInfo.size(); i ++){

            markerLocation[i] = new LatLng(busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
            marker[i] = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(markerLocation[i]));
            marker[i].setTitle(busInfo.get(i).getBusName());
            marker[i].setSnippet(busInfo.get(i).getDistance()[0] +
                    "\n" + busInfo.get(i).getDistance()[1] +
                    "\n" + busInfo.get(i).getDistance()[2]);



            marker[i].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.smallbuspost));
            Log.i("MyAddMarkers", "addMarkersToMap() GetBusStopJSON.busInfo[i].getDistance() : " + busInfo.get(i).getDistance()[0]);
        }





    }



}
