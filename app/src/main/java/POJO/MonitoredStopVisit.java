package POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class MonitoredStopVisit {

    @SerializedName("MonitoredVehicleJourney")
    @Expose
    private MonitoredVehicleJourney MonitoredVehicleJourney;
    @SerializedName("RecordedAtTime")
    @Expose
    private String RecordedAtTime;

    /**
     *
     * @return
     * The MonitoredVehicleJourney
     */
    public MonitoredVehicleJourney getMonitoredVehicleJourney() {
        return MonitoredVehicleJourney;
    }

    /**
     *
     * @param MonitoredVehicleJourney
     * The MonitoredVehicleJourney
     */
    public void setMonitoredVehicleJourney( MonitoredVehicleJourney MonitoredVehicleJourney) {
        this.MonitoredVehicleJourney = MonitoredVehicleJourney;
    }

    /**
     *
     * @return
     * The RecordedAtTime
     */
    public String getRecordedAtTime() {
        return RecordedAtTime;
    }

    /**
     *
     * @param RecordedAtTime
     * The RecordedAtTime
     */
    public void setRecordedAtTime(String RecordedAtTime) {
        this.RecordedAtTime = RecordedAtTime;
    }

}