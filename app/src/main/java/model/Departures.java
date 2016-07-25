package model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Departures {

    @SerializedName("59")
    @Expose
    private List<com.example._59> _59 = new ArrayList<com.example._59>();
    @SerializedName("68")
    @Expose
    private List<com.example._68> _68 = new ArrayList<com.example._68>();

    /**
     *
     * @return
     * The _59
     */
    public List<com.example._59> get59() {
        return _59;
    }

    /**
     *
     * @param _59
     * The 59
     */
    public void set59(List<com.example._59> _59) {
        this._59 = _59;
    }

    /**
     *
     * @return
     * The _68
     */
    public List<com.example._68> get68() {
        return _68;
    }

    /**
     *
     * @param _68
     * The 68
     */
    public void set68(List<com.example._68> _68) {
        this._68 = _68;
    }

}