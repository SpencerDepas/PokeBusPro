package clearfaun.com.pokebuspro;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;




import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends AppCompatActivity implements
        LocationProvider.LocationCallback  {

    static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    static LatLng latLng;
    static private LocationProvider mLocationProvider;
    static double latitude;
    static double longitude;
    static Context mContext;
    static GetBusStopJSON obj;
    static GetBusDistanceJSON objTwo;


    private DrawerLayout mDrawerLayout;
    @Bind(R.id.main_content)  View view;

    float zoom;
    float bearing;


    static ArrayList<BusInfo> busInfo = new ArrayList<>();
    static ArrayList<BusInfo> pokeBusbusInfo;
    static ArrayList<LatLng> pointList = new ArrayList<>();

    static Marker pokeBusMarker;
    static Timer timer;
    static TimerTask timerTask;

    /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/


    PrefsFragment prefsFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    int firstBoot = 0;
    static SharedPreferences prefs;

    public static final String TAG = MapsActivity.class.getSimpleName();
    static ProgressBar spinner;
    static ImageButton optionsButton;
    static ImageButton changeSelectedBus;
    static RelativeLayout back_dim_layout;
    //EMPIRE STATE BUILDING
  /*  static double testLat = 40.748441;
    static double testLng = -73.985664;
    static LatLng latLngEMPIRE ;*/

    int indexForBringSnippetToForground = 1;
    ArrayList<String> busIndexForBusStopCycle = new ArrayList<String>();
    boolean enabledGPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.i("MyMapsActivity", "onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);

        mContext = getApplicationContext();

        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext() , NoConnection.class);
            startActivity(intent);
            this.finish();

        }else {


            prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);

            //this loads in businfo of saved bus stops
            //pokeBusbusInfo = loadPokeBus();

            //Log.i("MyMapsActivity", "pokeBusbusInfo " + pokeBusbusInfo.size());

            spinner = (ProgressBar) findViewById(R.id.progressBar1);



            mLocationProvider = new LocationProvider(this, this);


            LocationManager lService = (LocationManager) getSystemService(LOCATION_SERVICE);
            enabledGPS = lService.isProviderEnabled(LocationManager.GPS_PROVIDER);


            mLocationProvider.connect();





            optionsButton = (ImageButton) findViewById(R.id.options_button);
            optionsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //settings
                    changeSelectedBus.setVisibility(View.INVISIBLE);
                    fm = getFragmentManager();
                    ft = fm.beginTransaction();

                    prefsFragment = new PrefsFragment();
                    ft.add(R.id.map, prefsFragment, "fragmentid");
                    ft.addToBackStack("TAG");
                    ft.commit();


                }
            });


            ImageButton refreshLocation = (ImageButton) findViewById(R.id.refresh_location_button);
            refreshLocation.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("MyMapsActivity", "onClick refreshLocation");

                    if(!isOnline()){
                        Log.i("MyMapsActivity", "!isOnline()");
                        Intent intent = new Intent(getApplicationContext() , NoConnection.class);
                        startActivity(intent);
                        //MapsActivity.finish();

                    }else if (spinner.getVisibility() == View.INVISIBLE) {

                        //refresh button
                        fromOnResume = false;
                        spinner.setVisibility(View.VISIBLE);

                        mLocationProvider.disconnect();
                        mLocationProvider.connect();
                        refreshMarkers();


                        zoom = mMap.getCameraPosition().zoom;
                        bearing = mMap.getCameraPosition().bearing;

                        mMap.setMyLocationEnabled(true);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)    // Sets the center of the map to Mountain View
                                .bearing(bearing)           // Sets the orientation of the camera to east
                                .zoom(zoom)                 // keeps zoom
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                }
            });
            ImageButton mapButton = (ImageButton) findViewById(R.id.map_button);
            mapButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("MyMapsActivity", "onClick busmap");
                    changeSelectedBus.setVisibility(View.INVISIBLE);
                    //go to map
                    //bus map
                    SharedPreferences mapPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String prefBusMap = mapPref.getString("KEY99", "Brooklyn");

                    Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);

                    Intent intent = new Intent(MapsActivity.mContext, BusMap.class);
                    intent.putExtra("maptype", "Current Map is: " + prefBusMap);
                    startActivity(intent);


                }
            });


            changeSelectedBus = (ImageButton) findViewById(R.id.change_selected_bus);
            changeSelectedBus.setVisibility(View.INVISIBLE);
            changeSelectedBus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //settings


                    cycleThroughPopup();


                }
            });


            if (savedInstanceState != null) {
                //Then the application is being reloaded
                Log.i("MyMapsActivity ", "savedInstanceState != null ");

            } else {
                Log.i("MyMapsActivity ", "savedInstanceState == null ");
                //Log.i("MyMapsActivity ", "pokeBusbusInfo SIZE" + pokeBusbusInfo.size());
                pokeBusbusInfo = loadPokeBus();
                if (pokeBusbusInfo != null) {

                    if(pokeBusbusInfo.size() == 0) {
                        Log.i("MyMapsActivity ", "pokeBusbusInfo.size() == 0 ");
                        Snackbar.make(view, "To set a Pokebus press on the bus stop window", Snackbar.LENGTH_LONG)
                                .show();


                    }
                }
            }

            //this loads in businfo of saved bus stops
            if(pokeBusbusInfo != null) {
                pokeBusbusInfo = loadPokeBus();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()== android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        mContext = getApplicationContext();
                        //Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                        if (menuItem.getTitle().equals(getString(R.string.radius_preference))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());


                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
                            CharSequence items[] = new CharSequence[]{"200 Feet", "250 Feet", "300 Feet"};
                            builder.setTitle("Set the radius for PokeBus");
                            builder.setNegativeButton("DISMIIS", null);
                            builder.setPositiveButton("OK",  new DialogInterface.OnClickListener()  {
                                public void onClick(DialogInterface dialog, int which) {
                                    prefs.edit().putString(getString(R.string.radius_key), "200").apply();
                                    MapsActivity.spinner.setVisibility(View.VISIBLE);
                                    MapsActivity.refreshMarkers();

                                    Log.d("MyMainActivity", "radius set to 200:");
                                }
                            });
                            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("AlertDialog", "Positive");
                                    dialog.dismiss();

                                    if (which == 0) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 0);

                                        prefs.edit().putString(getString(R.string.radius_key), "200").apply();
                                        MapsActivity.spinner.setVisibility(View.VISIBLE);
                                        MapsActivity.refreshMarkers();

                                        Log.d("MyMainActivity", "radius set to 200:");

                                    } else if (which == 1) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                                        prefs.edit().putString(getString(R.string.radius_key), "250").apply();
                                        MapsActivity.spinner.setVisibility(View.VISIBLE);
                                        MapsActivity.refreshMarkers();

                                        Log.d("MyMainActivity", "radius set to 250:");

                                    } else if (which == 2) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.radius_key), "300").apply();
                                        MapsActivity.spinner.setVisibility(View.VISIBLE);
                                        MapsActivity.refreshMarkers();

                                        Log.d("MyMainActivity", "radius set to 300:");

                                    }


                                }
                            });
                            builder.show();





                        } else if (menuItem.getTitle().equals(getString(R.string.auto_refresh_time))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());


                        } else if (menuItem.getTitle().equals(getString(R.string.remove_all_poke_bus))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));





                        }  else if (menuItem.getTitle().equals(getString(R.string.about))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                            Intent intent = new Intent(MapsActivity.mContext , AboutScreen.class);
                            startActivity(intent);



                        }else if (menuItem.getTitle().equals(getString(R.string.map_preference))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());
                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));




                        } else if (menuItem.getTitle().equals(getString(R.string.like_us_on_facebook))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());
                            //toaster("My ZIpcode is :" + parseUser.get("zip_code").toString());
                            String url = "https://www.facebook.com/ClearFaun?ref=bookmarks";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);



                        }else if (menuItem.getTitle().equals(getString(R.string.license))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());
                            //toaster("My ZIpcode is :" + parseUser.get("zip_code").toString());

                            Intent intent = new Intent(MapsActivity.mContext , LicenseActivity.class);
                            startActivity(intent);


                        }


                        String savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                        Log.d("MyMainActivity", "msavedRadius:" + savedRadius);

                        return true;
                    }
                });
    }

    boolean firstRun = true;
    public void cycleThroughPopup(){
        //changeSelectedBus.setVisibility(View.INVISIBLE);
        AddMarkers.whatSnippetIsOpen();

        //gets buses for each bus stop
        Log.i("MyMapsActivityy", "AddMarkers.lastOpenSnippet " + AddMarkers.lastOpenSnippet);

        String currentBusCode = AddMarkers.lastOpenSnippet;




        if(busIndexForBusStopCycle.size() == 0 ) {
            Log.i("MyMapsActivityy", "busIndexForBusStopCycle.size() == 0 " );
            if (Integer.parseInt(AddMarkers.lastOpenSnippet) > 0) {
                for (int i = 0; i < busInfo.size(); i++) {

                    //find marker index of all buses for stop
                    if (AddMarkers.lastOpenSnippet.equals(AddMarkers.marker[i].getTitle())) {
                        busIndexForBusStopCycle.add(i + "");
                        Log.i("MyMapsActivityy", "busIndexForBusStopCycle " + i);
                        Log.i("MyMapsActivityy", "busInfo naame " + busInfo.get(i).getBusName());
                    }

                }
            }
        }else if (!AddMarkers.lastOpenSnippet.equals(AddMarkers.marker[Integer.parseInt(busIndexForBusStopCycle.get(0))].getTitle())){
            Log.i("MyMapsActivityy", "else if  " + AddMarkers.lastOpenSnippet);
            //if its not the same snippet open as the last time button was pressed
            indexForBringSnippetToForground = 1;
            busIndexForBusStopCycle.clear();
            if (Integer.parseInt(AddMarkers.lastOpenSnippet) > 0) {
                for (int i = 0; i < busInfo.size(); i++) {

                    //find marker index of all buses for stop
                    if (AddMarkers.lastOpenSnippet.equals(AddMarkers.marker[i].getTitle())) {
                        busIndexForBusStopCycle.add(i + "");
                    }

                }
            }
        }




        Log.i("MyMapsActivityy", "indexForBringSnippetToForground ==  " + indexForBringSnippetToForground);
        Log.i("MyMapsActivityy", "busIndexForBusStopCycle.size() ==  " + busIndexForBusStopCycle.size());

        //resets which to select if iuts got to the last one
        if(indexForBringSnippetToForground == busIndexForBusStopCycle.size()) {
            firstRun = false;
            Log.i("MyMapsActivityy", "in indexForBringSnippetToForground == busIndexForBusStopCycle.size() ");
            indexForBringSnippetToForground = 0;
            for (int i = 0; i < AddMarkers.marker.length; i++) {
                for(int t = 0; t < busIndexForBusStopCycle.size(); t++) {
                    if (AddMarkers.marker[i].getTitle().equals(AddMarkers.marker[Integer.parseInt(busIndexForBusStopCycle.get(t))].getTitle())) {
                        t++;
                        //all markers with the same buscode

                        AddMarkers.marker[i].setVisible(false);
                        MapsActivity.busInfo.get(i).setAddedToPopup(false);
                        Log.i("MyMapsActivityy", "setting invisable marker: " + i);
                    }
                }
            }
        }

        //makes all at stop invisable
        //display next bus in cycle

        Log.i("MyMapsActivityy", "marker index set visable : " + Integer.parseInt(busIndexForBusStopCycle.get(indexForBringSnippetToForground)));
        AddMarkers.marker[Integer.parseInt(busIndexForBusStopCycle.get(indexForBringSnippetToForground))].setVisible(true);
        AddMarkers.marker[Integer.parseInt(busIndexForBusStopCycle.get(indexForBringSnippetToForground))].showInfoWindow();
        indexForBringSnippetToForground++;


    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    static Button btnDismiss;

    static void popupForPokebus(ImageButton optionsButton, String buscode, String id) {



        Log.i("MyMapsActivity ", "popupForPokebus buscode " + buscode);
        Log.i("MyMapsActivity ", "popupForPokebus buscode " + id);
        Log.i("MyMapsActivity ", "AddMarkers.marker[i].getId() " + AddMarkers.marker[0].getId());

        int busInfoIndexForBusName = -1;
        for(int i = 0 ; i < AddMarkers.marker.length; i ++){
            if( AddMarkers.marker[i].getId().equals(id)){
                busInfoIndexForBusName = i;
                break;
            }

        }
        Log.i("MyMapsActivity ", "businfo index " + busInfo.get(busInfoIndexForBusName).busName);

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



            btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.dismiss();
                    back_dim_layout.setVisibility(View.GONE);
                    AddMarkers.dialogOpon = false;

                }
            });

            final int index = busInfoIndexForBusName;

            Button btnSetPokeBus = (Button) popupView.findViewById(R.id.set_pokebus);
            btnSetPokeBus.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.dismiss();
                    back_dim_layout.setVisibility(View.GONE);

                    AddMarkers.dialogOpon = false;


                    if(pokeBusbusInfo == null){
                        pokeBusbusInfo = new ArrayList<>();
                    }


                    pokeBusbusInfo.add(busInfo.get(index));
                    MapsActivity.toasterShort("PokeBus set: " + "\n" + busInfo.get(index).busName + " : " + finalBuscode);


                    AddMarkers.addPokeBusColor();
                    //
                    AddMarkers.openSnippetWithIndex(index );
                    //AddMarkers.openClosestSnippet(busInfo);


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
        //saves last open snippet
        AddMarkers.whatSnippetIsOpen();

        //pointlist is to save info for onrotate
        pointList.clear();

        busInfo.clear();
        stopTimerTask();
        AddMarkers.marker = null;
        mMap.clear();



        getBusStops(busInfo);



        Log.i("MyMapsActivity", "after getBusStops(busInfo) refresh ");
        Log.i("MyMapsActivity", "after busInfo " + busInfo.size());


        getBusDistance(busInfo);



        Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

        updateBusDistance();
        Log.i("MyMapsActivity", "after updateBusDistance();");

    }




    @Override
    public void onBackPressed(){
        Log.i("MyMapsActivity", "onBackPressed");



        if(AddMarkers.dialogOpon){
            Log.i("MyMapsActivity", "AddMarkers.dialogOpon");
        }if(prefsFragment != null){
            ft = fm.beginTransaction();
            ft.remove(prefsFragment);
            ft.commit();
            prefsFragment = null;
            Log.i("MyMapsActivity", "prefsFragment != null");
        }else{
            Log.i("MyMapsActivity", "startMain");
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
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
        Log.i("MyMapsActivity", "onPause()");
        AddMarkers.whatSnippetIsOpen();
        zoom = mMap.getCameraPosition().zoom;
        bearing = mMap.getCameraPosition().bearing;



        stopTimerTask();
        savePokeBus();
        //BusInfo.onPauseNotForToastService(busInfo);
        //mLocationProvider.disconnect();
        //busInfo.clear();
        //AddMarkers.marker = null;
    }

    public void onDestroy() {
        super.onDestroy();



        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putFloat("zoom", zoom);
        savedInstanceState.putFloat("bearing", bearing);
      /*  savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");*/
        // etc.
        super.onSaveInstanceState(savedInstanceState);
    }
    //onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        zoom = savedInstanceState.getFloat("zoom");
        bearing = savedInstanceState.getFloat("bearing");
        /*boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
        double myDouble = savedInstanceState.getDouble("myDouble");
        int myInt = savedInstanceState.getInt("MyInt");
        String myString = savedInstanceState.getString("MyString");*/
    }

    int gpsPrompt = 0;

    boolean fromOnResume = false;
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity", "onResume()");

        boolean enabledAirplaneMode = isAirplaneModeOn(mContext);

        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext() , NoConnection.class);
            startActivity(intent);
            this.finish();

        }else if(!isLocationEnabled(mContext)){


            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Location services disabled");
            builder.setMessage("Native Speed needs to access your location.\n" +
                    "Please turn on location access.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d( "AlertDialog", "Positive" );
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("DISMIISS", null);
            builder.show();


        }else if(enabledAirplaneMode){
            Log.i("MyMapsActivity", "preference == enabledAirplaneMode");


            Intent intent = new Intent(MapsActivity.mContext , AirplaneMode.class);
            startActivity(intent);
            this.finish();

        }

        if (!enabledGPS && gpsPrompt == 0) {
            gpsPrompt++;
            Log.i("MyMapsActivity", "!enabledGPS");
            toaster("Turn on GPS for best results");
        }

        fromOnResume = true;
        mLocationProvider.disconnect();
        mLocationProvider.connect();
        if(AddMarkers.marker != null){
            AddMarkers.openClosestSnippet(busInfo);
        }

        setUpMapIfNeeded();

        updateBusDistance();

    }

    public static void deletePrefs(){
        pokeBusbusInfo.clear();
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }





    static boolean firstTimer = true;
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

            initializeTimerTask(firstTimer);
            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 20000, timeInMS);



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




    public static void initializeTimerTask(boolean firstTimer) {
        final boolean firstTime = firstTimer;
        timerTask = new TimerTask() {
            public void run() {
                Log.i("MyMapsActivity", "initializeTimerTask    timerTask");
                //this enables us to reset the timer in onreusme and it will not trigger it automatkly

                getBusDistance(busInfo);
                    //firstTime = false;


            }
        };
    }







    private void setUpMapIfNeeded() {
        Log.i("MyMapsActivity", "setUpMapIfNeeded() ");

        // Do a null check to confirm that we have not already instantiated the map.

        // Try to obtain the map from the SupportMapFragment.
        if(mMap == null) {
            Log.i("MyMapsActivity", "setUpMapIfNeeded()  mMap == null ");

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //makes marker the same as clicking button

                    MapsActivity.changeSelectedBus.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < MapsActivity.busInfo.size(); i++) {
                        if (MapsActivity.busInfo.get(i).isAddedToPopup()) {
                            MapsActivity.busInfo.get(i).setAddedToPopup(false);
                        }

                    }


                    if (marker.isInfoWindowShown()) {

                        cycleThroughPopup();

                    }
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    Log.i("MyMapsActivity", "Map clicked");

                   /* if(btnDismiss.getVisibility() == View.VISIBLE) {
                        Log.i("MyMapsActivity", "btnDismiss.getVisibility() == View.VISIBLE");


                        btnDismiss.performClick();
                    }*/

                    MapsActivity.changeSelectedBus.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < MapsActivity.busInfo.size(); i++) {
                        if (MapsActivity.busInfo.get(i).isAddedToPopup()) {
                            MapsActivity.busInfo.get(i).setAddedToPopup(false);
                        }

                    }
                }
            });
        }
    }





    private void savePokeBus() {
        Log.i("MyMapsActivity","setPokeBus()");
        Log.i("MyMapsActivity","busCode()" );
        //Log.i("MyMapsActivity","pokeBusbusInfo,size" + pokeBusbusInfo.size());

        //when a poke bus is called we dont want old information saved
        /*for(BusInfo businfo: pokeBusbusInfo){
            businfo.setDistanceNotAvailable();
        }*/

        if(pokeBusbusInfo != null){

            try {
                FileOutputStream fos = mContext.openFileOutput("BUSINFO", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(pokeBusbusInfo);
                os.close();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyMapsActivity", " e.printStackTrace();()" + e);
            }
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



    @Override
    public void handleNewLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);

        Log.i("MyMapsActivity", "handleNewLocation()zoom:" + zoom);
        Log.i("MyMapsActivity","handleNewLocation -----------");
        Log.i("MyMapsActivity","handleNewLocation -----------location.getAccuracy():" +  location.getAccuracy());
        Log.i("MyMapsActivity","fromOnResume :" + fromOnResume);
        //we want it from onresume to update location but not refresh
        if(!fromOnResume || firstBoot == 0) {
            //stopAcuracyTimerTask();
            Log.i("MyMapsActivity", "firstBoot ==  " + firstBoot);
            Log.i("MyMapsActivity", "fromOnResume ==  " + fromOnResume);

            Log.i("MyMapsActivity", "in !fromOnResume || firstBoot == 0 ");
            //only want location this to run on first time. Not activate on every location update
            if (pointList.size() > 0) {
                Log.i("MyMapsActivity", "afterpointList.size() > 0");
                //for after onrotate
                firstBoot++;
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
                //AddMarkers.addMarkersToMap(busInfo);

            } else if (firstBoot == 0) {
                Log.i("MyMapsActivity", "in first boot ");
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

                //Log.i("MyMapsActivityTime", "startTime : " + System.currentTimeMillis());
                long startTime = System.currentTimeMillis();
                getBusStops(busInfo);
                long endTime = (System.currentTimeMillis());
                Log.i("MyMapsActivityTime", "getBusStops(busInfo) endTime  : " + ((endTime - startTime)));


                getBusDistance(busInfo);


                Log.i("MyMapsActivity", "after getBusDistance(busInfo); ");

                updateBusDistance();
                Log.i("MyMapsActivity", "after updateBusDistance();");


            }
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


