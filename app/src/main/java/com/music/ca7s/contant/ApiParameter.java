package com.music.ca7s.contant;

public interface ApiParameter {
    String BASE_URL = "https://www.ca7s.com/ca7s/";
    String BASE_MUSIC_URL = "https://www.ca7s.com";
//    String BASE_URL = "http://3.130.108.91/ca7s/public/index.php/";
//    String BASE_MUSIC_URL = "http://3.130.108.91";
    String SUCCESS = "success";
    String FAILED = "failed";
    String INACTIVE = "inactive";
    String SET_COOKIE = "Set-Cookie";
    String COOKIE = "Cookie";
    String USER_AGENT = "TopUser-Agent";
    String CA7S_APP = "CA7S_APP";
    String CONTENT_TYPE = "Content-Type";
    String TYPE_JSON = "application/json";
    //Login
    String CHILD_URL_WS_LOGIN = "api/ws_login";
    String EMAIL = "email";
    String CURRENT_PASSWORD = "current_password";
    String NEW_PASSWORD = "new_password";
    String CONFIRM_PASSWORD = "confirm_password";
    String USER_PASSWORD = "user_password";
    //Sign up
    String CHILD_URL_WS_SIGNUP = "api/ws_signup";
    //forgot Password
//    String CHILD_URL_WS_RESET_PWD = "api/ws_reset_pwd";
    String CHILD_URL_WS_RESET_PWD = "api/ws_reset_password";
    //CheckUserEmail
    String CHILD_URL_WS_CHECK_EMAIL = "api/ws_check_email";
    // checl user name
    String CHILD_URL_WS_CHECK_USERNAME = "api/ws_check_username";
    String USER_NAME = "user_name";
    // edit profile OR add user name
    String CHILD_URL_WS_PROFILE_EDIT = "api/ws_profile_edit";
    String USER_ID = "user_id";
    String URL = "url";
    String NOTIFICATION_SETTING = "notification_setting";
    String LANGUAGE_SETTING = "language_setting";
    String PRIVATE_ACCOUNT = "private_account";
    String FULL_NAME = "full_name";
    String USER_CITY = "user_city";
    String USER_BIRTHDATE = "user_birthdate";
    String USER_GENDER = "user_gender";
    String USER_PHONE = "user_phone";
    //Slide Menu
    String CHILD_URL_WS_PROFILE_SIDEBAR = "api/ws_profile_sidebar";
    String USER_TOKEN = "user_token";
    //HomeFragment, TopList
    String CHILD_URL_WS_MUSIC_LISTING_ALBUMS = "api/ws_music_listing_albums";
    String PAGE = "page";
    //HomeFragment, Genre FollowersList Api
    String CHILD_URL_WS_GET_GENRES = "api/ws_get_available_genres";
    String TOP_GENRE_API = "api/ws_top_albums_mob";
    String NEW_RELEASE_GENRE_API = "api/ws_new_release_albums";
    String NEW_RELEASE_GENRE_BY_ID_API = "api/ws_new_release_by_albums";
    String RISING_STAR_API = "api/ws_rising_star_albums";
    String WS_GET_CITIES = "api/ws_get_cities";
    String WS_GET_GENRES = "api/ws_get_genres";// upload song time call
    //Get Top Tracks API
    String CHILD_URL_WS_MUSIC_LISTING_TRACKS = "api/ws_music_listing_tracks";
    String PLAYLIST_ID = "playlist_id";
    String TRACK_ID = "track_id";
    String UPLOAD_LIMIT = "api/ws_get_uploadlimit_mob";
    //Get Genre Track GenreTrackList
    String CHILD_URL_WS_FILEENTRY_GET_LIST = "api/ws_fileentry/get_list";
    //Get Profile
    String CHILD_URL_WS_PROFILE_GET = "api/ws_profile_get";
    String MALE = "Male";
    String Female = "Female";
    // Upload Profile pic
    String CHILD_URL_WS_PROFILE_CHANGE = "api/ws_profile_change";
    String PROFILE_PICTURE = "profile_picture";
    String IMAGE = "image";
    //View Profile
    String CHILD_URL_WS_PROFILE_VIEW = "api/ws_profile_view";
    String VIEW_ID = "view_id";
    //Followers FollowersList
    String CHILD_URL_WS_FOLLOWERS_LIST = "api/ws_followers_list";
    // Following list
    String CHILD_URL_WS_FOLLOWING_LIST = "api/ws_following_list";
    // Request SearchUserList
    String CHILD_URL_WS_REQUEST_LIST = "api/ws_request_list";
    //Accept request
    String CHILD_URL_WS_REQUEST_ACCEPT = "api/ws_request_accept";
    String FOLLOW_ID = "follow_id";
    //Reject request
    String CHILD_URL_WS_REQUEST_REJECT = "api/ws_request_reject";
    // remove friend from followers list
    String CHILD_URL_WS_FRIEND_REMOVE = "api/ws_friend_remove";
    //Follow
    String CHILD_URL_WS_FOLLOW = "api/ws_follow";
    //unfollow
    String CHILD_URL_WS_UNFOLLOW = "api/ws_unfollow";
    // search user
    String CHILD_URL_WS_SEARCH_USER = "api/ws_search_user";
    String SEARCH_TEXT = "search_text";
    String FOLLOW = "follow";
    //Social Login
    String CHILD_URL_WS_SOCIAL_LOGIN = "api/ws_social_login";
    String SOCIAL_ID = "social_id";
    String NAME = "name";
    String GENDER = "gender";
    // search track OR tap to scan
    String CHILD_URL_TRACK_IDENTIFY = "api/ws_fileentry/track_identify";
    String FILEFIELD = "filefield";
    //Notification
    String CHILD_URL_WS_GET_NOTIFICATION = "api/ws_get_notification"; // get list
    String CHILD_URL_WS_NOTIFICATION_CLEAR_ONES = "api/ws_notification_clear_ones";// delete single notification
    String MESSAGE_ID = "message_id";
    String CHILD_URL_WS_NOTIFICATION_CLEAR_ALL = "api/ws_notification_clear_all";// clear all notification
    //get Uploaded Music list
    String CHILD_URL_WS_FILEENTRY_SELF_GET_LIST = "api/ws_fileentry_self/get_list";
    //delete Uploaded Music list
    String CHILD_URL_WS_FILEENTRY_SELF_DELETE = "api/ws_fileentry/soft_delete";
    //AddSong
    String CHILD_URL_WS_FILEENTRY_ADD = "api/ws_fileentry/add";
    String SONG_TITLE = "song_title";
    String ALBUM_NAME = "album_name";
    String GENRE_ID = "genre_id";
    String YEAR = "year";
    String RELEASE_YEAR = "release_year";
    String ARTIST_NAME = "artist_name";
    String LYRICS = "lyrics";
    String PRIVACY = "privacy";
    String ADD_ARTWORK = "add_artwork";
    String MOBILE_ARTWORK = "mobile_artwork";
    //Share Song
    String TITLE = "title";
    String MUSIC_DESCRIPTION = "music_description";
    String MUSIC_THUMBNAIL = "music_thumbnail";
    String MUSIC_URL = "music_url";
    String TRACH_ID = "track_id";
    //Search Song History
    String CHILD_URL_WS_GET_HISTORY = "api/ws_get_history";
    //search song international
    String CHILD_URL_WS_SEARCH_INTERNATIONAL = "api/ws_search_international";
    String PER_PAGE = "per_page";
    //Favourite Album
    String CHILD_URL_WS_GET_FAVORITE_ALBUM = "api/ws_get_favorite_album";
    //Favourite Song
    String CHILD_URL_WS_GET_FAVORITE = "api/ws_get_favorite";
    // Favourite Playlist
    String CHILD_URL_WS_GET_PLAYLIST = "api/ws_get_playlist";
    //like track
    String CHILD_URL_WS_TRACK_LIKE = "api/ws_track_like";
    String ID = "id";
    //unlike track
    String CHILD_URL_WS_TRACK_UNLIKE = "api/ws_track_unlike";
    //unlike track
    String CHILD_URL_WS_TRACK_SHARE = "api/ws_make_share";
    //stream count
    String STREAM_COUNT_API = "api/play_song_mob";
    //download count
    String DOWNLOAD_COUNT_API = "api/download_song_mob";
    //favourite track
    String WS_TRACK_FAVORITE = "api/ws_track_favorite";
    String TRACK_TYPE = "track_type";
    String ALBUM_ID = "album_id";
    //remove favourite track
    String WS_TRACK_REMOVE_FAVORITE = "api/ws_track_remove_favorite";
    //add to playlist
    String WS_ADD_TO_PLAYLIST = "api/ws_add_to_playlist";
    //get ca7s album
    String WS_GET_TOP_CA7S_ALBUMS = "api/ws_get_top_ca7s_albums";

