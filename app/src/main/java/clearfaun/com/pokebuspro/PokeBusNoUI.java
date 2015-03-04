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
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

/**
 * Created by spencer on 3/4/2015.
 */
public class PokeBusNoUI extends Activity {



    final static public String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MyActivityNOUI", "onCreate");



        toasterShort("Hello World");


    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i("MyActivityNOUI", "onResume");

    }


    void toasterShort(String string){
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        toast.show();
    }


}