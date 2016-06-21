package model;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import clearfaun.com.pokebuspro.R;

/**
 * Created by SpencerDepas on 6/21/16.
 */
public class LoadAndSaveFavBusInfo {

    private Context mContext;

    private static LoadAndSaveFavBusInfo sLoadAndSaveFavBusInfo;

    public static LoadAndSaveFavBusInfo getInstance(Context mContext) {
        if (sLoadAndSaveFavBusInfo == null) {
            sLoadAndSaveFavBusInfo = new LoadAndSaveFavBusInfo(mContext);
        }
        return sLoadAndSaveFavBusInfo;
    }

    private LoadAndSaveFavBusInfo(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<String> loadFavBus() {
        Log.i("MyMapsActivity", "loadFavBus");

        try {
            FileInputStream fis = mContext.openFileInput(mContext.getResources().getString(R.string.fav_bus_key));
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<String> favBuses = (ArrayList) is.readObject();
            is.close();
            fis.close();
            Log.i("MyMapsActivity", "favBuses.size()" + favBuses.size());
            return favBuses;

        } catch (Exception e) {
            Log.i("MyMapsActivity", "e : " + e);
            e.printStackTrace();
        }
        Log.i("MyMapsActivity", "loadFavBus return dud");
        return new ArrayList<>();


    }


    public void saveFavBus(ArrayList<String> busCodeOfFavBusStops) {
        Log.i("MyMapsActivity", "saveFavBus()");


        if (busCodeOfFavBusStops != null) {

            try {
                FileOutputStream fos = mContext.openFileOutput(mContext.getResources().getString(R.string.fav_bus_key), Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(busCodeOfFavBusStops);
                os.close();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyMapsActivity", " e.printStackTrace();()" + e);
            }
        }

    }
}
