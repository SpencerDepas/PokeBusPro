package clearfaun.com.pokebuspro;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;


import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

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


public class MapsActivity extends AppCompatActivity implements
        LocationProvider.LocationCallback,
        DialogPopupListner,
        NoBusesInAreaInterface,
        FirstBusStopHasBeenDisplayed,
        OnMapReadyCallback ,
        GoogleMap.OnInfoWindowCloseListener{

    private LatLng onMapPresedLatLng;
    static LatLng latLng;
    static private LocationProvider mLocationProvider;
    static double latitude;
    static double longitude;
    static Context mContext;

    private boolean hasInstructionalSnackBarBeenShown = false;


    static String FINE_LOCATION_PERMISSION_ASKED = "fine_location_permission_has_been_asked";
    final int  MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 22;
    final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 23;
    boolean hasPermission;

    private DrawerLayout mDrawerLayout;
    @Bind(R.id.main_content)  View view;

    float zoom;
    float bearing;

    static GoogleMap googleMap;

    static Marker pokeBusMarker;
    Timer timer;
    TimerTask timerTask;

    private String[] snackBarInstructions = new String[4];

    /*static double testLat = 40.6455520;
    static double testLng = -73.9829084;*/


    FragmentManager fm;
    FragmentTransaction ft;
    int firstBoot = 0;
    SharedPreferences prefs;

    public static final String TAG = MapsActivity.class.getSimpleName();
    private ProgressBar spinner;

    static RelativeLayout back_dim_layout;
    //EMPIRE STATE BUILDING
  /*  static double testLat = 40.748441;
    static double testLng = -73.985664;
    static LatLng latLngEMPIRE ;*/
    static AddMarkers addMarkers;

    ArrayList<String> busCodeOfFavBusStops = new ArrayList<>();
    boolean enabledGPS;
    private Bundle savedInstanceState;
    private CallAndParse callAndParse;
    SupportMapFragment mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);






        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            hasPermission = permissionCheck();
            Log.i("MyMapsActivity", "permissionCheck hasPermission : " + hasPermission);

            if(!hasPermission){
                Log.i("MyMapsActivity", "permissionCheck !hasPermission");
                askForPermissionActivty();
            }else{
                permissionGranted();
            }


        } else{
            // do something for phones running an SDK before lollipop
            permissionGranted();
        }


        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mMap.getMapAsync(this);

        busCodeOfFavBusStops = loadFavBus();

        Log.i("MyMapsActivity", "busCodeOfFavBusStops.size : " + busCodeOfFavBusStops.size());

        addMarkers = AddMarkers.getInstance();
        addMarkers.setInterface(MapsActivity.this);
        PopupAdapterForMapMarkers.popupListner = MapsActivity.this;

        callAndParse = new CallAndParse(MapsActivity.this);


        snackBarInstructions[0] = getString(R.string.tap_for_search);
        snackBarInstructions[1] = getString(R.string.tap_search_icon);
        snackBarInstructions[2] = getString(R.string.my_location_icon);
        snackBarInstructions[3] = getString(R.string.my_bus_map);





    }

    private void permissionGranted(){






        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext() , NoConnectionActivity.class);
            startActivity(intent);
            this.finish();

        }else {


            prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);

            //this loads in businfo of saved bus stops
            //pokeBusbusInfo = loadFavBus();

            //Log.i("MyMapsActivity", "pokeBusbusInfo " + pokeBusbusInfo.size());

            spinner = (ProgressBar) findViewById(R.id.mapsAcitvityProgressBar);



            mLocationProvider = new LocationProvider(this, this);


            LocationManager lService = (LocationManager) getSystemService(LOCATION_SERVICE);
            enabledGPS = lService.isProviderEnabled(LocationManager.GPS_PROVIDER);




            if (savedInstanceState != null) {
                //Then the application is being reloaded
                Log.i("MyMapsActivity ", "savedInstanceState != null ");

            } else {
                Log.i("MyMapsActivity ", "savedInstanceState == null ");
                //Log.i("MyMapsActivity ", "pokeBusbusInfo SIZE" + pokeBusbusInfo.size());

            }


        }

    }


    @SuppressWarnings("unused")
    @OnClick(R.id.refresh_location_fab)
    public void refreshLocation(View view) {
        Log.i("MyMapsActivity", "onClick refreshLocation");


        refreshMarkers();

    }


    public void searchForLocationFab() {
        Log.i("MyMapsActivity", "onClick searchForLocation");



        final View finalView = view;
        LayoutInflater li = LayoutInflater.from(MapsActivity.this);
        View alertDialogView = li.inflate(R.layout.search_address_dialog, null);


        final EditText locationToSearchFor = (EditText) alertDialogView
                .findViewById(R.id.seacrhed_address);



        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(MapsActivity.this,
                        R.style.MyAlertDialogStyle);
        alertDialogBuilder.setView(alertDialogView);


        alertDialogBuilder.setPositiveButton("SEARCH",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get user input and set it to result
                        // edit text
                        Log.i("MyMapsActivity", "DialogInterface onClick ");

                        getLatLngForSearchLocation(locationToSearchFor.getText().toString());

                    }

                });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        final android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();



    }



    private void getLatLngForSearchLocation(String location){
        Log.i("MyMapsActivity", "getLatLngForSearchLocation ");
        List<Address> addressList = null;

        if (location != null || !location.equals("") ) {
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


    private void askForPermissionActivty(){
        Log.i("MyMapsActivity ", "permissionCheck askForPermission " );

        Intent intent = new Intent(getApplicationContext() , GetPermissionActivity.class);
        startActivity(intent);
        this.finish();


    }

    private void requestPermissionReadStorage(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.i("MyMapsActivity ", "permissionCheck askForPermission " );

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


                Log.i("MyMapsActivity ", "permissionCheck ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,\n" +
                        "                    Manifest.permission.ACCESS_FINE_LOCATION) " );
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                Log.i("MyMapsActivity ", " permissionCheck No explanation needed, we can request the permission. " );
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_GRANTED");

                    requestPermissionReadStorage();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED" );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_GRANTED");


                    permissionGranted();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED" );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.my_main_actvity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.my_location_item){

            onMapPresedLatLng = null;
            newLocationFromLatLng(latLng);

        }else if(item.getItemId()== R.id.search_item){
            searchForLocationFab();
        }else if(item.getItemId()== android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }else if(item.getItemId()== R.id.map_item){
            String prefBusMap = prefs.getString("KEY99", "Brooklyn");

            Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);
            Intent intent = new Intent(MapsActivity.mContext, BusMap.class);
            intent.putExtra("maptype", "Current Map is: " + prefBusMap);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void closeDrawer() {mDrawerLayout.closeDrawer(Gravity.LEFT);}

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        mContext = getApplicationContext();




                        String savedRadius = prefs.getString(getString(R.string.radius_key), "NA");


                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);


                        //Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                        if (menuItem.getTitle().equals(getString(R.string.radius_preference))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());


                            final String findWhatToPreSelect = prefs.getString(getString(R.string.radius_key), "0");

                            int preSelectedIndex = -1;
                            switch (Integer.parseInt(findWhatToPreSelect)) {
                                case 200:  preSelectedIndex = 0;
                                    break;
                                case 250:  preSelectedIndex = 1;
                                    break;
                                case 300:  preSelectedIndex = 2;
                                    break;
                                default:
                                    preSelectedIndex = 0;
                                    break;


                            }

                            Log.d("AlertDialog", "findWhatToPreSelect : " + findWhatToPreSelect);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                        spinner.setVisibility(View.VISIBLE);
                                        refreshMarkers();



                                        String savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                                        Log.d("MyMainActivity", "prefs refreshRate:" + findWhatToPreSelect);

                                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);


                                        Log.d("MyMainActivity", "radius set to 200:");

                                    } else if (which == 1) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                                        prefs.edit().putString(getString(R.string.radius_key), "250").apply();
                                        spinner.setVisibility(View.VISIBLE);
                                        refreshMarkers();

                                        String refreshRate = prefs.getString("KEY2", "DICK");

                                        String savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                                        Log.d("MyMainActivity", "prefs refreshRate:" + refreshRate);

                                        Log.d("MyMainActivity", "prefs msavedRadius:" + savedRadius);

                                        Log.d("MyMainActivity", "radius set to 250:");

                                    } else if (which == 2) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.radius_key), "300").apply();
                                        spinner.setVisibility(View.VISIBLE);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                        MapsActivity mapsActivity = new MapsActivity();
                                        mapsActivity.stopTimerTask();

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


                            Snackbar.make(view, getString(R.string.removed_fav_bus),
                                    Snackbar.LENGTH_LONG)
                                    .show();



                        }  else if (menuItem.getTitle().equals(getString(R.string.about))) {
                            Log.d("MyMainActivity", "menuItem.getTitle():" + menuItem.getTitle());

                            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                            Intent intent = new Intent(MapsActivity.mContext , AboutScreen.class);
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


                                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
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




                                        Log.d("MyMainActivity", "refreshrate set to 20:");

                                    } else if (which == 1) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Manhattan").apply();


                                        Log.d("MyMainActivity", "refreshrate set to 30:");

                                    } else if (which == 2) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Queens").apply();


                                        Log.d("MyMainActivity", "refreshrate set to 60:");

                                    } else if (which == 3) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Bronx").apply();


                                    }else if (which == 4) {
                                        Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                                        prefs.edit().putString(getString(R.string.bus_maps_key), "Staten Island").apply();


                                    }


                                }
                            });
                            builder.show();



                        }else if (menuItem.getTitle().equals(getString(R.string.like_us_on_facebook))) {
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




                        savedRadius = prefs.getString(getString(R.string.radius_key), "FUCK");

                        Log.d("MyMainActivity", "final prefs msavedRadius:" + savedRadius);

                        return true;
                    }
                });
    }


    public void removeFavBusMarkerColor(){
        addMarkers.removePokeBusColor(busCodeOfFavBusStops);
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





    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void refreshMarkers(){
        Log.i("MyMapsActivity", "refreshMarkers");

        if(!isOnline()){
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext() , NoConnectionActivity.class);
            startActivity(intent);


        }else if (spinner.getVisibility() == View.INVISIBLE) {

            zoom = googleMap.getCameraPosition().zoom;
            bearing = googleMap.getCameraPosition().bearing;

            //refresh button
            fromOnResume = false;
            spinner.setVisibility(View.VISIBLE);

            mLocationProvider.disconnect();
            mLocationProvider.connect();


            animateCameraPos();


        }

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Log.i("PopupAdapterForMapMark", "  onInfoWindowClose " );

         enableMapOnPress();
    }

    private void animateCameraPos(){

        if(zoom != 0 && onMapPresedLatLng != null){
            Log.i("MyMapsActivity", "zoom != 0 && onMapPresedLatLng != null");
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

            if(zoom != 0){
                Log.i("MyMapsActivity", "zoom != 0");
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
                Log.i("MyMapsActivity", "zoom == 0");
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


        //AddMarkers.whatSnippetIsOpen();
        zoom = googleMap.getCameraPosition().zoom;
        bearing = googleMap.getCameraPosition().bearing;




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

        if(hasPermission) {


            checkPhoneParams();

            mLocationProvider.disconnect();
            mLocationProvider.connect();


            startTimerTask();


            showInstructionalSnackBar();


        }

    }

    private void showInstructionalSnackBar(){

        if(!hasInstructionalSnackBarBeenShown){
            hasInstructionalSnackBarBeenShown = true;

            int randomNum = 0 + (int)(Math.random() * 3);

            Snackbar snackbar = Snackbar
                    .make(view, snackBarInstructions[randomNum], Snackbar.LENGTH_LONG)
                    .setAction("GOT IT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    });

            snackbar.show();


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


//            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
//            builder.setTitle("Location services disabled");
//            builder.setMessage("WaveBus needs to access your location for the best results." +
//                    "Please enable location access.");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.d("AlertDialog", "Positive");
//                    dialog.dismiss();
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            });
//            builder.setNegativeButton("DISMISS", null);
//            builder.show();


        } else if (enabledAirplaneMode) {
            Log.i("MyMapsActivity", "preference == enabledAirplaneMode");


            Intent intent = new Intent(MapsActivity.mContext, NoConnectionActivity.class);
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





    static boolean firstTimer = true;
    public void startTimerTask() {
        Log.i("MyMapsActivity", "startTimerTask()");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MapsActivity.mContext);
        String timeInMSString= prefs.getString(MapsActivity.mContext.getString(R.string.refresh_time_key), "20000");

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


                initializeTimerTask(firstTimer);
                //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
                timer.schedule(timerTask, 20000, timeInMS);



            }

        }
    }

    public void stopTimerTask() {
        Log.i("MyMapsActivity", "stopTimerTask()" );
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }




    public void initializeTimerTask(boolean firstTimer) {
        final boolean firstTime = firstTimer;


        timerTask = new TimerTask() {
            public void run() {
                Log.i("MyMapsActivity", "initializeTimerTask    timerTask");
                //this enables us to reset the timer in onreusme and it will not trigger it automatkly



                Log.i("MyMapsActivity", "initializeTimerTask prior to a callAndParse : " );

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
        return new ArrayList<String>();


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





    @Override
    public void handleNewLocation(Location location) {
        Log.i("MyMapsActivity", "handleNewLocation ");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);


        firstBoot++;
        //setUpMapIfNeeded();

        googleMap.setMyLocationEnabled(true);
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        animateCameraPos();



        selectCorrectLatLng();



        Log.i("MyMapsActivity", "after startTimerTask();");




    }

    private void selectCorrectLatLng(){
        Log.i("MyMapsActivity ", "selectCorrectLatLng " );

        String radius = prefs.getString("KEY1", "301");

        if(onMapPresedLatLng != null){

            callAndParse.getBusStopsAndBusDistances(onMapPresedLatLng, busCodeOfFavBusStops, radius);
        }else{
            callAndParse.getBusStopsAndBusDistances(latLng, busCodeOfFavBusStops, radius);
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




    @Override
    public void displayDialog(String buscode) {
        Log.i("MyMapsActivity", "displayDialog interface");




        final String finalBuscode = buscode;
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);
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

        spinner.setVisibility(View.INVISIBLE);

        stopTimerTask();
    }




    @Override
    public void onMapReady(GoogleMap newGoogleMap) {
        Log.d("MyMapsActivity", "onMapReadypoooo");
        this.googleMap = newGoogleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);


        //enabled by deafault
        enableMapOnPress();





    }

    public void disableMapOnPress(){
        Log.d("MyMapsActivity", "disableMapOnPress");
        googleMap.setOnMapClickListener(null);
        Log.d("MyMapsActivity", "i work once fEnable : " + isMapOnPressEnabled);
        isMapOnPressEnabled = false;
    }

    boolean isMapOnPressEnabled = false;

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


        zoom = googleMap.getCameraPosition().zoom;
        bearing = googleMap.getCameraPosition().bearing;
        selectCorrectLatLng();

        animateCameraPos();
    }


    @Override
    public void removeLoadingIcon() {
        Log.d("MyMapsActivity", "removeLoadingIcon");

        if(spinner.getVisibility() == View.VISIBLE){
            spinner.setVisibility(View.INVISIBLE);
        }
    }
}


