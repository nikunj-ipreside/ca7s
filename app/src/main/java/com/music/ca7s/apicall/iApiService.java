package com.music.ca7s.apicall;

import com.google.gson.JsonObject;
import com.music.ca7s.TrendingModel;
import com.music.ca7s.contant.ApiParameter;
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
import com.music.ca7s.model.search_song.international.InternationalPojo;
import com.music.ca7s.model.search_user.SearchUserPojo;
import com.music.ca7s.model.share.SharePojo;
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
import com.music.ca7s.utils.AppConstants;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface iApiService {

    //Login Api
    @POST(ApiParameter.CHILD_URL_WS_LOGIN)
    @FormUrlEncoded
    Call<LoginPojo> getLoginResponse(@FieldMap HashMap<String, String> params);

    //SignUp Api
    @POST(ApiParameter.CHILD_URL_WS_SIGNUP)
    @FormUrlEncoded
    Call<SignUpPojo> getSignUpResponse(@FieldMap HashMap<String, String> params);

    //Forgot password Api
    @POST(ApiParameter.CHILD_URL_WS_RESET_PWD)
    @FormUrlEncoded
    Call<ForgotPassPojo> getForgotPassResponse(@FieldMap HashMap<String, String> params);

    //check user email
    @POST(ApiParameter.CHILD_URL_WS_CHECK_EMAIL)
    @FormUrlEncoded
    Call<BaseModel> getCheckEmailResponse(@FieldMap HashMap<String, String> params);

    //check user name
    @POST(ApiParameter.CHILD_URL_WS_CHECK_USERNAME)
    @FormUrlEncoded
    Call<BaseModel> getCheckUserNameResponse(@FieldMap HashMap<String, String> params);

    //edit Profile OR Add TopUser name
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_PROFILE_EDIT)
    @FormUrlEncoded
    Call<EditProfilePojo> getEditProfileResponse(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //slide menu
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_PROFILE_SIDEBAR)
    @FormUrlEncoded
    Call<SlideMenuPojo> getSlideMenuResponse(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Top FollowersList
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_MUSIC_LISTING_ALBUMS)
    @FormUrlEncoded
    Call<TopListPojo> getTopListResponse(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Genre FollowersList
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @GET(ApiParameter.TOP_GENRE_API)
    Call<GenrePojo> getGenreListResponse(@Header(ApiParameter.COOKIE) String user_auth);

    //Genre FollowersList
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @GET(ApiParameter.NEW_RELEASE_GENRE_API)
    Call<GenrePojo> getNewReleaseListResponse(@Header(ApiParameter.COOKIE) String user_auth);

    //UploadLimit APi
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.UPLOAD_LIMIT)
    @FormUrlEncoded
    Call<JsonObject> getUploadLimit(@FieldMap HashMap<String, String> params,@Header(ApiParameter.COOKIE) String user_auth);

    //Genre List for upload song
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @GET(ApiParameter.WS_GET_GENRES)
    Call<GenrePojo> getGenreSongUpload(@Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @GET(ApiParameter.RISING_STAR_API)
    Call<GenrePojo> getRisingStarData(@Header(ApiParameter.COOKIE) String user_auth);

    //Get Profile list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_PROFILE_GET)
    @FormUrlEncoded
    Call<ProfilePojo> getProfileData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.NEW_RELEASE_GENRE_BY_ID_API)
    @FormUrlEncoded
    Call<GenrePojo> getNewReleaseGenreData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);



    //Upload profile pic
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_PROFILE_CHANGE)
    @FormUrlEncoded
    Call<ProfilePicPojo> getProfilePicData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //view profile(follow list)
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_PROFILE_VIEW)
    @FormUrlEncoded
    Call<ViewProfilePojo> getViewProfilePicData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Followers list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FOLLOWERS_LIST)
    @FormUrlEncoded
    Call<FollowersPojo> getFollowersList(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Following list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FOLLOWING_LIST)
    @FormUrlEncoded
    Call<FollowersPojo> getFollowingList(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Top Track GenreTrackList SocialLoginData(Click On Top GenreTrackList and get)
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_MUSIC_LISTING_TRACKS)
    @FormUrlEncoded
    Call<ListTopPojo> getTopTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Genre Track Api
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FILEENTRY_GET_LIST)
    @FormUrlEncoded
    Call<GetTopPojo> getGenreTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Genre Track Api
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_SONG_FROM_PLAYLIST)
    @FormUrlEncoded
    Call<GetTopPojo> getSongFromPlaylist(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Request SearchUserList
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_REQUEST_LIST)
    @FormUrlEncoded
    Call<RequestPojo> getRequestListData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Request Accept
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_REQUEST_ACCEPT)
    @FormUrlEncoded
    Call<BaseModel> getRequestAccept(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Request Reject
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_REQUEST_REJECT)
    @FormUrlEncoded
    Call<BaseModel> getRequestReject(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Remove friend
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FRIEND_REMOVE)
    @FormUrlEncoded
    Call<BaseModel> getRemoveFriendData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Follow
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FOLLOW)
    @FormUrlEncoded
    Call<BaseModel> getFollowData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Unfollow
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_UNFOLLOW)
    @FormUrlEncoded
    Call<BaseModel> getUnfollowData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Search user
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_SEARCH_USER)
    @FormUrlEncoded
    Call<SearchUserPojo> getSearchUserData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Social login api
    @POST(ApiParameter.CHILD_URL_WS_SOCIAL_LOGIN)
    @FormUrlEncoded
    Call<SocialLoginPojo> getSocialLoginData(@FieldMap HashMap<String, String> params);

    //Tap To scan API/Search track
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @Multipart
    @POST(ApiParameter.CHILD_URL_TRACK_IDENTIFY)
    Call<TapScanPojo> tapScanAudioData(@Part(ApiParameter.FILEFIELD + "\"; filename=\" " + AppConstants.songRecordedUrl + "\" ") RequestBody file
            , @Part(ApiParameter.USER_ID) RequestBody desc
            , @Header(ApiParameter.COOKIE) String user_auth);


    //Get Notification UploadedList
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_GET_NOTIFICATION)
    @FormUrlEncoded
    Call<NotificPojo> getNotificationData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Delete Single notification
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_NOTIFICATION_CLEAR_ONES)
    @FormUrlEncoded
    Call<BaseModel> getNotificationDeleteOne(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);


    //clear all notification
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_NOTIFICATION_CLEAR_ALL)
    @FormUrlEncoded
    Call<BaseModel> getNotificationDeleteAll(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get uploaded mucis list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FILEENTRY_SELF_GET_LIST)
    @FormUrlEncoded
    Call<UploadedPojo> getUploadedMusicData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get uploaded mucis list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_FILEENTRY_SELF_DELETE)
    @FormUrlEncoded
    Call<BaseModel> getUploadedSongDeleteOne(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);



    //Upload Music Api
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @Multipart
    @POST(ApiParameter.CHILD_URL_WS_FILEENTRY_ADD)
