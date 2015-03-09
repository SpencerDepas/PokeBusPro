package clearfaun.com.pokebuspro;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by spencer on 2/25/2015.
 */
class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;

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

        TextView busName =(TextView)popup.findViewById(R.id.bus_name);
        TextView busCode =(TextView)popup.findViewById(R.id.bus_code);
        TextView distances =(TextView)popup.findViewById(R.id.snippet);

        busName.setText(marker.getTitle());
        busCode.setText("301649");
        distances.setText(marker.getSnippet());


        return(popup);
    }
}