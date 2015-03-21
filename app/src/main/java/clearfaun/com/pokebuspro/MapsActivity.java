package clearfaun.com.pokebuspro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;




import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends FragmentActivity implements
        LocationProvider.LocationCallback  {

static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    static LatLng latLng;
    private LocationProvider mLocationProvider;
    static double latitude;
    static double longitude;
    static Context mContext;
    static GetBusStopJSON obj;
    static GetBusDistanceJSON objTwo;
    static String preferredMap;

    static ArrayList<BusInfo> busInfo = new ArrayList<>();
    static ArrayList<LatLng> pointList = new ArrayList<>();

    static Marker pokeBusMarker;
    static Timer timer;
    static TimerTask timerTask;
    static LatLng currentLocation;
    /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/
    SharedPreferences.Editor editor;
    static SharedPreferences prefs;
    static int pokeBusBusCode;

    static public String API_KEY_MTA ;
    PrefsFragment prefsFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    int firstBoot = 0;


    public static final String TAG = MapsActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_maps);


        Log.i("MyMapsActivity", "onCreate");

        mContext = getApplicationContext();

        API_KEY_MTA = getString(R.string.API_KEY_MTA);

        mLocationProvider = new LocationProvider(this, this);


        Log.i("MyMapsActivity", "after mLocationProvider.connect();");
        Log.i("MyMapsActivity", "latitude" + latitude);
        Log.i("MyMapsActivity", "longitude" + longitude);





        SharedPreferences pref = getSharedPreferences(
                "pokeBusCodePrefs", Context.MODE_PRIVATE);


        pokeBusBusCode = Integer.parseInt(pref.getString("pokeBusCode", "0"));



        ImageButton b = (ImageButton) findViewById(R.id.options_button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
        //settings

                fm = getFragmentManager();
                ft = fm.beginTransaction();

                prefsFragment = new PrefsFragment();
                ft.add(R.id.map, prefsFragment);
                ft.addToBackStack("TAG");
                ft.commit();


            }
        });

        ImageButton refreshLocation = (ImageButton) findViewById(R.id.refresh_location_button);
        refreshLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("MyMapsActivity", "onClick refreshLocation");
                //refresh button

                mLocationProvider.disconnect();
                mLocationProvider.connect();





            }
        });



    }

    public static void refreshMarkers(){
        Log.i("MyMapsActivity", "refreshMarkers");

        busInfo.clear();
        //stopTimerTask();
        AddMarkers.marker = null;
        mMap.clear();



        getBusStops(busInfo);
        Log.i("MyMapsActivity", "after getBusStops(busInfo) refresh " );
        Log.i("MyMapsActivity", "after busInfo " + busInfo.size());
        Log.i("MyMapsActivity", "after busInfo " +  busInfo.get(0).getBusCode());

        getBusDistance(busInfo);
        Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

        //updateBusDistance();
        Log.i("MyMapsActivity", "after updateBusDistance();");

    }

    public static void addPokeBusMarker(){

        if(pokeBusMarker == null) {
            pokeBusMarker = mMap.addMarker(new MarkerOptions()
                    .title("PokeBus")
                    .position(latLng)
                    .draggable(true));
            Log.i("MyMapsActivity", "onMenuItemClick marker created");
        }
    }



    @Override
    public void onBackPressed(){
        Log.i("MyMapsActivity", "onBackPressed");


        ft = fm.beginTransaction();
        ft.remove(prefsFragment);
        ft.commit();


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
        mLocationProvider.disconnect();
        //busInfo.clear();
        //stoptimertask(view);
        //AddMarkers.marker = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity","onResume()");

        mLocationProvider.connect();


        setUpMapIfNeeded();


        /*if(firstBoot != 0) {
            Log.i("MyMapsActivity", "firstBoot != 0");
            getBusStops(busInfo);
            Log.i("MyMapsActivity", "after getBusStops(busInfo);: ");

            getBusDistance(busInfo);
            Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

            //updateBusDistance();
            Log.i("MyMapsActivity", "after updateBusDistance();");
        }*/

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

                        double distance = distFrom(marker.getPosition().latitude,marker.getPosition()
                                .longitude , busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng() );

                        //activate within a radius of 10 meeters
                        if((int)distance < 10){

                            Log.i("MyMapsActivityMarker", "distance < 6000000. BusCode :" + busInfo.get(i).getBusCode() );

                            Toast.makeText(getBaseContext(), "PokeBus set to: " + busInfo.get(i).getBusCode(), Toast.LENGTH_SHORT).show();
                            marker.setVisible(false);
                            pokeBusMarker = null;
                            setPokeBus(busInfo.get(i).getBusCode(), busInfo.get(i).getBusName());
                            Log.i("MyMapsActivityMarker", "AddMarkers.marker[i].getId(); " + AddMarkers.marker[i].getId());

                        }
                    }
                }
            }
        });

    }



    public static void updateBusDistance() {
        Log.i("MyMapsActivity", "updateBusDistance()");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MapsActivity.mContext);
        String timeInMSString= sharedPrefs.getString(MapsActivity.mContext.getString(R.string.refresh_time_key), "20000");

        Log.i("MyMapsActivity", "timeInMSString()" + timeInMSString);
        int timeInMS = Integer.parseInt(timeInMSString) * 1000;
        Log.i("MyMapsActivity", "timeInMS()" + timeInMS);
        if(timeInMS != 0) {
            //set a new Timer
            timer = new Timer();
            //initialize the TimerTask's job
            initializeTimerTask();
            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms

            timer.schedule(timerTask, 5000, timeInMS);
        }
    }

    public static void stopTimerTask() {
        Log.i("MyMapsActivity", "stopTimerTask()" );
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("MyMapsActivity","initializeTimerTask    timerTask");
                getBusDistance(busInfo);
            }
        };
    }




    private void setUpMapIfNeeded() {
        Log.i("MyMapsActivity", "setUpMapIfNeeded() ");

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            Log.i("MyMapsActivity", "setUpMapIfNeeded()  mMap == null ");
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // Check if we were successful in obtaining the map.
            /*if (mMap != null && latLng != null) {

                Log.i("MyMapsActivity", "setUpMapIfNeeded()  mMap != null ");
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


            }*/

        }
    }


    private void setPokeBus(String busCode, String busName) {
        Log.i("MyMapsActivity","setPokeBus()");
        Log.i("MyMapsActivity","busCode()" + busCode);

        prefs = getSharedPreferences("pokeBusCodePrefs",
                Context.CONTEXT_IGNORE_SECURITY);
        editor = prefs.edit();
        editor.putString("pokeBusCode", busCode);
        editor.putString("pokeBusName", busName);
        editor.apply();

        pokeBusBusCode = Integer.parseInt(prefs.getString("pokeBusCode", "0"));

    }

    public static int getPokeBus(){
        return pokeBusBusCode;
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


    static void toaster(String string){
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_LONG);
        toast.show();
    }

    static void toasterShort(String string){
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_SHORT);
        toast.show();
    }



    @Override
    public void handleNewLocation(Location location) {

        Log.i("MyMapsActivity","handleNewLocation -----------");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);

        Log.i("MyMapsActivity","handleNewLocation ----------latitude " + latitude);
        //only want location this to run on first time. Not activate on every location update
        if(firstBoot == 0) {
            firstBoot++;
            mMap.setMyLocationEnabled(true);
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)    // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));

            getBusStops(busInfo);
            Log.i("MyMapsActivity", "after getBusStops(busInfo);: ");

            getBusDistance(busInfo);
            Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

            //updateBusDistance();
            Log.i("MyMapsActivity", "after updateBusDistance();");

        }

        mMap.setMyLocationEnabled(true);
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)    // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));

        refreshMarkers();
    }
}
