package utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import ui.activity.NoConnectionActivity;

/**
 * Created by SpencerDepas on 6/20/16.
 */
public class SystemStatus {

    private static Context mContext;
    private boolean mEnabledGPS;
    private static SystemStatus sSystemStatus;


    public static SystemStatus getInstance(Context context) {
        Log.i("SystemStatus", "getInstance()");

        mContext = context;

        if (sSystemStatus == null) {
            sSystemStatus = new SystemStatus(mContext);
        }
        return sSystemStatus;
    }

    private SystemStatus(Context context) {
        this.mContext = context;


    }


    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    private void getLocationEnabled() {

        LocationManager lService = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        mEnabledGPS = lService.isProviderEnabled(LocationManager.GPS_PROVIDER);


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public void checkPhoneParams() {

        getLocationEnabled();

        boolean enabledAirplaneMode = SystemStatus.isAirplaneModeOn(mContext);

        if (!isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            noConnectionIntent();


        } else if (!isLocationEnabled()) {


            Log.i("MyMapsActivity", "!isLocationEnabled(mContext)");


        } else if (enabledAirplaneMode) {
            Log.i("MyMapsActivity", "preference == enabledAirplaneMode");


            noConnectionIntent();

        }

        if (!mEnabledGPS) {

            Log.i("MyMapsActivity", "!enabledGPS");

            toaster("Turn on GPS for best results");
        }


    }

    private void toaster(String string) {
        Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_LONG);
        toast.show();
    }

    private void noConnectionIntent() {
        Intent intent = new Intent(mContext, NoConnectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }


}
