package clearfaun.com.pokebuspro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SpencerDepas on 3/14/16.
 */
public class GetPermissionActivity extends AppCompatActivity {


    final int  MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 22;
    final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 23;

    private Context mContext;

    @Bind(R.id.get_permission_tv)  TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_permission_activity);
        ButterKnife.bind(this);

        Log.i("GetPermissionActivity ", "permissionCheck askForPermission ");

//        View view = this.getWindow().getDecorView();
//
//        view.setBackgroundColor(Color.WHITE);

        mContext = getApplicationContext();

        //askForPermission();

    }


    @SuppressWarnings("unused")
    @OnClick(R.id.request_permission)
    public void submit(View view) {
        Log.i("GetPermissionActivity ", "request_permission OnClick ");


        ActivityCompat.requestPermissions(GetPermissionActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);
    }

    private void askForPermission(){
        Log.i("MyMapsActivity ", "permissionCheck askForPermission ");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.i("MyMapsActivity ", "permissionCheck askForPermission " );

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GetPermissionActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                Log.i("MyMapsActivity ", "permissionCheck ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,\n" +
                        "                    Manifest.permission.ACCESS_FINE_LOCATION) " );
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                Log.i("MyMapsActivity ", " permissionCheck No explanation needed, we can request the permission. " );
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(GetPermissionActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_GRANTED");

                    requestPermissionReadStorage();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED" );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    textView.setVisibility(View.VISIBLE);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_GRANTED");


                    permissionGranted();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i("MyMapsActivity ", " permissionCheck PERMISSION_DENIED" );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    textView.setVisibility(View.VISIBLE);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void requestPermissionReadStorage(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.i("MyMapsActivity ", "permissionCheck askForPermission " );

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GetPermissionActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


                Log.i("MyMapsActivity ", "permissionCheck ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,\n" +
                        "                    Manifest.permission.ACCESS_FINE_LOCATION) " );
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                Log.i("MyMapsActivity ", " permissionCheck No explanation needed, we can request the permission. " );
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(GetPermissionActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void permissionGranted(){
        Intent intent = new Intent(getApplicationContext() , MapsActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    }
