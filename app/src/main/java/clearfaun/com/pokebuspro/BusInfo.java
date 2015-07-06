package clearfaun.com.pokebuspro;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by spencer on 2/21/2015.
 */
public class BusInfo implements Serializable {

    private static ArrayList<String> addedBusCode = new ArrayList<String>();
    public String stopCode = "";
    public String busNameId = "";
    public String latitude = "";
    public String longitude = "";
    public String  radius = "200";
    boolean markerSet = false;
    public String busName = "";
    public boolean forNoUIToast = false;
    //public String[] distance = {"Not available", "Not available","Not available"};
    public String[] distance;
    //constructor
    int busDistanceArrayIndex;
    boolean addedToPopup = false;
    String longName;
    String busCodeAndBusName;
    static int totalNumberOfBusStopsCalled ;
    static int finishedLoadingBusStops;


    static boolean hasEveryBusStopFinishedLoading(){
        if(totalNumberOfBusStopsCalled -1  == finishedLoadingBusStops){

            return true;
        }
        return false;
    }


    public BusInfo(){
        distance = new String[3];
        distance[0] = "Not available";
        distance[1] = "Not available";
        distance[2] = "Not available";
        busDistanceArrayIndex = 0;

    }

    static String currentBusPopup;
    public static void setCurrentDisplayedBusName(String busname){
        currentBusPopup = busname;
    }

    public static String getDisplayedBusName(){
        return currentBusPopup;
    }

    public void setDistanceNotAvailable(){
        distance[0] = "Not available";
        distance[1] = "Not available";
        distance[2] = "Not available";
        busDistanceArrayIndex = 0;
    }

    public void setDistanceLoading(){
        distance[0] = "Loading";
        distance[1] = "Loading";
        distance[2] = "Loading";
        busDistanceArrayIndex = 0;

    }

   /* static void removeLoading(ArrayList<BusInfo> busInfo){

        Log.i("removeLoading", " busInfo.size" + busInfo.size());

        for(int i = 0; i < busInfo.size(); i ++) {


            Log.i("removeLoading", " busInfo.buscode" + busInfo.get(i).getBusCode());

            *//*if (busInfo.get(i).getDistance()[0].equals("Loading")) {
                busInfo.get(i).distance[0] = "Not available";
                busInfo.get(i).distance[1] = "Not available";
                busInfo.get(i).distance[2] = "Not available";
                Log.i("removeLoading", " busInfo.buscode" + busInfo.get(i).getBusCode());
                Log.i("removeLoading", " busInfo.get(i).distance[2]" + busInfo.get(i).distance[0]);
                Log.i("removeLoading", " busInfo.get(i).distance[2]" + busInfo.get(i).distance[1]);
                Log.i("removeLoading", " busInfo.get(i).distance[2]" + busInfo.get(i).distance[2]);
            }
            if (busInfo.get(i).getDistance()[1].equals("Loading")) {
                busInfo.get(i).distance[1] = "Not available";
                busInfo.get(i).distance[2] = "Not available";
                Log.i("removeLoading", " busInfo.buscode" + busInfo.get(i).getBusCode());
                Log.i("removeLoading", " busInfo.get(i).distance[2]" + busInfo.get(i).distance[0]);
                Log.i("removeLoading", " busInfo.get(i).distance[2]" + busInfo.get(i).distance[1]);
            }
            if (busInfo.get(i).getDistance()[1].equals("Loading")) {
                busInfo.get(i).distance[2] = "Not available";
                Log.i("removeLoading", " busInfo.buscode" + busInfo.get(i).getBusCode());
                Log.i("removeLoading", " busInfo.get(i).distance[2]" + busInfo.get(i).distance[2]);
            }*//*
        }

    }*/


    public void setLongDistanceNotLoading(){
        longName = "Loading";
    }
    public void setAddedToPopup(boolean addedToPopup) {
        this.addedToPopup = addedToPopup;
    }

    public boolean isAddedToPopup(){
        return addedToPopup;
    }

    public void setBusCode(String codeForStop) {
        stopCode = codeForStop;
    }


