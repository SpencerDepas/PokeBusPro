package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;



/**
 * Created by spencer on 4/2/2015.
 */
public class NoConnectionActivity extends AppCompatActivity {

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection_coord);


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Log.i("NoConnectionActivity", "onCreate");

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);


        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("NoConnectionActivity", "clicked button ");


                Intent intent = new Intent(MapsActivity.mContext , MapsActivity.class);
                startActivity(intent);



            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);

        if(item.getItemId()== R.id.map_item){
            String prefBusMap = prefs.getString("KEY99", "Brooklyn");

            Log.i("MyMapsActivity", "prefBusMap " + prefBusMap);
            Intent intent = new Intent(MapsActivity.mContext, BusMap.class);
            intent.putExtra("maptype", "Current Map is: " + prefBusMap);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("AirplaneMode", "onPause() enabledAirplaneMode");
        this.finish();
        System.exit(0);

    }

}
