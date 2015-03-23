package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by spencer on 3/23/2015.
 */
public class AirplaneMode extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airplane_mode);


        Log.i("AirplaneMode", "onCreate");

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);


        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("AirplaneMode", "clicked button ");


                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                startActivity(intent);

            }
        });



       /* Log.i("MyMapsActivity", "enabledAirplaneMode");
        toaster("Please disable 'Airplane Mode'");
        this.finish();
        System.exit(0);*/


    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("AirplaneMode", "onPause() enabledAirplaneMode");
        this.finish();
        System.exit(0);

    }

}
