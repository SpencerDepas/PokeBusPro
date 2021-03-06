package Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import butterknife.BindArray;
import clearfaun.com.pokebuspro.R;

/**
 * Created by SpencerDepas on 5/14/16.
 */
public class PreferenceManager {

    private static String PREF_NAME = "prefs";

    private static final String BUS_MAP_SELECTION = "KEY99";
    private static final String RADIUS = "KEY1";
    private static final String REFRESH_TIME = "KEY2";
    private static final String FINE_LOCATION_PERMISSION_ASKED = "fine_location_permission_has_been_asked";
    private static final String LAST_KNOWN_LAT = "last_known_lat";
    private static final String LAST_KNOWN_LNG = "last_known_lng";


    private static final String HAS_INSTRUCTIONAL_SNACKBAR_BEEN_DISMISSED = "KEY2";
    private static int randomNum = 0;


    public static final String BUS_MAP_SELECTION_BROOKLYN = "Brooklyn";
    public static final String BUS_MAP_SELECTION_QUEENS = "Queens";
    public static final String BUS_MAP_SELECTION_MANHATTAN = "Manhattan";
    public static final String BUS_MAP_SELECTION_BRONX = "Bronx";
    public static final String BUS_MAP_SELECTION_STATEN_ISLAND = "Staten Island";


    @BindArray(R.array.instructional_snackbar)
    protected String[] instructions;

    SharedPreferences sharedPreferences;


    public PreferenceManager(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
    }


    public boolean getHasFineLocationPermissionBeenAsked() {
        return this.sharedPreferences.getBoolean(FINE_LOCATION_PERMISSION_ASKED, false);

    }

    public void saveHasFineLocationPermissionBeenAsked(boolean value) {
        saveValue(FINE_LOCATION_PERMISSION_ASKED, value);
    }

    public void saveLatLng(LatLng value) {
        saveValue(LAST_KNOWN_LAT, value.latitude);
        saveValue(LAST_KNOWN_LNG, value.longitude);
    }

    public LatLng getLatLng() {
        LatLng latLng = new LatLng(
                this.sharedPreferences.getLong(LAST_KNOWN_LAT, 0),
                this.sharedPreferences.getLong(LAST_KNOWN_LNG, 0));

        return latLng;
    }


    public boolean getHasInstructionalSnackBarBeenDismissed() {
        return this.sharedPreferences.getBoolean(HAS_INSTRUCTIONAL_SNACKBAR_BEEN_DISMISSED, false);
    }

    public void saveHasInstructionalSnackBarBeenDismissed(boolean value) {
        saveValue(HAS_INSTRUCTIONAL_SNACKBAR_BEEN_DISMISSED, value);
    }

    public String getRefreshTime() {
        return this.sharedPreferences.getString(REFRESH_TIME, "20");

    }

    public boolean getinstructionalSnackbar() {
        randomNum = 0 + (int) (Math.random() * 3);
        return this.sharedPreferences.getBoolean(instructions[randomNum], false);

    }

    public void saveRefreshTime(String refreshTime) {
        saveValue(REFRESH_TIME, refreshTime);
    }

    public String getBusMapSelection() {
        return this.sharedPreferences.getString(BUS_MAP_SELECTION, "Brooklyn");
    }

    public void saveBusMapSelection(String busMapSelection) {
        saveValue(BUS_MAP_SELECTION, busMapSelection);
    }

    public String getRadius() {
        return this.sharedPreferences.getString(RADIUS, "300");

    }


    public void saveRadius(String radius) {
        saveValue(RADIUS, radius);
    }


    protected void saveValue(String key, String value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected void saveValue(String key, long value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    protected void saveValue(String key, int value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected void saveValue(String key, boolean value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public void saveValue(String key, double value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();

    }


}

