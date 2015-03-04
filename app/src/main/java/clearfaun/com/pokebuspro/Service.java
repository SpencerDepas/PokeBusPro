package clearfaun.com.pokebuspro;

import android.app.IntentService;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by spencer on 3/4/2015.
 */
public class Service extends IntentService {


    static LocationManager locationManager;
    private static String provider;
    int accuracy;



    public Service() {
        super("MyIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent){
        // Handle events on worker thread here
         Log.i("MyService", "Service onHandleIntent ");


    }









}