package com.music.ca7s.utils;

public interface AppConstants {

    public static final String BASE_WEB_URL = "https://www.ca7s.com/ca7s/";
    public static final String BASE_URL = "https://www.ca7s.com/ca7s/";
    public static final String CHROME_PACKAGE = "com.android.chrome";
    public static final String HOME_DATA_API = "home";
    public static final String DASHBOARD_DATA_API = "dashboard";
    public static final String REDEEM_DATA_API = "getRedeemkyc";
    public static final String LOGIN_DATA_API = "custlogin";
    public static final String FORGOT_DATA_API = "forgot_password";
    public static final String CHANGEPASSWORD_DATA_API = "password_update";
    public static final String REGISTER_DATA_API = "custsignup";
    public static final String CHECKRID_DATA_API = "check_rid";
    public static final String UPDATE_ADD_JUNCTION = "update_newadd_junction";
    public static final String COMPLETE_INACTVIE_TASK_DATA_API = "appchallenge";
    public static final String COMPLETE_CHALLENGE_TASK_DATA_API = "ActiveID";
    public static final String COMPLETE_APP_TASK_DATA_API = "complete_app_status";
    public static final String COMPLETE_INACTVIE_WEB_TASK_DATA_API = "webchallenge";
    public static final String ADD_BANK_DETAIL_DATA_API = "bankDetail";
    public static final String ADD_REDEEM_DETAIL_DATA_API = "sendRedeemRequest";
    public static final String SUPPORT_DETAIL_DATA_API = "support";
    public static final String COMPLETED_TODAY_TASK_DATA_API = "update_todaytask";
    public static final String COMPLETED_CPALEAD_TASK_DATA_API = "cpalead_app";
    public static final String COPLETED_APP_OFF_DAY = "update_app_of_day";
    public static final String COMPLETED_TODAY_TASK_DATA_API_SHIFT = "update_newtodaytask";
    public static final String SHOPPING_DATA_API = "shopping";
    public static final String APP_OFF_THE_DAY = "app_of_day";
    public static final String COUNTRY_DATA_API = "country_list";
    public static final String TODAY_TASK_DATA_API = "todaytasknew";
    public static final String CPALEAD_TASK_DATA_API = "campaign_json.php";
    public static final String NEWS_TASK_DATA_API = "newsreading";
    public static final String AD_JUNCTION_DATA_API = "add_junction";
    public static final String AD_JUNCTION_POINT_DATA_API = "adjunction_points";
    public static final String EARN_MORE_DATA_API = "earn_more";
    public static final String VIDEO_WALL_TASK_DATA_API = "video_wall";
    public static final String MY_NETWORK_DATA_API = "myNetwork";
    public static final String UPDATE_SCRATCH_DATA_API = "update_scratch";
    public static final String MY_NETWORK_TEAM_DATA_API = "myNetworkTeam";
    public static final String HOT_LIST_DATA_API = "HotList";
    public static final String SCRATCH_WIN_DATA_API = "scratch";
    public static final String TREEVIEW_DATA_API = "TreeView";
    public static final String MYTRANSACTION_DATA_API = "myTransaction";
    public static final String UPDATE_VIDEO_WALL_TASK_DATA_API = "update_video_wall";
    public static final String UPDATE_EARN_MORE_TASK_DATA_API = "update_earn_more";
    public static final String UPDATE_ADD_JUNCTION_TASK_DATA_API = "update_add_junction";
    public static final String UPDATE_NEWS_TASK_DATA_API = "update_news";
    public static final String GET_BANK_DETAIL_DATA_API = "getbankDetail";
    public static final String ADD_KYC_DETAIL_DATA_API = "UploadKyc";
    public static final String GET_KYC_DETAIL_DATA_API = "getKyc";
    public static final String GET_ADS_DATA_API = "getAds";
    public static final String STATE_DATA_API = "state_list";
    public static final String OTP_DATA_API = "send_otp";
    public static final String USER_ID = "user_id";

