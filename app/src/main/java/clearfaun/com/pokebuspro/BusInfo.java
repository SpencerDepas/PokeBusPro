package clearfaun.com.pokebuspro;

/**
 * Created by spencer on 2/21/2015.
 */
public class BusInfo {

    public String stopCode = "";
    public String busName = "";
    public String distance = "";
    static String latitude = "";
    static String longitude = "";
    public int radiusTaskNumber = 0;
    //constructor


    public void busCode(String codeForStop) {
        stopCode = codeForStop;
    }

    public void busId(String nameOfBus) {
        busName = nameOfBus;
    }

    public void busStopLat(String latatudeIn) {
        latitude = latatudeIn;
    }

    public void busStopLng(String longitudeIn) {
        longitude = longitudeIn;
    }

    public void busDistance(String busDistance) {
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
        return busName;
    }

    public String getDistance() {
        return distance;
    }



}
