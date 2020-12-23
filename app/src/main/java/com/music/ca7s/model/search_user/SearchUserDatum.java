
package com.music.ca7s.model.search_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchUserDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("user_city")
    @Expose
    private String userCity;
    @SerializedName("user_gender")
    @Expose
    private String userGender;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("account_privacy")
    @Expose
    private String accountPrivacy;
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("provider_user_id")
    @Expose
    private String providerUserId;
    @SerializedName("user_status")
    @Expose
    private String userStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAccountPrivacy() {
        return accountPrivacy;
    }

    public void setAccountPrivacy(String accountPrivacy) {
        this.accountPrivacy = accountPrivacy;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}
