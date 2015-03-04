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


    final static public String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MyActivityNOUI", "onCreate");


    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i("MyActivityNOUI", "onResume");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pokeBusCode = prefs.getString("pokeBusCode", null);
        if (pokeBusCode != null){

            toasterShort("Saved PokeBus is " + pokeBusCode);

        }else{

            //toasterShort("A pokeBus has not yet been set." + MapsActivity.busInfo.get(0).getBusCode());
            toasterShort("A pokeBus has not yet been set.");
        }
        //makes invisable screen go
        finish();
        /*Intent service = new Intent(this, Service.class);
        startService(service);*/

    }


    void toasterShort(String string){
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        toast.show();
    }


}