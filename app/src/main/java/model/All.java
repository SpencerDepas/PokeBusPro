package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 7/26/16.
 */
public class All {

    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("line")
    @Expose
    private String line;
    @SerializedName("line_name")
    @Expose
    private String lineName;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("aimed_departure_time")
    @Expose
    private String aimedDepartureTime;
    @SerializedName("expected_departure_time")
    @Expose
    private String expectedDepartureTime;
    @SerializedName("best_departure_estimate")
    @Expose
    private String bestDepartureEstimate;
    @SerializedName("source")
    @Expose
    private String source;

    /**
     *
     * @return
     * The mode
     */
    public String getMode() {
        return mode;
    }

    /**
     *
     * @param mode
     * The mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     *
     * @return
     * The line
     */
    public String getLine() {
        return line;
    }

    /**
     *
     * @param line
     * The line
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     *
     * @return
     * The lineName
     */
    public String getLineName() {
        return lineName;
    }

    /**
     *
     * @param lineName
     * The line_name
     */
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    /**
     *
     * @return
     * The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     *
     * @param direction
     * The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     *
     * @return
     * The operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     *
     * @param operator
     * The operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     *
     * @return
     * The aimedDepartureTime
     */
    public String getAimedDepartureTime() {
        return aimedDepartureTime;
    }

    /**
     *
     * @param aimedDepartureTime
     * The aimed_departure_time
     */
    public void setAimedDepartureTime(String aimedDepartureTime) {
        this.aimedDepartureTime = aimedDepartureTime;
    }

    /**
     *
     * @return
     * The expectedDepartureTime
     */
    public String getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    /**
     *
     * @param expectedDepartureTime
     * The expected_departure_time
     */
    public void setExpectedDepartureTime(String expectedDepartureTime) {
        this.expectedDepartureTime = expectedDepartureTime;
    }

    /**
     *
     * @return
     * The bestDepartureEstimate
     */
    public String getBestDepartureEstimate() {
        return bestDepartureEstimate;
    }

    /**
     *
     * @param bestDepartureEstimate
     * The best_departure_estimate
     */
    public void setBestDepartureEstimate(String bestDepartureEstimate) {
        this.bestDepartureEstimate = bestDepartureEstimate;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

}