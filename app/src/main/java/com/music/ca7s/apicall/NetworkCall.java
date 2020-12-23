package com.music.ca7s.apicall;


import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.TrendingModel;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.model.history_new_model.HistoryNewModel;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.SearchModel;
import com.music.ca7s.model.edit_profile.EditProfilePojo;
import com.music.ca7s.model.favourite.album.FavAlbumPojo;
import com.music.ca7s.model.favourite.playlist.FavPlaylistPojo;
import com.music.ca7s.model.favourite.song.FavSongPojo;
import com.music.ca7s.model.followers.FollowersPojo;
import com.music.ca7s.model.forgotpass.ForgotPassPojo;
import com.music.ca7s.model.general_settings.GeneralSettingsPojo;
import com.music.ca7s.model.genre_list.GenrePojo;
import com.music.ca7s.model.genre_track.GenreTrackPojo;
import com.music.ca7s.model.getcity.GetCityPojo;
import com.music.ca7s.model.login.SocialLoginPojo;
import com.music.ca7s.model.newsearchedata.SearchedByID;
import com.music.ca7s.model.notification.NotificPojo;
import com.music.ca7s.model.request_list.RequestPojo;
import com.music.ca7s.model.search_song.SearchHistoryPojo;
import com.music.ca7s.model.search_song.TapScanPojo;
import com.music.ca7s.model.search_user.SearchUserPojo;
import com.music.ca7s.model.top_track.ListTopPojo;
import com.music.ca7s.model.login.LoginPojo;
import com.music.ca7s.model.profile.ProfilePicPojo;
import com.music.ca7s.model.profile.ProfilePojo;
import com.music.ca7s.model.profile.ViewProfilePojo;
import com.music.ca7s.model.signup.SignUpPojo;
import com.music.ca7s.model.slidemenu.SlideMenuPojo;
import com.music.ca7s.model.topcas.TopCasPojo;
import com.music.ca7s.model.toplist.TopListPojo;
import com.music.ca7s.model.toptracklist.GetTopPojo;
import com.music.ca7s.model.upload_music.UploadMusicPojo;
import com.music.ca7s.model.uploaded_music.UploadedPojo;
import com.music.ca7s.utils.ProgressRequestBody;
import com.music.ca7s.utils.RetroClient;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NetworkCall implements iApiMethodList {

    private static NetworkCall instance = new NetworkCall();
    private iApiService apiService;

    private NetworkCall() {
        apiService = RetroClient.getApiService();
    }

    public static NetworkCall getInstance() {
        return instance;
    }


    @Override
    public void callLoginApi(HashMap<String, String> param, final iResponseCallback<LoginPojo> callback) {
        Call<LoginPojo> call = apiService.getLoginResponse(param);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.LARAVEL_COOCKIE, response.headers().get(ApiParameter.SET_COOKIE));

                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSignUpApi(HashMap<String, String> param, final iResponseCallback<SignUpPojo> callback) {

        Call<SignUpPojo> call = apiService.getSignUpResponse(param);
        call.enqueue(new Callback<SignUpPojo>() {
            @Override
            public void onResponse(Call<SignUpPojo> call, Response<SignUpPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<SignUpPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });

    }

    @Override
    public void callForgotPasswordApi(HashMap<String, String> param, final iResponseCallback<ForgotPassPojo> callback) {

        Call<ForgotPassPojo> call = apiService.getForgotPassResponse(param);
        call.enqueue(new Callback<ForgotPassPojo>() {
            @Override
            public void onResponse(Call<ForgotPassPojo> call, Response<ForgotPassPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<ForgotPassPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callCheckEmailApi(HashMap<String, String> param, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getCheckEmailResponse(param);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callUserNameApi(HashMap<String, String> param, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getCheckUserNameResponse(param);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callEditProfileApi(HashMap<String, String> param, String user_auth, final iResponseCallback<EditProfilePojo> callback) {
        Call<EditProfilePojo> call = apiService.getEditProfileResponse(param, user_auth);
        call.enqueue(new Callback<EditProfilePojo>() {
            @Override
            public void onResponse(Call<EditProfilePojo> call, Response<EditProfilePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<EditProfilePojo> call, Throwable t) {
                t.printStackTrace();
//                DebugLog.e("Error edit profile :" + t.toString() + "" + t.getMessage());
                callback.onError(call, t);
            }
        });

    }

    @Override
    public void callSlideMenuApi(HashMap<String, String> param, String user_auth, final iResponseCallback<SlideMenuPojo> callback) {
        Call<SlideMenuPojo> call = apiService.getSlideMenuResponse(param, user_auth);
        call.enqueue(new Callback<SlideMenuPojo>() {
            @Override
            public void onResponse(Call<SlideMenuPojo> call, Response<SlideMenuPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<SlideMenuPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callTopListApi(HashMap<String, String> param, String user_auth, final iResponseCallback<TopListPojo> callback) {
        Call<TopListPojo> call = apiService.getTopListResponse(param, user_auth);
        call.enqueue(new Callback<TopListPojo>() {
            @Override
            public void onResponse(Call<TopListPojo> call, Response<TopListPojo> response) {
                BaseModel baseModel = response.body();

                Log.d("response", "" + response.body());

                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<TopListPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callGenreListApi(String user_auth, final iResponseCallback<GenrePojo> callback) {
        Call<GenrePojo> call = apiService.getGenreListResponse(user_auth);
        call.enqueue(new Callback<GenrePojo>() {
            @Override
            public void onResponse(Call<GenrePojo> call, Response<GenrePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GenrePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }


    @Override
    public void callnewReleaseGenreListApi(String user_auth, final iResponseCallback<GenrePojo> callback) {
        Call<GenrePojo> call = apiService.getNewReleaseListResponse(user_auth);
        call.enqueue(new Callback<GenrePojo>() {
            @Override
            public void onResponse(Call<GenrePojo> call, Response<GenrePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GenrePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetRisingStarApi(String user_auth, final iResponseCallback<GenrePojo> callback) {
        Call<GenrePojo> call = apiService.getRisingStarData(user_auth);
        call.enqueue(new Callback<GenrePojo>() {
            @Override
            public void onResponse(Call<GenrePojo> call, Response<GenrePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GenrePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetNewReleaseByGenreApi(HashMap<String, String> param, String user_auth, final iResponseCallback<GenrePojo> callback) {
        Call<GenrePojo> call = apiService.getNewReleaseGenreData(param, user_auth);
        call.enqueue(new Callback<GenrePojo>() {
            @Override
            public void onResponse(Call<GenrePojo> call, Response<GenrePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GenrePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callGenreListForUploadSongApi(String user_auth, final iResponseCallback<GenrePojo> callback) {
        Call<GenrePojo> call = apiService.getGenreSongUpload(user_auth);
        call.enqueue(new Callback<GenrePojo>() {
            @Override
            public void onResponse(Call<GenrePojo> call, Response<GenrePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GenrePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callCityListApi(String user_auth, final iResponseCallback<GetCityPojo> callback) {
        Call<GetCityPojo> call = apiService.getCityResponse(user_auth);
        call.enqueue(new Callback<GetCityPojo>() {
            @Override
            public void onResponse(Call<GetCityPojo> call, Response<GetCityPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GetCityPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetProfileApi(HashMap<String, String> param, String user_auth, final iResponseCallback<ProfilePojo> callback) {

        Call<ProfilePojo> call = apiService.getProfileData(param, user_auth);
        call.enqueue(new Callback<ProfilePojo>() {
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetProfilePicApi(HashMap<String, String> param, String user_auth,
                                     final iResponseCallback<ProfilePicPojo> callback) {

        Call<ProfilePicPojo> call = apiService.getProfilePicData(param, user_auth);
        call.enqueue(new Callback<ProfilePicPojo>() {
            @Override
            public void onResponse(Call<ProfilePicPojo> call, Response<ProfilePicPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<ProfilePicPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetViewProfilePicApi(HashMap<String, String> param, String user_auth,
                                         final iResponseCallback<ViewProfilePojo> callback) {

        Call<ViewProfilePojo> call = apiService.getViewProfilePicData(param, user_auth);
        call.enqueue(new Callback<ViewProfilePojo>() {
            @Override
            public void onResponse(Call<ViewProfilePojo> call, Response<ViewProfilePojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<ViewProfilePojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetFollowersListApi(HashMap<String, String> param, String user_auth,
                                        final iResponseCallback<FollowersPojo> callback) {

        Call<FollowersPojo> call = apiService.getFollowersList(param, user_auth);
        call.enqueue(new Callback<FollowersPojo>() {
            @Override
            public void onResponse(Call<FollowersPojo> call, Response<FollowersPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<FollowersPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetFollowingListApi(HashMap<String, String> param, String user_auth, final iResponseCallback<FollowersPojo> callback) {

        Call<FollowersPojo> call = apiService.getFollowingList(param, user_auth);
        call.enqueue(new Callback<FollowersPojo>() {
            @Override
            public void onResponse(Call<FollowersPojo> call, Response<FollowersPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<FollowersPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetTopTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<ListTopPojo> callback) {

        Call<ListTopPojo> call = apiService.getTopTrackData(param, user_auth);
        call.enqueue(new Callback<ListTopPojo>() {
            @Override
            public void onResponse(Call<ListTopPojo> call, Response<ListTopPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<ListTopPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetGenreTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<GenreTrackPojo> callback) {
//        Call<GenreTrackPojo> call = apiService.getGenreTrackData(param, user_auth);
//        call.enqueue(new Callback<GenreTrackPojo>() {
//            @Override
//            public void onResponse(Call<GenreTrackPojo> call, Response<GenreTrackPojo> response) {
//                BaseModel baseModel = response.body();
//                if (response.isSuccessful()) {
//                    callback.sucess(response.body());
//                } else {
//                    callback.onFailure(baseModel);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GenreTrackPojo> call, Throwable t) {
//                callback.onError(call, t);
//            }
//        });
    }

    @Override
    public void callgetRequestListApi(HashMap<String, String> param, String user_auth, final iResponseCallback<RequestPojo> callback) {

        Call<RequestPojo> call = apiService.getRequestListData(param, user_auth);
        call.enqueue(new Callback<RequestPojo>() {
            @Override
            public void onResponse(Call<RequestPojo> call, Response<RequestPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<RequestPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetRequestAcceptApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {

        Call<BaseModel> call = apiService.getRequestAccept(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callPlayListApi(String type, HashMap<String, String> param, String user_auth, final iResponseCallback<JsonObject> callback) {
        Call<JsonObject> call = null;
        if (type.toString().equals(AppConstants.CREATE_PLAYLIST)) {
            ProgressRequestBody fileBody = null;
            MultipartBody.Part filePart = null;
            String image = param.get(ApiParameter.IMAGE);
            if (image != null && !image.isEmpty()) {
                File file = new File(image);
                fileBody = new ProgressRequestBody(file, null);
                filePart = MultipartBody.Part.createFormData(ApiParameter.IMAGE, file.getName(), fileBody);
            }
//        RequestBody songURL1 = RequestBody.create(MediaType.parse(songUrl), file);
            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),
                    AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), param.get(ApiParameter.NAME));
            call = apiService.createPlayList(filePart, user_id, name, user_auth);
        } else if (type.toString().equals(AppConstants.UPDATE_PLAYLIST)) {
            ProgressRequestBody fileBody = null;
            MultipartBody.Part filePart = null;
            String image = param.get(ApiParameter.IMAGE);
            if (image != null && !image.isEmpty()) {
                File file = new File(image);
                fileBody = new ProgressRequestBody(file, null);
                filePart = MultipartBody.Part.createFormData(ApiParameter.IMAGE, file.getName(), fileBody);
            }
//        RequestBody songURL1 = RequestBody.create(MediaType.parse(songUrl), file);
            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),
                    AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), param.get(ApiParameter.NAME));
            RequestBody playlist_id = RequestBody.create(MediaType.parse("text/plain"), param.get(ApiParameter.PLAYLIST_ID));
            call = apiService.updatePlayList(filePart, user_id, name, playlist_id, user_auth);
        } else if (type.toString().equals(AppConstants.USER_PLAYLIST)) {
            call = apiService.getUserPlaylist(param, user_auth);
        } else if (type.toString().equals(AppConstants.ADD_SONG_IN_PLAYLIST)) {
            call = apiService.addSonginPlayList(param, user_auth);
        } else if (type.toString().equals(AppConstants.REMOVE_SONG_FROM_PLAYLIST)) {
            call = apiService.removeSongPlayList(param, user_auth);
        } else if (type.toString().equals(AppConstants.REMOVE_PLAYLIST)) {
            call = apiService.removePlayList(param, user_auth);
        } else if (type.toString().equals(AppConstants.GET_SONG_FROM_PLAYLIST)) {
            call = apiService.getSongFromPlayList(param, user_auth);
        } else if (type.toString().equals(AppConstants.SHARED_MUSIC)) {
            call = apiService.getShareMusic(param, user_auth);
        }
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                BaseModel baseModel = new BaseModel();
//                try {
                if (jsonObject.has("message")) {
                    baseModel.setMessage(jsonObject.get("message").toString());
                }
                if (jsonObject.has("status")) {
                    baseModel.setStatus(jsonObject.get("status").toString());
                }
                if (jsonObject.has("code")) {
                    baseModel.setCode(jsonObject.get("code").getAsInt());
                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetRequestRejectApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getRequestReject(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callgetRemoveFriendApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {

        Call<BaseModel> call = apiService.getRemoveFriendData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callFollowApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getFollowData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callUnfollowApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getUnfollowData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSearchUserApi(HashMap<String, String> param, String user_auth, final iResponseCallback<SearchUserPojo> callback) {

        Call<SearchUserPojo> call = apiService.getSearchUserData(param, user_auth);
        call.enqueue(new Callback<SearchUserPojo>() {
            @Override
            public void onResponse(Call<SearchUserPojo> call, Response<SearchUserPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<SearchUserPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSocialLoginApi(HashMap<String, String> param, final iResponseCallback<SocialLoginPojo> callback) {

        Call<SocialLoginPojo> call = apiService.getSocialLoginData(param);
        call.enqueue(new Callback<SocialLoginPojo>() {
            @Override
            public void onResponse(Call<SocialLoginPojo> call, Response<SocialLoginPojo> response) {
//                DebugLog.e("headers : " + response.headers());
//                DebugLog.e("headers Cookie : " + response.headers().get("Set-Cookie"));
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.LARAVEL_COOCKIE,
                        response.headers().get(ApiParameter.SET_COOKIE));


                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<SocialLoginPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }


    @Override
    public void callAllTrendingApi(String param, final iResponseNewCallback<TrendingModel> callback) {
        Call<TrendingModel> call = apiService.getAllTrending(param);
        call.enqueue(new Callback<TrendingModel>() {
            @Override
            public void onResponse(Call<TrendingModel> call, Response<TrendingModel> response) {
//                DebugLog.e("headers : " + response.headers());
//                DebugLog.e("headers Cookie : " + response.headers().get("Set-Cookie"));
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.LARAVEL_COOCKIE,
                        response.headers().get(ApiParameter.SET_COOKIE));
//                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<TrendingModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSetTrendingApi(HashMap<String, String> param, String cookie, final iResponseNewCallback<TrendingModel> callback) {
        Call<TrendingModel> call = apiService.setTrending(param, cookie);
        call.enqueue(new Callback<TrendingModel>() {
            @Override
            public void onResponse(Call<TrendingModel> call, Response<TrendingModel> response) {
//                DebugLog.e("headers : " + response.headers());
//                DebugLog.e("headers Cookie : " + response.headers().get("Set-Cookie"));
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.LARAVEL_COOCKIE,
                        response.headers().get(ApiParameter.SET_COOKIE));
//                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<TrendingModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSearchSongApi(HashMap<String, String> param, String cookie, final iResponseNewCallback<SearchModel> callback) {
        Call<SearchModel> call = apiService.searchSong(param, cookie/*,ApiParameter.TYPE_JSON*/);
        call.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
//                DebugLog.e("headers : " + response.headers());
//                DebugLog.e("headers Cookie : " + response.headers().get("Set-Cookie"));
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.LARAVEL_COOCKIE,
                        response.headers().get(ApiParameter.SET_COOKIE));
//                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSearchSongBYIDApi(HashMap<String, String> param, String cookie, final iResponseNewCallback<SearchedByID> callback) {
        Call<SearchedByID> call = null;
        if (param.get("id").toString().equalsIgnoreCase("0")) {
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("keyword", param.get("third_part_title"));
            call = apiService.searchSongBYNAme(paramsMap, cookie);
        } else {
            call = apiService.searchSongBYID(param, cookie);
        }
        call.enqueue(new Callback<SearchedByID>() {
            @Override
            public void onResponse(Call<SearchedByID> call, Response<SearchedByID> response) {
//                DebugLog.e("headers : " + response.headers());
//                DebugLog.e("headers Cookie : " + response.headers().get("Set-Cookie"));
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.LARAVEL_COOCKIE,
                        response.headers().get(ApiParameter.SET_COOKIE));
//                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchedByID> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callTapToScanApi(RequestBody songBody, RequestBody userID, String user_auth,
                                 final iResponseCallback<TapScanPojo> callback) {

        Call<TapScanPojo> call = apiService.tapScanAudioData(songBody, userID, user_auth);
        call.enqueue(new Callback<TapScanPojo>() {
            @Override
            public void onResponse(Call<TapScanPojo> call, Response<TapScanPojo> response) {
                BaseModel baseModel = response.body();
//                DebugLog.e("callTapToScanApi : "+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<TapScanPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callNotificationApi(HashMap<String, String> param, String user_auth, final iResponseCallback<NotificPojo> callback) {

        Call<NotificPojo> call = apiService.getNotificationData(param, user_auth);
        call.enqueue(new Callback<NotificPojo>() {
            @Override
            public void onResponse(Call<NotificPojo> call, Response<NotificPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<NotificPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callNotificationDeleteOneApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {

        Call<BaseModel> call = apiService.getNotificationDeleteOne(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callUploadedSongDeleteOneApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {

        Call<BaseModel> call = apiService.getUploadedSongDeleteOne(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }


    @Override
    public void callNotificationDeleteAllApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {

        Call<BaseModel> call = apiService.getNotificationDeleteAll(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callUploadedMusicApi(HashMap<String, String> param, String user_auth, final iResponseCallback<UploadedPojo> callback) {

        Call<UploadedPojo> call = apiService.getUploadedMusicData(param, user_auth);
        call.enqueue(new Callback<UploadedPojo>() {
            @Override
            public void onResponse(Call<UploadedPojo> call, Response<UploadedPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<UploadedPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }


    @Override
    public void callUploadMusicApi(MultipartBody.Part songUrl, RequestBody userID, RequestBody songTitle, RequestBody albumName,
                                   RequestBody gnereID, RequestBody songYear, RequestBody artistName, RequestBody lyrics,
                                   RequestBody privacy, RequestBody songPic,RequestBody songPic2,RequestBody edtYear, String user_auth, final iResponseCallback<JsonObject> callback) {


        Call<JsonObject> call = apiService.upLoadMusicData(songUrl, userID, songTitle, albumName, gnereID, songYear, artistName,
                lyrics, privacy, songPic,songPic2,edtYear,user_auth);
//        Log.e("CallUploadMusic: "," "+new Gson().toJson(call.request()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
//                    JsonParser parser = new JsonParser();
//                    JsonElement mJson = null;
//                    try {
//                        mJson = parser.parse(response.errorBody().string());
//                        Log.e("uploadMusic : "," "+mJson.toString());
//                    } catch (IOException ex) {
//ex.printStackTrace();
//                    }

                    BaseModel baseModel = new BaseModel();
                    baseModel.setCode(response.code());
                    baseModel.setStatus(String.valueOf(response.isSuccessful()));
                    baseModel.setMessage(response.message());

                    callback.onFailure(baseModel);

                }
//                Log.e("call : ", " " + new Gson().toJson(call.request()));
//                Log.e("uploadMusic : ", " " + response.toString());
//                Log.e("CallUploadMusic2: "," "+new Gson().toJson(call.request()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(call, t);
//                Log.e("CallUploadMusic3: "," "+new Gson().toJson(call.request()));
            }
        });
    }

    @Override
    public void callSearchHistoryApi(HashMap<String, String> param, String user_auth, final iResponseCallback<SearchHistoryPojo> callback) {

        Call<SearchHistoryPojo> call = apiService.getSearchHistoryData(param, user_auth);
        call.enqueue(new Callback<SearchHistoryPojo>() {
            @Override
            public void onResponse(Call<SearchHistoryPojo> call, Response<SearchHistoryPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<SearchHistoryPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callSearchInternationalApi(HashMap<String, String> param, String user_auth, final iResponseCallback<HistoryNewModel> callback) {

        Call<HistoryNewModel> call = apiService.getSearchInternationalData(param, user_auth);
        call.enqueue(new Callback<HistoryNewModel>() {
            @Override
            public void onResponse(Call<HistoryNewModel> call, Response<HistoryNewModel> response) {
//                BaseModel baseModel = response.body();

                Log.d("response", "" + response.body());

                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
//                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<HistoryNewModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callFavouriteAlbumApi(HashMap<String, String> param, String user_auth, final iResponseCallback<FavAlbumPojo> callback) {

        Call<FavAlbumPojo> call = apiService.getFavouriteAlbumData(param, user_auth);
        call.enqueue(new Callback<FavAlbumPojo>() {
            @Override
            public void onResponse(Call<FavAlbumPojo> call, Response<FavAlbumPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<FavAlbumPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callFavouriteSongApi(HashMap<String, String> param, String user_auth, final iResponseCallback<FavSongPojo> callback) {

        Call<FavSongPojo> call = apiService.getFavouriteSongData(param, user_auth);
        call.enqueue(new Callback<FavSongPojo>() {
            @Override
            public void onResponse(Call<FavSongPojo> call, Response<FavSongPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<FavSongPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callFavouritePlayListApi(HashMap<String, String> param, String user_auth, final iResponseCallback<FavPlaylistPojo> callback) {

        Call<FavPlaylistPojo> call = apiService.getFavouritePlayListData(param, user_auth);
        call.enqueue(new Callback<FavPlaylistPojo>() {
            @Override
            public void onResponse(Call<FavPlaylistPojo> call, Response<FavPlaylistPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<FavPlaylistPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });

    }

    @Override
    public void callLikeTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getLikeTrackData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callUnlikeTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getUnlikeTrackData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callShareTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<JsonObject> callback) {
        Call<JsonObject> call = apiService.getShareTrackData(param, user_auth);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    BaseModel baseModel = new BaseModel();
                    baseModel.setCode(jsonObject.get("code").getAsInt());
                    baseModel.setStatus(jsonObject.get("status").getAsString());
                    baseModel.setMessage(jsonObject.get("message").getAsString());
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callDownloadandStreamApi(String type, HashMap<String, String> param, String user_auth, iResponseCallback<JsonObject> callback) {
        Call<JsonObject> call = null;
        if (type.toString().equalsIgnoreCase(AppConstants.DOWNLOAD)) {
            call = apiService.getDownloadCountData(param, user_auth);
        } else {
            call = apiService.getStreamCountData(param, user_auth);
        }
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
//                    BaseModel baseModel = new BaseModel();
//                    baseModel.setCode(jsonObject.get("code").getAsInt());
//                    baseModel.setStatus(jsonObject.get("status").getAsString());
//                    baseModel.setMessage(jsonObject.get("message").getAsString());
//                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
//                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callAddFavouriteTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getAddFavouriteTrackData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callRemoveFavouriteTrackApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getRemoveFavouriteTrackData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callAddToPlaylistApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.getAddToPlaylistData(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callTopCasApi(HashMap<String, String> param, String user_auth, final iResponseCallback<TopCasPojo> callback) {
        Call<TopCasPojo> call = apiService.getCasAlbumData(param, user_auth);
        call.enqueue(new Callback<TopCasPojo>() {
            @Override
            public void onResponse(Call<TopCasPojo> call, Response<TopCasPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());

                    Log.d("response", "" + response.body());

                    Log.d("id", "" + response.body().getList());

                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<TopCasPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callGetTopApi(String type, int genreId, HashMap<String, String> param, String user_auth, final iResponseCallback<GetTopPojo> callback) {
        Call<GetTopPojo> call = null;
        if (type.toString().equals(AppConstants.TOP) && genreId == 0) {
            call = apiService.getTopData(param, user_auth);
        } else if (type.toString().equals(AppConstants.TOP_GENRE) && genreId == 1) {
            call = apiService.getTopByGenreData(param, user_auth);
        } else if (type.toString().equals(AppConstants.NEW) && genreId == 0) {
            call = apiService.getNewReleaseData(param, user_auth);
        } else if (type.toString().equals(AppConstants.NEW_GENRE) && genreId == 1) {
            call = apiService.getNewReleaseBYALBUMData(param, user_auth);
        } else if (type.toString().equals(AppConstants.RISING) && genreId == 0) {
            call = apiService.getRisingData(param, user_auth);
        } else if (type.toString().equals(AppConstants.RISING_GENRE) && genreId == 1) {
            call = apiService.getRisingBYIDData(param, user_auth);
        } else if (type.toString().equals(AppConstants.FAVOURITE)) {
            call = apiService.getGenreTrackData(param, user_auth);
        } else if (type.toString().equals(AppConstants.GET_SONG_FROM_PLAYLIST) && genreId == 1) {
            call = apiService.getSongFromPlaylist(param, user_auth);
        }

        call.enqueue(new Callback<GetTopPojo>() {
            @Override
            public void onResponse(Call<GetTopPojo> call, Response<GetTopPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GetTopPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callGeneralSettigsApi(HashMap<String, String> param, String user_auth, final iResponseCallback<GeneralSettingsPojo> callback) {
        Call<GeneralSettingsPojo> call = apiService.postGeneralSettings(param, user_auth);
        call.enqueue(new Callback<GeneralSettingsPojo>() {
            @Override
            public void onResponse(Call<GeneralSettingsPojo> call, Response<GeneralSettingsPojo> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<GeneralSettingsPojo> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callChangePasswordApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.changePassword(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callClearAllHistoryApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.clearAllHistory(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }

    @Override
    public void callDeleteSingleHistoryApi(HashMap<String, String> param, String user_auth, final iResponseCallback<BaseModel> callback) {
        Call<BaseModel> call = apiService.postDeleteSingleHistory(param, user_auth);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (response.isSuccessful()) {
                    callback.sucess(response.body());
                } else {
                    callback.onFailure(baseModel);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                callback.onError(call, t);
            }
        });
    }
}
