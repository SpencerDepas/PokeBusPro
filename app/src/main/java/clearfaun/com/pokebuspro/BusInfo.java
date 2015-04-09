package clearfaun.com.pokebuspro;

import java.io.Serializable;

/**
 * Created by spencer on 2/21/2015.
 */
public class BusInfo implements Serializable {

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

    public BusInfo(){
        distance = new String[3];
        distance[0] = "Not available";
        distance[1] = "Not available";
        distance[2] = "Not available";
        busDistanceArrayIndex = 0;

    }

    public void setDistanceNotAvailable(){
        distance[0] = "Not available";
        distance[1] = "Not available";
        distance[2] = "Not available";
    }


    public void setBusCode(String codeForStop) {
        stopCode = codeForStop;
    }

    public void setBusName(String name) {
        busName = name;
    }

    public void setMarkerSet(Boolean setMarker) {
        markerSet = setMarker;
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

       if(busDistanceArrayIndex < 3) {
           distance[busDistanceArrayIndex] = busDistance;
           busDistanceArrayIndex++;
       }


    }

    public void setBusRadius(String TaskNumber) {
        radius = TaskNumber;
    }


    public boolean getForNoUIToast(){
        return forNoUIToast;
    }



    public String getBusCode(){
        return stopCode;
    }

    public String getBusName(){
        return busName;
    }

    public Boolean getMarkerSet(){
        return markerSet;
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
