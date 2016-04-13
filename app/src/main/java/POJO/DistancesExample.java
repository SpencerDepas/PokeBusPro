package POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/22/16.
 */
public class DistancesExample {

    @SerializedName("Siri")
    @Expose
    private Siri Siri;

    /**
     *
     * @return
     * The Siri
     */
    public  Siri getSiri() {
        return Siri;
    }

    /**
     *
     * @param Siri
     * The Siri
     */
    public void setSiri( Siri Siri) {
        this.Siri = Siri;
    }

}