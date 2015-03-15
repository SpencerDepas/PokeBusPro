package clearfaun.com.pokebuspro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import android.preference.PreferenceManager;
import java.util.Properties;

/**
 * Created by spencer on 3/4/2015.
 */
public class PokeBusNoUI extends Activity {

    //this is for when you have poked a bus and you want to toast it.




    static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MyActivityNOUI", "onCreate");

        mContext = getApplicationContext();
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i("MyActivityNOUI", "onResume");

        //makes invisable screen go


        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        long timeStamp = System.currentTimeMillis();
        long limitPresses = sharedpreferences.getLong("limit_presses", 0);
        //only press ever 3 seconds
        if (limitPresses == 0 || limitPresses + 3000 <= timeStamp) {
            Log.i("MyService", "limited presses");
            editor.putLong("limit_presses", timeStamp);
            editor.apply();

            Intent service = new Intent(this, Service.class);
            startService(service);

        }

        finish();
    }





}