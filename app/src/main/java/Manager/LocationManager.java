package Manager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by SpencerDepas on 12/18/16.
 */

public class LocationManager {

    private static LocationManager sLocationManager;
    private LatLng mUserLatLng;

    public static LocationManager getInstance() {
        if (sLocationManager == null) {
            sLocationManager = new LocationManager();
        }
        return sLocationManager;
    }

    public void setUserLatLng(LatLng latLng){
        this.mUserLatLng = latLng;
    }

    public LatLng getUserLatLng(){
        return mUserLatLng;
    }
}
