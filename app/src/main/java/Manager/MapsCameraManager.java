package Manager;

import android.graphics.Point;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import model.MapsCamera;
import ui.activity.MainActivity;
import utils.SystemStatus;

/**
 * Created by SpencerDepas on 12/18/16.
 */

public class MapsCameraManager {

    private final LatLng EMPIRE_STATE_BUILDING_LAT_LNG = new LatLng(40.748441, -73.985664);
    private boolean firstTimeLoadingForCameraAnimation = true;
    private LatLng onMapPresedLatLng;
    private MapsCamera mMapsCamera;

    private static MapsCameraManager sMapsCameraManager;

    private MapsCameraManager() {
        mMapsCamera = new MapsCamera();
    }


    public static MapsCameraManager getInstance() {
        if (sMapsCameraManager == null) {
            sMapsCameraManager = new MapsCameraManager();

        }
        return sMapsCameraManager;
    }

    public void animateCamera(LatLng target, float zoom, float bearing) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)               // Sets the center of the map to Mountain View
                .zoom(zoom)                   // Sets the zoom
                .bearing(bearing)             // Sets the orientation of the camera to eas
                // .tilt(tilt)                 // Sets the tilt of the camera to 30 degrees
                .build();                     // Creates a CameraPosition from the builder
        MainActivity.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                //MainActivity.googleMap.getUiSettings().setAllGesturesEnabled(true);
                saveCameraPositionOnMap();
//                fab.setEnabled(true);
//                fab.setClickable(true);

            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void mapStartingLocation(LatLng target, float zoom, float bearing) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)               // Sets the center of the map to Mountain View
                .zoom(mMapsCamera.getZoom())                   // Sets the zoom
                .bearing(bearing)             // Sets the orientation of the camera to eas
                // .tilt(tilt)                 // Sets the tilt of the camera to 30 degrees
                .build();                     // Creates a CameraPosition from the builder
        MainActivity.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public void saveCameraPositionOnMap() {
        //if (hasLocationPermission) {
        if (mMapsCamera.getZoom() != 0) {
            mMapsCamera.setTilt((int) MainActivity.googleMap.getCameraPosition().tilt);
            mMapsCamera.setZoom(MainActivity.googleMap.getCameraPosition().zoom);
            mMapsCamera.setBearing(MainActivity.googleMap.getCameraPosition().bearing);
        }


    }

    public void animateCameraPos() {

        if (onMapPresedLatLng != null) {

            if (mMapsCamera.getZoom() > 4) {
                animateCamera(onMapPresedLatLng, mMapsCamera.getZoom(), mMapsCamera.getBearing());
            } else {
                animateCamera(onMapPresedLatLng, 16, 40);
            }
        } else if (mMapsCamera.getZoom() > 4) {

            if (LocationManager.getInstance().getUserLatLng().latitude == 0) {
                animateCamera(EMPIRE_STATE_BUILDING_LAT_LNG, mMapsCamera.getZoom(), mMapsCamera.getBearing());
            } else {
                mapStartingLocation(LocationManager.getInstance().getUserLatLng(), mMapsCamera.getZoom(), mMapsCamera.getBearing());
                //animateCamera(LocationManager.getInstance().getUserLatLng(), mMapsCamera.getZoom(), mMapsCamera.getBearing());
            }
        } else {

            if (LocationManager.getInstance().getUserLatLng().latitude == 0) {
                animateCamera(EMPIRE_STATE_BUILDING_LAT_LNG, 16, 40);
            } else {
                animateCamera(LocationManager.getInstance().getUserLatLng(), 16, 40);
            }
        }
    }

    public void animateCameraToMarkerMiddleOfScreen(Marker marker, boolean firstTimeLoading) {
        LatLng aboveMarkerLatLng;

        if (!firstTimeLoading) {
            saveCameraPositionOnMap();

            int container_height = SystemStatus.getInstance().getScreenHeight();
            Projection projection = MainActivity.googleMap.getProjection();

            LatLng markerLatLng = new LatLng(marker.getPosition().latitude,
                    marker.getPosition().longitude);
            Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
            Point pointHalfScreenAbove = new Point(markerScreenPosition.x,
                    markerScreenPosition.y - (int) (container_height / 3.1));

            aboveMarkerLatLng = projection
                    .fromScreenLocation(pointHalfScreenAbove);
        } else {
            aboveMarkerLatLng = marker.getPosition();
        }


        marker.showInfoWindow();
        animateCamera(aboveMarkerLatLng, mMapsCamera.getZoom(), mMapsCamera.getBearing());
    }

    public boolean isFirstTimeLoadingForCameraAnimation() {
        return firstTimeLoadingForCameraAnimation;
    }

    public void setFirstTimeLoadingForCameraAnimation(boolean firstTimeLoadingForCameraAnimation) {
        this.firstTimeLoadingForCameraAnimation = firstTimeLoadingForCameraAnimation;
    }

    public LatLng getOnMapPresedLatLng() {
        return onMapPresedLatLng;
    }

    public void setOnMapPresedLatLng(LatLng onMapPresedLatLng) {
        this.onMapPresedLatLng = onMapPresedLatLng;
    }
}
