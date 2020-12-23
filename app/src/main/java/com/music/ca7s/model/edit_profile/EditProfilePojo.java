
package com.music.ca7s.model.edit_profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

import java.util.List;

public class EditProfilePojo extends BaseModel {

    @SerializedName("data")
    @Expose
    private EditProfileData data = null;

    public EditProfileData getData() {
        return data;
    }

    public void setData(EditProfileData data) {
        this.data = data;
    }


}
