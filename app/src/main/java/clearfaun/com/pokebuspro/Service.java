package clearfaun.com.pokebuspro;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by spencer on 3/4/2015.
 */
public class Service extends IntentService {


    public Service() {
        super("MyIntentService");
    }


    static GetBusDistanceJSON objTwo;

    SharedPreferences pref;


    @Override
    protected void onHandleIntent(Intent intent) {
        // Handle events on worker thread here
        Log.i("MyService", "Service onHandleIntent ");


        ArrayList<BusInfo> busInfoArrayList = loadBusInfo();
        try{
        if (busInfoArrayList.size() == 0) {

            new ToastMessageTask().execute("Please set a PokeBus in the main application");

        } else {


            Log.i("MyService", "loaded object: " + busInfoArrayList.get(0).getBusStopLng());
            Log.i("MyService", "loaded object: " + busInfoArrayList.size());


            //so get bus distance knows where we are coming from. API key stuff
            for (int i = 0; i < busInfoArrayList.size(); i++) {
                busInfoArrayList.get(i).setForNoUIToast(true);
                busInfoArrayList.get(i).busDistanceArrayIndex = 0;
                //busInfoArrayList.get(i).setBusDistance([""],[""],[""]);
            }


            getBusDistance(busInfoArrayList);


        }
    }catch(Exception e){
            Log.e("MyService", "Exception " + e);
        }


    }

    private ArrayList<BusInfo> loadBusInfo(){
        Log.i("MyService", "loadBusInfo " );
        try{
            FileInputStream fis = PokeBusNoUI.mContext.openFileInput("BUSINFO");
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<BusInfo> busInfo = (ArrayList)  is.readObject();
            is.close();
            fis.close();
            return busInfo;

        }catch(Exception e) {
            Log.i("MyService", "e : " + e );
            e.printStackTrace();
        }
        return null;
    }




    public static void getBusDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyService", "getBusDistance");
        objTwo = new GetBusDistanceJSON();
        objTwo.fetchBusDistanceJson(busInfo);

    }

    static int findClosestPokeBus(ArrayList<BusInfo> busInfo){
        Log.i("MyService", "findClosestPokeBus");
        double tempDistanceOfBusStop = 0;
        int indexOfClosestBus = 0;
        if(busInfo.size() > 1) {
            for (int i = 0; i < busInfo.size(); i++) {
                Log.i("MyService", "busInfo.size():" + busInfo.size());
                Log.i("MyService", "tempDistanceOfBusStop:" + tempDistanceOfBusStop);
                //first one allways goes in. then goes in only if lower.
                if (tempDistanceOfBusStop == 0 ||
                        distFrom(PokeBusNoUI.latLng.latitude, PokeBusNoUI.latLng.longitude,
                                busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng())
                                < tempDistanceOfBusStop) {

                    indexOfClosestBus = i;
                    tempDistanceOfBusStop = distFrom(PokeBusNoUI.latLng.latitude, PokeBusNoUI.latLng.longitude,
                            busInfo.get(i).getBusStopLat(), busInfo.get(i).getBusStopLng());
                }

            }
            return indexOfClosestBus;
        }else{
            return 0;
        }


    }

    public static void displayToastDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyService", "displayToastDistance");
        Log.i("MyService", "busInfoArrayList size : " + busInfo.size());


        int indexOfClosestBus = findClosestPokeBus(busInfo);




        new ToastMessageTask().execute(busInfo.get(indexOfClosestBus).getBusName() +  "'s en-route:  \n"
                + busInfo.get(indexOfClosestBus).getDistance()[0] + "\n"
                + busInfo.get(indexOfClosestBus).getDistance()[1] + "\n"
                + busInfo.get(indexOfClosestBus).getDistance()[2]);



        busInfo.clear();
    }

    static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


}