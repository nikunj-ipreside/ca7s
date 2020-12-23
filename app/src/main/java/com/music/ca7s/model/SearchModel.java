package com.music.ca7s.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchModel implements Serializable {
    @SerializedName("code")
    @Expose
    private long code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<SearchData> data = null;
    private final static long serialVersionUID = -23897450073580294L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchModel() {
    }

    /**
     *
     * @param message
     * @param data
     * @param status
     * @param code
     */
    public SearchModel(long code, String status, String message, ArrayList<SearchData> data) {
        super();
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
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

    public ArrayList<SearchData> getData() {
        return data;
    }

    public void setData(ArrayList<SearchData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("statu", status).append("message", message).append("data", data).toString();
    }
}
