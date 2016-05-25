package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class MonitoredVehicleJourney {

    @SerializedName("LineRef")
    @Expose
    private String LineRef;
    @SerializedName("DirectionRef")
    @Expose
    private String DirectionRef;
    @SerializedName("FramedVehicleJourneyRef")
    @Expose
    private  FramedVehicleJourneyRef FramedVehicleJourneyRef;
    @SerializedName("JourneyPatternRef")
    @Expose
    private String JourneyPatternRef;
    @SerializedName("PublishedLineName")
    @Expose
    private String PublishedLineName;
    @SerializedName("OperatorRef")
    @Expose
    private String OperatorRef;
    @SerializedName("OriginRef")
    @Expose
    private String OriginRef;
    @SerializedName("DestinationRef")
    @Expose
    private String DestinationRef;
    @SerializedName("DestinationName")
    @Expose
    private String DestinationName;
    @SerializedName("SituationRef")
    @Expose
    private List<Object> SituationRef = new  ArrayList<Object>();
    @SerializedName("Monitored")
    @Expose
    private Boolean Monitored;
    @SerializedName("VehicleLocation")
    @Expose
    private  VehicleLocation VehicleLocation;
    @SerializedName("Bearing")
    @Expose
    private Double Bearing;
    @SerializedName("ProgressRate")
    @Expose
    private String ProgressRate;
    @SerializedName("VehicleRef")
    @Expose
    private String VehicleRef;
    @SerializedName("MonitoredCall")
    @Expose
    private  MonitoredCall MonitoredCall;
    @SerializedName("OnwardCalls")
    @Expose
    private  OnwardCalls OnwardCalls;

    /**
     *
     * @return
     * The LineRef
     */
    public String getLineRef() {
        return LineRef;
    }

    /**
     *
     * @param LineRef
     * The LineRef
     */
    public void setLineRef(String LineRef) {
        this.LineRef = LineRef;
    }

    /**
     *
     * @return
     * The DirectionRef
     */
    public String getDirectionRef() {
        return DirectionRef;
    }

    /**
     *
     * @param DirectionRef
     * The DirectionRef
     */
    public void setDirectionRef(String DirectionRef) {
        this.DirectionRef = DirectionRef;
    }

    /**
     *
     * @return
     * The FramedVehicleJourneyRef
     */
    public  FramedVehicleJourneyRef getFramedVehicleJourneyRef() {
        return FramedVehicleJourneyRef;
    }

    /**
     *
     * @param FramedVehicleJourneyRef
     * The FramedVehicleJourneyRef
     */
    public void setFramedVehicleJourneyRef( FramedVehicleJourneyRef FramedVehicleJourneyRef) {
        this.FramedVehicleJourneyRef = FramedVehicleJourneyRef;
    }

    /**
     *
     * @return
     * The JourneyPatternRef
     */
    public String getJourneyPatternRef() {
        return JourneyPatternRef;
    }

    /**
     *
     * @param JourneyPatternRef
     * The JourneyPatternRef
     */
    public void setJourneyPatternRef(String JourneyPatternRef) {
        this.JourneyPatternRef = JourneyPatternRef;
    }

    /**
     *
     * @return
     * The PublishedLineName
     */
    public String getPublishedLineName() {
        return PublishedLineName;
    }

    /**
     *
     * @param PublishedLineName
     * The PublishedLineName
     */
    public void setPublishedLineName(String PublishedLineName) {
        this.PublishedLineName = PublishedLineName;
    }

    /**
     *
     * @return
     * The OperatorRef
     */
    public String getOperatorRef() {
        return OperatorRef;
    }

    /**
     *
     * @param OperatorRef
     * The OperatorRef
     */
    public void setOperatorRef(String OperatorRef) {
        this.OperatorRef = OperatorRef;
    }

    /**
     *
     * @return
     * The OriginRef
     */
    public String getOriginRef() {
        return OriginRef;
    }

    /**
     *
     * @param OriginRef
     * The OriginRef
     */
    public void setOriginRef(String OriginRef) {
        this.OriginRef = OriginRef;
    }

    /**
     *
     * @return
     * The DestinationRef
     */
    public String getDestinationRef() {
        return DestinationRef;
    }

    /**
     *
     * @param DestinationRef
     * The DestinationRef
     */
    public void setDestinationRef(String DestinationRef) {
        this.DestinationRef = DestinationRef;
    }

    /**
     *
     * @return
     * The DestinationName
     */
    public String getDestinationName() {
        return DestinationName;
    }

    /**
     *
     * @param DestinationName
     * The DestinationName
     */
    public void setDestinationName(String DestinationName) {
        this.DestinationName = DestinationName;
    }

    /**
     *
     * @return
     * The SituationRef
     */
    public List<Object> getSituationRef() {
        return SituationRef;
    }

    /**
     *
     * @param SituationRef
     * The SituationRef
     */
    public void setSituationRef(List<Object> SituationRef) {
        this.SituationRef = SituationRef;
    }

    /**
     *
     * @return
     * The Monitored
     */
    public Boolean getMonitored() {
        return Monitored;
    }

    /**
     *
     * @param Monitored
     * The Monitored
     */
    public void setMonitored(Boolean Monitored) {
        this.Monitored = Monitored;
    }

    /**
     *
     * @return
     * The VehicleLocation
     */
    public VehicleLocation getVehicleLocation() {
        return VehicleLocation;
    }

    /**
     *
     * @param VehicleLocation
     * The VehicleLocation
     */
    public void setVehicleLocation( VehicleLocation VehicleLocation) {
        this.VehicleLocation = VehicleLocation;
    }

    /**
     *
     * @return
     * The Bearing
     */
    public Double getBearing() {
        return Bearing;
    }

    /**
     *
     * @param Bearing
     * The Bearing
     */
    public void setBearing(Double Bearing) {
        this.Bearing = Bearing;
    }

    /**
     *
     * @return
     * The ProgressRate
     */
    public String getProgressRate() {
        return ProgressRate;
    }

    /**
     *
     * @param ProgressRate
     * The ProgressRate
     */
    public void setProgressRate(String ProgressRate) {
        this.ProgressRate = ProgressRate;
    }

    /**
     *
     * @return
     * The VehicleRef
     */
    public String getVehicleRef() {
        return VehicleRef;
    }

    /**
     *
     * @param VehicleRef
     * The VehicleRef
     */
    public void setVehicleRef(String VehicleRef) {
        this.VehicleRef = VehicleRef;
    }

    /**
     *
     * @return
     * The MonitoredCall
     */
    public  MonitoredCall getMonitoredCall() {
        return MonitoredCall;
    }

    /**
     *
     * @param MonitoredCall
     * The MonitoredCall
     */
    public void setMonitoredCall( MonitoredCall MonitoredCall) {
        this.MonitoredCall = MonitoredCall;
    }

    /**
     *
     * @return
     * The OnwardCalls
     */
    public  OnwardCalls getOnwardCalls() {
        return OnwardCalls;
    }

    /**
     *
     * @param OnwardCalls
     * The OnwardCalls
     */
    public void setOnwardCalls( OnwardCalls OnwardCalls) {
        this.OnwardCalls = OnwardCalls;
    }

}