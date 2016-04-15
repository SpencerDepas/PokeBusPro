package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by spencer on 3/16/2015.
 */
public class BusMap extends AppCompatActivity   {

    private ProgressBar spinner;
    SubsamplingScaleImageView imageView;
    SharedPreferences prefs;
    String savedBusMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        ButterKnife.bind(this);
        Log.i("BusMap", "onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Intent intent = getIntent();
        savedBusMap = intent.getStringExtra("maptype");

        Log.i("BusMap", "intent " + intent.getStringExtra("maptype"));

        savedBusMap = savedBusMap.substring(16);
        Log.i("BusMap", "savedBusMap " + savedBusMap);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        Log.i("BusMap", intent.getStringExtra("maptype"));
        Log.i("BusMap", "Brooklyn");


        loadMap();
        Log.i("BusMap", "not ==");




        prefs = getSharedPreferences("pokeBusCodePrefs", Context.MODE_PRIVATE);


    }

    private void loadMap(){
        Log.i("BusMap", "loadMap");



        if(savedBusMap.equals("Brooklyn")){
            Log.i("BusMap", "in bk");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.busbkln_map));
        }else if(savedBusMap.equals("Manhattan")){
            Log.i("BusMap", "in mn");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.manbus));
        }else if(savedBusMap.equals("Queens")){
            Log.i("BusMap", "in qns");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.busqns));
        }else if(savedBusMap.equals("Bronx")){
            Log.i("BusMap", "in bx");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.busbx));
        }else if(savedBusMap.equals("Staten Island")){
            Log.i("BusMap", "in si");
            imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
            imageView.setImage(ImageSource.resource(R.drawable.bussi));
        }
    }

    private int findPreselectedIndex(){

        String findWhatToPreSelect = prefs.getString(getString(R.string.bus_maps_key), "0");

        String prefBusMap = prefs.getString(getString(R.string.bus_maps_key), "Brooklyn");

        int preSelectedIndex = 0;

        switch ( prefBusMap) {
            case "Brooklyn":
                preSelectedIndex = 0;
                break;
            case "Manhattan":
                preSelectedIndex = 1;
                break;
            case "Queens":
                preSelectedIndex = 2;
                break;
            case "Bronx":
                preSelectedIndex = 3;
                break;
            case "Staten Island":
                preSelectedIndex = 4;
                break;
            default:
                preSelectedIndex = 0;
                break;

        }
        return preSelectedIndex;
    }


    private void selectMapDialog(){


        int preSelectedIndex = findPreselectedIndex();



        AlertDialog.Builder builder = new AlertDialog.Builder(BusMap.this, R.style.AppCompatAlertDialogStyle);
        CharSequence items[] = new CharSequence[]{"Brooklyn", "Manhattan", "Queens", "Bronx", "Staten Island"};
        builder.setTitle(getString(R.string.select_bus_map_tittle));
        builder.setNegativeButton("DISMISS", null);
        builder.setSingleChoiceItems(items, preSelectedIndex, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "Positive");
                dialog.dismiss();

                if (which == 0) {
                    Log.d("MyMainActivity", "menuItem.getTitle():" + 0);

                    prefs.edit().putString(getString(R.string.bus_maps_key), "Brooklyn").apply();

                    savedBusMap = "Brooklyn";


                    Log.d("MyMainActivity", "refreshrate set to 20:");

                } else if (which == 1) {
                    Log.d("MyMainActivity", "menuItem.getTitle():" + 1);

                    prefs.edit().putString(getString(R.string.bus_maps_key), "Manhattan").apply();

                    savedBusMap = "Manhattan";
                    Log.d("MyMainActivity", "refreshrate set to 30:");

                } else if (which == 2) {
                    Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                    prefs.edit().putString(getString(R.string.bus_maps_key), "Queens").apply();
                    savedBusMap = "Queens";

                    Log.d("MyMainActivity", "refreshrate set to 60:");

                } else if (which == 3) {
                    Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                    prefs.edit().putString(getString(R.string.bus_maps_key), "Bronx").apply();
                    savedBusMap = "Bronx";

                }else if (which == 4) {
                    Log.d("MyMainActivity", "menuItem.getTitle():" + 2);

                    prefs.edit().putString(getString(R.string.bus_maps_key), "Staten Island").apply();
                    savedBusMap = "Staten Island";

                }

                loadMap();


            }
        });
        builder.show();
    }




    @SuppressWarnings("unused")
    @OnClick(R.id.map_fab)
    public void selectMap(View view) {
        Log.i("BusMap", "selectMap");


        selectMapDialog();

    }

    @Override
    public  boolean  onOptionsItemSelected(MenuItem item) {

        this.finish();
        return  true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




}
