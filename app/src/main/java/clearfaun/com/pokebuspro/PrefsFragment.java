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
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by spencer on 3/9/2015.
 */
public class PrefsFragment extends PreferenceFragment {

    static String radius;
    ListPreference prefSumBusMap;
    ListPreference prefRadius;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("PrefsFragment", "onCreate");
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);






        PreferenceScreen prefSumPokeBus =
                (PreferenceScreen) findPreference("KEY41");
        prefSumPokeBus.setSummary("Current PokeBus: " + MapsActivity.getPokeBus());

        prefSumBusMap =
                (ListPreference) findPreference("KEY99");
        prefSumBusMap.setSummary("Current Map is: " + prefSumBusMap.getValue() );








        prefSumBusMap.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Log.i("PrefsFragment", "onPreferenceChange map");
                Log.i("PrefsFragment", "onPreferenceChange map newValue" + newValue.toString() );
                Log.i("PrefsFragment", "onPreferenceChange map preference" + preference.toString() );
                ListPreference prefSumBusMap =
                        (ListPreference) findPreference("KEY99");

                prefSumBusMap.setSummary("Current Map is: " + newValue );
                return true;
            }
        });


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.mContext);

        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("PrefsFragment", "onSharedPreferenceChanged");
                if(key.equals("KEY1")){
                    Log.i("PrefsFragment", "KEY1");
                    MapsActivity.refreshMarkers();
                }
            }
        });

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

        }else if(preference.getKey().equals("KEY2")){
            //make auto refresh kick in

           /* MapsActivity.stopTimerTask();
            MapsActivity.updateBusDistance();*/

        }else if(preference.getKey().equals("KEY4888")){
            //bus map
            Log.i("PrefsFragment", "preference == aboutscreen");
            Intent intent = new Intent(MapsActivity.mContext , BusMap.class);
            intent.putExtra("maptype", prefSumBusMap.getSummary());
            startActivity(intent);

        }else if(preference.getKey().equals("KEY1")){
            //set radius


        }else if(preference.getKey().equals("KEY001")){
            String url = "https://www.facebook.com/ClearFaun?ref=bookmarks";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);


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

       /* MapsActivity.stopTimerTask();
        MapsActivity.updateBusDistance();*/

    }


}