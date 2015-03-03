package clearfaun.com.pokebuspro;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.PopupMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

    Timer timer;
    TimerTask timerTask;
    LatLng currentLocation;
    /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.i("MyMapsActivity", "onCreate");

        mContext = getApplicationContext();


        Location location = getLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        currentLocation = new LatLng(latitude, longitude);


        setUpMapIfNeeded();



        getBusStops(busInfo);
        Log.i("MyMapsActivity", "after getBusStops(busInfo);: " );


        getBusDistance(busInfo);
        Log.i("MyMapsActivity", "after while (!getBusDistance(busInfo)); " );

        Log.i("MyMapsActivity", "after busDistance  addDistance distance : " + busInfo.get(0).getDistance() );
        Log.i("MyMapsActivity", "after busDistance addDistance size() : " + busInfo.size() );



        updateBusDistance();




        Button b = (Button) findViewById(R.id.options_button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getBaseContext(), v);

                popup.getMenuInflater().inflate(R.menu.my_menu, popup.getMenu());



                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                /** Showing the popup menu */
                popup.show();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity","onResume()");
        setUpMapIfNeeded();
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
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                mMap.setMyLocationEnabled(true);


                //busInfo = new BusInfo[5];

                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)    // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));


            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */



    public Location getLocation() {

        Log.i("MyMapsActivity","getLocation()");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.getBestProvider(criteria,true);

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


}
