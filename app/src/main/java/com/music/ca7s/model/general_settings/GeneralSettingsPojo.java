
package com.music.ca7s.model.general_settings;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class GeneralSettingsPojo extends BaseModel{


    @SerializedName("data")
    @Expose
    private List<GeneralSettingsData> data = null;

    public List<GeneralSettingsData> getData() {
        return data;
    }

    public void setData(List<GeneralSettingsData> data) {
        this.data = data;
    }

}
