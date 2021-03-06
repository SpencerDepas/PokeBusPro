package ui.activity;

import android.graphics.Point;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.WindowManager;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
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


import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


import Preference.PreferenceManager;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import model.LoadAndSaveFavBusInfo;
import model.NavigationViewItems;
import ui.activity.interfaces.TimerTaskInterface;
import ui.component.AddMarkers;
import clearfaun.com.pokebuspro.LocationProvider;
import ui.component.MarkerManager;
import ui.component.PopupAdapterForMapMarkers;
import clearfaun.com.pokebuspro.R;
import client.CallAndParse;
import ui.activity.interfaces.DialogPopupListener;
import ui.activity.interfaces.AddMarkersCallback;
import ui.activity.interfaces.NoBusesInAreaInterface;
import model.AnswersManager;
import utils.RefreshTimer;
import utils.SystemStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        LocationProvider.LocationCallback,
        DialogPopupListener,
        NoBusesInAreaInterface,
        TimerTaskInterface,
        AddMarkersCallback,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowCloseListener {


    public static GoogleMap googleMap;

    private LatLng onMapPresedLatLng;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    private double latitude;
    private double longitude;
    private Context mContext;
    private boolean isMapOnPressEnabled = false;
    private int SDK_LEVEL;
    private boolean responseAnsweredForRuntimePermission = false;
    private final int ENABLE_GPS = 799;
    final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 68;

    private boolean hasLocationPermission;
    private boolean firstTimeLoadingForCameraAnimation = true;

    static boolean active = false;
    private static int tilt = 30;
    private static float zoom = 16;
    private static float bearing = 40;
    private ArrayList<String> busCodeOfFavBusStops = new ArrayList<>();
    private CallAndParse callAndParse;
    private LoadAndSaveFavBusInfo loadAndSaveFavBusInfo;
    private RefreshTimer refreshTimer;

    private AddMarkers addMarkers;
    private SupportMapFragment mMap;
    private final LatLng EMPIRE_STATE_BUILDING_LAT_LNG = new LatLng(40.748441, -73.985664);

    final String DIALOG_TITLE = "Access Phone Location";
    final String DIALOG_MESSAGE = "WaveBus needs to acces your location to find local bus stops.";

    private PreferenceManager preferenceManager;
    private String prefBusMap = PreferenceManager.BUS_MAP_SELECTION_BROOKLYN;
    private String savedRadius = "300";
    private String refreshTimerTaskTime = "20";
    private Toolbar toolbar;
    private SystemStatus systemStatus;

    @BindArray(R.array.boroughs)
    protected String[] boroughs;

    @BindArray(R.array.radius_entries)
    protected String[] radiusEntries;

    @BindArray(R.array.radius_entries_values)
    protected String[] radiusEntriesValues;

    @BindArray(R.array.timer_entries)
    protected String[] timerTaskEntries;

    @BindArray(R.array.timer_entries_values)
    protected String[] timerTaskEntriesValues;


    @BindView(R.id.refresh_location_fab)
    FloatingActionButton fab;

    @BindView(R.id.main_coordinatorLayout)
    CoordinatorLayout view;
    @BindView(R.id.progress_bar_activity_main)
    ProgressBar progressBar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

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
        Fabric.with(this, new Crashlytics());

        Log.i("MyMapsActivity", "onCreate");


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);


        mContext = getApplicationContext();
        preferenceManager = new PreferenceManager(mContext);

        systemStatus = SystemStatus.getInstance(mContext);


        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mMap.getMapAsync(this);


        refreshTimerTaskTime = preferenceManager.getRefreshTime();
        savedRadius = preferenceManager.getRadius();

        Log.i("MyMapsActivity", "refreshTimerTaskTime :  " + refreshTimerTaskTime);
        fab.setEnabled(false);
        fab.setClickable(false);

    }


    private void permissionAtRunTime() {
        Log.i("MyMapsActivity", "permissionAtRunTime ");

        SDK_LEVEL = android.os.Build.VERSION.SDK_INT;
        if (SDK_LEVEL >= Build.VERSION_CODES.M) {
            // ask runtime for lollipop and above versions
            hasLocationPermission = permissionCheck();
            Log.i("MyMapsActivity", "permissionCheck hasLocationPermission : " + hasLocationPermission);

            if (!hasLocationPermission) {
                Log.i("MyMapsActivity", "permissionCheck !hasLocationPermission");

                boolean hasFineLocationBeenRequested =
                        preferenceManager.getHasFineLocationPermissionBeenAsked();
                if (!hasFineLocationBeenRequested) {
                    //if it has not been asked, ask!
                    showPermissionAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE);
                } else {
                    phonePermissionNotGranted();
                }

            } else {
                responseAnsweredForRuntimePermission = true;
                hasLocationPermission = true;
                setUpAfterPermissionRequest();
            }


        } else {
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
                preferenceManager.saveHasFineLocationPermissionBeenAsked(true);

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_GRANTED");

                    closeDrawer();
                    googleMap.clear();
                    mLocationProvider = new LocationProvider(this, this);


                    onMapPresedLatLng = null;


                    hasLocationPermission = true;
                    setUpAfterPermissionRequest();

                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName(AnswersManager.FINE_LOCATION)
                            .putContentType(AnswersManager.SELECTION)
                            .putCustomAttribute(AnswersManager.RUNTIME_PERMISSION, AnswersManager.ACCEPTED));

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED");
                    hasLocationPermission = false;
                    closeDrawer();
                    setUpAfterPermissionRequest();

                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName(AnswersManager.FINE_LOCATION)
                            .putContentType(AnswersManager.SELECTION)
                            .putCustomAttribute(AnswersManager.RUNTIME_PERMISSION, AnswersManager.DENIED));

                }

            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void setUpAfterPermissionRequest() {
        Log.i("MyMapsActivity ", "setUpAfterPermissionRequest");

        Log.i("MyMapsActivity ", "responseAnsweredForRuntimePermission : " + responseAnsweredForRuntimePermission);
        if (responseAnsweredForRuntimePermission) {

            addMarkers = AddMarkers.getInstance();
            addMarkers.setInterface(MainActivity.this);


            PopupAdapterForMapMarkers.mPopupListner = MainActivity.this;
            callAndParse = new CallAndParse(MainActivity.this);


            if (hasLocationPermission) {

                Log.i("MyMapsActivity", "hasLocationPermission " + hasLocationPermission);
                mLocationProvider = new LocationProvider(this, this);
                mLocationProvider.disconnect();
                mLocationProvider.connect();
                Log.i("MyMapsActivity", "hasLocationPermission  LocationProvider on" + hasLocationPermission);


                boolean isLocationEnabled = systemStatus.isLocationEnabled();
                if (!isLocationEnabled) {
                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled");

                    latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                    onMapPresedLatLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                    refreshMarkers();

                }


            } else {
                Log.i("MyMapsActivity", "hasLocationPermission " + hasLocationPermission);

                latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                onMapPresedLatLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                refreshMarkers();


            }


        }


        // permission denied, boo! Disable the
        // functionality that depends on this permission.
    }


    private void phonePermissionNotGranted() {
        Log.d("MyMapsActivity", "phonePermissionNotGranted");
        mMap.getMapAsync(this);
        hasLocationPermission = false;
        responseAnsweredForRuntimePermission = true;
        setUpAfterPermissionRequest();


    }

    private void showPermissionAlertDialog(String title, String message) {
        Log.d("MyMapsActivity", "showPermissionAlertDialog");


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


    @SuppressWarnings("unused")
    @OnClick(R.id.refresh_location_fab)
    public void refreshLocation(View view) {
        Log.i("MyMapsActivity", "onClick refreshLocation");


        refreshMarkers();

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersManager.FAB_ON_REFRESH)
                .putContentType(AnswersManager.ACTION));

    }


    private void searchForLocationFromAddress() {
        Log.i("MyMapsActivity", "onClick searchForLocation");


        //brings up keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

                        getLatLngForSearchLocationFromAddress(locationToSearchFor.getText().toString() + "new york ny");

                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );

                        Answers.getInstance().logContentView(new ContentViewEvent()
                                .putContentName(AnswersManager.SEARCHED_FOR_BUS)
                                .putContentType(AnswersManager.SELECTION)
                                .putCustomAttribute(AnswersManager.ADDRESS_SEARCHED, locationToSearchFor.getText().toString()));


                    }

                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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


    private void getLatLngForSearchLocationFromAddress(String location) {
        Log.i("MyMapsActivity", "getLatLngForSearchLocationFromAddress ");
        List<Address> addressList = null;

        if (location != null && location.length() > 0) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 2);
            } catch (IOException e) {
                e.printStackTrace();

            }


            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng onSearchLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                newLocationFromLatLng(onSearchLatLng);
            } else {
                Snackbar.make(view, getString(R.string.location_not_found), Snackbar.LENGTH_LONG)
                        .show();
            }


        }

    }

    private boolean permissionCheck() {
        Log.i("MyMapsActivity ", "permissionCheck");
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == 0) {
            return true;
        }

        Log.i("MyMapsActivity ", "permissionCheck == " + permissionCheck);

        return false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_main_actvity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.my_location_item) {
            if (hasLocationPermission) {


                boolean isLocationEnabled = systemStatus.isLocationEnabled();
                if (!isLocationEnabled) {
                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled");

                    Snackbar.make(view, getString(R.string.turn_on_location_snackbar_request), Snackbar.LENGTH_LONG)
                            .show();

                } else {
                    onMapPresedLatLng = null;
                    newLocationFromLatLng(latLng);
                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName(AnswersManager.PRESSED_MY_LOCATION)
                            .putContentType(AnswersManager.ACTION));
                }

            } else {
                Snackbar.make(view, getString(R.string.turn_on_location_snackbar_request), Snackbar.LENGTH_LONG)
                        .show();
            }


        } else if (item.getItemId() == R.id.search_item) {
            searchForLocationFromAddress();
        } else if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.map_item) {


            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(AnswersManager.LAUNCH_MAP_ACTIVITY)
                    .putContentType(AnswersManager.ACTION)
            );

            Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);
            Intent intent = new Intent(mContext, BoroughBusMapActivity.class);
            intent.putExtra("maptype", "Current Map is: " + prefBusMap);
            startActivity(intent);


        }


        return super.onOptionsItemSelected(item);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Answers.getInstance().logContentView(new ContentViewEvent()
                                .putContentName("Nav view open")
                                .putContentType(AnswersManager.ACTION)
                        );

                        mContext = getApplicationContext();
                        prefBusMap = preferenceManager.getBusMapSelection();

                        Intent intent = new Intent(mContext, AboutAppActivity.class);


                        switch (menuItem.getTitle().toString()) {
                            case NavigationViewItems.SET_RADIUS:

                                setRadiusNavViewSelection();

                                break;

                            case NavigationViewItems.SET_AUTO_REFRESH_TIME:

                                setRefreshTimerNavViewSelection();

                                break;

                            case NavigationViewItems.DELETE_SAVED_BUS_STOPS:

                                deleteSavedFavoriteBusesNavViewSelection();

                                break;

                            case NavigationViewItems.ABOUT_BUSBUS:

                                startActivity(intent);
                                mDrawerLayout.closeDrawers();

                                break;
                            case NavigationViewItems.SELECT_FAV_MAP:

                                selectDefaultBusMapNavViewSelection();

                                break;
                            case NavigationViewItems.FOLLOW_ME_ON_TWITTER:

                                Answers.getInstance().logContentView(new ContentViewEvent()
                                        .putContentName("Follow me on twitter")
                                        .putContentType(AnswersManager.ACTION)

                                );
                                openTwitterIntent();
                                closeDrawer();

                                break;
                            case NavigationViewItems.LICENSE:

                                intent = new Intent(mContext, LicenseActivity.class);
                                startActivity(intent);
                                closeDrawer();


                                break;
                            case NavigationViewItems.ENABLE_LOCATION:

                                enableLocationNavViewSelection();

                                break;


                        }


                        return true;
                    }
                });
    }

    private void enableLocationNavViewSelection() {

        if (!hasLocationPermission) {
            Log.d("MyMainActivity", "!hasLocationPermission || SDK_LEVEL < 23");
            showPermissionAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE);
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName("Enable Location")
                    .putContentType(AnswersManager.ACTION)
                    .putCustomAttribute("Fine permission enabled", "False")
            );
        } else {

            boolean isLocationEnabled = systemStatus.isLocationEnabled();
            if (!isLocationEnabled) {
                Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled");

                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ENABLE_GPS);

                onMapPresedLatLng = null;

                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Enable Location")
                        .putContentType(AnswersManager.ACTION)
                        .putCustomAttribute("Fine permission enabled", "true")
                        .putCustomAttribute("Enable GPS intent fired", "true")
                );

            } else {
                Toast.makeText(mContext, getString(R.string.you_allready_have_permission),
                        Toast.LENGTH_LONG).show();
                ;

                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Enable Location")
                        .putContentType(AnswersManager.ACTION)
                        .putCustomAttribute("Location allready enabled", "true")
                );
            }

        }

    }


    private void selectDefaultBusMapNavViewSelection() {


        final String findWhatToPreSelect = preferenceManager.getBusMapSelection();

        int preSelectedIndex = 0;

        switch (findWhatToPreSelect) {
            case PreferenceManager.BUS_MAP_SELECTION_BROOKLYN:
                preSelectedIndex = 0;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_MANHATTAN:
                preSelectedIndex = 1;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_QUEENS:
                preSelectedIndex = 2;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_BRONX:
                preSelectedIndex = 3;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_STATEN_ISLAND:
                preSelectedIndex = 4;
                break;
            default:
                preSelectedIndex = 0;
                break;

        }


        alertDialogWithList(getString(R.string.select_bus_map_tittle), preSelectedIndex, boroughs);


    }


    private void deleteSavedFavoriteBusesNavViewSelection() {


        //removes change of color from icon color
        addMarkers.removePokeBusColor(busCodeOfFavBusStops);

        removeSavedFavBusFromStorage();


        Toast.makeText(mContext, getString(R.string.removed_fav_bus),
                Toast.LENGTH_LONG).show();


        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Deleted favorite Buses")
                .putContentType(AnswersManager.ACTION)

        );

        closeDrawer();

    }

    private void setRefreshTimerNavViewSelection() {


        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Refresh Timer Dialog")
                .putContentType(AnswersManager.ACTION)
        );


        final String findWhatToPreSelect = refreshTimerTaskTime;

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
        } catch (Exception e) {
            preSelectedIndex = 3;
        }


        alertDialogWithList(getString(R.string.auto_refresh_time), preSelectedIndex, timerTaskEntries);


    }

    private void setRadiusNavViewSelection() {

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Refresh Timer Dialog")
                .putContentType(AnswersManager.ACTION)
        );


        final String findWhatToPreSelect = savedRadius;

        int preSelectedIndex = -1;
        switch (Integer.parseInt(findWhatToPreSelect)) {
            case 200:
                preSelectedIndex = 0;
                break;
            case 250:
                preSelectedIndex = 1;
                break;
            case 300:
                preSelectedIndex = 2;
                break;
            default:
                preSelectedIndex = 2;
                break;


        }

        alertDialogWithList(getString(R.string.dialog_set_radius), preSelectedIndex, radiusEntries);


    }

    private void openTwitterIntent() {
        String mTwitterName = "spencerdepas";
        Intent intent = null;
        try {
            // get the Twitter app if possible


            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" +
                    mTwitterName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        } catch (Exception e) {
            Log.d("MyMapsActivity", "e :  " + e.toString());
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" +
                    mTwitterName));
            startActivity(intent);


        }

    }


    private void alertDialogWithList(final String tittle, int preSelectedIndex, final String[] items) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(tittle);
        builder.setNegativeButton(R.string.dimiss, null);
        builder.setSingleChoiceItems(items, preSelectedIndex, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, final int which) {
                Log.d("AlertDialog", "Positive");
                dialog.dismiss();


                alertDialogLogic(tittle, which);

            }
        });
        builder.show();


    }


    private void alertDialogLogic(String alertDialogTittle, int which) {

        Log.d("MyMapsActivity", "alertDialogTittle.toLowerCase() : " + alertDialogTittle.toLowerCase());

        Log.d("MyMapsActivity", " which : " + which);


        switch (alertDialogTittle.toLowerCase()) {
            case NavigationViewItems.DIALOG_TITTLE_BOROUGH:

                Log.d("MyMapsActivity", "alertDialogLogic Select your borough boroughs[which] : " + boroughs[which]);


                preferenceManager.saveBusMapSelection(boroughs[which]);
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Select Map")
                        .putContentType("Selection")
                        .putCustomAttribute(AnswersManager.MAP_SELECTION, boroughs[which])
                );


                break;
            case NavigationViewItems.DIALOG_TITTLE_SET_RADIUS:

                Log.d("MyMapsActivity", "alertDialogLogic Select your radius radius[which] : " + radiusEntriesValues[which]);

                MarkerManager markerManager = MarkerManager.getInstance();

                Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();

                markerHashTable.clear();
                googleMap.clear();

                savedRadius = radiusEntriesValues[which];
                preferenceManager.saveRadius(radiusEntriesValues[which]);

                preferenceManager.saveRadius(radiusEntriesValues[which]);

                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersManager.SET_RADIUS)
                        .putContentType(AnswersManager.ACTION)
                        .putCustomAttribute(AnswersManager.RADIUS, radiusEntriesValues[which])

                );


                progressBar.setVisibility(view.VISIBLE);
                refreshMarkers();

                break;
            case NavigationViewItems.DIALOG_TITTLE_AUTO_REFRESH_TIME:


                if (timerTaskEntriesValues[which] == "0") {
                    Log.i("MyMapsActivity", "timerTaskEntriesValues[which] : " + timerTaskEntriesValues[which]);

                    refreshTimerTaskTime = timerTaskEntriesValues[which];
                    preferenceManager.saveRefreshTime(timerTaskEntriesValues[which]);

                    refreshTimer.stopTimerTask();


                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName(AnswersManager.REFRESH_TIMER_TIME)
                            .putContentType(AnswersManager.SELECTION)
                            .putCustomAttribute(AnswersManager.TIME, "0")
                    );

                } else {

                    refreshTimerTaskTime = timerTaskEntriesValues[which];
                    preferenceManager.saveRefreshTime(timerTaskEntriesValues[which]);
                    refreshMarkers();
                    refreshTimer.stopTimerTask();


                    refreshTimer.startTimerTask();
                    Log.i("MyMapsActivity", " refreshMarkers();");

                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName(AnswersManager.REFRESH_TIMER_TIME)
                            .putContentType(AnswersManager.SELECTION)
                            .putCustomAttribute(AnswersManager.TIME, timerTaskEntriesValues[which])
                    );


                }


                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("MyMapsActivity", "onActivityResult");

        if (requestCode == ENABLE_GPS) {
            Log.i("MyMapsActivity", "requestCode==ENABLE_GPS");


            boolean isLocationEnabled = systemStatus.isLocationEnabled();
            if (isLocationEnabled) {
                Log.i("MyMapsActivity", "onActivityResult isLocationEnabled");


                onMapPresedLatLng = null;
                newLocationFromLatLng(latLng);
                closeDrawer();
            }

        }
    }


    private void refreshMarkers() {
        Log.i("MyMapsActivity", "refreshMarkers");

        if (!systemStatus.isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);


        } else {

            zoom = googleMap.getCameraPosition().zoom;
            bearing = googleMap.getCameraPosition().bearing;


            progressBar.setVisibility(view.VISIBLE);

            if (hasLocationPermission) {
                Log.i("MyMapsActivity", "hasLocationPermission  " + hasLocationPermission);


//                zoom = 16;
//                bearing = 40;
                boolean isLocationEnabled = systemStatus.isLocationEnabled();
                if (!isLocationEnabled) {
                    Log.i("MyMapsActivity", "hasLocationPermission !isLocationEnabled");

                    selectCorrectLatLng();

                } else {
                    mLocationProvider.disconnect();
                    mLocationProvider.connect();
                }


            } else {
                Log.i("MyMapsActivity", "hasLocationPermission : " + hasLocationPermission);

                selectCorrectLatLng();
            }

            MarkerManager markerManager = MarkerManager.getInstance();
            Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();

            Marker currentMarker = markerHashTable.get(PopupAdapterForMapMarkers.sMarkerCurrentKey);


            if(currentMarker != null){

                animateCameraToMarkerMiddleOfScreen(currentMarker, firstTimeLoadingForCameraAnimation);
            }else{
                animateCameraPos();
            }




        }

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Log.i("PopupAdapterForMapMark", "  onInfoWindowClose ");

        enableMapOnPress();

    }


    private void animateCameraPos() {
        Log.i("MyMapsActivity", "animateCameraPos");

        Log.i("MyMapsActivity", "animateCameraPos zoom : " + zoom);
        Log.i("MyMapsActivity", "onMapPresedLatLng : " + onMapPresedLatLng);

        if (onMapPresedLatLng != null) {
            Log.i("MyMapsActivity", "animateCameraPos onMapPresedLatLng != null");
            Log.i("MyMapsActivity", "animateCameraPos zoom : " + zoom);
            //this is for location from touch or search
            if (zoom > 4) {
                Log.i("MyMapsActivity", "animateCameraPos bearing != 0");

                animateCamera(onMapPresedLatLng, zoom, bearing);


            } else {
                Log.i("MyMapsActivity", "animateCameraPos bearing == 0");

                animateCamera(onMapPresedLatLng, 16, 40);


            }


        } else if (zoom > 4) {
            Log.i("MyMapsActivity", "animateCameraPos bearing zoom > 4");


            if (latLng.latitude == 0) {
                Log.i("MyMapsActivity ", "latLng.latitude == 0");

                animateCamera(EMPIRE_STATE_BUILDING_LAT_LNG, zoom, bearing);

            } else {
                animateCamera(latLng, zoom, bearing);

            }


        } else {
            Log.i("MyMapsActivity", "animateCameraPos bearing == 0");


            if (latLng.latitude == 0) {
                Log.i("MyMapsActivity ", "latLng.latitude == 0");

                animateCamera(EMPIRE_STATE_BUILDING_LAT_LNG, 16, 40);

            } else {
                animateCamera(latLng, 16, 40);

            }

        }


    }


    private void animateCamera(LatLng target, float zoom, float bearing) {
        Log.i("MyMapsActivity", "tst animateCamera() being called ");



        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)    // Sets the center of the map to Mountain View
                .zoom(zoom)                   // Sets the zoom
                .bearing(bearing)                // Sets the orientation of the camera to east
                //.tilt(tilt)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                //DO some stuff here!
                Log.d("MyMapsActivity", "onFinishCalled");
                if (firstTimeLoadingForCameraAnimation) {

                    firstTimeLoadingForCameraAnimation = false;

                    Log.d("MyMapsActivity", "onFinishCalled tst firstTimeLoading  :"
                            + firstTimeLoadingForCameraAnimation);



                    googleMap.getUiSettings().setAllGesturesEnabled(true);

                }
                fab.setEnabled(true);
                fab.setClickable(true);
                saveCameraFields();
            }

            @Override
            public void onCancel() {
                Log.d("MyMapsActivity", "onCancel");



            }
        });
        googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));
        googleMap.setOnInfoWindowCloseListener(this);


    }


    private void saveCameraFields() {
        Log.d("MyMapsActivity", "saveCameraFields : ");

        Log.d("MyMapsActivity", "tst tilt : " + tilt);
        Log.d("MyMapsActivity", "tst bearing : " + bearing);
        Log.d("MyMapsActivity", "tst animateCamera zoom : " + zoom);


        tilt = (int) googleMap.getCameraPosition().tilt;
        zoom = googleMap.getCameraPosition().zoom;
        bearing = googleMap.getCameraPosition().bearing;


    }

    public void onPause() {
        super.onPause();
        Log.i("MyMapsActivity", "onPause()");


        if (zoom != 0) {
            tilt = (int) googleMap.getCameraPosition().tilt;
            zoom = googleMap.getCameraPosition().zoom;
            bearing = googleMap.getCameraPosition().bearing;
            Log.i("MyMapsActivity", "saving zoom :" + zoom);
            Log.i("MyMapsActivity", "saving bearking : " + bearing);
        }

        closeDrawer();

        refreshTimer.stopTimerTask();
        loadAndSaveFavBusInfo.saveFavBus(busCodeOfFavBusStops);


    }


    public void onDestroy() {
        super.onDestroy();
        Log.i("MyMapsActivity", "onDestroy()");

        MarkerManager markerManager = MarkerManager.getInstance();
        Hashtable<String, Marker> markerHashTable = markerManager.getMarkerHashTable();

        markerHashTable.clear();

        active = false;
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

        Log.i("MyMapsActivity", "onResume() hasLocationPermission: " + hasLocationPermission);


        Log.i("MyMapsActivity", "mMap!= null" + (mMap!= null));
        Log.i("MyMapsActivity", "googleMap!= null)" + (googleMap!= null));
        systemStatus = SystemStatus.getInstance(mContext);
        loadAndSaveFavBusInfo = LoadAndSaveFavBusInfo.getInstance(mContext);
        refreshTimer = RefreshTimer.getInstance(MainActivity.this, refreshTimerTaskTime);


        if (hasLocationPermission) {
            Log.i("MyMapsActivity", "onResume() hasLocationPermission" + hasLocationPermission);


            systemStatus.checkPhoneParams();

            mLocationProvider.disconnect();
            mLocationProvider.connect();

            refreshTimer.startTimerTask();


        } else {


            setUpAfterPermissionRequest();


        }


    }


    private void removeSavedFavBusFromStorage() {


        busCodeOfFavBusStops.clear();
        Log.i("MyMapsActivity", "busCodeOfFavBusStops.size : " + busCodeOfFavBusStops.size());
    }


    @Override
    public void handleNewLocation(Location location) {
        Log.i("MyMapsActivity", "handleNewLocation ");

        if (!systemStatus.isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);
            this.finish();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);


        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));

        //animateCameraPos();

        selectCorrectLatLng();


    }

    private void selectCorrectLatLng() {
        Log.i("MyMapsActivity ", "selectCorrectLatLng ");


        if (callAndParse == null) {
            callAndParse = new CallAndParse(MainActivity.this);
        }


        if (onMapPresedLatLng != null) {

            callAndParse.getBusStops(onMapPresedLatLng, busCodeOfFavBusStops, savedRadius);
        } else {

            if (latLng == null) {
                Log.i("MyMapsActivity ", "Used prefrence manager latLng ");

                latLng = preferenceManager.getLatLng();


            } else {

                if (latLng.latitude == 0) {
                    Log.i("MyMapsActivity ", "latLng.latitude == 0");

                    latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                }

            }
            Log.i("MyMapsActivity ", "getBusStops");

            Log.i("MyMapsActivity ", "latLng : " + latLng.latitude);

            if (latLng.latitude == 0) {
                Log.i("MyMapsActivity ", "latLng.latitude == 0");

                latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
            }


            callAndParse.getBusStops(latLng, busCodeOfFavBusStops, savedRadius);

        }
    }


    @Override
    public void displayDialog(String buscode) {
        Log.i("MyMapsActivity", "displayDialog interface");


        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersManager.SET_FAV_BUS_STOP_DIALOG)
                .putContentType(AnswersManager.ACTION)
        );


        final String finalBuscode = buscode;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.set_fav_bus));
        builder.setMessage("BusCode: " + finalBuscode
                + "\n" + "\n" + getString(R.string.set_fav_bus_body));
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "Positive");
                dialog.dismiss();


                busCodeOfFavBusStops.add(finalBuscode);
                addMarkers.addPokeBusColor(finalBuscode);

                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersManager.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersManager.SELECTION)
                        .putCustomAttribute(AnswersManager.FAV_BUS_STOP, finalBuscode)
                );


            }
        });
        builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "dismiss");
                dialog.dismiss();


                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersManager.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersManager.SELECTION)
                        .putCustomAttribute(AnswersManager.FAV_BUS_STOP_DISMISSED, AnswersManager.DISMISSED)
                );
            }
        });
        builder.show();
    }


    @Override
    public void noBusesFound() {
        Log.d("AlertDialog", "noBusesFound");
        Snackbar.make(view, "No Buses found in the area", Snackbar.LENGTH_LONG)
                .show();

        progressBar.setVisibility(view.INVISIBLE);


        refreshTimer.stopTimerTask();
    }


    boolean oneTimeCall = false;


    @Override
    public void onMapReady(GoogleMap newGoogleMap) {
        Log.d("MyMapsActivity", "onMapReadypoooo");
        this.googleMap = newGoogleMap;
        if (firstTimeLoadingForCameraAnimation) {
            googleMap.getUiSettings().setAllGesturesEnabled(false);

        }

        googleMap.getUiSettings().setMapToolbarEnabled(false);


        mapMarkerClickListener();

        enableMapOnPress();

        if (!oneTimeCall) {
            oneTimeCall = true;

            busCodeOfFavBusStops = loadAndSaveFavBusInfo.loadFavBus();

            permissionAtRunTime();


            refreshTimer.startTimerTask();


        }


    }


    private void mapMarkerClickListener() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {



                animateCameraToMarkerMiddleOfScreen(marker, firstTimeLoadingForCameraAnimation);


                return true;
            }

        });

    }

    private void animateCameraToMarkerMiddleOfScreen(Marker marker, boolean firstTimeLoading) {


        Log.d("MyMapsActivity", "tst animateCameraToMarkerMiddleOfScreen tilt " + tilt);
        Log.d("MyMapsActivity", "tst animateCameraToMarkerMiddleOfScreen zoom : " + zoom);
        Log.d("MyMapsActivity", "tst animateCameraToMarkerMiddleOfScreen bearing : " + bearing);

        LatLng aboveMarkerLatLng;
        Log.d("MyMapsActivity", "tst firstTimeLoading  :"  + firstTimeLoading);



        if (!firstTimeLoading) {
            Log.d("MyMapsActivity", "tst !firstTimeLoading  ");

            saveCameraFields();

            NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
            int container_height = navView.getHeight();

            Projection projection = googleMap.getProjection();

            LatLng markerLatLng = new LatLng(marker.getPosition().latitude,
                    marker.getPosition().longitude);
            Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
            Point pointHalfScreenAbove = new Point(markerScreenPosition.x,
                    markerScreenPosition.y - (int) (container_height / 3.1));

            aboveMarkerLatLng = projection
                    .fromScreenLocation(pointHalfScreenAbove);
        } else {

            aboveMarkerLatLng = marker.getPosition();
        }


        marker.showInfoWindow();
        animateCamera(aboveMarkerLatLng, zoom, bearing);


    }

    public void disableMapOnPress() {
        Log.d("MyMapsActivity", "disableMapOnPress");
        googleMap.setOnMapClickListener(null);
        Log.d("MyMapsActivity", "i work once fEnable : " + isMapOnPressEnabled);
        isMapOnPressEnabled = false;
    }


    public void enableMapOnPress() {
        Log.d("MyMapsActivity", "enableMapOnPress");

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                Log.d("MyMapsActivity", "onMapClick");

                //Log.d("MyMapsActivity", "fEnable : " + fEnable);
                if (isMapOnPressEnabled) {
                    isMapOnPressEnabled = false;
                    // TODO Auto-generated method stub
                    Log.d("MyMapsActivity", arg0.latitude + "-" + arg0.longitude);

                    Answers.getInstance().logContentView(new ContentViewEvent()
                            .putContentName(AnswersManager.MAP_ON_PRESS)
                            .putContentType(AnswersManager.SELECTION)
                    );

                    newLocationFromLatLng(arg0);
                } else {

                    isMapOnPressEnabled = true;
                    //Log.d("MyMapsActivity", "isMapOnPressEnabled : " + isMapOnPressEnabled);
                }


            }
        });


    }

    private void newLocationFromLatLng(LatLng latLng) {
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


        //animateCameraPos();
    }


    @Override
    public void removeLoadingIcon() {
        Log.d("MyMapsActivity", "removeLoadingIcon");

        //showInstructionalSnackBar();

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(view.INVISIBLE);

            if (onMapPresedLatLng != null) {
                if (onMapPresedLatLng.equals(EMPIRE_STATE_BUILDING_LAT_LNG)) {
                    Log.d("MyMapsActivity", "latLng.equals(EMPIRE_STATE_BUILDING_LAT_LNG)");

                    Snackbar.make(view, "Location not available", Snackbar.LENGTH_LONG)
                            .show();
                }
            }

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }




    @Override
    public void animateCameraToMarker(Marker marker) {

        Log.d("MyMapsActivity", "animateCameraToMarker");
        Log.d("MyMapsActivity", "zoom : " + zoom);
        Log.d("MyMapsActivity", "bearing : " + bearing);


        animateCameraToMarkerMiddleOfScreen(marker, firstTimeLoadingForCameraAnimation);
    }

    @Override
    public void runTimer() {
        //called from refreshTimer
        Log.d("MyMapsActivity", "runTimer()");

        selectCorrectLatLng();
    }
}


