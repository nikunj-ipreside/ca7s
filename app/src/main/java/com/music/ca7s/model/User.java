package com.music.ca7s.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("permalink_url")
    @Expose
    private String permalinkUrl;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    private final static long serialVersionUID = 2375362388600618472L;

    /**
     * No args constructor for use in serialization
     */
    public User() {
    }

    /**
     * @param id
     * @param username
     * @param permalinkUrl
     * @param permalink
     * @param avatarUrl
     * @param caption
     * @param uri
     */
    public User(String id, String permalink, String username, String caption, String uri, String permalinkUrl, String avatarUrl) {
        super();
        this.id = id;
        this.permalink = permalink;
        this.username = username;
        this.caption = caption;
        this.uri = uri;
        this.permalinkUrl = permalinkUrl;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("permalink", permalink).append("username", username).append("caption", caption).append("uri", uri).append("permalinkUrl", permalinkUrl).append("avatarUrl", avatarUrl).toString();
    }
}
