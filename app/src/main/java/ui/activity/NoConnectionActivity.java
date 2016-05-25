package ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import butterknife.ButterKnife;
import butterknife.OnClick;
import clearfaun.com.pokebuspro.R;


/**
 * Created by spencer on 4/2/2015.
 */
public class NoConnectionActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection_coord);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Log.i("NoConnectionActivity", "onCreate");

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        mContext = getApplicationContext();



        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("No connection Activity")
                .putContentType("Activity")
        );




    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.no_connection_fab)
    public void mapsActivityIntent(View view) {
        Log.i("MyMapsActivity", "onClick refreshLocation");

        if(isOnline()){
            Intent intent = new Intent(mContext , MainActivity.class);
            startActivity(intent);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);

        if(item.getItemId()== R.id.map_item){
            String prefBusMap = prefs.getString("KEY99", "Brooklyn");

            Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);
            Intent intent = new Intent(mContext, BoroughBusMapActivity.class);
            intent.putExtra("maptype", "Current Map is: " + prefBusMap);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.no_connection_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("AirplaneMode", "onPause() enabledAirplaneMode");
        this.finish();
        System.exit(0);

    }

}
