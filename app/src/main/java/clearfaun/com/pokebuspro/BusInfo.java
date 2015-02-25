package clearfaun.com.pokebuspro;

/**
 * Created by spencer on 2/21/2015.
 */
public class BusInfo {

    public String stopCode = "";
    public String busNameId = "";
    public String[] distance = {"Not available", "Not available","Not available"};
    public String latitude = "";
    public String longitude = "";
    public int radiusTaskNumber = 0;
    boolean distanceIsSet = false;
    public String busName = "";
    //constructor




    public void setBusCode(String codeForStop) {
        stopCode = codeForStop;
    }

    public void setBusName(String name) {
        busName = name;
    }

    public void setDistanceBoolean(Boolean setDistance) {
        distanceIsSet = setDistance;
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

    public void setBusDistance(String[] busDistance) {
        distance = busDistance;
    }

    public void busRadiusTaskNumber(int TaskNumber) {
        radiusTaskNumber = TaskNumber;
    }


    public int getBusCodeInt(){
        return Integer.parseInt(stopCode);
    }

    public String getBusCode(){
        return stopCode;
    }

    public String getBusName(){
        return busName;
    }

    public Boolean getDistanceBoolean(){
        return distanceIsSet;
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

    public int getBusRadiusTaskNumber(){
        return radiusTaskNumber;
    }

    public String getBusId(){
        return busNameId;
    }

    public String[] getDistance() {
        return distance;
    }



}
