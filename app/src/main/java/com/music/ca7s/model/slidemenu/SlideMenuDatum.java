
package com.music.ca7s.model.slidemenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlideMenuDatum {

    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_city")
    @Expose
    private String userCity;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("user_token")
    @Expose
    private Object userToken;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Object getUserToken() {
        return userToken;
    }

    public void setUserToken(Object userToken) {
        this.userToken = userToken;
    }

}