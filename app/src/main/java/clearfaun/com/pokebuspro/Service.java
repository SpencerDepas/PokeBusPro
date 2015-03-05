package clearfaun.com.pokebuspro;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by spencer on 3/4/2015.
 */
public class Service extends IntentService {


    public Service() {
        super("MyIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent){
        // Handle events on worker thread here
         Log.i("MyService", "Service onHandleIntent ");





        try {

            Log.i("MyService", "in try " );

            Context con = createPackageContext("clearfaun.com.pokebuspro", 0);
            SharedPreferences pref = con.getSharedPreferences(
                    "pokeBusCodePrefs", Context.MODE_PRIVATE);

            String data = pref.getString("pokeBusCode", "No Value");
            new ToastMessageTask().execute("Saved PokeBus is " + data);


        } catch (Exception e) {
            Log.e("Not data shared", e.toString());
            Log.i("MyService", "in catch " );
        }



      /*  Context serviceContext = MapsActivity.mContext;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(serviceContext);
        Log.i("MyService", "Service prefs " + prefs.toString());
        Log.i("MyService", "Service prefs " + prefs.getString("pokeBusCode", null));
        String pokeBusCode = prefs.getString("pokeBusCode", null);
        if (pokeBusCode != null){


            new ToastMessageTask().execute("Saved PokeBus is " + pokeBusCode);
        }else{

            //toasterShort("A pokeBus has not yet been set." + MapsActivity.busInfo.get(0).getBusCode());
            new ToastMessageTask().execute("A pokeBus has not yet been set.");

        }*/



    }









}