//    Call<UploadMusicPojo> upLoadMusicData(@Part(ApiParameter.FILEFIELD + "\"; filename=\" ") MultipartBody.Part songURL,
    Call<JsonObject> upLoadMusicData(@Part MultipartBody.Part songURL,
                                          @Part(ApiParameter.USER_ID) RequestBody userID,
                                          @Part(ApiParameter.SONG_TITLE) RequestBody songTitle,
                                          @Part(ApiParameter.ALBUM_NAME) RequestBody albumName,
                                          @Part(ApiParameter.GENRE_ID) RequestBody GenreID,
                                          @Part(ApiParameter.YEAR) RequestBody songYear,
                                          @Part(ApiParameter.ARTIST_NAME) RequestBody artistName,
                                          @Part(ApiParameter.LYRICS) RequestBody songLyrics,
                                          @Part(ApiParameter.PRIVACY) RequestBody songPrivacy,
                                          @Part(ApiParameter.ADD_ARTWORK) RequestBody songPic,
                                         @Part(ApiParameter.MOBILE_ARTWORK) RequestBody songPic2,
                                          @Part(ApiParameter.RELEASE_YEAR) RequestBody releaseYear,
                                          @Header(ApiParameter.COOKIE) String user_auth);


    //search history
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_GET_HISTORY)
    @FormUrlEncoded
    Call<SearchHistoryPojo> getSearchHistoryData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //search International
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_SEARCH_INTERNATIONAL)
    @FormUrlEncoded
    Call<HistoryNewModel> getSearchInternationalData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Favourite Album
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_GET_FAVORITE_ALBUM)
    @FormUrlEncoded
    Call<FavAlbumPojo> getFavouriteAlbumData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Favourite Song
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_GET_FAVORITE)
    @FormUrlEncoded
    Call<FavSongPojo> getFavouriteSongData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Favourite Playlist
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_GET_PLAYLIST)
    @FormUrlEncoded
    Call<FavPlaylistPojo> getFavouritePlayListData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //like track
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_TRACK_LIKE)
    @FormUrlEncoded
    Call<BaseModel> getLikeTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //unlike track
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_TRACK_UNLIKE)
    @FormUrlEncoded
    Call<BaseModel> getUnlikeTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //share track
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CHILD_URL_WS_TRACK_SHARE)
    @FormUrlEncoded
    Call<JsonObject> getShareTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //stream count api
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.STREAM_COUNT_API)
    @FormUrlEncoded
    Call<JsonObject> getStreamCountData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);


    //download count api
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.DOWNLOAD_COUNT_API)
    @FormUrlEncoded
    Call<JsonObject> getDownloadCountData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);


    //favourite track
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_TRACK_FAVORITE)
    @FormUrlEncoded
    Call<BaseModel> getAddFavouriteTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Remove favourite track
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_TRACK_REMOVE_FAVORITE)
    @FormUrlEncoded
    Call<BaseModel> getRemoveFavouriteTrackData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //add to playlist
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_ADD_TO_PLAYLIST)
    @FormUrlEncoded
    Call<BaseModel> getAddToPlaylistData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get ca7s album
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_GET_TOP_CA7S_ALBUMS)
    @FormUrlEncoded
    Call<TopCasPojo> getCasAlbumData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_GET_TOP)
    @FormUrlEncoded
    Call<GetTopPojo> getTopData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_TOP_BY_GENRE)
    @FormUrlEncoded
    Call<GetTopPojo> getTopByGenreData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_NEW_RELEASE)
    @FormUrlEncoded
    Call<GetTopPojo> getNewReleaseData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_NEW_RELEASE_BY_ALBUM)
    @FormUrlEncoded
    Call<GetTopPojo> getNewReleaseBYALBUMData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.CREATE_PLAYLIST_API)
    @Multipart
    Call<JsonObject> createPlayList(@Part MultipartBody.Part songURL,
                                    @Part(ApiParameter.USER_ID) RequestBody userID,
                                    @Part(ApiParameter.NAME) RequestBody songTitle, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.UPDATE_PLAYLIST_API)
    @Multipart
    Call<JsonObject> updatePlayList(@Part MultipartBody.Part songURL,
                                    @Part(ApiParameter.USER_ID) RequestBody userID,
                                    @Part(ApiParameter.NAME) RequestBody songTitle,@Part(ApiParameter.PLAYLIST_ID) RequestBody playlistId, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_USER_PLAYLIST)
    @FormUrlEncoded
    Call<JsonObject> getUserPlaylist(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.ADD_SONG_IN_PLAYLIST_API)
    @FormUrlEncoded
    Call<JsonObject> addSonginPlayList(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.REMOVE_SONG_FROM_PLAYLIST_API)
    @FormUrlEncoded
    Call<JsonObject> removeSongPlayList(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.REMOVE_PLAYLIST_API)
    @FormUrlEncoded
    Call<JsonObject> removePlayList(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_SONG_FROM_PLAYLIST)
    @FormUrlEncoded
    Call<JsonObject> getSongFromPlayList(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_SHARED_MUSIC_API)
    @FormUrlEncoded
    Call<JsonObject> getShareMusic(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //PlayList Api
    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_RISING_STAR)
    @FormUrlEncoded
    Call<GetTopPojo> getRisingData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);



    //get top track list
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.GET_RISING_STAR_BY_ID)
    @FormUrlEncoded
    Call<GetTopPojo> getRisingBYIDData(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //Get city
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @GET(ApiParameter.WS_GET_CITIES)
    Call<GetCityPojo> getCityResponse(@Header(ApiParameter.COOKIE) String user_auth);

    //Get city
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @GET(ApiParameter.GET_TRENDING_ALL)
    Call<TrendingModel> getAllTrending(@Header(ApiParameter.COOKIE) String user_auth);

    //trending
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.SET_TRENDING)
    @FormUrlEncoded
    Call<TrendingModel> setTrending(@FieldMap HashMap<String, String> param, @Header(ApiParameter.COOKIE) String user_auth);

    //search song
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.SEARCH_SONG)
    @FormUrlEncoded
    Call<SearchModel> searchSong(@FieldMap HashMap<String, String> param, @Header(ApiParameter.COOKIE) String cookie/*,@Header(ApiParameter.CONTENT_TYPE) String jsonType*/);

    //search song by id
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.SEARCH_SONG_BYID)
    @FormUrlEncoded
    Call<SearchedByID> searchSongBYID(@FieldMap HashMap<String, String> param, @Header(ApiParameter.COOKIE) String cookie);

    //search song by id
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.SEARCH_SONG)
    @FormUrlEncoded
    Call<SearchedByID> searchSongBYNAme(@FieldMap HashMap<String, String> param, @Header(ApiParameter.COOKIE) String cookie);


    //general settings
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_GET_SETTING)
    @FormUrlEncoded
    Call<GeneralSettingsPojo> postGeneralSettings(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //change password
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_CHANGE_PASSWORD)
    @FormUrlEncoded
    Call<BaseModel> changePassword(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    //clear all history
    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_CLEAR_HISTORY_ALL)
    @FormUrlEncoded
    Call<BaseModel> clearAllHistory(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

    @Headers({ApiParameter.USER_AGENT + ":" + ApiParameter.CA7S_APP})
    @POST(ApiParameter.WS_CLEAR_HISTORY_ONCE)
    @FormUrlEncoded
    Call<BaseModel> postDeleteSingleHistory(@FieldMap HashMap<String, String> params, @Header(ApiParameter.COOKIE) String user_auth);

}
