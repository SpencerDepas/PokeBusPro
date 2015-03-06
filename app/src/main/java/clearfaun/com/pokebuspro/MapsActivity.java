package clearfaun.com.pokebuspro;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends FragmentActivity {

    static GoogleMap mMap; // Might be null if Google Play services APK is not available.



    static double latitude;
    static double longitude;
    static Context mContext;
    static GetBusStopJSON obj;
    static GetBusDistanceJSON objTwo;

    static ArrayList<BusInfo> busInfo = new ArrayList<>();
    static ArrayList<LatLng> pointList = new ArrayList<>();

    Marker pokeBusMarker;
    Timer timer;
    TimerTask timerTask;
    LatLng currentLocation;
    /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/
    SharedPreferences.Editor editor;
    static SharedPreferences prefs;

    static public String API_KEY_MTA ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.i("MyMapsActivity", "onCreate");

        mContext = getApplicationContext();

        API_KEY_MTA = getString(R.string.API_KEY_MTA);



        Location location = getLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        currentLocation = new LatLng(latitude, longitude);



        Button b = (Button) findViewById(R.id.options_button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu pokeBusMovableMarker = new PopupMenu(getBaseContext(), v);

                pokeBusMovableMarker.getMenuInflater().inflate(R.menu.my_menu, pokeBusMovableMarker.getMenu());



                pokeBusMovableMarker.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.i("MyMapsActivity", "onMenuItemClick");


                        Log.i("MyMapsActivity", "onMenuItemClick item.getTitle() : " + item.getTitle());
                        if(item.getTitle().toString().equals("Add a BusPoke!")) {


                            pokeBusMarker = mMap.addMarker(new MarkerOptions()
                                    .title("PokeBus")
                                    .position(currentLocation)
                                    .draggable(true));
                            Log.i("MyMapsActivity", "onMenuItemClick marker created");

                            Toast.makeText(getBaseContext(), "Put the circle over desired stop", Toast.LENGTH_SHORT).show();
                        }else if(item.getTitle().toString().equals("What is saved PokeBus")){

                            prefs = getSharedPreferences(
                                    "pokeBusCodePrefs", Context.MODE_PRIVATE);
                            String pokeBusCode = prefs.getString("pokeBusCode", null);

                            if (pokeBusCode != null){

                                Toast.makeText(getBaseContext(), "Saved PokeBus is " + pokeBusCode, Toast.LENGTH_SHORT).show();

                            }

                        }else {

                            Toast.makeText(getBaseContext(), "A PokeBus has not yet been saved", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });


                pokeBusMovableMarker.show();
            }
        });



    }





    public static void getBusStops(ArrayList<BusInfo> busInfo){
            Log.i("MyMapsActivity", "inside getBusStops");
            obj = new GetBusStopJSON();
            obj.fetchBusStop(busInfo);

        Log.i("MyMapsActivity", "before while");
        while(obj.parsingComplete);

        Log.i("MyMapsActivity", "after while while(obj.parsingComplete);    busInfo.size():" + busInfo.size());


    }



    public static void getBusDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyMapsActivity", "getBusDistance");
        objTwo = new GetBusDistanceJSON();
        objTwo.fetchBusDistanceJson(busInfo);

    }

    public void onPause() {
        super.onPause();
        Log.i("MyMapsActivity","onPause()");
        busInfo.clear();
        AddMarkers.marker = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity","onResume()");




        setUpMapIfNeeded();

        getBusStops(busInfo);
        Log.i("MyMapsActivity", "after getBusStops(busInfo);: " );

        getBusDistance(busInfo);
        Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

        //updateBusDistance();
        Log.i("MyMapsActivity", "after updateBusDistance();");
    }



    public void updateBusDistance() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 20000); //
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("MyMapsActivity","initializeTimerTask    timerTask");
                getBusDistance(busInfo);
            }
        };
    }




    private void setUpMapIfNeeded() {
        Log.i("MyMapsActivity","setUpMapIfNeeded() ");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            Log.i("MyMapsActivity","setUpMapIfNeeded()  mMap == null ");
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {


                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        Log.i("MyMapsActivity","onMarkerDragStart ");
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        Log.i("MyMapsActivity","onMarkerDrag ");
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        //only one dragable marker, to set a  pokebus

                        Log.i("MyMapsActivity", "onMarkerDragEnd ");
                        if (marker.getTitle().equals("PokeBus")) {

                            for(int i = 0 ; i < AddMarkers.marker.length; i ++ ){
                                //static double testLat = 40.6455520;
                                int movableMarkerLat =  (int)(marker.getPosition().latitude * 1000000);
                                int movableMarkerLng = (int)(marker.getPosition().longitude * 1000000);

                                int busStopMarkerLat = (int)(busInfo.get(i).getBusStopLat()* 1000000);
                                int busStopMarkerLng = (int)(busInfo.get(i).getBusStopLng()  * 1000000);
                                Log.i("MyMapsActivityMarker", "busInfo.get(i).getBusStopLat()  " + busInfo.get(i).getBusStopLat());
                                Log.i("MyMapsActivityMarker", "tempLat " + movableMarkerLat);
                                Log.i("MyMapsActivityMarker", "tempLng " + movableMarkerLng);

                                Log.i("MyMapsActivityMarker", "busStopMarkerLat " + busStopMarkerLat);
                                Log.i("MyMapsActivityMarker", "busStopMarkerLng " + busStopMarkerLng);

                                Log.i("MyMapsActivityMarker", " busStopMarkerLat - busStopMarkerLng " + (busStopMarkerLat - busStopMarkerLng));

                                Log.i("MyMapsActivityMarker", "AddMarkers.marker[i].getId(); " + AddMarkers.marker[i].getId());

                                double distance = distFrom(marker.getPosition().latitude,marker.getPosition().longitude , busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng() );
                                Log.i("MyMapsActivityMarker", "distance    " + distance);
                                Log.i("MyMapsActivityMarker", "distance  (int)  " + (int) distance);
                                Log.i("MyMapsActivityMarker", "distance  (int) *10 " + (int) (distance *10) );

                                //activate within a radius of 10 meeters
                                if((int)distance < 10){

                                    Log.i("MyMapsActivityMarker", "distance < 6000000. BusCode :" + busInfo.get(i).getBusCode() );

                                    Toast.makeText(getBaseContext(), "You have activated a PokeBus on Bus " + busInfo.get(i).getBusCode(), Toast.LENGTH_SHORT).show();

                                    marker.setVisible(false);
                                    setPokeBus(busInfo.get(i).getBusCode());
                                    Log.i("MyMapsActivityMarker", "AddMarkers.marker[i].getId(); " + AddMarkers.marker[i].getId());

                                }


                            }


                        }



                    }
                });


                Log.i("MyMapsActivity","setUpMapIfNeeded()  mMap != null ");
                mMap.setMyLocationEnabled(true);
                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)    // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            }
        }else{
            Log.i("MyMapsActivity","setUpMapIfNeeded()  in the else ");
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)    // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }

    private void setPokeBus(String busCode) {
        Log.i("MyMapsActivity","setPokeBus()");
        Log.i("MyMapsActivity","busCode()" + busCode);

        prefs = getSharedPreferences("pokeBusCodePrefs",
                Context.CONTEXT_IGNORE_SECURITY);
        editor = prefs.edit();
        editor.putString("pokeBusCode", busCode);
        editor.apply();



    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    private Location getLocation() {

        Log.i("MyMapsActivity","getLocation()");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.getBestProvider(criteria,true);

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


    static void toaster(String string){
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_LONG);
        toast.show();
    }

    static void toasterShort(String string){
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_SHORT);
        toast.show();
    }

}
