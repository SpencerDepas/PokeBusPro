package ui.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import utils.SystemStatus;

/**
 * Created by SpencerDepas on 12/17/16.
 */

public class PokeBusApplicationClass extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        PokeBusApplicationClass.context = getApplicationContext();
    }

    public static Context getContext() {
        return PokeBusApplicationClass.context;
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

//    private void checkIfDeviceIsOnline(){
//        !SystemStatus.getInstance(getApplicationContext()).isOnline() &&
//        if (this !instanceof NoConnectionActivity) {
//            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
//            startActivity(intent);
//            get.finish();
//        }
//    }



    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}