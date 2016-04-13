package POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class Extensions {

    @SerializedName("Distances")
    @Expose
    private  Distances Distances;

    /**
     *
     * @return
     * The Distances
     */
    public  Distances getDistances() {
        return Distances;
    }

    /**
     *
     * @param Distances
     * The Distances
     */
    public void setDistances( Distances Distances) {
        this.Distances = Distances;
    }

}