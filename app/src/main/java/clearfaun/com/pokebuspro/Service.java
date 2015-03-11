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
    static public String API_KEY_MTA ;

    @Override
    protected void onHandleIntent(Intent intent){
        // Handle events on worker thread here
         Log.i("MyService", "Service onHandleIntent ");

        API_KEY_MTA = getString(R.string.API_KEY_MTA);


        try {

            Log.i("MyService", "in try " );

            Context con = createPackageContext("clearfaun.com.pokebuspro", 0);
            SharedPreferences pref = con.getSharedPreferences(
                    "pokeBusCodePrefs", Context.MODE_PRIVATE);

            String pokeBusCode = pref.getString("pokeBusCode", "No Value");
            String pokeBusName = pref.getString("pokeBusName", "No Value");
            BusInfo businfo = new BusInfo();
            businfo.setBusCode(pokeBusCode);
            businfo.setBusName(pokeBusName);
            businfo.setForNoUIToast(true);
            busInfoArrayList.add(0,businfo );
            //new ToastMessageTask().execute("Saved PokeBus is " + busInfoArrayList.get(0).getBusCode());
            Log.i("MyService", "in busCode " + pokeBusCode );
            Log.i("MyService", "in pokeBusName " + pokeBusName );

        } catch (Exception e) {
            Log.e("Not data shared", e.toString());
            Log.i("MyService", "in catch " );
        }


        getBusDistance(busInfoArrayList);



    }




    public static void getBusDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyService", "getBusDistance");
        objTwo = new GetBusDistanceJSON();
        objTwo.fetchBusDistanceJson(busInfo);

    }


    public static void displayToastDistance(ArrayList<BusInfo> busInfo){
        Log.i("MyService", "displayToastDistance");



        if(busInfoArrayList.get(0).getDistance()[2].equals("Not available")){

            new ToastMessageTask().execute(busInfoArrayList.get(0).getBusName() +  " en-route:  \n"
                    + busInfoArrayList.get(0).getDistance()[0]+ "\n"
                    + busInfoArrayList.get(0).getDistance()[1]);

        }else if(busInfoArrayList.get(0).getDistance()[1].equals("Not available")){

            new ToastMessageTask().execute(busInfoArrayList.get(0).getBusName() +  " en-route:  \n"
                    + busInfoArrayList.get(0).getDistance()[0]);


        }else{

            new ToastMessageTask().execute(busInfoArrayList.get(0).getBusName() +  " en-route:  \n"
                    + busInfoArrayList.get(0).getDistance()[0] + "\n"
                    + busInfoArrayList.get(0).getDistance()[1] + "\n"
                    + busInfoArrayList.get(0).getDistance()[2]);
        }



    }


}