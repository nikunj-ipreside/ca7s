package com.music.ca7s.model.history_new_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DummyData : Serializable {

    @SerializedName("data")
    @Expose
    var list: ArrayList<HistoryNewModelData> = ArrayList<HistoryNewModelData>()
}