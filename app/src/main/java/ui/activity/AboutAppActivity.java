package ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import clearfaun.com.pokebuspro.R;

/**
 * Created by spencer on 3/15/2015.
 */
public class AboutAppActivity extends AppCompatActivity {


    @BindView(R.id.clearfaun_image)
    ImageView clearFaunLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_main_layout);
        ButterKnife.bind(this);
        Log.d("MyAboutScreen", "onCreate");

        View view = this.getWindow().getDecorView();

        view.setBackgroundColor(Color.WHITE);

//
//        Glide.with(getApplicationContext())
//                .load(R.drawable.clearfaunwithtitle)
//
//                .into(clearFaunLogo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


//        CallAndParse callAndParse = new CallAndParse();
//
//        callAndParse.getBusStops("will be lat lng");


        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("About Screen")
                .putContentType("Activity")
        );


    }

    @OnClick(R.id.clearfaun_image)
    public void submit(View view) {
        String url = "http://www.clearfaun.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("About Screen image clicked")
                .putContentType("Action")
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!isOnline()) {
            Log.i("MyMapsActivity", "!isOnline()");
            Intent intent = new Intent(getApplicationContext(), NoConnectionActivity.class);
            startActivity(intent);
            this.finish();
        } else {

            if(MainActivity.active == true){
                this.finish();
            }else{
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
