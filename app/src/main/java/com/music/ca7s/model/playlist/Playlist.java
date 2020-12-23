package com.music.ca7s.model.playlist;

import java.io.Serializable;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Playlist implements Serializable
{

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    private final static long serialVersionUID = -3068770760131434110L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Playlist() {
    }

    /**
     *
     * @param id
     * @param createdAt
     * @param name
     * @param userId
     */
    public Playlist(long id, long userId, String name, String createdAt,String image) {
        super();
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.createdAt = createdAt;
        this.image=image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("name", name).append("createdAt", createdAt).toString();
    }

}
