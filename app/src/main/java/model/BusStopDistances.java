package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BusStopDistances {

    @SerializedName("atcocode")
    @Expose
    private String atcocode;
    @SerializedName("smscode")
    @Expose
    private String smscode;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("departures")
    @Expose
    private Departures departures;
    @SerializedName("source")
    @Expose
    private String source;

    /**
     *
     * @return
     * The atcocode
     */
    public String getAtcocode() {
        return atcocode;
    }

    /**
     *
     * @param atcocode
     * The atcocode
     */
    public void setAtcocode(String atcocode) {
        this.atcocode = atcocode;
    }

    /**
     *
     * @return
     * The smscode
     */
    public String getSmscode() {
        return smscode;
    }

    /**
     *
     * @param smscode
     * The smscode
     */
    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    /**
     *
     * @return
     * The requestTime
     */
    public String getRequestTime() {
        return requestTime;
    }

    /**
     *
     * @param requestTime
     * The request_time
     */
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    /**
     *
     * @return
     * The departures
     */
    public Departures getDepartures() {
        return departures;
    }

    /**
     *
     * @param departures
     * The departures
     */
    public void setDepartures(Departures departures) {
        this.departures = departures;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

}