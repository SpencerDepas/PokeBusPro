package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class MonitoredCall {

    @SerializedName("ExpectedArrivalTime")
    @Expose
    private String ExpectedArrivalTime;
    @SerializedName("ExpectedDepartureTime")
    @Expose
    private String ExpectedDepartureTime;
    @SerializedName("Extensions")
    @Expose
    private  Extensions Extensions;
    @SerializedName("StopPointRef")
    @Expose
    private String StopPointRef;
    @SerializedName("VisitNumber")
    @Expose
    private Integer VisitNumber;
    @SerializedName("StopPointName")
    @Expose
    private String StopPointName;

    /**
     *
     * @return
     * The ExpectedArrivalTime
     */
    public String getExpectedArrivalTime() {
        return ExpectedArrivalTime;
    }

    /**
     *
     * @param ExpectedArrivalTime
     * The ExpectedArrivalTime
     */
    public void setExpectedArrivalTime(String ExpectedArrivalTime) {
        this.ExpectedArrivalTime = ExpectedArrivalTime;
    }

    /**
     *
     * @return
     * The ExpectedDepartureTime
     */
    public String getExpectedDepartureTime() {
        return ExpectedDepartureTime;
    }

    /**
     *
     * @param ExpectedDepartureTime
     * The ExpectedDepartureTime
     */
    public void setExpectedDepartureTime(String ExpectedDepartureTime) {
        this.ExpectedDepartureTime = ExpectedDepartureTime;
    }

    /**
     *
     * @return
     * The Extensions
     */
    public  Extensions getExtensions() {
        return Extensions;
    }

    /**
     *
     * @param Extensions
     * The Extensions
     */
    public void setExtensions( Extensions Extensions) {
        this.Extensions = Extensions;
    }

    /**
     *
     * @return
     * The StopPointRef
     */
    public String getStopPointRef() {
        return StopPointRef;
    }

    /**
     *
     * @param StopPointRef
     * The StopPointRef
     */
    public void setStopPointRef(String StopPointRef) {
        this.StopPointRef = StopPointRef;
    }

    /**
     *
     * @return
     * The VisitNumber
     */
    public Integer getVisitNumber() {
        return VisitNumber;
    }

    /**
     *
     * @param VisitNumber
     * The VisitNumber
     */
    public void setVisitNumber(Integer VisitNumber) {
        this.VisitNumber = VisitNumber;
    }

    /**
     *
     * @return
     * The StopPointName
     */
    public String getStopPointName() {
        return StopPointName;
    }

    /**
     *
     * @param StopPointName
     * The StopPointName
     */
    public void setStopPointName(String StopPointName) {
        this.StopPointName = StopPointName;
    }

}
