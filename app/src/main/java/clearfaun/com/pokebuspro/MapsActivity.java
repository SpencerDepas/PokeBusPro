package clearfaun.com.pokebuspro;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;


import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.internal.ge;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import io.fabric.sdk.android.Fabric;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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


    static ArrayList<BusInfo> busInfo = new ArrayList<>();
    static ArrayList<BusInfo> pokeBusbusInfo;
    static ArrayList<LatLng> pointList = new ArrayList<>();

    static Marker pokeBusMarker;
    static Timer timer;
    static TimerTask timerTask;

    /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/
    SharedPreferences.Editor editor;



    static public String API_KEY_MTA ;
    PrefsFragment prefsFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    int firstBoot = 0;
    static SharedPreferences prefs;

    public static final String TAG = MapsActivity.class.getSimpleName();
    static ProgressBarCircularIndeterminate spinner;
    static ImageButton optionsButton;
    static RelativeLayout back_dim_layout;
    //EMPIRE STATE BUILDING
    static double testLat = 40.748441;
    static double testLng = -73.985664;
    static LatLng latLngEMPIRE ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_maps);
        Log.i("MyMapsActivity", "onCreate");

        prefs = getSharedPreferences(
                "pokeBusCodePrefs", Context.MODE_PRIVATE);

        //this loads in businfo of saved bus stops
        pokeBusbusInfo = loadPokeBus();

        Log.i("MyMapsActivity", "pokeBusbusInfo " + pokeBusbusInfo.size());
        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        latLngEMPIRE = new LatLng(testLat, testLng);

        spinner = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBar1);


        mContext = getApplicationContext();



        mLocationProvider = new LocationProvider(this, this);



        LocationManager lService = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = lService.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabledAirplaneMode = isAirplaneModeOn(mContext);

        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(MapsActivity.mContext , NoConnection.class);
            startActivity(intent);
            this.finish();

        }if(enabledAirplaneMode){
            Log.i("MyMapsActivity", "preference == enabledAirplaneMode");


            Intent intent = new Intent(MapsActivity.mContext , AirplaneMode.class);
            startActivity(intent);
            this.finish();

        }else if(!enabledGPS){
            Log.i("MyMapsActivity", "!enabledGPS");
            toaster("Tern GPS on for best results");
        }






        optionsButton = (ImageButton) findViewById(R.id.options_button);
        optionsButton.setOnClickListener(new View.OnClickListener() {

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
                bearing = mMap.getCameraPosition().bearing;
                zoom = mMap.getCameraPosition().zoom;

                mMap.setMyLocationEnabled(true);
                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.




                mLocationProvider.disconnect();
                mLocationProvider.connect();







            }
        });
        ImageButton mapButton = (ImageButton) findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("MyMapsActivity", "onClick busmap");
                //go to map
                //bus map
                SharedPreferences mapPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String prefBusMap = mapPref.getString("KEY99", "Brooklyn");

                Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);

                Intent intent = new Intent(MapsActivity.mContext , BusMap.class);
                intent.putExtra("maptype", "Current Map is: " + prefBusMap);
                startActivity(intent);


            }
        });

        mLocationProvider.connect();




        SnackBar snackbar = new SnackBar(MapsActivity.this, "To set a Pokebus press on the bus stop window", "DISMISS", new View.OnClickListener() {
            public void onClick(View v) {
                // it was the 1st button



            }
        });
        snackbar.setDismissTimer(8000);
        snackbar.show();



    }

    static void popupForPokebus(ImageButton optionsButton, String buscode) {

        //to prevent null pouinter
        if(optionsButton != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(MapsActivity.mContext);
            View popupView = layoutInflater.inflate(R.layout.popup_set_pokebus, null);

            back_dim_layout.setVisibility(View.VISIBLE);

            final String finalBuscode = buscode;


            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);


            popupWindow.showAtLocation(optionsButton, Gravity.CENTER, 0, 0);

            ButtonFlat btnDismiss = (ButtonFlat) popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.dismiss();
                    back_dim_layout.setVisibility(View.GONE);
                    AddMarkers.dialogOpon = false;

                }
            });

            ButtonFlat btnSetPokeBus = (ButtonFlat) popupView.findViewById(R.id.set_pokebus);
            btnSetPokeBus.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.dismiss();
                    back_dim_layout.setVisibility(View.GONE);

                    AddMarkers.dialogOpon = false;


                    for (int i = 0; i < busInfo.size(); i++) {

                        if (busInfo.get(i).getBusCode().equals(finalBuscode)) {
                            pokeBusbusInfo.add(busInfo.get(i));
                            MapsActivity.toasterShort("PokeBus set: " + finalBuscode);
                            break;
                        }

                    }


                    AddMarkers.addPokeBusColor();


                }
            });
        }

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static void refreshMarkers(){
        Log.i("MyMapsActivity", "refreshMarkers");

        busInfo.clear();
        stopTimerTask();
        AddMarkers.marker = null;
        mMap.clear();


        getBusStops(busInfo);
        Log.i("MyMapsActivity", "after getBusStops(busInfo) refresh " );
        Log.i("MyMapsActivity", "after busInfo " + busInfo.size());
        Log.i("MyMapsActivity", "after busInfo " +  busInfo.get(0).getBusCode());

        getBusDistance(busInfo);
        Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

        updateBusDistance();
        Log.i("MyMapsActivity", "after updateBusDistance();");

    }

    public static void addPokeBusMarker(){


        pokeBusMarker = mMap.addMarker(new MarkerOptions()
                .title("PokeBus")
                .position(latLng)
                .draggable(true));
        Log.i("MyMapsActivity", "onMenuItemClick marker created");


    }



    @Override
    public void onBackPressed(){
        Log.i("MyMapsActivity", "onBackPressed");

        if(AddMarkers.dialogOpon){

        }else {

            ft = fm.beginTransaction();
            ft.remove(prefsFragment);
            ft.commit();
        }

    }


    public static void getBusStops(ArrayList<BusInfo> busInfo){
            Log.i("MyMapsActivity", "inside getBusStops");
            obj = new GetBusStopJSON();
            obj.fetchBusStop(busInfo);

        Log.i("MyMapsActivity", "before while");

        while (obj.parsingComplete){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            continue;
        }

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
        zoom = mMap.getCameraPosition().zoom;
        bearing = mMap.getCameraPosition().bearing;

        stopTimerTask();
        savePokeBus();

        //mLocationProvider.disconnect();
        //busInfo.clear();
        //AddMarkers.marker = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity","onResume()");

        //mLocationProvider.connect();


        setUpMapIfNeeded();


    }

    public static void deletePrefs(){
        pokeBusbusInfo.clear();
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

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

        if(mMap == null) {
            Log.i("MyMapsActivity", "setUpMapIfNeeded()  mMap == null ");
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }else{
            AddMarkers.openClosestSnippet(busInfo);
        }


    }





    private void savePokeBus() {
        Log.i("MyMapsActivity","setPokeBus()");
        Log.i("MyMapsActivity","busCode()" );
        Log.i("MyMapsActivity","pokeBusbusInfo,size" + pokeBusbusInfo.size());


        try{
            FileOutputStream fos = mContext.openFileOutput("BUSINFO", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(pokeBusbusInfo);
            os.close();
            fos.close();

        }catch(Exception e) {
            e.printStackTrace();
            Log.i("MyMapsActivity"," e.printStackTrace();()" +  e);
        }


    }

    private ArrayList<BusInfo> loadPokeBus() {
        Log.i("MyMapsActivity","loadPokeBus");

        try{
            FileInputStream fis = this.openFileInput("BUSINFO");
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<BusInfo> busInfo = (ArrayList)  is.readObject();
            is.close();
            fis.close();
            Log.i("MyMapsActivity","loadPokeBus return 1 :)");
            return busInfo;

        }catch(Exception e) {
            Log.i("MyMapsActivity", "e : " + e );
            e.printStackTrace();
        }
        Log.i("MyMapsActivity","loadPokeBus return dud");
        return new ArrayList<BusInfo>();


    }



    static double distFrom(double lat1, double lng1, double lat2, double lng2) {
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

    float zoom;
    float bearing;

    @Override
    public void handleNewLocation(Location location) {


        Log.i("MyMapsActivity","handleNewLocation -----------");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);

        Log.i("MyMapsActivity","handleNewLocation ----------latitude " + latitude);
        //only want location this to run on first time. Not activate on every location update
        if(pointList.size() > 0){
            Log.i("MyMapsActivity", "afterpointList.size() > 0");



            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            mMap.setMyLocationEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)    // Sets the center of the map to Mountain View
                    .bearing(bearing)                // Sets the orientation of the camera to east
                    .zoom(zoom)                   // keeps zoom
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            refreshMarkers();
            AddMarkers.addMarkersToMap(busInfo);

        }else if(firstBoot == 0) {
            Log.i("MyMapsActivity","in first boot " );
            firstBoot++;
            mMap.setMyLocationEnabled(true);
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)    // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(40)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));

            getBusStops(busInfo);
            Log.i("MyMapsActivity", "after getBusStops(busInfo);: ");

            getBusDistance(busInfo);
            Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

            updateBusDistance();
            Log.i("MyMapsActivity", "after updateBusDistance();");

        }

    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }




}


