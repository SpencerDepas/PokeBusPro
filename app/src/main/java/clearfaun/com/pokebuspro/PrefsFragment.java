package clearfaun.com.pokebuspro;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by spencer on 3/9/2015.
 */
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            getView().setBackgroundColor(Color.GRAY);
        }catch (Exception e){
            Log.i("PrefsFragment", "Eception e " + e);
        }

    }
}