
package com.music.ca7s.model.top_track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListTopImages {

    @SerializedName("square_150")
    @Expose
    private String square150;
    @SerializedName("square_250")
    @Expose
    private String square250;
    @SerializedName("square_500")
    @Expose
    private String square500;

    public String getSquare150() {
        return square150;
    }

    public void setSquare150(String square150) {
        this.square150 = square150;
    }

    public String getSquare250() {
        return square250;
    }

    public void setSquare250(String square250) {
        this.square250 = square250;
    }

    public String getSquare500() {
        return square500;
    }

    public void setSquare500(String square500) {
        this.square500 = square500;
    }

}
