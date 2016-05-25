package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public class Stop  {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("locationType")
    @Expose
    private Integer locationType;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("routes")
    @Expose
    private List<Route> routes = new ArrayList<>();
    @SerializedName("wheelchairBoarding")
    @Expose
    private String wheelchairBoarding;

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     *
     * @param direction
     * The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The locationType
     */
    public Integer getLocationType() {
        return locationType;
    }

    /**
     *
     * @param locationType
     * The locationType
     */
    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    /**
     *
     * @return
     * The lon
     */
    public Double getLon() {
        return lon;
    }

    /**
     *
     * @param lon
     * The lon
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The routes
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     *
     * @param routes
     * The routes
     */
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    /**
     *
     * @return
     * The wheelchairBoarding
     */
    public String getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    /**
     *
     * @param wheelchairBoarding
     * The wheelchairBoarding
     */
    public void setWheelchairBoarding(String wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
    }

}
