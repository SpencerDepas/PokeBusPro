package clearfaun.com.pokebuspro;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    protected void onHandleIntent(Intent intent){
        // Handle events on worker thread here
         Log.i("MyService", "Service onHandleIntent ");





        try {

            Log.i("MyService", "in try " );

            Context con = createPackageContext("clearfaun.com.pokebuspro", 0);
            SharedPreferences pref = con.getSharedPreferences(
                    "pokeBusCodePrefs", Context.MODE_PRIVATE);

            String data = pref.getString("pokeBusCode", "No Value");
            BusInfo businfo = new BusInfo();
            businfo.setBusCode(data);
            businfo.setForNoUIToast(true);
            busInfoArrayList.add(0,businfo );
            //new ToastMessageTask().execute("Saved PokeBus is " + busInfoArrayList.get(0).getBusCode());


        } catch (Exception e) {
            Log.e("Not data shared", e.toString());
            Log.i("MyService", "in catch " );
        }


        getBusDistance(busInfoArrayList);



    }




    public static void getBusDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyMapsActivity", "getBusDistance");
        objTwo = new GetBusDistanceJSON();
        objTwo.fetchBusDistanceJson(busInfo);

    }


    public static void displayToastDistance(ArrayList<BusInfo> busInfo){

        new ToastMessageTask().execute("The Bus you poked is \n" + busInfoArrayList.get(0).getDistance()[0] + "\n"
        + busInfoArrayList.get(0).getDistance()[1] + "\n"
                + busInfoArrayList.get(0).getDistance()[2]);

    }


}