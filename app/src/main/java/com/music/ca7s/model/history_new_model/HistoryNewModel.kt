package com.music.ca7s.model.history_new_model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class HistoryNewModel : Serializable {
    @SerializedName("code")
    @Expose
    var code: Long = 0
    @SerializedName("status")
    @Expose
    var status: String = ""
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("list")
    @Expose
    var list: DummyData = DummyData()
    @SerializedName("download_status")
    @Expose
    var downloadStatus: Boolean = false
    private  val serialVersionUID = 8323969222289554695L
}