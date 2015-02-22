package clearfaun.com.pokebuspro;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    static double latitude;
    static double longitude;
    static Context mContext;
    static HandleJSON obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.i("MyMapsActivity", "onCreate");

        mContext = getApplicationContext();


        Location location = getLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        LatLng currentLocation = new LatLng(latitude, longitude);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);


        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLocation)    // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        open();

        AddMarkers.addMarkersToMap();

    }





    public static void open(){

        Log.i("MyMapsActivity", "inside open");


        obj = new HandleJSON();
        obj.fetchJSON();

        Log.i("MyMapsActivity", "before while");
        while(obj.parsingComplete);

    /*    busName.setText(HandleJSON.busInfo[1].getBusId());
        stopCode.setText(HandleJSON.busInfo[1].getBusCode());
        lat.setText(HandleJSON.busInfo[1].getBusStopLatString());
        lng.setText(HandleJSON.busInfo[1].getBusStopLngString());*/
        Log.i("MyMapsActivity", "after while");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity","onResume()");
        setUpMapIfNeeded();
    }



    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }


    public Location getLocation() {

        Log.i("MyMapsActivity","getLocation()");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.getBestProvider(criteria,true);

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


}
