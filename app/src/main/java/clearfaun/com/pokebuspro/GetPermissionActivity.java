package clearfaun.com.pokebuspro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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



    final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 23;
    final int  MY_PERMISSIONS_REQUEST_FINE_LOCATION = 68;

    final String DIALOG_TITLE = "Access Phone Location";
    final String DIALOG_MESSAGE = "Wave Bus needs to acces your location in order to find the buses that are close to you.";


    private Context mContext;

    @Bind(R.id.get_permission_tv)  TextView textView;
    @Bind(R.id.request_permission_button)  Button request_permission_button;

    final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_permission_coord);
        ButterKnife.bind(this);

        Log.i("GetPermissionActivity ", "onCreate ");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        request_permission_button.
                setBackgroundColor(getResources().getColor(R.color.colorAccent));

        Log.i("GetPermissionActivity ", "permissionCheck askForPermission ");




        mContext = getApplicationContext();

        permissionAtRunTime();

    }



    @OnClick(R.id.fab_get_permission)
    public void fab_button(View view) {
        Log.i("GetPermissionActivity ", "request_permission OnClick ");


        ActivityCompat.requestPermissions(GetPermissionActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    private void showAlertDialog(String title, String message, final String permission) {
        Log.d("GetPermissionActivity", "showAlertDialog" );

        //getSupportActionBar().getThemedContext()
        //when add a toolbar add this
        AlertDialog dialog = new AlertDialog.Builder(this)

                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //permissionHelper.requestAfterExplanation(permission);
                        ActivityCompat.requestPermissions(GetPermissionActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    }

                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void permissionAtRunTime(){
        if(Build.VERSION.SDK_INT >= 23 ){
            Log.d("GetPermissionActivity", "ask for call permission 23> " );

            if( this.checkSelfPermission(PERMISSION)== (int) PackageManager.PERMISSION_GRANTED){
                Log.d("GetPermissionActivity", "permission allready granted " );
                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
                this.finish();
            }else{
                Log.d("GetPermissionActivity", "permission  needs requesting" );

                if(shouldWeAsk("fine_location_permission_has_been_asked") ){
                    Log.d("GetPermissionActivity", "permission has yet to be asked" );

                    showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
                }else{
                    Log.d("GetPermissionActivity", "permission has allready been denied" );
                    phonePermissionNotGranted();
                }


            }

        }
    }

    private void phonePermissionNotGranted() {
        Log.d("GetPermissionActivity", "phonePermissionNotGranted" );
    }

    private boolean shouldWeAsk(String permission){
        Log.d("GetPermissionActivity", "shouldWeAsk" );
        Log.d("GetPermissionActivity", "permission : " + permission );
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean needToAsk = sharedPreferences.getBoolean(permission, true);
        Log.d("GetPermissionActivity", "shouldWeAsk : " + needToAsk );
        return needToAsk;

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.request_permission_button)
    public void submit(View view) {
        Log.i("GetPermissionActivity ", "request_permission OnClick ");


        ActivityCompat.requestPermissions(GetPermissionActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {

                        Log.d("GetPermissionActivity", "fine location granted" );
                        Intent intent = new Intent(mContext, MapsActivity.class);
                        startActivity(intent);
                        this.finish();

                    } else {
                        Log.d("GetPermissionActivity", "fine location not granted" );
                        phonePermissionNotGranted();
                    }


                    savePermissionAsked("fine_location_permission_has_been_asked");
                }
            }
        }
    }

    private void savePermissionAsked(String permission){
        Log.d("GetPermissionActivity", "savePermissionAsked" );
        Log.d("GetPermissionActivity", "permission : " + permission );
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(permission, false);
        editor.apply();
    }


    private void requestPermissionReadStorage(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.i("GetPermissionActivity ", "permissionCheck askForPermission " );

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GetPermissionActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


                Log.i("GetPermissionActivity ", "permissionCheck ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,\n" +
                        "                    Manifest.permission.ACCESS_FINE_LOCATION) " );
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                Log.i("GetPermissionActivity ", " permissionCheck No explanation needed, we can request the permission. " );
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
