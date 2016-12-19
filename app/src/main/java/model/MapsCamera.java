package model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by SpencerDepas on 12/18/16.
 */

public class MapsCamera {

    private float zoom = 16;
    private int tilt = 30;
    private float bearing = 40;
    private LatLng target;


    public LatLng getTarget() {
        return target;
    }

    public void setTarget(LatLng target) {
        this.target = target;
    }

    public int getTilt() {
        return tilt;
    }

    public void setTilt(int tilt) {
        tilt = tilt;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }


    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

}
