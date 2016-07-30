package ui.component;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import clearfaun.com.pokebuspro.R;
import ui.activity.MainActivity;
import ui.activity.interfaces.DialogPopupListener;

/**
 * Created by spencer on 2/25/2015.
 */
public class PopupAdapterForMapMarkers implements GoogleMap.InfoWindowAdapter {

    private View mPopup = null;
    private LayoutInflater mInflater = null;
    private MainActivity mMainActivity;
    public static String sMarkerCurrentKey = "";
    public static DialogPopupListener mPopupListner = null;
    public PopupAdapterForMapMarkers(LayoutInflater inflater) {
        this.mInflater = inflater;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }


    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {


        Log.i("PopupAdapterForMapMark", "new mPopup() ");
        if (mPopup == null) {
            mPopup = mInflater.inflate(R.layout.popup_snippet, null);
        }


        mMainActivity = new MainActivity();

        TextView busCode = (TextView) mPopup.findViewById(R.id.bus_code);
        TextView distances = (TextView) mPopup.findViewById(R.id.snippet);


        //BusInfo.setCurrentDisplayedBusName(MainActivity.busInfo.get(i).busName);


        busCode.setText(marker.getTitle() + "\n" +
                marker.getSnippet(), TextView.BufferType.SPANNABLE);


        busCode.setText(marker.getTitle());
        distances.setText(marker.getSnippet());


        sMarkerCurrentKey = marker.getTitle().substring(0, 6);
        final String fBusCode = marker.getTitle();
        mMainActivity.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker Pin) {

                Log.i("PopupAdapterForMapMark", "  setOnInfoWindowClickListener ");


                mPopupListner.displayDialog(fBusCode);


            }
        });


        mMainActivity.disableMapOnPress();


        return (mPopup);
    }


}
