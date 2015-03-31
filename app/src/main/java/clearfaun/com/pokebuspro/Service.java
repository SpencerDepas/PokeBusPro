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

    static ArrayList<BusInfo> busInfoArrayList = new ArrayList<>();
    static GetBusDistanceJSON objTwo;
    static public String API_KEY_MTA ;
    SharedPreferences pref;


    @Override
    protected void onHandleIntent(Intent intent){
        // Handle events on worker thread here
         Log.i("MyService", "Service onHandleIntent ");

        API_KEY_MTA = getString(R.string.API_KEY_MTA);

        ArrayList<BusInfo> busInfoArrayList = loadBusInfo();

        Log.i("MyService", "loaded object: " + busInfoArrayList.get(0).getBusStopLng());
        Log.i("MyService", "loaded object: " + busInfoArrayList.size());

      /*  String[] pokeBusCode = loadArray("savedPokeBuses");
        for(int i=0;i<pokeBusCode.length;i++) {
            Log.i("MyService", "Service pokeBusCode: " + pokeBusCode[i]);
            BusInfo businfo = new BusInfo();
            businfo.setBusCode(pokeBusCode[i]);
            businfo.setForNoUIToast(true);
            busInfoArrayList.add(i, businfo);
        }



        getBusDistance(busInfoArrayList);*/





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


    public static void displayToastDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyService", "displayToastDistance");
        Log.i("MyService", "busInfoArrayList size : " + busInfoArrayList.size());
        Log.i("MyService", "PokeBusNoUI.latLng.toString() : " + PokeBusNoUI.latLng.toString());


        double tempDistanceOfBusStop = 0;
        int indexOfClosestBus = 0;
        /*for(int i = 0 ; i < busInfoArrayList.size(); i ++){


            //first one allways goes in. then goes in only if lower.
            if(tempDistanceOfBusStop == 0 ||
                    distFrom(PokeBusNoUI.latLng.latitude, PokeBusNoUI.latLng.longitude,
                    busInfoArrayList.get(i).getBusStopLat(), busInfoArrayList.get(i).getBusStopLng())
                            < tempDistanceOfBusStop){
                indexOfClosestBus = i;
                tempDistanceOfBusStop = distFrom(PokeBusNoUI.latLng.latitude, PokeBusNoUI.latLng.longitude,
                        busInfoArrayList.get(i).getBusStopLat(), busInfoArrayList.get(i).getBusStopLng());
            }

        }


        if(busInfoArrayList.get(indexOfClosestBus).getDistance()[2].equals("Not available")){

            new ToastMessageTask().execute(busInfoArrayList.get(indexOfClosestBus).getBusName() +  "'s en-route:  \n"
                    + busInfoArrayList.get(indexOfClosestBus).getDistance()[0]+ "\n"
                    + busInfoArrayList.get(indexOfClosestBus).getDistance()[1]);

        }else if(busInfoArrayList.get(indexOfClosestBus).getDistance()[1].equals("Not available")){

            new ToastMessageTask().execute(busInfoArrayList.get(indexOfClosestBus).getBusName() +  "'s en-route:  \n"
                    + busInfoArrayList.get(indexOfClosestBus).getDistance()[0]);


        }else{


            if(busInfoArrayList.get(indexOfClosestBus).getDistance()[indexOfClosestBus].equals("Not available")){
                new ToastMessageTask().execute("No " + busInfoArrayList.get(0).getBusName() +  "'s currently en-route.");
            }else{
                new ToastMessageTask().execute(busInfoArrayList.get(indexOfClosestBus).getBusName() +  "'s en-route:  \n"
                        + busInfoArrayList.get(indexOfClosestBus).getDistance()[0] + "\n"
                        + busInfoArrayList.get(indexOfClosestBus).getDistance()[1] + "\n"
                        + busInfoArrayList.get(indexOfClosestBus).getDistance()[2]);
            }




        }*/


        busInfoArrayList.clear();
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