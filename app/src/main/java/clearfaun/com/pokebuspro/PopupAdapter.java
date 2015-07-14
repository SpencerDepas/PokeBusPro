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
        //MapsActivity.changeSelectedBus.setVisibility(View.INVISIBLE);

       /* MapsActivity.changeSelectedBus.setVisibility(View.INVISIBLE);
        for (int i = 0; i < MapsActivity.busInfo.size(); i++) {
            if (MapsActivity.busInfo.get(i).isAddedToPopup()) {
                MapsActivity.busInfo.get(i).setAddedToPopup(false);
            }

        }*/


        for(int i = 0 ; i < MapsActivity.busInfo.size(); i ++){
            Log.i("PopupAdapter", "getInfoWindow MapsActivity.busInfo.get(i).getBusCode() " + MapsActivity.busInfo.get(i).getBusCode());
            //if the marker buscode matches the businfo bus code we know what bus it is


            Log.i("PopupAdapter", "marker.getTitle() " + marker.getTitle());
            Log.i("PopupAdapter", "MapsActivity.busInfo.get(i).getBusName() " + MapsActivity.busInfo.get(i).getBusName());
            Log.i("PopupAdapter", "MapsActivity.busInfo.get(i).isAddedToPopup() " + MapsActivity.busInfo.get(i).isAddedToPopup());
            if(marker.getTitle().equals(MapsActivity.busInfo.get(i).getBusCode()) && !MapsActivity.busInfo.get(i).isAddedToPopup()) {
                Log.i("PopupAdapter", "ITS TRUE " );
                /*for (int q = 0; q < MapsActivity.pokeBusbusInfo.size(); q++) {
                    if (MapsActivity.busInfo.get(i).getBusCode().equals(MapsActivity.pokeBusbusInfo.get(q).getBusCode() + "")) {
                        hasPokeBusIndex = q;
                        Log.i("PopupAdapterr", "pokebus is here " + hasPokeBusIndex);
                        Log.i("PopupAdapterr", "pokebus is here " + MapsActivity.pokeBusbusInfo.get(hasPokeBusIndex).getBusName());

                    }
                }*/

                //if in the stack of markers we have a pokebus we want the pokebus to be on the top of the stack
                //this is if there is no pokebus



                BusInfo.setCurrentDisplayedBusName(MapsActivity.busInfo.get(i).busName);
                busName.setText(MapsActivity.busInfo.get(i).busName);


                busCode.setText(marker.getTitle() + "\n" +
                        MapsActivity.busInfo.get(i).longName, TextView.BufferType.SPANNABLE);
                Spannable text = (Spannable) busCode.getText();

                text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                text.setSpan(new RelativeSizeSpan(.7f), 6, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                busCode.setText(text);

                distances.setText(MapsActivity.busInfo.get(i).distance[0]
                        + "\n" + MapsActivity.busInfo.get(i).distance[1]
                        + "\n" + MapsActivity.busInfo.get(i).distance[2]);
                MapsActivity.busInfo.get(i).setAddedToPopup(true);



                for(int z = 0 ; z < AddMarkers.marker.length; z ++){


                    if (z == i ) {
                        //skip
                        Log.i("PopupAdapter", "z == i SKIP " + MapsActivity.busInfo.get(i).getBusCode());
                        Log.i("PopupAdapter", "z == i SKIP " + MapsActivity.busInfo.get(i).getBusName());


                    } else if(MapsActivity.busInfo.get(z).getBusCode().equals(AddMarkers.marker[i].getTitle()) ){
                        MapsActivity.changeSelectedBus.setVisibility(View.VISIBLE);


                        currentPopUpIndex = i;


                        Log.i("PopupAdapter", " else if " + MapsActivity.busInfo.get(i).getBusCode());
                        Log.i("PopupAdapter", "else if" + MapsActivity.busInfo.get(i).getBusName());
                        Log.i("PopupAdapter", "AddMarkers.marker[i].isVisible()" + AddMarkers.marker[i].isVisible());
                        Log.i("PopupAdapter", "currentPopUpIndex " + currentPopUpIndex);




                    }

                }

                break;


            }


        }









        return(popup);
    }

    static int currentPopUpIndex;

}