package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public class BusStopData  {

    @SerializedName("limitExceeded")
    @Expose
    private Boolean limitExceeded;
    @SerializedName("stops")
    @Expose
    private List<Stop> stops = new ArrayList<>();

    /**
     *
     * @return
     * The limitExceeded
     */
    public Boolean getLimitExceeded() {
        return limitExceeded;
    }

    /**
     *
     * @param limitExceeded
     * The limitExceeded
     */
    public void setLimitExceeded(Boolean limitExceeded) {
        this.limitExceeded = limitExceeded;
    }

    /**
     *
     * @return
     * The stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     *
     * @param stops
     * The stops
     */
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

}