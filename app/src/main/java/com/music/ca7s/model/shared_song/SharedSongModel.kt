package com.music.ca7s.model.shared_song

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SharedSongModel : Serializable {

    @SerializedName("code")
    @Expose
    var code: String = ""
    @SerializedName("status")
    @Expose
    var status: String = ""
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("data")
    @Expose
    var data: SharedSongData = SharedSongData()

    companion object {
        private const val serialVersionUID = 842190636824919547L
    }

}