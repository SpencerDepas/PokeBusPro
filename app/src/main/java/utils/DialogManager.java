package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import clearfaun.com.pokebuspro.R;
import model.AnswersConstants;

/**
 * Created by SpencerDepas on 6/21/16.
 */
public class DialogManager {

    Context mContext;

    public DialogManager(Context mContext){
        this.mContext = mContext;
    }



    public void displayDialog(String buscode) {
        Log.i("MyMapsActivity", "displayDialog interface");


        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersConstants.SET_FAV_BUS_STOP_DIALOG)
                .putContentType(AnswersConstants.ACTION)
        );


        final String finalBuscode = buscode;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(mContext.getString(R.string.set_fav_bus));
        builder.setMessage("BusCode: " + finalBuscode
                + "\n" + "\n" + mContext.getString(R.string.set_fav_bus_body));
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "Positive");
                dialog.dismiss();


//                busCodeOfFavBusStops.add(finalBuscode);
//                addMarkers.addPokeBusColor(finalBuscode);

                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersConstants.SELECTION)
                        .putCustomAttribute(AnswersConstants.FAV_BUS_STOP, finalBuscode)
                );


            }
        });
        builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "dismiss");
                dialog.dismiss();


                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersConstants.SELECTION)
                        .putCustomAttribute(AnswersConstants.FAV_BUS_STOP_DISMISSED, AnswersConstants.DISMISSED)
                );
            }
        });
        builder.show();
    }

//    private void searchForLocationFromAddress() {
//        Log.i("MyMapsActivity", "onClick searchForLocation");
//
//
//        //brings up keyboard
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//
//        LayoutInflater li = LayoutInflater.from(MainActivity.this);
//        View alertDialogView = li.inflate(R.layout.search_address_dialog, null);
//
//
//        final EditText locationToSearchFor = (EditText) alertDialogView
//                .findViewById(R.id.seacrhed_address);
//
//
//        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
//                new android.support.v7.app.AlertDialog.Builder(MainActivity.this,
//                        R.style.AppCompatAlertDialogStyle);
//        alertDialogBuilder.setView(alertDialogView);
//
//
//        alertDialogBuilder.setPositiveButton("SEARCH",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // get user input and set it to result
//                        // edit text
//                        Log.i("MyMapsActivity", "DialogInterface onClick ");
//
//                        getLatLngForSearchLocationFromAddress(locationToSearchFor.getText().toString() + "new york ny");
//
//                        getWindow().setSoftInputMode(
//                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                        );
//
//                        Answers.getInstance().logContentView(new ContentViewEvent()
//                                .putContentName(AnswersConstants.SEARCHED_FOR_BUS)
//                                .putContentType(AnswersConstants.SELECTION)
//                                .putCustomAttribute(AnswersConstants.ADDRESS_SEARCHED, locationToSearchFor.getText().toString()));
//
//
//                    }
//
//                });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // get user input and set it to result
//                // edit text
//                Log.i("MyMapsActivity", "DialogInterface onClick Cancel");
//
//                getWindow().setSoftInputMode(
//                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                );
//
//            }
//
//        });
//
//        final android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//
//
//    }

}
