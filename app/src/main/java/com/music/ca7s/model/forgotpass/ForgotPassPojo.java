package com.music.ca7s.model.forgotpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class ForgotPassPojo extends BaseModel {
    @SerializedName("note")
    @Expose
    private String note;


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
