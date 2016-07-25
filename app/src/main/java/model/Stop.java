package model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stop {

    @SerializedName("atcocode")
    @Expose
    private String atcocode;
    @SerializedName("smscode")
    @Expose
    private String smscode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("bearing")
    @Expose
    private String bearing;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("indicator")
    @Expose
    private String indicator;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("distance")
    @Expose
    private Integer distance;

    /**
     * @return The atcocode
     */
    public String getAtcocode() {
        return atcocode;
    }

    /**
     * @param atcocode The atcocode
     */
    public void setAtcocode(String atcocode) {
        this.atcocode = atcocode;
    }

    /**
     * @return The smscode
     */
    public String getSmscode() {
        return smscode;
    }

    /**
     * @param smscode The smscode
     */
    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode The mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return The bearing
     */
    public String getBearing() {
        return bearing;
    }

    /**
     * @param bearing The bearing
     */
    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    /**
     * @return The locality
     */
    public String getLocality() {
        return locality;
    }

    /**
     * @param locality The locality
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * @return The indicator
     */
    public String getIndicator() {
        return indicator;
    }

    /**
     * @param indicator The indicator
     */
    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    /**
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}