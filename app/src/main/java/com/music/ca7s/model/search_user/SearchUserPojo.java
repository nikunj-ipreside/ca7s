
package com.music.ca7s.model.search_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.music.ca7s.model.BaseModel;

public class SearchUserPojo extends BaseModel {


    @SerializedName("list")
    @Expose
    private SearchUserList list;

    public SearchUserList getList() {
        return list;
    }

    public void setList(SearchUserList list) {
        this.list = list;
    }

}