    public void setLongName(String longName) {

        if(longName != null) {
            this.longName = longName.replace("-", "");


            //this.longName = this.longName.replaceAll(" ", "");
            if (this.longName.length() > 44) {
                for (int i = 45; i >= 0; i--) {
                    if (this.longName.charAt(i) == ' ') {
                        this.longName = this.longName.substring(0, i) + "\n" + this.longName.substring(i + 1, this.longName.length());
                        break;
                    }

                }
                for (int i = 30; i >= 0; i--) {
                    if (this.longName.charAt(i) == ' ') {
                        this.longName = this.longName.substring(0, i) + "\n" + this.longName.substring(i + 1, this.longName.length());
                        break;
                    }

                }
                for (int i = 16; i >= 0; i--) {
                    if (this.longName.charAt(i) == ' ') {
                        this.longName = this.longName.substring(0, i) + "\n" + this.longName.substring(i + 1, this.longName.length());
                        break;
                    }

                }
            } else if (this.longName.length() > 29) {
                for (int i = 30; i >= 0; i--) {
                    if (this.longName.charAt(i) == ' ') {
                        this.longName = this.longName.substring(0, i) + "\n" + this.longName.substring(i + 1, this.longName.length());
                        break;
                    }

                }
                for (int i = 13; i >= 0; i--) {
                    if (this.longName.charAt(i) == ' ') {
                        this.longName = this.longName.substring(0, i) + "\n" + this.longName.substring(i + 1, this.longName.length());
                        break;
                    }

                }

            } else if (this.longName.length() > 16) {
                for (int i = 16; i >= 0; i--) {
                    if (this.longName.charAt(i) == ' ') {
                        this.longName = this.longName.substring(0, i) + "\n" + this.longName.substring(i + 1, this.longName.length());
                        break;
                    }

                }
            }
        }else{
            this.longName = "Not available";
        }

    }

    public void setBusName(String name) {
        busName = name;
    }

    public void setHashMapKey(String busCodeAndBusName) {
        this.busCodeAndBusName = busCodeAndBusName;
    }

    public String getHashMapKey() {
        return busCodeAndBusName;
    }

    public void setBusId(String nameOfBus) {
        busNameId = nameOfBus;
    }

    public void setBusStopLat(String latatudeIn) {
        latitude = latatudeIn;
    }

    public void setBusStopLng(String longitudeIn) {
        longitude = longitudeIn;
    }

    public void setForNoUIToast(Boolean forUI){
        forNoUIToast = forUI;
    }

    public void setBusDistance(String busDistance) {

        //made so you can put in one at a time

        Log.i("hasBusCodeBeenploplo", "buscode " + getBusCode());
        Log.i("hasBusCodeBeenploplo", "busDistance " + busDistance);
        Log.i("hasBusCodeBeenploplo", "busDistanceArrayIndex " + busDistanceArrayIndex);
        if(busDistanceArrayIndex < 3) {
           distance[busDistanceArrayIndex] = busDistance;
           busDistanceArrayIndex++;
        }


    }



    public void resetBusDistanceCounter(BusInfo busInfo){


        busInfo.busDistanceArrayIndex = 0;


    }


    public void setBusRadius(String TaskNumber) {
        radius = TaskNumber;
    }


    public static void addBusCodeBeenCalledJson(String busCode){

        addedBusCode.add(busCode);

    }

    public static boolean hasBusCodeBeenCalledJson(String busCode){

        if(addedBusCode.size() > 0) {
            for (int i = 0; i < addedBusCode.size(); i++) {
                if (addedBusCode.get(i).equals(busCode)) {
                    return true;
                }
            }
        }
        return false;
    }



    public static void clearJson(){

        addedBusCode.clear();
    }


    public String getBusCode(){
        return stopCode;
    }

    public String getBusName(){
        return busName;
    }

    public String getLongName(){
        return longName;
    }

    public String getBusStopLatString(){
        return latitude;
    }

    public double getBusStopLat(){
        return Double.parseDouble(latitude);
    }

    public double getBusStopLng(){
        return Double.parseDouble(longitude);
    }

    public String getBusStopLngString(){
        return longitude;
    }

    public String getBusRadius(){
        return radius;
    }

    public String getBusId(){
        return busNameId;
    }

    public String[] getDistance() {
        return distance;
    }



}
