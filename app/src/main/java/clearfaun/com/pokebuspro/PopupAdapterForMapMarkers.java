package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by spencer on 2/25/2015.
 */
class PopupAdapterForMapMarkers implements GoogleMap.InfoWindowAdapter {
    private View popup = null;
    private LayoutInflater inflater = null;
    static Marker lastOpenMarker;

    PopupAdapterForMapMarkers(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public static Marker getLastOpenMarker(){
        if(lastOpenMarker == null){
            return null;
        }
        return lastOpenMarker;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }


    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {

        lastOpenMarker = marker;
        Log.i("PopupAdapterForMapMark", "new popup() ");
        if (popup == null) {
            popup = inflater.inflate(R.layout.popup_snippet, null);
        }

        Log.i("PopupAdapterForMapMark", "addMarkersToMap() ");

        if (AddMarkers.fromOpenSnippetWithIndex) {
            AddMarkers.fromOpenSnippetWithIndex = false;
            Log.i("PopupAdapterForMapMark", "fromOpenSnippetWithIndex + true ");
            return popup;
        } else {
            Log.i("PopupAdapterForMapMark", "fromOpenSnippetWithIndex + false ");


        TextView busName = (TextView) popup.findViewById(R.id.bus_name);
        TextView busCode = (TextView) popup.findViewById(R.id.bus_code);
        TextView distances = (TextView) popup.findViewById(R.id.snippet);


        //BusInfo.setCurrentDisplayedBusName(MapsActivity.busInfo.get(i).busName);
        busName.setText(marker.getTitle().substring(6));


        busCode.setText(marker.getTitle() + "\n" +
                marker.getSnippet(), TextView.BufferType.SPANNABLE);
//        Spannable text = (Spannable) busCode.getText();
//
//        text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        text.setSpan(new RelativeSizeSpan(.7f), 6, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        busCode.setText(marker.getTitle().substring(0, 6));

        distances.setText(marker.getSnippet());
        //MapsActivity.busInfo.get(i).setAddedToPopup(true);





        return (popup);
    }

}
    static int currentPopUpIndex;

}