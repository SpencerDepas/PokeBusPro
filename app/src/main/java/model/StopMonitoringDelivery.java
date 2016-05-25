package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class StopMonitoringDelivery {

    @SerializedName("MonitoredStopVisit")
    @Expose
    private List< MonitoredStopVisit> MonitoredStopVisit = new ArrayList<>();
    @SerializedName("ResponseTimestamp")
    @Expose
    private String ResponseTimestamp;
    @SerializedName("ValidUntil")
    @Expose
    private String ValidUntil;

    /**
     *
     * @return
     * The MonitoredStopVisit
     */
    public List< MonitoredStopVisit> getMonitoredStopVisit() {
        return MonitoredStopVisit;
    }

    /**
     *
     * @param MonitoredStopVisit
     * The MonitoredStopVisit
     */
    public void setMonitoredStopVisit(List<MonitoredStopVisit> MonitoredStopVisit) {
        this.MonitoredStopVisit = MonitoredStopVisit;
    }

    /**
     *
     * @return
     * The ResponseTimestamp
     */
    public String getResponseTimestamp() {
        return ResponseTimestamp;
    }

    /**
     *
     * @param ResponseTimestamp
     * The ResponseTimestamp
     */
    public void setResponseTimestamp(String ResponseTimestamp) {
        this.ResponseTimestamp = ResponseTimestamp;
    }

    /**
     *
     * @return
     * The ValidUntil
     */
    public String getValidUntil() {
        return ValidUntil;
    }

    /**
     *
     * @param ValidUntil
     * The ValidUntil
     */
    public void setValidUntil(String ValidUntil) {
        this.ValidUntil = ValidUntil;
    }

}