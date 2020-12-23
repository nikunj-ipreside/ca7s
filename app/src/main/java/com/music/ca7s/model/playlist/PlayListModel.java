package com.music.ca7s.model.playlist;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PlayListModel implements Serializable {

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
    private PlayListData data;
    private final static long serialVersionUID = 4629844387893362479L;

    /**
     * No args constructor for use in serialization
     */
    public PlayListModel() {
    }

    /**
     * @param message
     * @param status
     * @param data
     * @param code
     */
    public PlayListModel(long code, String status, String message, PlayListData data) {
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

    public PlayListData getData() {
        return data;
    }

    public void setData(PlayListData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("status", status).append("message", message).append("data", data).toString();
    }
}
