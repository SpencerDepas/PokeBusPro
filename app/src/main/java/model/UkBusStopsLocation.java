package model;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 7/25/16.
 */
public class UkBusStopsLocation {

    @SerializedName("minlon")
    @Expose
    private Double minlon;
    @SerializedName("minlat")
    @Expose
    private Double minlat;
    @SerializedName("maxlon")
    @Expose
    private Double maxlon;
    @SerializedName("maxlat")
    @Expose
    private Double maxlat;
    @SerializedName("searchlon")
    @Expose
    private Double searchlon;
    @SerializedName("searchlat")
    @Expose
    private Double searchlat;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("rpp")
    @Expose
    private Integer rpp;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("stops")
    @Expose
    private List<Stop> stops = new ArrayList<Stop>();

    /**
     * @return The minlon
     */
    public Double getMinlon() {
        return minlon;
    }

    /**
     * @param minlon The minlon
     */
    public void setMinlon(Double minlon) {
        this.minlon = minlon;
    }

    /**
     * @return The minlat
     */
    public Double getMinlat() {
        return minlat;
    }

    /**
     * @param minlat The minlat
     */
    public void setMinlat(Double minlat) {
        this.minlat = minlat;
    }

    /**
     * @return The maxlon
     */
    public Double getMaxlon() {
        return maxlon;
    }

    /**
     * @param maxlon The maxlon
     */
    public void setMaxlon(Double maxlon) {
        this.maxlon = maxlon;
    }

    /**
     * @return The maxlat
     */
    public Double getMaxlat() {
        return maxlat;
    }

    /**
     * @param maxlat The maxlat
     */
    public void setMaxlat(Double maxlat) {
        this.maxlat = maxlat;
    }

    /**
     * @return The searchlon
     */
    public Double getSearchlon() {
        return searchlon;
    }

    /**
     * @param searchlon The searchlon
     */
    public void setSearchlon(Double searchlon) {
        this.searchlon = searchlon;
    }

    /**
     * @return The searchlat
     */
    public Double getSearchlat() {
        return searchlat;
    }

    /**
     * @param searchlat The searchlat
     */
    public void setSearchlat(Double searchlat) {
        this.searchlat = searchlat;
    }

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The rpp
     */
    public Integer getRpp() {
        return rpp;
    }

    /**
     * @param rpp The rpp
     */
    public void setRpp(Integer rpp) {
        this.rpp = rpp;
    }

    /**
     * @return The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return The requestTime
     */
    public String getRequestTime() {
        return requestTime;
    }

    /**
     * @param requestTime The request_time
     */
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * @return The stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * @param stops The stops
     */
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

}