package com.music.ca7s;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.TrendingData;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class TrendingModel implements Serializable {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<TrendingData> data = new ArrayList<>();
    private final static long serialVersionUID = 6533763140887140636L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TrendingModel() {
    }

    /**
     *
     * @param message
     * @param status
     * @param data
     * @param code
     */
    public TrendingModel(String code, String status, String message, ArrayList<TrendingData> data) {
        super();
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TrendingData> getData() {
        return data;
    }

    public void setData(ArrayList<TrendingData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("status", status).append("message", message).append("data", data).toString();
    }

}
