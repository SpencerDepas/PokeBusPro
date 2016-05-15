package Preference;

import android.content.SharedPreferences;

import javax.inject.Inject;

import butterknife.BindArray;
import clearfaun.com.pokebuspro.R;

/**
 * Created by SpencerDepas on 5/14/16.
 */public class PreferenceManager {

    private static String PREF_NAME = "prefs";

    private static final String BUS_MAP_SELECTION = "KEY99";
    private static final String RADIUS = "KEY1";
    private static final String REFRESH_TIME = "KEY2";
    private static final String HAS_INSTRUCTIONAL_SNACKBAR_BEEN_DISMISSED = "KEY2";


    @BindArray(R.array.instructional_snackbar)
    protected String [] instructions;



    @Inject
    SharedPreferences sharedPreferences;




    public String getRefreshTime() {
        return this.sharedPreferences.getString(REFRESH_TIME, "200");

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
        return this.sharedPreferences.getString(RADIUS, "NA");

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

}

