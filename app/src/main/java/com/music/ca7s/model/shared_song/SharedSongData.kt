package com.music.ca7s.model.shared_song

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SharedSongData : Serializable {

    @SerializedName("id")
    @Expose
    var id: Long = 0
    @SerializedName("user_id")
    @Expose
    var userId: Long = 0
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("music_description")
    @Expose
    var musicDescription: String = ""
    @SerializedName("music_thumbnail")
    @Expose
    var musicThumbnail: String = ""
    @SerializedName("music_url")
    @Expose
    var musicUrl: String = ""
    @SerializedName("track_id")
    @Expose
    var trackId: String = ""
    @SerializedName("is_deleted")
    @Expose
    var isDeleted: Long = 0
    @SerializedName("created_at")
    @Expose
    var createdAt: Any? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: Any? = null
    @SerializedName("thirdparty_song")
    @Expose
    var thirdpartySong: Boolean = false
    @SerializedName("is_like")
    @Expose
    var isLike: Boolean = false
    @SerializedName("is_favorite")
    @Expose
    var isFavorite: Boolean = false
    @SerializedName("like_count")
    @Expose
    var likeCount: Int = 0
//    @SerializedName("user")
//    @Expose
//    var user: Long = 0
    companion object {
        private const val serialVersionUID = -712521766437114471L
    }

}