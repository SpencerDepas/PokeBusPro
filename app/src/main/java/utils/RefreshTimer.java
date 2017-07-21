package utils;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import ui.activity.interfaces.TimerTaskInterface;

/**
 * Created by SpencerDepas on 6/20/16.
 */
public class RefreshTimer {

    private TimerTaskInterface mTimerTaskInterface = null;
    private static String sRefreshTimerTaskTime;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static RefreshTimer sRefreshTimer;

    public static RefreshTimer getInstance(TimerTaskInterface timerTaskInterface, String refreshTimerTaskTime) {
        Log.i("RefreshTimer", "getInstance()");

        sRefreshTimerTaskTime = refreshTimerTaskTime;

        if (sRefreshTimer == null) {
            sRefreshTimer = new RefreshTimer(timerTaskInterface);
        }
        return sRefreshTimer;
    }

    private RefreshTimer(TimerTaskInterface timerTaskInterface) {
        this.mTimerTaskInterface = timerTaskInterface;
    }

    public void startTimerTask() {
        Log.i("RefreshTimer", "startTimerTask()");

        Log.i("RefreshTimer", "sRefreshTimerTaskTime : " + sRefreshTimerTaskTime);


        if (sRefreshTimerTaskTime.equals("0")) {
            sRefreshTimerTaskTime = "0";
        } else {


            Log.i("RefreshTimer", "sRefreshTimerTaskTime()" + sRefreshTimerTaskTime);

            int timeInMS = Integer.parseInt(sRefreshTimerTaskTime) * 1000;
            Log.i("RefreshTimer", "timeInMS()" + timeInMS);
            if (timeInMS != 0) {
                //set a new Timer
                mTimer = new Timer();
                //initialize the TimerTask's job


                initializeTimerTask();
                //schedule the mTimer, after the first 5000ms the TimerTask will run every 10000ms
                mTimer.schedule(mTimerTask, 20000, timeInMS);


            }

        }
    }

    public void stopTimerTask() {
        Log.i("RefreshTimer", "stopTimerTask()");
        //stop the mTimer, if it's not already null
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public void initializeTimerTask() {

        mTimerTask = new TimerTask() {
            public void run() {
                Log.i("RefreshTimer", "initializeTimerTask    mTimerTask");

                //selectCorrectLatLng();
               // mTimerTaskInterface.runTimer();

            }
        };
    }


}
