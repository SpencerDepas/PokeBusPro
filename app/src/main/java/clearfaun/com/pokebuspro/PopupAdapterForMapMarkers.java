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
public class PopupAdapterForMapMarkers implements GoogleMap.InfoWindowAdapter{

    private View popup = null;
    private LayoutInflater inflater = null;
    private MapsActivity mapsActivity;

    static boolean isMarkerOpen = false;

    static DialogPopupListner popupListner = null;

    PopupAdapterForMapMarkers(LayoutInflater inflater) {
        this.inflater = inflater;
    }



    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }






    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {


        Log.i("PopupAdapterForMapMark", "new popup() ");
        if (popup == null) {
            popup = inflater.inflate(R.layout.popup_snippet, null);
        }

        if(marker.isInfoWindowShown()){
            isMarkerOpen = true;
        }else{
            isMarkerOpen = false;
        }

        mapsActivity = new MapsActivity();

        TextView busCode = (TextView) popup.findViewById(R.id.bus_code);
        TextView distances = (TextView) popup.findViewById(R.id.snippet);


        //BusInfo.setCurrentDisplayedBusName(MapsActivity.busInfo.get(i).busName);



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



        final String fBusCode = marker.getTitle().substring(0, 6);
        mapsActivity.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker Pin) {

                Log.i("PopupAdapterForMapMark", "  setOnInfoWindowClickListener " );


                popupListner.displayDialog(fBusCode);


            }
        });


        mapsActivity.disableMapOnPress();






        return (popup);
    }


}
