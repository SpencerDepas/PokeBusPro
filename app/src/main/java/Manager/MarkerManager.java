package Manager;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.Hashtable;

/**
 * Created by SpencerDepas on 4/15/16.
 */
public class MarkerManager {

    private static MarkerManager sMarkerManager;

    //private List<Crime> mCrimes;
    private Hashtable<String, Marker> markerHashTable;

    public static synchronized MarkerManager getInstance() {
        if (sMarkerManager == null) {
            sMarkerManager = new MarkerManager();
        }
        return sMarkerManager;
    }

    private MarkerManager() {
        Log.i("MarkerManager", " ");
    }

    public Hashtable<String, Marker> getMarkerHashTable() {
        if (markerHashTable == null) {
            markerHashTable = new Hashtable<String, Marker>();
        }

        return markerHashTable;
    }
}
