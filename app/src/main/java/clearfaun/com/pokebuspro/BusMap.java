package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;



/**
 * Created by spencer on 3/16/2015.
 */
public class BusMap extends Activity {

    private ProgressBar spinner;
    SubsamplingScaleImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_map);
        Log.i("BusMap", "onCreate");

        Intent intent = getIntent();
        String savedBusMap = intent.getStringExtra("maptype");

        Log.i("BusMap", "intent " + intent.getStringExtra("maptype"));


        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        Log.i("BusMap", intent.getStringExtra("maptype"));
        Log.i("BusMap", "Brooklyn");

        if(savedBusMap.equals("Current Map is: Brooklyn")){
            Log.i("BusMap", "in bk");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.busbkln_map));
        }else if(savedBusMap.equals("Current Map is: Manhattan")){
            Log.i("BusMap", "in mn");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.manbus));
        }else if(savedBusMap.equals("Current Map is: Queens")){
            Log.i("BusMap", "in qns");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.busqns));
        }else if(savedBusMap.equals("Current Map is: Bronx")){
            Log.i("BusMap", "in bx");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.busbx));
        }else if(savedBusMap.equals("Current Map is: Staten Island")){
            Log.i("BusMap", "in si");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.bussi));
        }

        Log.i("BusMap", "not ==");






    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
