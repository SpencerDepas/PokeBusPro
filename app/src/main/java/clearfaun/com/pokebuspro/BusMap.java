package clearfaun.com.pokebuspro;

import android.app.Activity;
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

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);




        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.busbkln_map));






    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
