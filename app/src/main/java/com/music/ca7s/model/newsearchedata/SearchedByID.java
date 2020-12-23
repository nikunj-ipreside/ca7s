package com.music.ca7s.model.newsearchedata;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchedByID implements Serializable
{

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
    private ArrayList<SearchedDataByID> data = null;
    private final static long serialVersionUID = -6019895519177926161L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchedByID() {
    }

    /**
     *
     * @param code
     * @param data
     * @param message
     * @param status
     */
    public SearchedByID(String code, String status, String message, ArrayList<SearchedDataByID> data) {
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

    public ArrayList<SearchedDataByID> getData() {
        return data;
    }

    public void setData(ArrayList<SearchedDataByID> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("status", status).append("message", message).append("data", data).toString();
    }

}
