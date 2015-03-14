package clearfaun.com.pokebuspro;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.util.Log;



/**
 * Created by spencer on 3/9/2015.
 */
public class PrefsFragment extends PreferenceFragment {

    private ListPreference mListPreference;


    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private ListPreference markerUI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("PrefsFragment", "onCreate");
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen prefSet = getPreferenceScreen();

        markerUI = (ListPreference) prefSet.findPreference("pokebus_key_marker");


    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.i("PrefsFragment", "onPreferenceTreeClick");


        Log.i("PrefsFragment", "onPreferenceTreeClick preference " + preference.getKey());

        if (preference.getKey().equals("KEY41")) {
            Log.i("PrefsFragment", "preference == markerUI");
            MapsActivity.addPokeBusMarker();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(this);
            ft.commit();
        }


        return true;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("PrefsFragment", "onActivityCreated");


        try {
            getView().setBackgroundColor(Color.WHITE);
        }catch (Exception e){
            Log.i("PrefsFragment", "Eception e " + e);
        }







    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i("PrefsFragment", "onDestroyView" );
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MapsActivity.mContext);
        String settings= "RefreshTime is: " + sharedPrefs.getString(getString(R.string.refresh_time_key), "NOREFRESHTIME");

        //MapsActivity.toaster(settings);

        MapsActivity.stopTimerTask();
        MapsActivity.updateBusDistance();

    }



}