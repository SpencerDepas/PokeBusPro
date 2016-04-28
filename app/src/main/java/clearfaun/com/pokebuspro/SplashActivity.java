package clearfaun.com.pokebuspro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MySplashActivity", "before crashlitics");

        Fabric.with(this, new Crashlytics());
        Log.i("MySplashActivity", "onCreate");


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}
