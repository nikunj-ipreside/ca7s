package com.music.ca7s.apicall;


import com.music.ca7s.model.BaseModel;

import retrofit2.Call;


public interface iResponseCallback<T> {
    void sucess(T data);

    void onFailure(BaseModel baseModel);

    void onError(Call<T> responseCall, Throwable T);
}
