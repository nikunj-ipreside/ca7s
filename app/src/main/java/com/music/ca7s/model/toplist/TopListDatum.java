
package com.music.ca7s.model.toplist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopListDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image_icon")
    @Expose
    private String imageUrl;
    @SerializedName("tracks_count")
//    @Expose
//    private String imageIcon;
//    @SerializedName("image_url")
    @Expose
    private Integer tracksCount;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("image_url")
    @Expose
    private String url;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("user")
    @Expose
    private TopUser user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public String getImageIcon() {
//        return imageIcon;
//    }
//
//    public void setImageIcon(String imageIcon) {
//        this.imageIcon = imageIcon;
//    }

    public Integer getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(Integer tracksCount) {
        this.tracksCount = tracksCount;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public TopUser getUser() {
        return user;
    }

    public void setUser(TopUser topUser) {
        this.user = topUser;
    }

}
