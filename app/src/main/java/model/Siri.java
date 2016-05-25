package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class Siri {

    @SerializedName("ServiceDelivery")
    @Expose
    private ServiceDelivery ServiceDelivery;

    /**
     *
     * @return
     * The ServiceDelivery
     */
    public  ServiceDelivery getServiceDelivery() {
        return ServiceDelivery;
    }

    /**
     *
     * @param ServiceDelivery
     * The ServiceDelivery
     */
    public void setServiceDelivery( ServiceDelivery ServiceDelivery) {
        this.ServiceDelivery = ServiceDelivery;
    }

}
