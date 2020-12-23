
package com.music.ca7s.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class NotificPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private NotificationList list;



    public NotificationList getList() {
        return list;
    }

    public void setList(NotificationList list) {
        this.list = list;
    }

}
