
package com.music.ca7s.model.general_settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralSettingsData {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("language_setting")
    @Expose
    private String languageSetting;
    @SerializedName("notification_setting")
    @Expose
    private Boolean notificationSetting;
    @SerializedName("private_account")
    @Expose
    private Boolean privateAccount;
    @SerializedName("verified")
    @Expose
    private Integer verified;
    @SerializedName("email")
    @Expose
    private String email;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLanguageSetting() {
        return languageSetting;
    }

    public void setLanguageSetting(String languageSetting) {
        this.languageSetting = languageSetting;
    }

    public Boolean getNotificationSetting() {
        return notificationSetting;
    }

    public void setNotificationSetting(Boolean notificationSetting) {
        this.notificationSetting = notificationSetting;
    }

    public Boolean getPrivateAccount() {
        return privateAccount;
    }

    public void setPrivateAccount(Boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
