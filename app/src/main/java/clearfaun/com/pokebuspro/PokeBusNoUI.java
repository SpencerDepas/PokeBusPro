package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import task.ToastMessageTask;

/**
 * Created by spencer on 3/4/2015.
 */
public class PokeBusNoUI extends Activity implements
        LocationProvider.LocationCallback  {
    //this is for when you have poked a bus and you want to toast it.


    double latitude;
    double longitude;
    static LatLng latLng;
    static Context mContext;
    private LocationProvider mLocationProvider;
    static boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MyActivityNOUI", "onCreate");

        mContext = getApplicationContext();
        mLocationProvider = new LocationProvider(this, this);

        if(!isOnline()){
            Log.i("PokeBusNoUI", "!isOnline()");
            new ToastMessageTask().execute("No connection" + "\n" +
                    "Please connect to the internet" );


            finish();
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("MyActivityNOUI", "onResume");

        mLocationProvider.connect();
        //makes invisable screen go


        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        long timeStamp = System.currentTimeMillis();
        long limitPresses = sharedpreferences.getLong("limit_presses", 0);
        Log.i("MyService", "limited == " + limitPresses);
        //only press ever 3 seconds
        if (limitPresses == 0 || limitPresses + 5000 <= timeStamp) {
            Log.i("MyService", "limited presses");
            editor.putLong("limit_presses", timeStamp);
            editor.apply();

            Intent service = new Intent(this, Service.class);
            startService(service);

        }else{
            Log.i("MyService", "you have been limited presses");
        }

        finish();
    }


    @Override
    public void handleNewLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);

    }
}