    String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z]).{6,12}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String birthDatePattern = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";

    String sTrue = "true";
    String sFalse = "false";
    String songRecordedUrl = "CA7SSearchTrack.mp3";


    String TOP ="top";
    String NEW = "new";
    String RISING = "rising";
    String FAVOURITE = "favourite";
    String TOP_GENRE ="top_genre";
    String NEW_GENRE = "new_genre";
    String RISING_GENRE = "rising_genere";
    String FAVOURITE_GENRE = "favourite_genre";


    //from
    String FROM_FAVOURITE_SONGS = "favourite_songs";
    String FROM_FAVOURITE_ALBUM = "favourite_album";
    String FROM_FAVOURITE_PLAYLIST = "favourite_playlist";
    String FROM_HOME_TOP = "home_top";
    String FROM_HOME_INTERNATIONAL = "home_international";
    String FROM_HOME_GENRE = "home_genre";
    String FROM_UPLOAD_MUSIC = "upload_music";
    String FROM_MY_MUSIC = "my_music";
    String FROM_SEARCH_RESULT = "search_result";

    String CREATE_PLAYLIST = "create_playlist";
    String UPDATE_PLAYLIST = "api/update_playlist";
    String USER_PLAYLIST = "user_playlist";
    String ADD_SONG_IN_PLAYLIST = "add_song_playlist";
    String REMOVE_SONG_FROM_PLAYLIST="remove_song_playlist";
    String REMOVE_PLAYLIST = "remove_playlist";
    String GET_SONG_FROM_PLAYLIST ="get_playlist_song";
    String DOWNLOADED_PLAYLIST ="downloaded_playlist";
    String ADD ="add";
    String EDIT ="edit";
    String REMOVE ="remove";
    String DELETE = "delete";
    String UPDATE = "update";
    String TOP_BROWSE ="top_browse";
    String NEW_RELEASE ="new_release";
    String RISING_STAR ="rising_star";
    String SHARED_MUSIC ="shared_music";
    String DOWNLOAD = "download_count";
    String STREAM = "stream_count";


    String DEVICE_PATH = "/storage/emulated/0/Android/data/com.music.ca7s/cache/CA7S.nomedia/";
    String TEMPRARY_MUSIC_EXTENTION =".mp3";
    String PERMANENT_MUSIC_EXTENTION = ".mp3";



    //Sqlite DataBase
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static final String TABLE_DOWNLOADED_SONG = "downloaded_song";
    public static final String TABLE_DOWNLOADED_PLAYLIST_SONG = "downloaded_playlist_song";
    public static final String TABLE_PLAYLIST ="table_playlist";
    public static final String TABLE_OFFLINE_SEARCH = "offline_search";
    public static final String PLAYLIST_ID = "playlist_id";
    public static final String PLAYLIST_NAME = "playlist_name";
    public static final String PLAYLIST_IMAGE = "playlist_image";
    public static final String PLAYLIST_VALUES = "playlist_values";



    //OrdersFields
    public static final String SONG_NUMBER = "song_number";
    public static final String SONG_IMAGE_PATH ="song_image_path";
    public static final String IS_INTERNATIONAL = "is_internation";
    public static final String SONG_ID = "songID";
    public static final String SONG_TITLE = "songTitle";
    public static final String SONG_ALBUM = "songAlbum";
    public static final String SONG_URL = "songUrl";
    public static final String SONG_IMAGE_URL = "song_imageUrl";
    public static final String IS_LIKE = "is_like";
    public static final String IS_FAVOURITE = "is_favourire";
    public static final String IS_DOWNLOAD = "is_download";
    public static final String TRACK_TYPE = "track_type";
    public static final String ALBUM_ID = "albumID";
    public static final String FROM = "fromscreen";
    public static final String SONG_PATH = "song_path";
    public static final String IS_PLAYLIST = "is_playlist";
    public static final String LIKE_COUNT = "like_count";
    public static final String CREATED_AT = "created_at";
    public static final String LYRICS = "lyrics";
    public static final String SONG_ARTIST = "song_artist";



    //Permissions and RequestCode
    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public static final int LOCATION = 2;
    public static final int FRAGMENT_CAMERA = 3;
    public static final int FRAGMENT_GALLERY = 4;
    public static final int FRAGMENT_LOCATION = 5;
    public static final int WRITE_CONTACTS = 6;
    public static final int SEND_SMS = 7;
    public static final int CALL_PHONE = 8;
    public static final int ID_CARD = 9;
    public static final int CALL_PHONE_WEBSITE = 10;
    public static final int CALL_PHONE_SUPPORT = 11;
    public static final int CALL_PHONE_HELP = 12;


}