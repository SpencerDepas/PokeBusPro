package ui.activity;

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
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Manager.AnswersManager;
import Manager.BusStopDataManager;
import Manager.LocationManager;
import Manager.MapsCameraManager;
import Manager.MarkerManager;
import Manager.PreferenceManager;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clearfaun.com.pokebuspro.LocationProvider;
import clearfaun.com.pokebuspro.R;
import io.fabric.sdk.android.Fabric;
import model.AnswersConstants;
import model.LoadAndSaveFavBusInfo;
import model.MapsCamera;
import model.NavigationViewItems;
import ui.activity.interfaces.AddMarkersCallback;
import ui.activity.interfaces.DialogPopupListener;
import ui.activity.interfaces.NoBusesInAreaInterface;
import ui.activity.interfaces.TimerTaskInterface;
import ui.component.AddMarkers;
import ui.component.PopupAdapterForMapMarkers;
import utils.MarkerRefreshTimerConverter;
import utils.RefreshTimer;
import utils.SystemStatus;


public class MainActivity extends AppCompatActivity implements
        LocationProvider.LocationCallback,
        DialogPopupListener,
        NoBusesInAreaInterface,
        TimerTaskInterface,
        AddMarkersCallback,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowCloseListener {


    private final String TAG = "MainActivity";
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

    static boolean active = false;
    private ArrayList<String> busCodeOfFavBusStops = new ArrayList<>();
    private LoadAndSaveFavBusInfo loadAndSaveFavBusInfo;
    private RefreshTimer refreshTimer;
    boolean oneTimeCall = false;
    private MapsCamera mMapsCamera;

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

        initUI();


        mContext = getApplicationContext();

        preferenceManager = new PreferenceManager(mContext);

        refreshTimerTaskTime = preferenceManager.getRefreshTime();
        savedRadius = preferenceManager.getRadius();

        mMapsCamera = new MapsCamera();


    }

    private void initUI() {

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mMap.getMapAsync(this);

//        fab.setEnabled(false);
//        fab.setClickable(false);
    }


    private void permissionAtRunTime() {

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

                    AnswersManager.getInstance().fineLocationPermissionAsked(AnswersConstants.ACCEPTED);


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED");
                    hasLocationPermission = false;
                    closeDrawer();
                    setUpAfterPermissionRequest();

                    AnswersManager.getInstance().fineLocationPermissionAsked(AnswersConstants.DENIED);

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


            if (hasLocationPermission) {

                Log.i("MyMapsActivity", "hasLocationPermission " + hasLocationPermission);
                mLocationProvider = new LocationProvider(this, this);
                mLocationProvider.disconnect();
                mLocationProvider.connect();
                Log.i("MyMapsActivity", "hasLocationPermission  LocationProvider on" + hasLocationPermission);


                if (!SystemStatus.getInstance().isLocationEnabled()) {

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


        MapsCameraManager.getInstance().saveCameraPositionOnMap();


        refreshMarkers();

        AnswersManager.getInstance().fabOnRefreshPressed();

        MapsCameraManager.getInstance().animateCamera(EMPIRE_STATE_BUILDING_LAT_LNG, mMapsCamera.getZoom(), mMapsCamera.getBearing());


    }


    private void searchForLocationFromAddress() {

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

                        getLatLngForSearchLocationFromAddress(locationToSearchFor.getText().toString());

                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );

                        AnswersManager.getInstance().searchedForBus(locationToSearchFor.getText().toString());

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


                if (!SystemStatus.getInstance().isLocationEnabled()) {

                    Snackbar.make(view, getString(R.string.turn_on_location_snackbar_request), Snackbar.LENGTH_LONG)
                            .show();

                } else {
                    onMapPresedLatLng = null;
                    newLocationFromLatLng(latLng);
                    AnswersManager.getInstance().pressedMyLocation();

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


            AnswersManager.getInstance().launchMapActivty();
            Intent intent = new Intent(mContext, BusMapActivity.class);
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


                        AnswersManager.getInstance().openNavView();
                        mContext = getApplicationContext();
                        prefBusMap = preferenceManager.getBusMapSelection();


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
                                Intent intent = new Intent(mContext, AboutAppActivity.class);
                                startActivity(intent);
                                mDrawerLayout.closeDrawers();

                                break;
                            case NavigationViewItems.SELECT_FAV_MAP:

                                selectDefaultBusMapNavViewSelection();

                                break;
                            case NavigationViewItems.FOLLOW_ME_ON_TWITTER:

                                AnswersManager.getInstance().followMeOnTwitter();
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
            AnswersManager.getInstance().fineLocationDenied();


        } else {

            if (!SystemStatus.getInstance().isLocationEnabled()) {

                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ENABLE_GPS);
                onMapPresedLatLng = null;
                AnswersManager.getInstance().enableLocation();


            } else {
                Toast.makeText(mContext, getString(R.string.you_allready_have_permission),
                        Toast.LENGTH_LONG).show();
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

        AnswersManager.getInstance().deleteFavoriteBusStops();

        closeDrawer();

    }

    private void setRefreshTimerNavViewSelection() {


        MarkerRefreshTimerConverter markerRefreshTimerConverter
                = new MarkerRefreshTimerConverter();
        int preSelectedIndex = markerRefreshTimerConverter.convertStringToInt(refreshTimerTaskTime);

        alertDialogWithList(getString(R.string.auto_refresh_time), preSelectedIndex, timerTaskEntries);


    }

    private void setRadiusNavViewSelection() {

        AnswersManager.getInstance().refreshDialog();

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


    private void alertDialogLogic(String alertDialogTittle, int index) {

        Log.d("MyMapsActivity", "alertDialogTittle.toLowerCase() : " + alertDialogTittle.toLowerCase());

        Log.d("MyMapsActivity", " which : " + index);


        switch (alertDialogTittle.toLowerCase()) {
            case NavigationViewItems.DIALOG_TITTLE_BOROUGH:

                preferenceManager.saveBusMapSelection(boroughs[index]);
                AnswersManager.getInstance().selectMap(boroughs[index]);

                break;
            case NavigationViewItems.DIALOG_TITTLE_SET_RADIUS:

                MarkerManager.getInstance().getMarkerHashTable().clear();
                googleMap.clear();

                savedRadius = radiusEntriesValues[index];
                preferenceManager.saveRadius(radiusEntriesValues[index]);

                preferenceManager.saveRadius(radiusEntriesValues[index]);

                AnswersManager.getInstance().setRadius(radiusEntriesValues[index]);

                progressBar.setVisibility(view.VISIBLE);
                refreshMarkers();

                break;
            case NavigationViewItems.DIALOG_TITTLE_AUTO_REFRESH_TIME:


                if (timerTaskEntriesValues[index] == "0") {

                    refreshTimerTaskTime = timerTaskEntriesValues[index];
                    preferenceManager.saveRefreshTime(timerTaskEntriesValues[index]);

                    refreshTimer.stopTimerTask();

                    AnswersManager.getInstance().setRefreshTime("0");


                } else {

                    refreshTimerTaskTime = timerTaskEntriesValues[index];
                    preferenceManager.saveRefreshTime(timerTaskEntriesValues[index]);
                    refreshMarkers();
                    refreshTimer.stopTimerTask();
                    refreshTimer.startTimerTask();
                    AnswersManager.getInstance().setRefreshTime(timerTaskEntriesValues[index]);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("MyMapsActivity", "onActivityResult");

        if (requestCode == ENABLE_GPS) {
            Log.i("MyMapsActivity", "requestCode==ENABLE_GPS");


            if (SystemStatus.getInstance().isLocationEnabled()) {
                onMapPresedLatLng = null;
                newLocationFromLatLng(latLng);
                closeDrawer();
            }

        }
    }


    private void refreshMarkers() {
        Log.i("MyMapsActivity", "refreshMarkers");


        if (!SystemStatus.getInstance().isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);


        } else {

            MapsCameraManager.getInstance().saveCameraPositionOnMap();

            progressBar.setVisibility(view.VISIBLE);

            if (hasLocationPermission) {

                if (!SystemStatus.getInstance().isLocationEnabled()) {

                    selectCorrectLatLng();

                } else {
                    mLocationProvider.disconnect();
                    mLocationProvider.connect();
                }


            } else {
                Log.i("MyMapsActivity", "hasLocationPermission : " + hasLocationPermission);

                selectCorrectLatLng();
            }


            Marker currentMarker = MarkerManager.getInstance().getMarkerHashTable()
                    .get(PopupAdapterForMapMarkers.sMarkerCurrentKey);

            if (currentMarker != null) {
                MapsCameraManager.getInstance().animateCameraToMarkerMiddleOfScreen(currentMarker,
                        MapsCameraManager.getInstance().isFirstTimeLoadingForCameraAnimation());
            } else {
                MapsCameraManager.getInstance().animateCameraPos();
            }


        }

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Log.i("PopupAdapterForMapMark", "  onInfoWindowClose ");

        enableMapOnPress();

    }

    public void onPause() {
        super.onPause();


        MapsCameraManager.getInstance().saveCameraPositionOnMap();


        closeDrawer();

        refreshTimer.stopTimerTask();
        loadAndSaveFavBusInfo.saveFavBus(busCodeOfFavBusStops);
    }


    public void onDestroy() {
        super.onDestroy();

        MarkerManager.getInstance().getMarkerHashTable().clear();
        active = false;
        mMap = null;
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putFloat("zoom", mMapsCamera.getZoom());
        savedInstanceState.putFloat("bearing", mMapsCamera.getBearing());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mMapsCamera.setZoom(savedInstanceState.getFloat("zoom"));
            mMapsCamera.setBearing(savedInstanceState.getFloat("bearing"));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadAndSaveFavBusInfo = LoadAndSaveFavBusInfo.getInstance(mContext);
        refreshTimer = RefreshTimer.getInstance(MainActivity.this, refreshTimerTaskTime);

        if (hasLocationPermission) {
            SystemStatus.getInstance().checkPhoneParams();
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


        if (!SystemStatus.getInstance().isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);
            this.finish();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);
        LocationManager.getInstance().setUserLatLng(latLng);


        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(new PopupAdapterForMapMarkers(getLayoutInflater()));
        googleMap.setOnInfoWindowCloseListener(this);
        MapsCameraManager.getInstance().animateCameraPos();
        selectCorrectLatLng();
    }

    private void selectCorrectLatLng() {

        if (onMapPresedLatLng != null) {

            BusStopDataManager busStopDataManager = new BusStopDataManager();
            busStopDataManager.getBusStops(this);

            //callAndParse.getBusStops(onMapPresedLatLng, busCodeOfFavBusStops, savedRadius);
        } else {

            if (latLng == null) {
                Log.i("MyMapsActivity ", "Used prefrence manager latLng ");

                latLng = preferenceManager.getLatLng();
                LocationManager.getInstance().setUserLatLng(latLng);


            } else {

                if (latLng.latitude == 0) {
                    Log.i("MyMapsActivity ", "latLng.latitude == 0");

                    latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                    LocationManager.getInstance().setUserLatLng(latLng);

                }

            }
            Log.i("MyMapsActivity ", "getBusStops");

            Log.i("MyMapsActivity ", "latLng : " + latLng.latitude);

            if (latLng.latitude == 0) {
                Log.i("MyMapsActivity ", "latLng.latitude == 0");

                //latLng = EMPIRE_STATE_BUILDING_LAT_LNG;
                LocationManager.getInstance().setUserLatLng(latLng);
            }

            BusStopDataManager busStopDataManager = new BusStopDataManager();
            busStopDataManager.getBusStops(this);
            //callAndParse.getBusStops(latLng, busCodeOfFavBusStops, savedRadius);

        }
    }


    @Override
    public void displayDialog(String buscode) {
        Log.i("MyMapsActivity", "displayDialog interface");


        AnswersManager.getInstance().setFavBusStopDialog();

        final String finalBuscode = buscode;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.set_fav_bus));
        builder.setMessage("BusCode: " + finalBuscode
                + "\n" + "\n" + getString(R.string.set_fav_bus_body));
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                busCodeOfFavBusStops.add(finalBuscode);
                addMarkers.addPokeBusColor(finalBuscode);
                AnswersManager.getInstance().selectedFavBus(finalBuscode);

            }
        });
        builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "dismiss");
                dialog.dismiss();
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


    @Override
    public void onMapReady(GoogleMap newGoogleMap) {
        Log.d("MyMapsActivity", "onMapReadypoooo");
        this.googleMap = newGoogleMap;

        if (MapsCameraManager.getInstance().isFirstTimeLoadingForCameraAnimation()) {
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }

        googleMap.getUiSettings().setMapToolbarEnabled(false);


        mapMarkerClickListener();

        enableMapOnPress();

        if (!oneTimeCall) {
            oneTimeCall = true;

            busCodeOfFavBusStops = loadAndSaveFavBusInfo.loadFavBus();

            permissionAtRunTime();

            if (responseAnsweredForRuntimePermission) {
                refreshTimer.startTimerTask();
            }


        }


    }


    private void mapMarkerClickListener() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                MapsCameraManager.getInstance().animateCameraToMarkerMiddleOfScreen(marker,
                        MapsCameraManager.getInstance().isFirstTimeLoadingForCameraAnimation());
                return true;
            }

        });

    }

    public void disableMapOnPress() {
        googleMap.setOnMapClickListener(null);
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

                    AnswersManager.getInstance().mapOnPress();

                    newLocationFromLatLng(arg0);
                } else {

                    isMapOnPressEnabled = true;
                    //Log.d("MyMapsActivity", "isMapOnPressEnabled : " + isMapOnPressEnabled);
                }
            }
        });


    }

    private void newLocationFromLatLng(LatLng latLng) {

        onMapPresedLatLng = latLng;
        MarkerManager.getInstance().getMarkerHashTable().clear();
        googleMap.clear();
        progressBar.setVisibility(view.VISIBLE);
        MapsCameraManager.getInstance().saveCameraPositionOnMap();

        selectCorrectLatLng();
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

        MapsCameraManager.getInstance().animateCameraToMarkerMiddleOfScreen(marker,
                MapsCameraManager.getInstance().isFirstTimeLoadingForCameraAnimation());
    }

    @Override
    public void runTimer() {
        //called from refreshTimer
        selectCorrectLatLng();
    }
}


