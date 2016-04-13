package POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class ServiceDelivery {

    @SerializedName("ResponseTimestamp")
    @Expose
    private String ResponseTimestamp;
    @SerializedName("StopMonitoringDelivery")
    @Expose
    private List< StopMonitoringDelivery> StopMonitoringDelivery = new ArrayList< StopMonitoringDelivery>();
    @SerializedName("SituationExchangeDelivery")
    @Expose
    private List<Object> SituationExchangeDelivery = new ArrayList<>();

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
     * The StopMonitoringDelivery
     */
    public List<StopMonitoringDelivery> getStopMonitoringDelivery() {
        return StopMonitoringDelivery;
    }

    /**
     *
     * @param StopMonitoringDelivery
     * The StopMonitoringDelivery
     */
    public void setStopMonitoringDelivery(List< StopMonitoringDelivery> StopMonitoringDelivery) {
        this.StopMonitoringDelivery = StopMonitoringDelivery;
    }

    /**
     *
     * @return
     * The SituationExchangeDelivery
     */
    public List<Object> getSituationExchangeDelivery() {
        return SituationExchangeDelivery;
    }

    /**
     *
     * @param SituationExchangeDelivery
     * The SituationExchangeDelivery
     */
    public void setSituationExchangeDelivery(List<Object> SituationExchangeDelivery) {
        this.SituationExchangeDelivery = SituationExchangeDelivery;
    }

}