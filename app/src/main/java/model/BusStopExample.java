package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public class BusStopExample {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("currentTime")
    @Expose
    private String currentTime;
    @SerializedName("data")
    @Expose
    private BusStopData data;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("version")
    @Expose
    private Integer version;

    /**
     *
     * @return
     * The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The currentTime
     */
    public String getCurrentTime() {
        return currentTime;
    }

    /**
     *
     * @param currentTime
     * The currentTime
     */
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    /**
     *
     * @return
     * The data
     */
    public BusStopData getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(BusStopData data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

}