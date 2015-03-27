package clearfaun.com.pokebuspro;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;



/**
 * Created by spencer on 3/9/2015.
 */
public class PrefsFragment extends PreferenceFragment {

    static String radius;
    ListPreference prefSumBusMap;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("PrefsFragment", "onCreate");
        // Load the preferences from an XML resource



        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.mContext);

        PreferenceScreen prefSumPokeBus = (PreferenceScreen) findPreference("KEY41");
        prefSumPokeBus.setSummary("Current PokeBus: " + MapsActivity.getPokeBus());

        prefSumBusMap = (ListPreference) findPreference("KEY99");
        prefSumBusMap.setSummary("Current Map is: " + prefSumBusMap.getValue() );


        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // Implementation
            Log.i("PrefsFragment", "onSharedPreferenceChanged");
            if (key.equals("KEY1")) {
                //for changing radius
                Log.i("PrefsFragment", "KEY1");
                MapsActivity.refreshMarkers();
            }else if(key.equals("KEY99")){
                //changing summery to siaplay current bus map
                ListPreference prefSumBusMap = (ListPreference) findPreference("KEY99");

                prefSumBusMap.setSummary("Current Map is: " + prefSumBusMap.getValue());
            }else if(key.equals("KEY2")){
                //this is to change auto refresh

                MapsActivity.refreshMarkers();


            }
            }
        };

        sp.registerOnSharedPreferenceChangeListener(listener);



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
        }else if(preference.getKey().equals("KEY32")){
            //about screen
            Log.i("PrefsFragment", "preference == aboutscreen");
            Intent intent = new Intent(MapsActivity.mContext , AboutScreen.class);
            startActivity(intent);

        }else if(preference.getKey().equals("KEY4888")){
            //bus map
            Log.i("PrefsFragment", "preference == aboutscreen");
            Intent intent = new Intent(MapsActivity.mContext , BusMap.class);
            intent.putExtra("maptype", prefSumBusMap.getSummary());
            startActivity(intent);

        }else if(preference.getKey().equals("KEY001")){
            String url = "https://www.facebook.com/ClearFaun?ref=bookmarks";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }else if(preference.getKey().equals("KEY033")){
            Log.i("PrefsFragment", "Remove all pokebuses");
            MapsActivity.listPokeBusCode.clear();
            MapsActivity.toaster("Poke Buses deleted");
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



       /* MapsActivity.stopTimerTask();
        MapsActivity.updateBusDistance();*/

    }


}