package clearfaun.com.pokebuspro;

import android.view.WindowManager;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements
        LocationProvider.LocationCallback,
        DialogPopupListner,
        NoBusesInAreaInterface,
        FirstBusStopHasBeenDisplayed,
        OnMapReadyCallback ,
        GoogleMap.OnInfoWindowCloseListener{


    private final String FINE_LOCATION_PERMISSION_ASKED = "fine_location_permission_has_been_asked";
    static GoogleMap googleMap;

    private final String MAP_SELECTION = "Map selection";
    private LatLng onMapPresedLatLng;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    private double latitude;
    private double longitude;
    private Context mContext;
    private boolean isMapOnPressEnabled = false;
    private boolean hasInstructionalSnackBarBeenShownOnThisLaunch = false;
    private int SDK_LEVEL;
    private boolean responseAnsweredForRuntimePermission = false;
    private final int ENABLE_GPS = 799;


    final int  MY_PERMISSIONS_REQUEST_FINE_LOCATION = 68;

    private boolean hasLocationPermission;

    private int gpsPrompt = 0;
    private float zoom = 16;
    private float bearing = 40;
    private Timer timer;
    private TimerTask timerTask;
    private String[] snackBarInstructions = new String[4];
    private SharedPreferences prefs;
    private ArrayList<String> busCodeOfFavBusStops = new ArrayList<>();
    private boolean enabledGPS;
    private Bundle savedInstanceState;
    private CallAndParse callAndParse;

    @Bind(R.id.main_content)  View view;
    @Bind(R.id.progress_bar_activity_main) ProgressBar progressBar;
    @Bind(R.id.drawer_layout)  DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)  NavigationView navigationView;

    private AddMarkers addMarkers;
    private SupportMapFragment mMap;
    private final LatLng EMPIRE_STATE_BUILDING_LAT_LNG = new LatLng(40.748441, -73.985664);

    final String DIALOG_TITLE = "Access Phone Location";
    final String DIALOG_MESSAGE = "WaveBus needs to acces your location to find local bus stops.";
    final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private int randomNum;
        /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/
    //EMPIRE STATE BUILDING
  /*  static double testLat = 40.748441;
    static double testLng = -73.985664;
    static LatLng latLngEMPIRE ;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_draw);
        ButterKnife.bind(this);
        Log.i("MyMapsActivity", "onCreate");



        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);


        this.savedInstanceState = savedInstanceState;

        mContext = getApplicationContext();




        prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }




        //busCodeOfFavBusStops = loadFavBus();


        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mMap.getMapAsync(this);



        //for instructional snackbar
        randomNum = 0 + (int)(Math.random() * 3);






    }



    private void permissionAtRunTime(){
        Log.i("MyMapsActivity", "permissionAtRunTime ");

        SDK_LEVEL = android.os.Build.VERSION.SDK_INT;
        if (SDK_LEVEL >= Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            hasLocationPermission = permissionCheck();
            Log.i("MyMapsActivity", "permissionCheck hasLocationPermission : " + hasLocationPermission);

            if(!hasLocationPermission){
                Log.i("MyMapsActivity", "permissionCheck !hasLocationPermission");

                if(shouldWeAsk(FINE_LOCATION_PERMISSION_ASKED) ){
                    showPermissionAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE);
                }else {
                    phonePermissionNotGranted();
                }

            }else{
                responseAnsweredForRuntimePermission = true;
                hasLocationPermission = true;
                setUpAfterPermissionRequest();
            }




        } else{
            // do something for phones running an SDK before lollipop
            responseAnsweredForRuntimePermission = true;
            hasLocationPermission = true;
            setUpAfterPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.

                //this is for onMapReady
                responseAnsweredForRuntimePermission = true;
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_GRANTED");

                    googleMap.clear();
                    mLocationProvider = new LocationProvider(this, this);


                    onMapPresedLatLng = null;


                    hasLocationPermission = true;
                    setUpAfterPermissionRequest();
                    savePermissionAsked(FINE_LOCATION_PERMISSION_ASKED);

                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("Fine location permission")
                            .putContentType("Selection")
                            .putCustomAttribute("runtime permission", "Accepted"));

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED" );
                    hasLocationPermission = false;
                    savePermissionAsked(FINE_LOCATION_PERMISSION_ASKED);

                    setUpAfterPermissionRequest();

                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("Fine location permission")
                            .putContentType("Selection")
                            .putCustomAttribute("runtime permission", "Denied"));

                }

            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void setUpAfterPermissionRequest(){
        Log.i("MyMapsActivity ", "setUpAfterPermissionRequest" );

        if(responseAnsweredForRuntimePermission){
            Log.i("MyMapsActivity ", "responseAnsweredForRuntimePermission : " + responseAnsweredForRuntimePermission );

            addMarkers = AddMarkers.getInstance();
            addMarkers.setInterface(MainActivity.this);


            PopupAdapterForMapMarkers.popupListner = MainActivity.this;
            callAndParse = new CallAndParse(MainActivity.this);


            snackBarInstructions[0] = getString(R.string.tap_for_search);
            snackBarInstructions[1] = getString(R.string.tap_search_icon);
            snackBarInstructions[2] = getString(R.string.my_location_icon);
            snackBarInstructions[3] = getString(R.string.tap_bus_distances);


            prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);



            if(hasLocationPermission){

                Log.i("MyMapsActivity", "hasLocationPermission " + hasLocationPermission);
                mLocationProvider = new LocationProvider(this, this);
                mLocationProvider.disconnect();
                mLocationProvider.connect();
                Log.i("MyMapsActivity", "hasLocationPermission  LocationProvider on" + hasLocationPermission);
                LocationManager lService = (LocationManager) getSystemService(LOCATION_SERVICE);
                enabledGPS = lService.isProviderEnabled(LocationManager.GPS_PROVIDER);


                boolean isLocationEnabled = isLocationEnabled(this);
                if(!isLocationEnabled){
                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled" );

                    latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                    onMapPresedLatLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                    refreshMarkers();

                }


            }else {
                Log.i("MyMapsActivity", "hasLocationPermission " + hasLocationPermission);

                latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                onMapPresedLatLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                refreshMarkers();


            }



        }




        // permission denied, boo! Disable the
        // functionality that depends on this permission.
    }

    private void savePermissionAsked(String permission){
        Log.d("MyMapsActivity", "savePermissionAsked" );
        Log.d("MyMapsActivity", "permission : " + permission );
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(permission, false);
        editor.apply();
    }

    private void phonePermissionNotGranted() {
        Log.d("MyMapsActivity", "phonePermissionNotGranted" );
        mMap.getMapAsync(this);
        hasLocationPermission = false;
        setUpAfterPermissionRequest();


    }

    private void showPermissionAlertDialog(String title, String message) {
        Log.d("MyMapsActivity", "showPermissionAlertDialog" );


        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.request_location_dialog_titlle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //permissionHelper.requestAfterExplanation(permission);
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    }

                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //dialog.getWindow().setLayout(getResources(R.dimen.activity_horizontal_margin), 750);//width, height

    }


    private boolean shouldWeAsk(String permission){
        Log.d("MyMapsActivity", "shouldWeAsk" );
        Log.d("MyMapsActivity", "permission : " + permission );
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean needToAsk = sharedPreferences.getBoolean(permission, true);
        Log.d("MyMapsActivity", "shouldWeAsk : " + needToAsk );
        responseAnsweredForRuntimePermission = !needToAsk;
        return needToAsk;

    }



    @SuppressWarnings("unused")
    @OnClick(R.id.refresh_location_fab)
    public void refreshLocation(View view) {
        Log.i("MyMapsActivity", "onClick refreshLocation");


        refreshMarkers();
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Pressed Refresh location fab")
                .putContentType("Action"));

    }


    private void searchForLocationFromAddress() {
        Log.i("MyMapsActivity", "onClick searchForLocation");


        //brings up keyboard
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View alertDialogView = li.inflate(R.layout.search_address_dialog, null);


        final EditText locationToSearchFor = (EditText) alertDialogView
                .findViewById(R.id.seacrhed_address);



        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(MainActivity.this,
                        R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setView(alertDialogView);


        alertDialogBuilder.setPositiveButton("SEARCH",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get user input and set it to result
                        // edit text
                        Log.i("MyMapsActivity", "DialogInterface onClick ");

                        getLatLngForSearchLocationFromAddress(locationToSearchFor.getText().toString());

                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );

                        Answers.getInstance().logContentView(new ContentViewEvent()
                                .putContentName("Searched for Bus stop from dialog")
                                .putContentType("Action"));

                    }

                });
        alertDialogBuilder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get user input and set it to result
                // edit text
                Log.i("MyMapsActivity", "DialogInterface onClick Cancel");

                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

            }

        });

        final android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();



    }



    public void addMarkerToMap(LatLng markerLocation){
        Log.i("MyMapsActivity", "addMarkerToMap ");
        googleMap.addMarker(new MarkerOptions().position(markerLocation));

    }



    private void getLatLngForSearchLocationFromAddress(String location){
        Log.i("MyMapsActivity", "getLatLngForSearchLocationFromAddress ");
        List<Address> addressList = null;

        if (location != null && location.length() > 0 ) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 2);
            } catch (IOException e) {
                e.printStackTrace();

            }



            if(addressList.size() > 0){
                Address address = addressList.get(0);
                LatLng onSearchLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                newLocationFromLatLng(onSearchLatLng);
            }else {
                Snackbar.make(view, getString(R.string.location_not_found), Snackbar.LENGTH_LONG)
                        .show();
            }


        }

    }

    private boolean permissionCheck(){
        Log.i("MyMapsActivity ", "permissionCheck");
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == 0){
            return true;
        }

        Log.i("MyMapsActivity ", "permissionCheck == " + permissionCheck);

        return false;

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.my_main_actvity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.my_location_item){
            if(hasLocationPermission){


                boolean isLocationEnabled = isLocationEnabled(this);
                if(!isLocationEnabled){
                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled" );

                    Snackbar.make(view, getString(R.string.turn_on_location_snackbar_request), Snackbar.LENGTH_LONG)
                            .show();

                }else{
                    onMapPresedLatLng = null;
                    newLocationFromLatLng(latLng);
                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName("Pressed My Location")
                            .putContentType("Action"));
                }

            }else{
                Snackbar.make(view, getString(R.string.turn_on_location_snackbar_request), Snackbar.LENGTH_LONG)
                        .show();
            }


        }else if(item.getItemId()== R.id.search_item){
            searchForLocationFromAddress();
        }else if(item.getItemId()== android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }else if(item.getItemId()== R.id.map_item){
            String prefBusMap = prefs.getString("KEY99", "Brooklyn");

            Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);
            Intent intent = new Intent(mContext, MTABusMapActivity.class);
            intent.putExtra("maptype", "Current Map is: " + prefBusMap);
            startActivity(intent);

            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName("Launch Map Activity")
                    .putContentType("Action")
                    );
        }


        return super.onOptionsItemSelected(item);
    }

    public void closeDrawer() {mDrawerLayout.closeDrawer(Gravity.LEFT);}

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Answers.getInstance().logContentView(new ContentViewEvent()
                                .putContentName("Nav view open")
                                .putContentType("Action")
                        );

                        mContext = getApplicationContext();




                        String savedRadius = prefs.getString(getString(R.string.radius_key), "NA");


                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);


                        //Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                        if (menuItem.getTitle().equals(getString(R.string.radius_preference))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());


                            final String findWhatToPreSelect = prefs.getString(getString(R.string.radius_key), "2");

                            int preSelectedIndex = -1;
                            switch (Integer.parseInt(findWhatToPreSelect)) {
                                case 200:  preSelectedIndex = 0;
                                    break;
                                case 250:  preSelectedIndex = 1;
                                    break;
                                case 300:  preSelectedIndex = 2;
                                    break;
                                default:
                                    preSelectedIndex = 2;
                                    break;


                            }

                            Log.d("AlertDialog", "findWhatToPreSelect : " + findWhatToPreSelect);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                            CharSequence items[] = new CharSequence[]{"200 Feet", "250 Feet", "300 Feet"};
                            builder.setTitle("Set the radius for PokeBus");
                            builder.setNegativeButton("DISMISS", null);
                            builder.setSingleChoiceItems(items, preSelectedIndex, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("AlertDialog", "Positive");
                                    dialog.dismiss();

                                    Log.d("AlertDialog", "findWhatToPreSelect : " + findWhatToPreSelect);


                                    if (which == 0) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 0);

                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 0);



                                        prefs.edit().putString(getString(R.string.radius_key), "200").apply();
                                        progressBar.setVisibility(view.VISIBLE);
                                        refreshMarkers();



                                        String savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                                        Log.d("MyMainActivity", "prefs refreshRate:" + findWhatToPreSelect);

                                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);


                                        Log.d("MyMainActivity", "radius set to 200:");

                                    } else if (which == 1) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                                        prefs.edit().putString(getString(R.string.radius_key), "250").apply();
                                        progressBar.setVisibility(view.VISIBLE);                                       refreshMarkers();

                                        String refreshRate = prefs.getString("KEY2", "DICK");

                                        String savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                                        Log.d("MyMainActivity", "prefs refreshRate:" + refreshRate);

                                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);

                                        Log.d("MyMainActivity", "radius set to 250:");

                                    } else if (which == 2) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.radius_key), "300").apply();
                                        progressBar.setVisibility(view.VISIBLE);
                                        refreshMarkers();


                                        String refreshRate = prefs.getString("KEY2", "DICK");

                                        String savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                                        Log.d("MyMainActivity", "prefs refreshRate:" + refreshRate);

                                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);
                                        Log.d("MyMainActivity", "radius set to 300:");

                                    }


                                }
                            });
                            builder.show();





                        } else if (menuItem.getTitle().equals(getString(R.string.auto_refresh_time))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            //prefs.edit().putString("KEY99", "FUCKKK").apply();




                            final String findWhatToPreSelect = prefs.getString(getString(R.string.refresh_time_key), "0");

                            int preSelectedIndex = 0;
                            try {
                                switch (Integer.parseInt(findWhatToPreSelect)) {
                                    case 20:
                                        preSelectedIndex = 0;
                                        break;
                                    case 30:
                                        preSelectedIndex = 1;
                                        break;
                                    case 60:
                                        preSelectedIndex = 2;
                                        break;
                                    default:
                                        preSelectedIndex = 3;
                                        break;


                                }
                            }catch (Exception e){
                                preSelectedIndex = 3;
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                            CharSequence items[] = new CharSequence[]{"20 Secconds", "30 Secconds", "60 Secconds", "OFF"};
                            builder.setTitle(getString(R.string.set_bus_update_frequency));
                            builder.setNegativeButton("DISMISS", null);
                            builder.setSingleChoiceItems(items, preSelectedIndex, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("AlertDialog", "Positive");
                                    dialog.dismiss();

                                    if (which == 0) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 0);

                                        prefs.edit().putString("KEY2", "20").apply();

                                        refreshMarkers();

                                        Log.d("MyMainActivity", "refreshrate set to 20:");

                                    } else if (which == 1) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                                        prefs.edit().putString("KEY2", "30").apply();

                                        refreshMarkers();

                                        Log.d("MyMainActivity", "refreshrate set to 30:");

                                    } else if (which == 2) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString("KEY2", "60").apply();

                                        refreshMarkers();

                                        Log.d("MyMainActivity", "refreshrate set to 60:");

                                    } else if (which == 3) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString("KEY2", "0").apply();
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.stopTimerTask();

                                    }


                                }
                            });
                            builder.show();



                        } else if (menuItem.getTitle().equals(getString(R.string.remove_all_poke_bus))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));


                            removeSavedFavBusFromStorage();

                            //removes change of color from icon color
                            removeFavBusMarkerColor();


                            Toast.makeText(mContext, getString(R.string.removed_fav_bus),
                                    Toast.LENGTH_LONG).show();;


                        }  else if (menuItem.getTitle().equals(getString(R.string.about))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                            Intent intent = new Intent(mContext , AboutAppActivity.class);
                            startActivity(intent);

                            mDrawerLayout.closeDrawers();


                        }else if (menuItem.getTitle().equals(getString(R.string.map_preference))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());
                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                            final String findWhatToPreSelect = prefs.getString(getString(R.string.bus_maps_key), "0");

                            int preSelectedIndex = 0;

                            switch ( findWhatToPreSelect) {
                                case "Brooklyn":
                                    preSelectedIndex = 0;
                                    break;
                                case "Manhattan":
                                    preSelectedIndex = 1;
                                    break;
                                case "Queens":
                                    preSelectedIndex = 2;
                                    break;
                                case "Bronx":
                                    preSelectedIndex = 3;
                                    break;
                                case "Staten Island":
                                    preSelectedIndex = 4;
                                    break;
                                default:
                                    preSelectedIndex = 0;
                                    break;

                            }


                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                            CharSequence items[] = new CharSequence[]{"Brooklyn", "Manhattan", "Queens", "Bronx", "Staten Island"};
                            builder.setTitle(getString(R.string.select_bus_map_tittle));
                            builder.setNegativeButton("DISMISS", null);
                            builder.setSingleChoiceItems(items, preSelectedIndex, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("AlertDialog", "Positive");
                                    dialog.dismiss();

                                    if (which == 0) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 0);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Brooklyn").apply();

                                        Answers.getInstance().logContentView(new ContentViewEvent()
                                                .putContentName("Select Map")
                                                .putContentType("Selection")
                                                .putCustomAttribute(MAP_SELECTION, "Brooklyn")
                                        );



                                        Log.d("MyMainActivity", "refreshrate set to 20:");

                                    } else if (which == 1) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Manhattan").apply();


                                        Log.d("MyMainActivity", "refreshrate set to 30:");

                                        Answers.getInstance().logContentView(new ContentViewEvent()
                                                .putContentName("Select Map")
                                                .putContentType("Selection")
                                                .putCustomAttribute(MAP_SELECTION, "Manhattan")
                                        );

                                    } else if (which == 2) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Queens").apply();

                                        Answers.getInstance().logContentView(new ContentViewEvent()
                                                .putContentName("Select Map")
                                                .putContentType("Selection")
                                                .putCustomAttribute(MAP_SELECTION, "Queens")
                                        );

                                        Log.d("MyMainActivity", "refreshrate set to 60:");

                                    } else if (which == 3) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Bronx").apply();

                                        Answers.getInstance().logContentView(new ContentViewEvent()
                                                .putContentName("Select Map")
                                                .putContentType("Selection")
                                                .putCustomAttribute(MAP_SELECTION, "Bronx")
                                        );
                                    }else if (which == 4) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Staten Island").apply();


                                        Answers.getInstance().logContentView(new ContentViewEvent()
                                                .putContentName("Select Map")
                                                .putContentType("Selection")
                                                .putCustomAttribute(MAP_SELECTION, "Staten Island")
                                        );
                                    }


                                }
                            });
                            builder.show();



                        }else if (menuItem.getTitle().equals(getString(R.string.follow_me_on_twitter))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());
                            //toaster("My ZIpcode is :" + parseUser.get("zip_code").toString());
                            openTwitterIntent();



                        }else if (menuItem.getTitle().equals(getString(R.string.license))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());
                            //toaster("My ZIpcode is :" + parseUser.get("zip_code").toString());

                            Intent intent = new Intent(mContext , LicenseActivity.class);
                            startActivity(intent);


                        }else if (menuItem.getTitle().equals(getString(R.string.enable_location))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            if(!hasLocationPermission){
                                Log.d("MyMainActivity", "!hasLocationPermission || SDK_LEVEL < 23" + menuItem.getTitle());
                                showPermissionAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE);
                            }else{

                                boolean isLocationEnabled = isLocationEnabled(mContext);
                                if(!isLocationEnabled){
                                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled" );

                                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ENABLE_GPS);

                                    onMapPresedLatLng = null;

                                }else{
                                    Toast.makeText(mContext, getString(R.string.you_allready_have_permission),
                                            Toast.LENGTH_LONG).show();;
                                }

                            }


                        }

                        savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                        Log.d("MyMainActivity", "final prefs msavedRadius:" + savedRadius);

                        return true;
                    }
                });
    }

    private void openTwitterIntent() {
        Log.d("MyDetailNational", "openTwitterIntent ");
        String mTwitterName = "spencerdepas";
        Log.d("MyDetailNational", "mTwitterName " + mTwitterName);
        Intent intent = null;
        try {
            Log.d("MyDetailNational", "try ");
            // get the Twitter app if possible



            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" +
                    mTwitterName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {
            Log.d("MyDetailNational", "catch ");
            Log.d("MyDetailNational", "e :  " + e.toString());
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" +
                    mTwitterName));
        }
        this.startActivity(intent);
    }


    private void removeFavBusMarkerColor(){
        addMarkers.removePokeBusColor(busCodeOfFavBusStops);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("MyMapsActivity", "onActivityResult" );

        if(requestCode==ENABLE_GPS) {
            Log.i("MyMapsActivity", "requestCode==ENABLE_GPS" );


            boolean isLocationEnabled = isLocationEnabled(mContext);
            if(isLocationEnabled){
                Log.i("MyMapsActivity", "onActivityResult isLocationEnabled" );


                onMapPresedLatLng = null;
                newLocationFromLatLng(latLng);
            }

        }
    }

    private boolean isLocationEnabled(Context context) {
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





    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void refreshMarkers(){
        Log.i("MyMapsActivity", "refreshMarkers");

        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext() , NoConnectionActivity.class);
            startActivity(intent);


        }else {

            zoom = googleMap.getCameraPosition().zoom;
            bearing = googleMap.getCameraPosition().bearing;


            progressBar.setVisibility(view.VISIBLE);

            if(hasLocationPermission){
                Log.i("MyMapsActivity", "hasLocationPermission  " + hasLocationPermission);


//                zoom = 16;
//                bearing = 40;
                boolean isLocationEnabled = isLocationEnabled(this);
                if(!isLocationEnabled){
                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled" );

                    selectCorrectLatLng();

                }else{
                    mLocationProvider.disconnect();
                    mLocationProvider.connect();
                }



            }else{
                Log.i("MyMapsActivity", "hasLocationPermission : " + hasLocationPermission);

                selectCorrectLatLng();
            }


            animateCameraPos();


        }

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Log.i("PopupAdapterForMapMark", "  onInfoWindowClose " );

         enableMapOnPress();

    }

    private void animateCameraPos(){
        Log.i("MyMapsActivity", "animateCameraPos");

        Log.i("MyMapsActivity", "animateCameraPos zoom : " + zoom);
        Log.i("MyMapsActivity", "onMapPresedLatLng : " + onMapPresedLatLng);

        if(onMapPresedLatLng != null){
            Log.i("MyMapsActivity", "animateCameraPos onMapPresedLatLng != null");
            Log.i("MyMapsActivity", "animateCameraPos zoom : " + zoom);
            //this is for location from touch or search
            if(zoom > 4){
                Log.i("MyMapsActivity", "animateCameraPos bearing != 0");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(onMapPresedLatLng)    // Sets the center of the map to Mountain View
                        .zoom(zoom)                   // Sets the zoom
                        .bearing(bearing)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));
                googleMap.setOnInfoWindowCloseListener(this);

            }else{
                Log.i("MyMapsActivity", "animateCameraPos bearing == 0");
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(onMapPresedLatLng)    // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .bearing(40)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));
                googleMap.setOnInfoWindowCloseListener(this);

            }



        }else {
            Log.i("MyMapsActivity", "animateCameraPos onMapPresedLatLng == null");
            //this is for location from GPS

            if(zoom > 4){
                Log.i("MyMapsActivity", "animateCameraPos bearing zoom > 4");
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)    // Sets the center of the map to Mountain View
                        .zoom(zoom)                   // Sets the zoom
                        .bearing(bearing)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));
                googleMap.setOnInfoWindowCloseListener(this);

            }else{
                Log.i("MyMapsActivity", "animateCameraPos bearing == 0");
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)    // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .bearing(40)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));
                googleMap.setOnInfoWindowCloseListener(this);

            }
        }


    }








    public void onPause() {
        super.onPause();
        Log.i("MyMapsActivity", "onPause()");

//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        //AddMarkers.whatSnippetIsOpen();
        if(zoom != 0){
            zoom = googleMap.getCameraPosition().zoom;
            bearing = googleMap.getCameraPosition().bearing;
            Log.i("MyMapsActivity", "saving zoom :" + zoom);
            Log.i("MyMapsActivity", "saving bearking : " + bearing);
        }





        stopTimerTask();
        saveFavBus();



    }


    public void onDestroy() {
        super.onDestroy();
        Log.i("MyMapsActivity", "onDestroy()");

        MarkerManager markerManager = MarkerManager.getInstance();

        Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();

        markerHashTable.clear();

        mMap = null;
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putFloat("zoom", zoom);
        savedInstanceState.putFloat("bearing", bearing);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        zoom = savedInstanceState.getFloat("zoom");
        bearing = savedInstanceState.getFloat("bearing");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyMapsActivity", "onResume()");

        if(hasLocationPermission) {
            Log.i("MyMapsActivity", "onResume() hasLocationPermission" + hasLocationPermission);

            checkPhoneParams();

            mLocationProvider.disconnect();
            mLocationProvider.connect();


            startTimerTask();


            //showInstructionalSnackBar();


        }else{



            setUpAfterPermissionRequest();



        }

    }



    private void showInstructionalSnackBar(){
        Log.i("MyMapsActivity", "showInstructionalSnackBar()");


        Log.i("MyMapsActivity", "showInstructionalSnackBar() : " + randomNum);

        boolean hasInstructionalSnackBarBeenAccepted
                = prefs.getBoolean(snackBarInstructions[randomNum], false);

        if(!hasInstructionalSnackBarBeenAccepted) {
            Log.i("MyMapsActivity", "showInstructionalSnackBar hasInstructionalSnackBarBeenAccepted : "
                    + hasInstructionalSnackBarBeenAccepted);

            Log.i("MyMapsActivity", "showInstructionalSnackBar hasInstructionalSnackBarBeenShownOnThisLaunch : "
                    + hasInstructionalSnackBarBeenShownOnThisLaunch);

            if (!hasInstructionalSnackBarBeenShownOnThisLaunch) {
                hasInstructionalSnackBarBeenShownOnThisLaunch = true;

                Log.i("MyMapsActivity", "!hasInstructionalSnackBarBeenShownOnThisLaunch()");
                Snackbar snackbar = Snackbar
                        .make(view, snackBarInstructions[randomNum], Snackbar.LENGTH_LONG)
                        .setAction("GOT IT", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Log.i("MyMapsActivity", "!hasInstructionalSnackBarBeenShownOnThisLaunch() onclick");
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putBoolean(snackBarInstructions[randomNum], true);
                                editor.commit();

                            }
                        });

                snackbar.show();


            }

        }


    }




    private void checkPhoneParams(){

        boolean enabledAirplaneMode = isAirplaneModeOn(mContext);

        if (!isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);
            this.finish();

        } else if (!isLocationEnabled(mContext)) {


            Log.i("MyMapsActivity", "!isLocationEnabled(mContext)");


        } else if (enabledAirplaneMode) {
            Log.i("MyMapsActivity", "preference == enabledAirplaneMode");


            Intent intent = new Intent(mContext, NoConnectionActivity.class);
            startActivity(intent);
            this.finish();

        }

        if (!enabledGPS && gpsPrompt == 0) {
            gpsPrompt++;
            Log.i("MyMapsActivity", "!enabledGPS");

            toaster("Turn on GPS for best results");
        }


    }


    private void removeSavedFavBusFromStorage(){



        busCodeOfFavBusStops.clear();
        Log.i("MyMapsActivity", "busCodeOfFavBusStops.size : " + busCodeOfFavBusStops.size());
    }



    private void deletePrefs(){

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }

    public void startTimerTask() {
        Log.i("MyMapsActivity", "startTimerTask()");
        String timeInMSString= prefs.getString(mContext.getString(R.string.refresh_time_key), "20000");

        if(timeInMSString.equals("OFF")){
            timeInMSString = "0";
        }else{


            Log.i("MyMapsActivity", "timeInMSString()" + timeInMSString);

            int timeInMS = Integer.parseInt(timeInMSString) * 1000;
            Log.i("MyMapsActivity", "timeInMS()" + timeInMS);
            if(timeInMS != 0) {
                //set a new Timer
                timer = new Timer();
                //initialize the TimerTask's job


                initializeTimerTask();
                //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
                timer.schedule(timerTask, 20000, timeInMS);



            }

        }
    }

    private void stopTimerTask() {
        Log.i("MyMapsActivity", "stopTimerTask()" );
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }




    private void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                Log.i("MyMapsActivity", "initializeTimerTask    timerTask");

                selectCorrectLatLng();



            }
        };
    }





    private void saveFavBus() {
        Log.i("MyMapsActivity","saveFavBus()");



        if(busCodeOfFavBusStops != null){

            try {
                FileOutputStream fos = mContext.openFileOutput(getResources().getString(R.string.fav_bus_key), Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(busCodeOfFavBusStops);
                os.close();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyMapsActivity", " e.printStackTrace();()" + e);
            }
        }

    }

    private ArrayList<String> loadFavBus() {
        Log.i("MyMapsActivity","loadFavBus");

        try{
            FileInputStream fis = this.openFileInput(getResources().getString(R.string.fav_bus_key));
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<String> favBuses = (ArrayList)  is.readObject();
            is.close();
            fis.close();
            Log.i("MyMapsActivity","favBuses.size()" + favBuses.size());
            return favBuses;

        }catch(Exception e) {
            Log.i("MyMapsActivity", "e : " + e );
            e.printStackTrace();
        }
        Log.i("MyMapsActivity","loadFavBus return dud");
        return new ArrayList<>();


    }



    double distFrom(double lat1, double lng1, double lat2, double lng2) {
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


    private void toaster(String string){
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_LONG);
        toast.show();
    }





    @Override
    public void handleNewLocation(Location location) {
        Log.i("MyMapsActivity", "handleNewLocation ");

        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);
            this.finish();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);


        googleMap.setMyLocationEnabled(true);


        animateCameraPos();

        selectCorrectLatLng();






    }

    private void selectCorrectLatLng(){
        Log.i("MyMapsActivity ", "selectCorrectLatLng " );

        String radius = prefs.getString("KEY1", "301");

        if(callAndParse == null){
            callAndParse = new CallAndParse(MainActivity.this);
        }


        if(onMapPresedLatLng != null){

            callAndParse.getBusStopsAndBusDistances(onMapPresedLatLng, busCodeOfFavBusStops, radius);
        }else{
            callAndParse.getBusStopsAndBusDistances(latLng, busCodeOfFavBusStops, radius);
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }




    @Override
    public void displayDialog(String buscode) {
        Log.i("MyMapsActivity", "displayDialog interface");




        final String finalBuscode = buscode;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.set_fav_bus));
        builder.setMessage("BusCode: " + finalBuscode
                + "\n" + "\n" +  getString(R.string.set_fav_bus_body));
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener()  {
            public void onClick(DialogInterface dialog, int which) {
                Log.d( "AlertDialog", "Positive" );
                dialog.dismiss();


                busCodeOfFavBusStops.add(finalBuscode);
                addMarkers.addPokeBusColor(finalBuscode);



            }
        });
        builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "Positive");
                dialog.dismiss();



            }
        });
        builder.show();
    }


    @Override
    public void noBususFound() {
        Log.d("AlertDialog", "noBususFound");
        Snackbar.make(view, "No Buses found in the area", Snackbar.LENGTH_LONG)
                .show();

        progressBar.setVisibility(view.INVISIBLE);


        stopTimerTask();
    }


    boolean oneTimeCall = false;


    @Override
    public void onMapReady(GoogleMap newGoogleMap) {
        Log.d("MyMapsActivity", "onMapReadypoooo");
        this.googleMap = newGoogleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);




        enableMapOnPress();

        if(!oneTimeCall){
            oneTimeCall = true;

            busCodeOfFavBusStops = loadFavBus();

            permissionAtRunTime();




        }





    }

    public void disableMapOnPress(){
        Log.d("MyMapsActivity", "disableMapOnPress");
        googleMap.setOnMapClickListener(null);
        Log.d("MyMapsActivity", "i work once fEnable : " + isMapOnPressEnabled);
        isMapOnPressEnabled = false;
    }



    public void enableMapOnPress(){
        Log.d("MyMapsActivity", "enableMapOnPress");

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                Log.d("MyMapsActivity", "onMapClick");

                //Log.d("MyMapsActivity", "fEnable : " + fEnable);
                if(isMapOnPressEnabled) {
                    isMapOnPressEnabled = false;
                    // TODO Auto-generated method stub
                    Log.d("MyMapsActivity", arg0.latitude + "-" + arg0.longitude);

                    newLocationFromLatLng(arg0);
                } else{

                    isMapOnPressEnabled = true;
                    //Log.d("MyMapsActivity", "isMapOnPressEnabled : " + isMapOnPressEnabled);
                }



            }
        });


    }

    private void newLocationFromLatLng(LatLng latLng){
        Log.d("MyMapsActivity", "newLocationFromLatLng");

        MarkerManager markerManager = MarkerManager.getInstance();

        Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();




        onMapPresedLatLng = latLng;

        markerHashTable.clear();
        googleMap.clear();
        progressBar.setVisibility(view.VISIBLE);

        zoom = googleMap.getCameraPosition().zoom;
        bearing = googleMap.getCameraPosition().bearing;
        selectCorrectLatLng();

        animateCameraPos();
    }


    @Override
    public void removeLoadingIcon() {
        Log.d("MyMapsActivity", "removeLoadingIcon");

        showInstructionalSnackBar();

        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(view.INVISIBLE);
        }



    }
}