    String GET_TOP_BY_GENRE = "api/ws_get_top_ca7s";
    String GET_NEW_RELEASE = "api/new_release";
    String GET_NEW_RELEASE_BY_ALBUM = "api/ws_new_release_by_albums";
    String GET_RISING_STAR = "api/rising_star";
    String GET_RISING_STAR_BY_ID = "api/ws_rising_star_by_albums";
    //get trending main list
    String GET_TRENDING_ALL = "api/get_trending";
    //get trending main list
    String SET_TRENDING = "api/trending";
    String SEARCH_SONG = "api/song_search";
    String SEARCH_SONG_BYID = "api/ws_get_single_track_by_id";
    //get top track list
    String WS_GET_TOP = "api/ws_get_top";
    String TYPE = "type";
    String WS_GET_SETTING = "api/ws_get_setting";
    String WS_CHANGE_PASSWORD = "api/ws_change_password";
    String WS_CLEAR_HISTORY_ALL = "api/ws_clear_history_all";
    // Delete single History
    String WS_CLEAR_HISTORY_ONCE = "api/ws_clear_history_once";
    String HISTORY_ID = "history_id";
    //Play list Keys
    String CREATE_PLAYLIST_API = "api/ws_create_playlist";
    String UPDATE_PLAYLIST_API = "api/ws_update_playlist";
    String GET_USER_PLAYLIST = "api/ws_get_user_playlist";
    String ADD_SONG_IN_PLAYLIST_API = "api/ws_add_song_playlist";
    String REMOVE_SONG_FROM_PLAYLIST_API = "api/ws_remove_song_playlist";
    String REMOVE_PLAYLIST_API = "api/ws_remove_playlist";
    String GET_SONG_FROM_PLAYLIST = "api/ws_get_playlist_song";
    String GET_SHARED_MUSIC_API = "api/ws_share_music";


    String THIRD_PARTY = "thirdparty_song";
    String LANGUAGE = "language";
    String INDEX = "index";
    String LIST = "list";
    String SEEKTO = "seekto";
    String FROM_NOTIFICATION = "from_notification";
    String FROM_SEARCH = "Search_type";

}
