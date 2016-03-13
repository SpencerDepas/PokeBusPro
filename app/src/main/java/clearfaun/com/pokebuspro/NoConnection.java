package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;



/**
 * Created by spencer on 4/2/2015.
 */
public class NoConnection extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection);


        Log.i("NoConnection", "onCreate");

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);


        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("NoConnection", "clicked button ");


                Intent intent = new Intent(MapsActivity.mContext , MapsActivity.class);
                startActivity(intent);



            }
        });





    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("AirplaneMode", "onPause() enabledAirplaneMode");
        this.finish();
        System.exit(0);

    }

}
