package utils;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import ui.activity.interfaces.TimerTaskInterface;

/**
 * Created by SpencerDepas on 6/20/16.
 */
public class RefreshTimer {

    private TimerTaskInterface timerTaskInterface = null;
    private String refreshTimerTaskTime = "20";
    private Timer timer;
    private TimerTask timerTask;


    private static RefreshTimer sRefreshTimer;

    public static RefreshTimer getInstance(TimerTaskInterface timerTaskInterface) {
        if (sRefreshTimer == null) {
            sRefreshTimer = new RefreshTimer(timerTaskInterface);
        }
        return sRefreshTimer;
    }

    private RefreshTimer(TimerTaskInterface timerTaskInterface) {
        this.timerTaskInterface = timerTaskInterface;
    }

    public void startTimerTask() {
        Log.i("RefreshTimer", "startTimerTask()");

        Log.i("RefreshTimer", "refreshTimerTaskTime : " + refreshTimerTaskTime);


        if (refreshTimerTaskTime.equals("OFF")) {
            refreshTimerTaskTime = "0";
        } else {


            Log.i("RefreshTimer", "refreshTimerTaskTime()" + refreshTimerTaskTime);

            int timeInMS = Integer.parseInt(refreshTimerTaskTime) * 1000;
            Log.i("RefreshTimer", "timeInMS()" + timeInMS);
            if (timeInMS != 0) {
                //set a new Timer
                timer = new Timer();
                //initialize the TimerTask's job


                initializeTimerTask();
                //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
                timer.schedule(timerTask, 20000, timeInMS);


            }

        }
    }

    public void stopTimerTask() {
        Log.i("RefreshTimer", "stopTimerTask()");
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                Log.i("RefreshTimer", "initializeTimerTask    timerTask");

                //selectCorrectLatLng();
                timerTaskInterface.runTimer();

            }
        };
    }


}
