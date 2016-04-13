package POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class Distances {

    @SerializedName("PresentableDistance")
    @Expose
    private String PresentableDistance;
    @SerializedName("DistanceFromCall")
    @Expose
    private Double DistanceFromCall;
    @SerializedName("StopsFromCall")
    @Expose
    private Integer StopsFromCall;
    @SerializedName("CallDistanceAlongRoute")
    @Expose
    private Double CallDistanceAlongRoute;

/**
 *
 * @return
 * The PresentableDistance
 */
    public String getPresentableDistance() {
        return PresentableDistance;
    }

    /**
     *
     * @param PresentableDistance
     * The PresentableDistance
     */
    public void setPresentableDistance(String PresentableDistance) {
        this.PresentableDistance = PresentableDistance;
    }

    /**
     *
     * @return
     * The DistanceFromCall
     */
    public Double getDistanceFromCall() {
        return DistanceFromCall;
    }

    /**
     *
     * @param DistanceFromCall
     * The DistanceFromCall
     */
    public void setDistanceFromCall(Double DistanceFromCall) {
        this.DistanceFromCall = DistanceFromCall;
    }

    /**
     *
     * @return
     * The StopsFromCall
     */
    public Integer getStopsFromCall() {
        return StopsFromCall;
    }

    /**
     *
     * @param StopsFromCall
     * The StopsFromCall
     */
    public void setStopsFromCall(Integer StopsFromCall) {
        this.StopsFromCall = StopsFromCall;
    }

    /**
     *
     * @return
     * The CallDistanceAlongRoute
     */
    public Double getCallDistanceAlongRoute() {
        return CallDistanceAlongRoute;
    }

    /**
     *
     * @param CallDistanceAlongRoute
     * The CallDistanceAlongRoute
     */
    public void setCallDistanceAlongRoute(Double CallDistanceAlongRoute) {
        this.CallDistanceAlongRoute = CallDistanceAlongRoute;
    }

}

