package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class FramedVehicleJourneyRef {

    @SerializedName("DataFrameRef")
    @Expose
    private String DataFrameRef;
    @SerializedName("DatedVehicleJourneyRef")
    @Expose
    private String DatedVehicleJourneyRef;

    /**
     *
     * @return
     * The DataFrameRef
     */
    public String getDataFrameRef() {
        return DataFrameRef;
    }

    /**
     *
     * @param DataFrameRef
     * The DataFrameRef
     */
    public void setDataFrameRef(String DataFrameRef) {
        this.DataFrameRef = DataFrameRef;
    }

    /**
     *
     * @return
     * The DatedVehicleJourneyRef
     */
    public String getDatedVehicleJourneyRef() {
        return DatedVehicleJourneyRef;
    }

    /**
     *
     * @param DatedVehicleJourneyRef
     * The DatedVehicleJourneyRef
     */
    public void setDatedVehicleJourneyRef(String DatedVehicleJourneyRef) {
        this.DatedVehicleJourneyRef = DatedVehicleJourneyRef;
    }

}
