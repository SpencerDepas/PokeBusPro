package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

    ArrayList<String> overlappingMarkersIndex;


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


        for(int i = 0 ; i < MapsActivity.busInfo.size(); i ++){
            Log.i("PopupAdapter", "getInfoWindow MapsActivity.busInfo.get(i).getBusCode() " + MapsActivity.busInfo.get(i).getBusCode());
            //if the marker buscode matches the businfo bus code we know what bus it is




            if(marker.getTitle().equals(MapsActivity.busInfo.get(i).getBusCode()) && !MapsActivity.busInfo.get(i).isAddedToPopup()){


                busName.setText(MapsActivity.busInfo.get(i).busName);
                busCode.setText(marker.getTitle());
                distances.setText(MapsActivity.busInfo.get(i).distance[0]
                + "\n" + MapsActivity.busInfo.get(i).distance[1]
                + "\n" + MapsActivity.busInfo.get(i).distance[2]);
                MapsActivity.busInfo.get(i).setAddedToPopup(true);

                break;

            }


        }

        return(popup);
    }

}