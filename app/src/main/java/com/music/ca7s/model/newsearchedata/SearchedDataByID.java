package com.music.ca7s.model.newsearchedata;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchedDataByID implements Serializable
{

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("mime")
    @Expose
    private String mime;
    @SerializedName("original_filename")
    @Expose
    private String originalFilename;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("song_title")
    @Expose
    private String songTitle;
    @SerializedName("album_name")
    @Expose
    private String albumName;
    @SerializedName("artist_name")
    @Expose
    private String artistName;
    @SerializedName("genre_id")
    @Expose
    private long genreId;
    @SerializedName("year")
    @Expose
    private Object year;
    @SerializedName("lyrics")
    @Expose
    private Object lyrics;
    @SerializedName("size")
    @Expose
    private Double size;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("count")
    @Expose
    private long count;
    @SerializedName("privacy")
    @Expose
    private long privacy;
    @SerializedName("soft_delete")
    @Expose
    private long softDelete;
    @SerializedName("new_release")
    @Expose
    private long newRelease;
    @SerializedName("download_count")
    @Expose
    private Object downloadCount;
    @SerializedName("stream_count")
    @Expose
    private Object streamCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("thirdparty_song")
    @Expose
    private boolean thirdpartySong;
    @SerializedName("is_like")
    @Expose
    private boolean isLike;
    @SerializedName("is_favorite")
    @Expose
    private boolean isFavorite;
    @SerializedName("is_playlist")
    @Expose
    private boolean isPlaylist;
    private final static long serialVersionUID = 5341059243666139886L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchedDataByID() {
    }

    /**
     *
     * @param genreId
     * @param albumName
     * @param isLike
     * @param year
     * @param mime
     * @param privacy
     * @param createdAt
     * @param imageUrl
     * @param id
     * @param streamCount
     * @param lyrics
     * @param updatedAt
     * @param streamUrl
     * @param songTitle
     * @param isPlaylist
     * @param count
     * @param userId
     * @param filename
     * @param newRelease
     * @param size
     * @param softDelete
     * @param artistName
     * @param userType
     * @param originalFilename
     * @param downloadCount
     * @param thirdpartySong
     * @param status
     * @param isFavorite
     */
    public SearchedDataByID(long id, long userId, String filename, String mime, String originalFilename,
                            String imageUrl, String songTitle, String albumName, String artistName,
                            long genreId, Object year, Object lyrics, Double size, String userType, long status, long count, long privacy, long softDelete, long newRelease, Object downloadCount, Object streamCount, String createdAt, String updatedAt, String streamUrl, boolean thirdpartySong, boolean isLike, boolean isFavorite, boolean isPlaylist) {
        super();
        this.id = id;
        this.userId = userId;
        this.filename = filename;
        this.mime = mime;
        this.originalFilename = originalFilename;
        this.imageUrl = imageUrl;
        this.songTitle = songTitle;
        this.albumName = albumName;
        this.artistName = artistName;
        this.genreId = genreId;
        this.year = year;
        this.lyrics = lyrics;
        this.size = size;
        this.userType = userType;
        this.status = status;
        this.count = count;
        this.privacy = privacy;
        this.softDelete = softDelete;
        this.newRelease = newRelease;
        this.downloadCount = downloadCount;
        this.streamCount = streamCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.streamUrl = streamUrl;
        this.thirdpartySong = thirdpartySong;
        this.isLike = isLike;
        this.isFavorite = isFavorite;
        this.isPlaylist = isPlaylist;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public Object getYear() {
        return year;
    }

    public void setYear(Object year) {
        this.year = year;
    }

    public Object getLyrics() {
        return lyrics;
    }

    public void setLyrics(Object lyrics) {
        this.lyrics = lyrics;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getPrivacy() {
        return privacy;
    }

    public void setPrivacy(long privacy) {
        this.privacy = privacy;
    }

    public long getSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(long softDelete) {
        this.softDelete = softDelete;
    }

    public long getNewRelease() {
        return newRelease;
    }

    public void setNewRelease(long newRelease) {
        this.newRelease = newRelease;
    }

    public Object getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Object downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Object getStreamCount() {
        return streamCount;
    }

    public void setStreamCount(Object streamCount) {
        this.streamCount = streamCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public boolean isThirdpartySong() {
        return thirdpartySong;
    }

    public void setThirdpartySong(boolean thirdpartySong) {
        this.thirdpartySong = thirdpartySong;
    }

    public boolean isIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean isIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isIsPlaylist() {
        return isPlaylist;
    }

    public void setIsPlaylist(boolean isPlaylist) {
        this.isPlaylist = isPlaylist;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("filename", filename).append("mime", mime).append("originalFilename", originalFilename).append("imageUrl", imageUrl).append("songTitle", songTitle).append("albumName", albumName).append("artistName", artistName).append("genreId", genreId).append("year", year).append("lyrics", lyrics).append("size", size).append("userType", userType).append("status", status).append("count", count).append("privacy", privacy).append("softDelete", softDelete).append("newRelease", newRelease).append("downloadCount", downloadCount).append("streamCount", streamCount).append("createdAt", createdAt).append("updatedAt", updatedAt).append("streamUrl", streamUrl).append("thirdpartySong", thirdpartySong).append("isLike", isLike).append("isFavorite", isFavorite).append("isPlaylist", isPlaylist).toString();
    }

}
