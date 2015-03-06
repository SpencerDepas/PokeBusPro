package clearfaun.com.pokebuspro;

/**
 * Created by spencer on 2/21/2015.
 */
public class BusInfo {

    public String stopCode = "";
    public String busNameId = "";
    public String latitude = "";
    public String longitude = "";
    public int radiusTaskNumber = 0;
    boolean markerSet = false;
    public String busName = "";
    public boolean forNoUIToast = false;
    //public String[] distance = {"Not available", "Not available","Not available"};
    public String[] distance = {"Not available", "Not available","Not available"};
    //constructor




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

    public void setBusDistance(String[] busDistance) {
        distance = new String[3];
        distance[0] = "Not available";
        distance[1] = "Not available";
        distance[2] = "Not available";
        for(int i = 0 ; i < busDistance.length; i ++) {
            distance[i] = busDistance[i];
        }
    }

    public void busRadiusTaskNumber(int TaskNumber) {
        radiusTaskNumber = TaskNumber;
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
