package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public class Agency {

    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("privateService")
    @Expose
    private Boolean privateService;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     * The disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     *
     * @param disclaimer
     * The disclaimer
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The lang
     */
    public String getLang() {
        return lang;
    }

    /**
     *
     * @param lang
     * The lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     * The privateService
     */
    public Boolean getPrivateService() {
        return privateService;
    }

    /**
     *
     * @param privateService
     * The privateService
     */
    public void setPrivateService(Boolean privateService) {
        this.privateService = privateService;
    }

    /**
     *
     * @return
     * The timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     *
     * @param timezone
     * The timezone
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}