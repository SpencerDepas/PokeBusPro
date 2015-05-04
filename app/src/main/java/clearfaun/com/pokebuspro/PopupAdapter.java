package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by spencer on 2/25/2015.
 */
class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup = null;
    private LayoutInflater inflater = null;

    PopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }




    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }




    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup_snippet, null);
        }

        Log.i("PopupAdapter", "addMarkersToMap() " );


        TextView busName =(TextView)popup.findViewById(R.id.bus_name);
        TextView busCode =(TextView)popup.findViewById(R.id.bus_code);
        TextView distances =(TextView)popup.findViewById(R.id.snippet);
        //ImageView image = (ImageView)popup_snippet.findViewById(R.id.icon);
        MapsActivity.changeSelectedBus.setVisibility(View.INVISIBLE);


        for(int i = 0 ; i < MapsActivity.busInfo.size(); i ++){
            Log.i("PopupAdapter", "getInfoWindow MapsActivity.busInfo.get(i).getBusCode() " + MapsActivity.busInfo.get(i).getBusCode());
            //if the marker buscode matches the businfo bus code we know what bus it is




            if(marker.getTitle().equals(MapsActivity.busInfo.get(i).getBusCode()) && !MapsActivity.busInfo.get(i).isAddedToPopup()){


                busName.setText(MapsActivity.busInfo.get(i).busName);


                busCode.setText(marker.getTitle() + "\n" +
                        MapsActivity.busInfo.get(i).longName, TextView.BufferType.SPANNABLE);
                Spannable text = (Spannable) busCode.getText();

                text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                text.setSpan(new RelativeSizeSpan(.8f), 6, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                busCode.setText(text );

                distances.setText(MapsActivity.busInfo.get(i).distance[0]
                + "\n" + MapsActivity.busInfo.get(i).distance[1]
                + "\n" + MapsActivity.busInfo.get(i).distance[2]);
                MapsActivity.busInfo.get(i).setAddedToPopup(true);
                currentPopUpIndex = i;

                for(int z = 0 ; z < MapsActivity.busInfo.size(); z ++){

                    if(z == i){
                        //skip
                    }else if(MapsActivity.busInfo.get(i).getBusCode().equals(MapsActivity.busInfo.get(z).getBusCode())){
                        //make button visable
                        MapsActivity.changeSelectedBus.setVisibility(View.VISIBLE);
                    }

                }


                break;

            }


        }






        return(popup);
    }

    static int currentPopUpIndex;

}