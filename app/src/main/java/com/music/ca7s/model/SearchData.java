package com.music.ca7s.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class SearchData implements Serializable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("release_timestamp")
    @Expose
    public long releaseTimestamp;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("geo")
    @Expose
    public String geo;
    @SerializedName("tags")
    @Expose
    public String tags;
    @SerializedName("taged_artists")
    @Expose
    public String tagedArtists;
    @SerializedName("bpm")
    @Expose
    public String bpm;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("license")
    @Expose
    public String license;
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("downloadable")
    @Expose
    public boolean downloadable;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("genre_slush")
    @Expose
    public String genreSlush;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;
    @SerializedName("thumb")
    @Expose
    public String thumb;
    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;
    @SerializedName("artwork_url_retina")
    @Expose
    public String artworkUrlRetina;
    @SerializedName("background_url")
    @Expose
    public String backgroundUrl;
    @SerializedName("waveform_data")
    @Expose
    public String waveformData;
    @SerializedName("waveform_url")
    @Expose
    public String waveformUrl;
    @SerializedName("stream_url")
    @Expose
    public String streamUrl;
    @SerializedName("preview_url")
    @Expose
    public String previewUrl;
    @SerializedName("download_url")
    @Expose
    public String downloadUrl;
    @SerializedName("download_filename")
    @Expose
    public String downloadFilename;
    @SerializedName("playback_count")
    @Expose
    public String playbackCount;
    @SerializedName("download_count")
    @Expose
    public String downloadCount;
    @SerializedName("favoritings_count")
    @Expose
    public String favoritingsCount;
    @SerializedName("reshares_count")
    @Expose
    public String resharesCount;
    @SerializedName("comment_count")
    @Expose
    public String commentCount;
    @SerializedName("played")
    @Expose
    public boolean played;
    @SerializedName("favorited")
    @Expose
    public boolean favorited;
    @SerializedName("liked")
    @Expose
    public boolean liked;
    @SerializedName("reshared")
    @Expose
    public boolean reshared;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("song_title")
    @Expose
    public String songTitle;
    @SerializedName("filename")
    @Expose
    public String filename;
    @SerializedName("thirdparty_song")
    @Expose
    public boolean thirdpartySong;
    @SerializedName("is_like")
    @Expose
    public boolean isLike;
    @SerializedName("is_favorite")
    @Expose
    public boolean isFavorite;
    @SerializedName("track_type")
    @Expose
    public long trackType;
    @SerializedName("is_playlist")
    @Expose
    public String isPlaylist;
    private final static long serialVersionUID = -4623926330870567439L;


}