package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class VehicleLocation {

    @SerializedName("Longitude")
    @Expose
    private Double Longitude;
    @SerializedName("Latitude")
    @Expose
    private Double Latitude;

    /**
     *
     * @return
     * The Longitude
     */
    public Double getLongitude() {
        return Longitude;
    }

    /**
     *
     * @param Longitude
     * The Longitude
     */
    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    /**
     *
     * @return
     * The Latitude
     */
    public Double getLatitude() {
        return Latitude;
    }

    /**
     *
     * @param Latitude
     * The Latitude
     */
    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

}