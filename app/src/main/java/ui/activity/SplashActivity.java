package ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Log.i("MySplashActivity", "onCreate");


        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (statusCode == ConnectionResult.SUCCESS) {
            Log.i("MySplashActivity", "has g services");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        try {
            Log.i("MySplashActivity", "versionName" + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

            //this.version.setText("Version " + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

}
