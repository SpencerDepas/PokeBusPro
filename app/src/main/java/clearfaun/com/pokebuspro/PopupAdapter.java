package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

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
            popup=inflater.inflate(R.layout.popup, null);
        }

        Log.i("PopupAdapter", "addMarkersToMap() " );


        TextView busName =(TextView)popup.findViewById(R.id.bus_name);
        TextView busCode =(TextView)popup.findViewById(R.id.bus_code);
        TextView distances =(TextView)popup.findViewById(R.id.snippet);
        //ImageView image = (ImageView)popup.findViewById(R.id.icon);


        for(int i = 0 ; i < MapsActivity.busInfo.size(); i ++){
            Log.i("PopupAdapter", "getInfoWindow MapsActivity.busInfo.get(i).getBusCode() " + MapsActivity.busInfo.get(i).getBusCode());
            //if the marker buscode matches the businfo bus code we know what bus it is
            if(marker.getTitle().equals(MapsActivity.busInfo.get(i).getBusCode())){
                busName.setText(MapsActivity.busInfo.get(i).busName);
                busCode.setText( marker.getTitle());
                distances.setText(marker.getSnippet());
                break;
            }


        }

        return(popup);
    }

}