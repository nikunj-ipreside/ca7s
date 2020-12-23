package com.music.ca7s.model.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class SignUpPojo extends BaseModel {

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
