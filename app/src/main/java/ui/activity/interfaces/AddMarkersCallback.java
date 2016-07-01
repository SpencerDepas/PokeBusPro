package ui.activity.interfaces;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by SpencerDepas on 4/21/16.
 */
public interface AddMarkersCallback {

    public void removeLoadingIcon();
    public void animateCameraToMarker(Marker marker);


}
