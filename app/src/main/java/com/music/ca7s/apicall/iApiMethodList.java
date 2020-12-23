package com.music.ca7s.apicall;

import com.google.gson.JsonObject;
import com.music.ca7s.TrendingModel;
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
import com.music.ca7s.model.history_new_model.HistoryNewModel;
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

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface iApiMethodList {

    void callLoginApi(HashMap<String, String> param, iResponseCallback<LoginPojo> callback);

    void callSignUpApi(HashMap<String, String> param, iResponseCallback<SignUpPojo> callback);

    void callForgotPasswordApi(HashMap<String, String> param, iResponseCallback<ForgotPassPojo> callback);

    void callCheckEmailApi(HashMap<String, String> param, iResponseCallback<BaseModel> callback);

    void callUserNameApi(HashMap<String, String> param, iResponseCallback<BaseModel> callback);

    void callEditProfileApi(HashMap<String, String> param, String user_auth, iResponseCallback<EditProfilePojo> callback);

    void callSlideMenuApi(HashMap<String, String> param, String user_auth, iResponseCallback<SlideMenuPojo> callback);

    void callTopListApi(HashMap<String, String> param, String user_auth, iResponseCallback<TopListPojo> callback);

    void callGenreListApi(String user_auth, iResponseCallback<GenrePojo> callback);

    void callnewReleaseGenreListApi(String user_auth, iResponseCallback<GenrePojo> callback);

    void callGenreListForUploadSongApi(String user_auth, iResponseCallback<GenrePojo> callback);

    void callgetNewReleaseByGenreApi(HashMap<String, String> param, String user_auth, iResponseCallback<GenrePojo> callback);

    void callgetRisingStarApi(String user_auth, iResponseCallback<GenrePojo> callback);

    void callgetProfileApi(HashMap<String, String> param, String user_auth, iResponseCallback<ProfilePojo> callback);

    void callPlayListApi(String type,HashMap<String, String> param, String user_auth, iResponseCallback<JsonObject> callback);

    void callDownloadandStreamApi(String type,HashMap<String, String> param, String user_auth, iResponseCallback<JsonObject> callback);

    void callgetProfilePicApi(HashMap<String, String> param, String user_auth, iResponseCallback<ProfilePicPojo> callback);

    void callgetViewProfilePicApi(HashMap<String, String> param, String user_auth, iResponseCallback<ViewProfilePojo> callback);

    void callgetFollowersListApi(HashMap<String, String> param, String user_auth, iResponseCallback<FollowersPojo> callback);

    void callgetFollowingListApi(HashMap<String, String> param, String user_auth, iResponseCallback<FollowersPojo> callback);

    void callgetTopTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<ListTopPojo> callback);

    void callgetGenreTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<GenreTrackPojo> callback);

    void callgetRequestListApi(HashMap<String, String> param, String user_auth, iResponseCallback<RequestPojo> callback);

    void callgetRequestAcceptApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callgetRequestRejectApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callgetRemoveFriendApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callFollowApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callUnfollowApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callSearchUserApi(HashMap<String, String> param, String user_auth, iResponseCallback<SearchUserPojo> callback);

    void callSocialLoginApi(HashMap<String, String> param, iResponseCallback<SocialLoginPojo> callback);

    void callAllTrendingApi(String param, iResponseNewCallback<TrendingModel> callback);

    void callSetTrendingApi(HashMap<String, String> param, String user_auth, iResponseNewCallback<TrendingModel> callback);

    void callSearchSongApi(HashMap<String, String> param, String user_auth, iResponseNewCallback<SearchModel> callback);

    void callSearchSongBYIDApi(HashMap<String, String> param, String user_auth, iResponseNewCallback<SearchedByID> callback);



    void callTapToScanApi(RequestBody songBody, RequestBody userID, String user_auth, iResponseCallback<TapScanPojo> callback);

    void callNotificationApi(HashMap<String, String> param, String user_auth, iResponseCallback<NotificPojo> callback);

    void callNotificationDeleteOneApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callUploadedSongDeleteOneApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callNotificationDeleteAllApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callUploadedMusicApi(HashMap<String, String> param, String user_auth, iResponseCallback<UploadedPojo> callback);

    void callUploadMusicApi(MultipartBody.Part songUrl,
                            RequestBody userID,
                            RequestBody songTitle,
                            RequestBody albumName,
                            RequestBody gnereID,
                            RequestBody songYear,
                            RequestBody artistName,
                            RequestBody lyrics,
                            RequestBody privacy,
                            RequestBody songPic,
                            RequestBody songPic2,
                            RequestBody releaseYear,
                            String user_auth,
                            iResponseCallback<JsonObject> callback);


    void callSearchHistoryApi(HashMap<String, String> param, String user_auth, iResponseCallback<SearchHistoryPojo> callback);

    void callSearchInternationalApi(HashMap<String, String> param, String user_auth, iResponseCallback<HistoryNewModel> callback);

    void callFavouriteAlbumApi(HashMap<String, String> param, String user_auth, iResponseCallback<FavAlbumPojo> callback);

    void callFavouriteSongApi(HashMap<String, String> param, String user_auth, iResponseCallback<FavSongPojo> callback);

    void callFavouritePlayListApi(HashMap<String, String> param, String user_auth, iResponseCallback<FavPlaylistPojo> callback);

    void callLikeTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callUnlikeTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callShareTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<JsonObject> callback);



    void callAddFavouriteTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callRemoveFavouriteTrackApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callAddToPlaylistApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callTopCasApi(HashMap<String, String> param, String user_auth, iResponseCallback<TopCasPojo> callback);

    void callGetTopApi(String type, int genreId, HashMap<String, String> param, String user_auth, iResponseCallback<GetTopPojo> callback);

    void callGeneralSettigsApi(HashMap<String, String> param, String user_auth, iResponseCallback<GeneralSettingsPojo> callback);

    void callChangePasswordApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callClearAllHistoryApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);

    void callCityListApi(String user_auth, iResponseCallback<GetCityPojo> callback);

    void callDeleteSingleHistoryApi(HashMap<String, String> param, String user_auth, iResponseCallback<BaseModel> callback);
}
