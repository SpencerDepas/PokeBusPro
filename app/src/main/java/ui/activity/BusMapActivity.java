package ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import Manager.PreferenceManager;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clearfaun.com.pokebuspro.R;
import io.fabric.sdk.android.Fabric;
import ui.activity.interfaces.NoBusesInAreaInterface;


/**
 * Created by spencer on 3/16/2015.
 */
public class BusMapActivity extends AppCompatActivity implements NoBusesInAreaInterface {

    private ProgressBar spinner;
    SubsamplingScaleImageView imageView;
    String savedBusMap;
    private final String MAP_SELECTION = "Map selection";
    private PreferenceManager preferenceManager;
    private Context mContext;
    private static final String TAG = "BusMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_borough_map);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());

        Log.i("BusMapActivity", "onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        mContext = getApplicationContext();
        preferenceManager = new PreferenceManager(mContext);


        savedBusMap = preferenceManager.getBusMapSelection();


        spinner = (ProgressBar) findViewById(R.id.spinner);


        Log.d("BusMapActivity", "savedBusMap:" + savedBusMap);

        loadMap();
    }

    private void loadMap() {
        Log.i("BusMapActivity", "loadMap");


        if (savedBusMap.equals(PreferenceManager.BUS_MAP_SELECTION_BROOKLYN)) {
            Log.i("BusMapActivity", "in bk");
            imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
            imageView.setImage(ImageSource.asset("busbklnmap.jpg"));
        } else if (savedBusMap.equals(PreferenceManager.BUS_MAP_SELECTION_MANHATTAN)) {
            Log.i("BusMapActivity", "in mn");
            imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
            imageView.setImage(ImageSource.asset("manbusmap.jpg"));
        } else if (savedBusMap.equals(PreferenceManager.BUS_MAP_SELECTION_QUEENS)) {
            Log.i("BusMapActivity", "in qns");
            imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
            imageView.setImage(ImageSource.asset("busqnsmap.jpg"));
        } else if (savedBusMap.equals(PreferenceManager.BUS_MAP_SELECTION_BRONX)) {
            Log.i("BusMapActivity", "in bx");
            imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
            imageView.setImage(ImageSource.asset("busbxmap.jpg"));
        } else if (savedBusMap.equals(PreferenceManager.BUS_MAP_SELECTION_STATEN_ISLAND)) {
            Log.i("BusMapActivity", "in si");
            imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
            imageView.setImage(ImageSource.asset("bussimap.jpg"));

        }

        imageView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
            }

            @Override
            public void onImageLoaded() {
                spinner.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {

            }

            @Override
            public void onTileLoadError(Exception e) {

            }
        });
    }

    private int findPreselectedIndex() {


        int preSelectedIndex = 0;

        switch (savedBusMap) {
            case PreferenceManager.BUS_MAP_SELECTION_BROOKLYN:
                preSelectedIndex = 0;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_MANHATTAN:
                preSelectedIndex = 1;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_QUEENS:
                preSelectedIndex = 2;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_BRONX:
                preSelectedIndex = 3;
                break;
            case PreferenceManager.BUS_MAP_SELECTION_STATEN_ISLAND:
                preSelectedIndex = 4;
                break;
            default:
                preSelectedIndex = 0;
                break;

        }
        return preSelectedIndex;
    }


    private void selectMapDialog() {


        int preSelectedIndex = findPreselectedIndex();


        AlertDialog.Builder builder = new AlertDialog.Builder(BusMapActivity.this, R.style.AppCompatAlertDialogStyle);
        CharSequence items[] = new CharSequence[]{"Brooklyn", "Manhattan", "Queens", "Bronx", "Staten Island"};
        builder.setTitle(getString(R.string.select_bus_map_tittle));
        builder.setNegativeButton("DISMISS", null);
        builder.setSingleChoiceItems(items, preSelectedIndex, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("BusMapActivity", "Positive");
                spinner.setVisibility(View.VISIBLE);
                dialog.dismiss();

                if (which == 0) {
                    Log.d("BusMapActivity", "menuItem.getTitle():" + 0);

                    saveMapSelection(PreferenceManager.BUS_MAP_SELECTION_BROOKLYN);


                } else if (which == 1) {
                    Log.d("BusMapActivity", "menuItem.getTitle():" + 1);

                    saveMapSelection(PreferenceManager.BUS_MAP_SELECTION_MANHATTAN);


                } else if (which == 2) {
                    Log.d("BusMapActivity", "menuItem.getTitle():" + 2);

                    saveMapSelection(PreferenceManager.BUS_MAP_SELECTION_QUEENS);


                } else if (which == 3) {
                    Log.d("BusMapActivity", "menuItem.getTitle():" + 2);

                    saveMapSelection(PreferenceManager.BUS_MAP_SELECTION_BRONX);


                } else if (which == 4) {
                    Log.d("BusMapActivity", "menuItem.getTitle():" + 2);


                    saveMapSelection(PreferenceManager.BUS_MAP_SELECTION_STATEN_ISLAND);


                }

                loadMap();


            }
        });
        builder.show();
    }

    private void saveMapSelection(String mapSelection) {

        preferenceManager.saveBusMapSelection(mapSelection);
        savedBusMap = mapSelection;
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Select Map")
                .putContentType("Selection")
                .putCustomAttribute(MAP_SELECTION, mapSelection)
        );


    }


    @SuppressWarnings("unused")
    @OnClick(R.id.map_fab)
    public void selectMap(View view) {
        Log.i("BusMapActivity", "selectMap");


        selectMapDialog();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!isOnline()) {
            Log.i("BusMapActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            Log.i("BusMapActivity", "isOnline()");

            if (MainActivity.active == true) {
                Log.i("BusMapActivity", "MainActivity.active == true");

                this.finish();
            } else {
                Log.i("BusMapActivity", "else");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                this.finish();
            }

        }

        return true;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void noBusesFound() {
        Log.d(TAG, "noBusesFound: noBusesFound");
    }
}
