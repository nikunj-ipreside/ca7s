package com.music.ca7s.model.history_new_model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.music.ca7s.model.User



class HistoryNewModelData : Serializable {
    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("stream_url")
    @Expose
    var streamUrl: String = ""
    @SerializedName("user_id")
    @Expose
    var userId: String = ""
    @SerializedName("image_url")
    @Expose
    var imageUrl: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("lyrics")
    @Expose
    var lyrics: String = ""
    @SerializedName("artist_name")
    @Expose
    var artistName: String = ""
    @SerializedName("genre_id")
    @Expose
    var genreId: Long = 0
    @SerializedName("album_name")
    @Expose
    var albumName: String = ""
    @SerializedName("privacy")
    @Expose
    var privacy: Long = 0
    @SerializedName("published_at")
    @Expose
    var publishedAt: String = ""
    @SerializedName("filename")
    @Expose
    var filename: String = ""
    @SerializedName("download_filename")
    @Expose
    var downloadFilename: String = ""
    @SerializedName("album_id")
    @Expose
    var albumId: Long = 0
    @SerializedName("private")
    @Expose
    var _private: Boolean = false
    @SerializedName("is_like")
    @Expose
    var isLike: Boolean = false
    @SerializedName("like_count")
    @Expose
    var likeCount: Long = 0
    @SerializedName("is_favorite")
    @Expose
    var isFavorite: Boolean = false
    @SerializedName("is_playlist")
    @Expose
    var isPlaylist: String = ""
    @SerializedName("is_download")
    @Expose
    var isDownload: Boolean = false
//    @SerializedName("user")
//    @Expose
//    var user: User? = null
    @SerializedName("track_type")
    @Expose
    var trackType: Long = 0
    @SerializedName("created_at")
    @Expose
    var createdAt: String = ""
    @SerializedName("release_date")
    @Expose
    var releaseDate: String = ""
    @SerializedName("release_timestamp")
    @Expose
    var releaseTimestamp: Long = 0
    @SerializedName("duration")
    @Expose
    var duration: String = ""
    @SerializedName("permalink")
    @Expose
    var permalink: String = ""
    @SerializedName("description")
    @Expose
    var description: String = ""
    @SerializedName("geo")
    @Expose
    var geo: String = ""
    @SerializedName("tags")
    @Expose
    var tags: String = ""
    @SerializedName("taged_artists")
    @Expose
    var tagedArtists: String = ""
    @SerializedName("bpm")
    @Expose
    var bpm: String = ""
    @SerializedName("key")
    @Expose
    var key: String = ""
    @SerializedName("license")
    @Expose
    var license: String = ""
    @SerializedName("version")
    @Expose
    var version: String = ""
    @SerializedName("type")
    @Expose
    var type: String = ""
    @SerializedName("downloadable")
    @Expose
    var downloadable: Boolean = false
    @SerializedName("genre")
    @Expose
    var genre: String = ""
    @SerializedName("genre_slush")
    @Expose
    var genreSlush: String = ""
    @SerializedName("uri")
    @Expose
    var uri: String = ""
    @SerializedName("permalink_url")
    @Expose
    var permalinkUrl: String = ""
    @SerializedName("thumb")
    @Expose
    var thumb: String = ""
    @SerializedName("artwork_url")
    @Expose
    var artworkUrl: String = ""
    @SerializedName("artwork_url_retina")
    @Expose
    var artworkUrlRetina: String = ""
    @SerializedName("background_url")
    @Expose
    var backgroundUrl: String = ""
    @SerializedName("waveform_data")
    @Expose
    var waveformData: String = ""
    @SerializedName("waveform_url")
    @Expose
    var waveformUrl: String = ""
    @SerializedName("preview_url")
    @Expose
    var previewUrl: String = ""
    @SerializedName("download_url")
    @Expose
    var downloadUrl: String = ""
    @SerializedName("playback_count")
    @Expose
    var playbackCount: String = ""
    @SerializedName("download_count")
    @Expose
    var downloadCount: String = ""
    @SerializedName("favoritings_count")
    @Expose
    var favoritingsCount: String = ""
    @SerializedName("reshares_count")
    @Expose
    var resharesCount: String = ""
    @SerializedName("comment_count")
    @Expose
    var commentCount: String = ""
    @SerializedName("played")
    @Expose
    var played: Boolean = false
    @SerializedName("favorited")
    @Expose
    var favorited: Boolean = false
    @SerializedName("liked")
    @Expose
    var liked: Boolean = false
    @SerializedName("thirdparty_song")
    @Expose
    var thirdparty_song: Boolean = false
    @SerializedName("reshared")
    @Expose
    var reshared: Boolean = false
    private  val serialVersionUID = 7359211723985150990L
}