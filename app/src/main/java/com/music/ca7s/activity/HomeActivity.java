package com.music.ca7s.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.adapter.MyPagerAdapter;
import com.music.ca7s.adapter.homeadapters.DownloadPlayListAdapter;
import com.music.ca7s.adapter.homeadapters.NowPlayingSongsAdapter;
import com.music.ca7s.adapter.homeadapters.PlayListAdapter;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.contant.RecylerViewSongItemClickListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.dialog.UserNameRegisterFragmentDialog;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.fragment.NavigationDrawerFragment;
import com.music.ca7s.fragment.SearchFragment;
import com.music.ca7s.helper.OnStartDragListener;
import com.music.ca7s.helper.SimpleItemTouchHelperCallback;
import com.music.ca7s.listener.MyMusicCallbackListener;
import com.music.ca7s.listener.RecyclerItemListener;
import com.music.ca7s.listener.iDrawerHelper;
import com.music.ca7s.listener.iNavigationItemClick;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.mediaplayer.MediaPlayerService;
import com.music.ca7s.mediaplayer.MyDownloadManager;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.mediaplayer.StorageUtil;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.DownloadPlaylistModel;
import com.music.ca7s.model.NavigationDrawerModel;
import com.music.ca7s.model.SearchModel;
import com.music.ca7s.model.playlist.PlayListModel;
import com.music.ca7s.model.shared_song.SharedSongData;
import com.music.ca7s.model.shared_song.SharedSongModel;
import com.music.ca7s.model.slidemenu.SlideMenuDatum;
import com.music.ca7s.model.slidemenu.SlideMenuPojo;
import com.music.ca7s.receiver.ConnectivityReceiver;
import com.music.ca7s.slidinguppanel.SlidingUpPanelLayout;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.CircleProgressBar;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ImagePicker;
import com.music.ca7s.utils.LockableBottomSheetBehavior;
import com.music.ca7s.utils.Util;
import com.music.ca7s.utils.connectivity_check.ConnectionStateReceiver;
import com.music.ca7s.viewpagify.ViewPagify;
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.NotNull;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;

import static com.music.ca7s.AppLevelClass.getInstance;
import static com.music.ca7s.AppLevelClass.isDataSaverDialogShowing;
import static com.music.ca7s.fragment.BaseFragment.homeActivity;
import static com.music.ca7s.fragment.ViewPagerDownloadSongsFragment.mViewPagerDownloadFragment;
import static com.music.ca7s.mediaplayer.MediaPlayerService.ACTION_NEXT;
import static com.music.ca7s.mediaplayer.MediaPlayerService.ACTION_NEXT_COMPLETE;
import static com.music.ca7s.mediaplayer.MediaPlayerService.ACTION_PAUSE;
import static com.music.ca7s.mediaplayer.MediaPlayerService.ACTION_PLAY;
import static com.music.ca7s.mediaplayer.MediaPlayerService.ACTION_PREVIOUS;
import static com.music.ca7s.mediaplayer.MediaPlayerService.ACTION_STOP;
import static com.music.ca7s.utils.AppConstants.TABLE_DOWNLOADED_SONG;
import static com.music.ca7s.utils.Util.getSongTime;
import static com.music.ca7s.utils.Util.getTimeinInt;

public class HomeActivity extends AppNavigationActivity implements iNavigationItemClick, iDrawerHelper, ViewPagify
        .OnItemClickedListener, ViewPager.OnPageChangeListener, RecylerViewSongItemClickListener,
        DownloadSongListener, RecyclerItemListener, MyMusicCallbackListener,
        OnStartDragListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private Boolean isHidden = false;
    Notification status;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.music.ca7s.PlayNewAudio";
    public String language = "en";
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;
    private Boolean isVisible = false;
    private SlidingUpPanelLayout mLayout;
    LockableBottomSheetBehavior botto_sheet_music_behaviour;
    private DatabaseHandler databaseHandler;
    private Boolean fromNotification = false;

    private String imageURL = "";
    //Bottom RadioButtons
    public RadioButton rbHome, rbBroadCast, rbMyMusic, rbFavourite;
    //    private SwipeLayout swipe_layout;
    private FrameLayout fragment_container, fragment_nav_container;
    private DrawerLayout drawer_layout;
    private ImageView iv_playlistBanner, iv_download_notification;
    //Bottom Music Layout
    private TextView txtSongNameBottom, txtSongContentBottom, tv_song_name, tv_total_time, tv_play_time, tv_like_count, tv_song_album_name;
    private SeekBar seekbar_bottom, seekbar_main_player;
    private long mLastClickTime = 0;
    private RelativeLayout ll_mini_player, rl_bottom_bar, rl_bottom_menu, rl_download, rl_star;
    private ImageView iv_song_image_bottom, iv_play_song_bottom, iv_previous_song_bottom, iv_next_song_bottom, iv_leftview, /*iv_rightview,*/
            iv_share, iv_song_image,
            iv_like_main_player, iv_favourite_main_player, iv_song_download, iv_add_play_list,
            iv_play_song, iv_previous_song, iv_next_song, iv_song_shuffle, iv_song_repeat;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final float END_SCALE = 0.75f;
    private Boolean isInternetOn = false;
    private String sCookie;
    private ImageView iv_close_player, iv_up;
    public Context context;
    private LinearLayout bottom_sheet_music, ll_baner_visible, ll_main;
    private EditText et_lyrics;
    private ViewPager viewpager;
    private RecyclerView rv_songs;
    private CircleProgressBar download_progress_bar;
    private Boolean isPaused = true;
    private int curruntPosition;
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Song> upNextList = new ArrayList<Song>();
    private NowPlayingSongsAdapter mNowPlayingAdapter;
    private MyPagerAdapter myPagerAdapter;
    BroadcastReceiver NotifyAction;
    StorageUtil storage;
    public MediaPlayerService player;
    public static boolean isRegistered = false;
    public static boolean serviceBound = false;
    private View parentLayout;
    private Boolean isShuffle = false;
    private Boolean isRepeat = false;
    private ProgressBar play_progressbar_main, play_progressbar;
    public static Intent playerIntent;
    private ItemTouchHelper touchHelper;
    private String apiKey = "8355f17e8db00c7768301ab25a3f7488";
    private MusixMatch musixMatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColored(this);
        setContentView(R.layout.activity_home);
        musixMatch = new MusixMatch(apiKey);
        ButterKnife.bind(this);
        databaseHandler = new DatabaseHandler(HomeActivity.this);
        storage = new StorageUtil(HomeActivity.this);
        init();
        subscribeFirebaseTopic();
        context = getApplicationContext();
        AppLevelClass.context = context;
        initViewsWithlisteners();
        setNavigationDrawer();
        setBottomSheetBehaviour();
        setPanelLayoutBehaviour();
        setFragmentWithLanguage();
        setSeekBarChangeListener();
        checkNetworkType();
        handleIntent();
        openHomeFragment(FragmentState.REPLACE);

    }

    private void subscribeFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "msg subscribed";
                        if (!task.isSuccessful()) {
                            msg ="not subscribed";
                        }
                        Log.e("FIrebase ", msg);
                    }
                });
    }

    @Override
    public void setCurrentFragmentName(String fragmentTAG1) {
        super.setCurrentFragmentName(fragmentTAG1);
        fragmentTAG = fragmentTAG1;
        if (fragmentTAG1.equalsIgnoreCase(SearchModel.class.getSimpleName())) {
            showBottomMenu(false);
        } else {
            showBottomMenu(true);
        }
//        Log.e("Fragment_name: ",fragmentTAG1);
    }

    //start and bind the service when the activity starts

    @Override
    public void onStart() {
        super.onStart();
//        DebugLog.e("onStart");
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            register_notify();
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    public void register_notify() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(AppLevelClass.Broadcast_NOTIFY);
        registerReceiver(NotifyAction, filter);
        isRegistered = true;

        if (player != null) {
            curruntPosition = MediaPlayerService.audioIndex;
            setCurrenPosition(curruntPosition);
            songList = MediaPlayerService.audioList;
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED && mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                if (player.isPng()) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            } else {

            }
            setOriginalSongData();
        } else {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        handleIntent();
    }


    public void showDownloadNotification(Boolean isDownloaded) {
        if (isDownloaded) {
            iv_download_notification.setVisibility(View.VISIBLE);
        } else {
            iv_download_notification.setVisibility(View.GONE);
        }
    }

    /**
     * Change value in dp to pixels
     *
     * @param dp
     * @param context
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDownloadSuccess() {
        showDownloadNotification(true);
        if (mViewPagerDownloadFragment != null) {
            mViewPagerDownloadFragment.onRefreshAfterDownload();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    if (extras.containsKey(ApiParameter.FROM_NOTIFICATION)) {
                        fromNotification = true;
                        int position = extras.getInt(ApiParameter.INDEX);
                        int duration = extras.getInt(ApiParameter.SEEKTO);
                        String listData = extras.getString(ApiParameter.LIST);
                        curruntPosition = position;
                        Gson gson = new Gson();
                        ArrayList<Song> myLIst = gson.fromJson(listData, new TypeToken<ArrayList<Song>>() {
                        }.getType());
                        songList = myLIst;
                        if (songList.size() >= position) {
                            setSongData(songList.get(position));
                        }
                        if (myPagerAdapter != null) {
                            myPagerAdapter.updateList(songList);
                        }
                        onsetSongData(songList.get(curruntPosition),curruntPosition);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        player = MediaPlayerService.myMediaPlayerService;
                        if (player != null && player.isPng()){
                            iv_play_song.setImageResource(R.drawable.ic_pause);
                            iv_play_song_bottom.setImageResource(R.drawable.ic_pause);
                        }
                    } else {
                        fromNotification = false;
                        String action = intent.getAction();
                        if (action != null) {
                            Uri data = intent.getData();
                            if (data != null) {
                                try {
                                    getSharedMusic(data.toString());
                                    setIntent(null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    }
                } else {
                    fromNotification = false;
                }

            } else {
                fromNotification = false;
            }
        } catch (Exception e) {
            fromNotification = false;
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void checkNetworkType() {
        int typeEthernet = ConnectivityReceiver.getNetworkType(HomeActivity.this);
        if (typeEthernet == ConnectivityManager.TYPE_MOBILE) {
            showDataModeDialog(getString(R.string.data_saver_message));
        } else if (typeEthernet == 5) {
            showSnackBar(parentLayout, getString(R.string.sorry_no_internet));
        }
        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.INTERNET_MODE, String.valueOf(typeEthernet));
    }

    private void init() {
        playerIntent = new Intent(this, MediaPlayerService.class);
        startService(playerIntent);
//        Log.e("init90", " " + serviceBound);
        if (!serviceBound) {
            bindServiceCustom();
            MediaPlayerService.setCallBackListener(getReference());
//            Log.e("init92", " " + serviceBound);
        }

        NotifyAction = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_PLAY)) {
                    AppLevelClass.NotiFyAction = null;
                    if (player != null) {
                        player.playMedia();
                    }

//                    adapter1.fn_play();

                } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_STOP)) {
                    AppLevelClass.NotiFyAction = null;
                    if (player != null) {
                        player.removeNotification();
                        player.stopMedia();
                    }
//                    adapter1.fn_pause();

//                    Log.e("ACTION_PREVIOUS", "241");

                } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_NEXT)) {
                    AppLevelClass.NotiFyAction = null;
                    if (player != null) {
                        player.skipToNext();
                    }
//                    adapter1.fn_playNext();
//                    Log.e("ACTION_NEXT", "247");
                } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_PREVIOUS)) {
                    AppLevelClass.NotiFyAction = null;
                    if (player != null) {
                        player.skipToPrevious();
                    }
//                    adapter1.fn_playPrevious();
//                    Log.e("ACTION_PREVIOUS", "252");
                } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_PAUSE)) {
                    AppLevelClass.NotiFyAction = null;
                    if (player != null) {
                        player.pauseMedia();
                    }
//                    adapter1.fn_pause();
                } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_NEXT_COMPLETE)) {
//                    if(playAll){
                    AppLevelClass.NotiFyAction = null;

//                        adapter1.fn_playNext();
//                    }
//                    Log.e("ACTION_NEXT", "292");
                }
//                else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_DESTROY)) {
//                    AppLevelClass.NotiFyAction = null;
////                    player.stopMedia();
////                    adapter1.fn_pause();
//
////                    Log.e("ACTION_PREVIOUS", "241");
//
//                }


            }
        };
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void setSeekBarChangeListener() {
        SeekBar.OnSeekBarChangeListener seekbarChangelistener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                try {
                    if (b) {
                        player.setSongPosition(i);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekbar_main_player.setOnSeekBarChangeListener(seekbarChangelistener);
        seekbar_bottom.setOnSeekBarChangeListener(seekbarChangelistener);

    }


    private void initViewsWithlisteners() {
        //Music layout Casts
        rl_star = findViewById(R.id.rl_star);
        iv_download_notification = findViewById(R.id.iv_download_notification);
        play_progressbar_main = findViewById(R.id.play_progressbar_main);
        play_progressbar = findViewById(R.id.play_progressbar);
        ;
        ll_baner_visible = findViewById(R.id.ll_baner_visible);
        parentLayout = findViewById(android.R.id.content);
        iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);
        tv_song_album_name = findViewById(R.id.tv_song_album_name);
        iv_song_image = findViewById(R.id.iv_song_image);
        tv_song_name = findViewById(R.id.tv_song_name);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_play_time = findViewById(R.id.tv_play_time);
        iv_like_main_player = findViewById(R.id.iv_favourite_bottom_sheet);
        iv_like_main_player.setOnClickListener(this);
        iv_favourite_main_player = findViewById(R.id.iv_like_bottom_sheet);
        iv_favourite_main_player.setOnClickListener(this);
        iv_song_download = findViewById(R.id.iv_song_download);
        iv_add_play_list = findViewById(R.id.iv_add_play_list);
        iv_add_play_list.setOnClickListener(this);
        iv_play_song = findViewById(R.id.iv_play_song);
        iv_play_song.setOnClickListener(this);
        iv_previous_song = findViewById(R.id.iv_previous_song);
        iv_previous_song.setOnClickListener(this);
        iv_next_song = findViewById(R.id.iv_next_song);
        iv_next_song.setOnClickListener(this);
        iv_song_shuffle = findViewById(R.id.iv_song_shuffle);
        iv_song_shuffle.setOnClickListener(this);
        iv_song_repeat = findViewById(R.id.iv_song_repeat);
        iv_song_repeat.setOnClickListener(this);
        seekbar_main_player = findViewById(R.id.seekbar_main_player);
        tv_like_count = findViewById(R.id.tv_like_count);
        et_lyrics = findViewById(R.id.et_lyrics);
        viewpager = findViewById(R.id.viewpager);
        rv_songs = findViewById(R.id.rv_songs);
        rl_download = findViewById(R.id.rl_download);
        rl_download.setOnClickListener(this);
        download_progress_bar = findViewById(R.id.download_progress_bar);
        mLayout = findViewById(R.id.sliding_layout);
        bottom_sheet_music = findViewById(R.id.bottom_sheet_music);
        iv_up = findViewById(R.id.iv_up);
        iv_up.setOnClickListener(this);
        fragment_container = findViewById(R.id.fragment_container_main);
        fragment_nav_container = findViewById(R.id.fragment_nav_container);
        drawer_layout = findViewById(R.id.drawer_layout);
        iv_close_player = findViewById(R.id.iv_close_player);
//        swipe_layout = findViewById(R.id.swipe_layout);
        iv_leftview = findViewById(R.id.iv_leftview);
//        iv_rightview = findViewById(R.id.iv_rightview);
        rl_bottom_menu = findViewById(R.id.relativeRoot);
        ll_mini_player = findViewById(R.id.ll_mini_player);
        rl_bottom_bar = findViewById(R.id.RelBottomBar);
        iv_song_image_bottom = findViewById(R.id.iv_song_image_bottom);
        txtSongNameBottom = findViewById(R.id.txtSongNameBottom);
        txtSongContentBottom = findViewById(R.id.txtSongContentBottom);
        iv_play_song_bottom = findViewById(R.id.iv_play_song_bottom);
        iv_previous_song_bottom = findViewById(R.id.iv_previous_song_bottom);
        iv_next_song_bottom = findViewById(R.id.iv_next_song_bottom);
        seekbar_bottom = findViewById(R.id.seekbarSongBottom);
        ll_mini_player.setOnClickListener(this);
        iv_next_song_bottom.setOnClickListener(this);
        iv_previous_song_bottom.setOnClickListener(this);
        iv_play_song_bottom.setOnClickListener(this);
        rbHome = findViewById(R.id.rbHome);
        rbBroadCast = findViewById(R.id.rbBroadCast);
        rbMyMusic = findViewById(R.id.rbMyMusic);
        rbFavourite = findViewById(R.id.rbFavourite);
        rbHome.setChecked(true);
        rbHome.setOnClickListener(this);
        rbBroadCast.setOnClickListener(this);
        rbMyMusic.setOnClickListener(this);
        rbFavourite.setOnClickListener(this);
        iv_leftview.setOnClickListener(this);
//        iv_rightview.setOnClickListener(this);
//        swipe_layout.setOnClickListener(this);
        iv_close_player.setOnClickListener(this);
        ll_main = findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bottom_sheet_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rv_songs.setHasFixedSize(true);
        mNowPlayingAdapter = new NowPlayingSongsAdapter(upNextList, HomeActivity.this, this, this);
        rv_songs.setAdapter(mNowPlayingAdapter);
        myPagerAdapter = new MyPagerAdapter(HomeActivity.this, songList, this);
        viewpager.setPadding(110, viewpager.getTop(), 110,
                viewpager.getBottom());
        viewpager.setPageMargin(15);
//        viewpager.setPageTransformer(false, mTransformer);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(myPagerAdapter);
//        viewpager.setOnItemClickListener(this);
        viewpager.addOnPageChangeListener(this);
        setViewPagerPosition();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mNowPlayingAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv_songs);

    }

    /**
     * Converts pixels in dpi.
     *
     * @param pixels value to be converted
     * @return value in dpi
     */
    private int getDPI(int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    @Override
    public void onNoteListChanged(ArrayList<Song> customers) {
        //after drag and drop operation, the new list of Customers is passed in here
        upNextList = customers;
        updateUpnextList(upNextList);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }


    private void setFragmentWithLanguage() {
        Util.changeLanguage(getBaseContext());
        AppLevelClass.getInstance().getBus().register(this);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        if (ConnectionStateReceiver.isConnected(this)) {
            if (AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.IS_UPDATE).equalsIgnoreCase(AppConstants.sFalse)) {
                UserNameRegisterFragmentDialog dialog = UserNameRegisterFragmentDialog.newInstance(this, drawer_layout);
                dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
//                setNavigationDrawer();
                openHomeFragment(FragmentState.REPLACE);
            } else {

            }
        } else {
//            setNavigationDrawer();
            openHomeFragment(FragmentState.REPLACE);
        }
    }

    private void setPanelLayoutBehaviour() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("onPanelSlide", "onPanelSlide, offset " + slideOffset);
                if (myPagerAdapter != null) {
                    myPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("onPanelStateChanged", "onPanelStateChanged " + newState);
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    ll_mini_player.setVisibility(View.GONE);
                    rl_bottom_menu.setVisibility(View.GONE);
                    setOriginalSongData();
                    try {
                        if (myPagerAdapter != null) {
                            myPagerAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (AppLevelClass.getInstance().getTutorialPrefrences().getString(SharedPref.FOR_LYRICS).equalsIgnoreCase("1")) {

                    } else {
                        AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.FOR_LYRICS, "1");
                        Dialog dialog = new Dialog(HomeActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.tooltip_layout_center);
                        TextView tv_msg = dialog.findViewById(R.id.tooltip_text);
                        tv_msg.setText(R.string.click_on_artwork);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
                        wmlp.x = 30;   //x position
                        wmlp.y = 400;
                        dialog.show();
                    }

                    if (AppLevelClass.getInstance().getTutorialPrefrences().getString(SharedPref.FOR_DOWNLOAD).equalsIgnoreCase("1")) {

                    } else {
                        AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.FOR_DOWNLOAD, "1");
                        Dialog dialog1 = new Dialog(HomeActivity.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setCancelable(true);
                        dialog1.setContentView(R.layout.tooltip_layout_center_download);
                        TextView tv_msg1 = dialog1.findViewById(R.id.tooltip_text);
                        tv_msg1.setText(R.string.tap_to_download);
                        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        WindowManager.LayoutParams wmlp1 = dialog1.getWindow().getAttributes();
                        wmlp1.gravity = Gravity.TOP | Gravity.LEFT;
                        wmlp1.x = 20;   //x position
                        wmlp1.y = 480;
                        dialog1.show();
                    }
//                    swipe_layout.clearFocus();
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                    setOriginalSongData();
                    ll_mini_player.setVisibility(View.VISIBLE);
                    rl_bottom_menu.setVisibility(View.VISIBLE);
                    setMarginToLayout(true);
                    if (myPagerAdapter != null) {
                        myPagerAdapter.notifyDataSetChanged();
                    }
                } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
                    if (myPagerAdapter != null) {
                        myPagerAdapter.notifyDataSetChanged();
                    }
                } else if (newState == SlidingUpPanelLayout.PanelState.HIDDEN) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    rl_bottom_menu.setVisibility(View.VISIBLE);
                    setMarginToLayout(false);
                    if (myPagerAdapter != null) {
                        myPagerAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOriginalSongData();
                if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                if (myPagerAdapter != null) {
                    myPagerAdapter.notifyDataSetChanged();
                }
            }
        });
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void setOriginalSongData() {
        if (player != null && player.getSongData(HomeActivity.this) != null) {
//            Log.e("SetOriginal : ", new Gson().toJson(player.getSongData(HomeActivity.this)));
            setSongData(player.getSongData(HomeActivity.this));
        } else {
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED &&  mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }}
    }

    private void setBottomSheetBehaviour() {
        botto_sheet_music_behaviour = (LockableBottomSheetBehavior) LockableBottomSheetBehavior.from(bottom_sheet_music);
        botto_sheet_music_behaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        botto_sheet_music_behaviour.setHideable(false);//Important to add
        botto_sheet_music_behaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        mNowPlayingAdapter.refreshAdapter(upNextList);
                        iv_up.setImageResource(R.drawable.ic_down_theme);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        iv_up.setImageResource(R.drawable.ic_up_theme);
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    public void setFragment(Fragment fragment, FragmentState fragmentState) {
        fragmentChange(fragment, fragmentState);
    }

    private void showLyricsDialog(Song song) {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.lyrics_layout);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        TextView tv_songname, tv_title, tv_lyrics;
        ImageView iv_close;
        LinearLayout ll_main = dialog.findViewById(R.id.ll_main);
        LinearLayout ll_main1 = dialog.findViewById(R.id.ll_main1);
        tv_songname = dialog.findViewById(R.id.tv_songname);
        tv_title = dialog.findViewById(R.id.tv_title);
        tv_lyrics = dialog.findViewById(R.id.tv_lyrics);
        iv_close = dialog.findViewById(R.id.iv_close);
        tv_songname.setText(song.getSongTitle());
        tv_title.setText(song.getSongArtist());
        if (song.getLyrics() != null && !song.getLyrics().isEmpty()) {
            tv_lyrics.setText(song.getLyrics());
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMusicXLyrics(tv_lyrics,song.getSongTitle(),song.getSongArtist());
                }
            },2000);

        }

        tv_lyrics.setMovementMethod(new ScrollingMovementMethod());
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
    }

    private void showMusicXLyrics(TextView tv_lyrics, String songTitle, String songArtist) {
        // Track Search [ Fuzzy ]
        String trackName = "Don't stop the Party";
        String artistName = "The Black Eyed Peas";
        Track track = null;
        TrackData data=null;
        try {
                    track = musixMatch.getMatchingTrack(songTitle, songArtist);
//            track = musixMatch.getMatchingTrack(song.getSongTitle(), song.getSongArtist());
            data = track.getTrack();
        } catch (MusixMatchException e) {
            e.printStackTrace();
            tv_lyrics.setText(getString(R.string.lyrics_not_availabe));
        }
        catch (Exception e){
            e.printStackTrace();
            tv_lyrics.setText(getString(R.string.lyrics_not_availabe));
        }

        if (data != null) {
//            System.out.println("AlbumID : " + data.getAlbumId());
//            System.out.println("Album Name : " + data.getAlbumName());
//            System.out.println("Artist ID : " + data.getArtistId());
//            System.out.println("Album Name : " + data.getArtistName());
//            System.out.println("Track ID : " + data.getTrackId());
            int trackID = data.getTrackId();

            Lyrics lyrics = null;
            try {
                lyrics = musixMatch.getLyrics(trackID);
            } catch (Exception e) {
                e.printStackTrace();
                tv_lyrics.setText(getString(R.string.lyrics_not_availabe));
            }
            if (lyrics != null) {
//                System.out.println("Lyrics ID       : " + lyrics.getLyricsId());
//                System.out.println("Lyrics Language : " + lyrics.getLyricsLang());
//                System.out.println("Lyrics Body     : " + lyrics.getLyricsBody());
//                System.out.println("Script-Tracking-URL : " + lyrics.getScriptTrackingURL());
//                System.out.println("Pixel-Tracking-URL : " + lyrics.getPixelTrackingURL());
//                System.out.println("Lyrics Copyright : " + lyrics.getLyricsCopyright());

                if (lyrics.getLyricsBody() != null && !lyrics.getLyricsBody().isEmpty()){
                    tv_lyrics.setText(lyrics.getLyricsBody());
                }else {
                    tv_lyrics.setText(getString(R.string.lyrics_not_availabe));
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rbBroadCast:
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                openSearchFragment(FragmentState.REPLACE);
                break;
            case R.id.rbFavourite:
                if (isLogin()) {
                    if (player != null && player.isPng()) {
                        setOriginalSongData();
                        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    }
                    if (checkMultipleClick()) {
                        openFavouriteFragment(FragmentState.REPLACE);
                    }
                } else {
                    rbHome.setChecked(true);
                }
                break;
            case R.id.rbHome:
                if (player != null && player.isPng()) {
                    setOriginalSongData();
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
                if (checkMultipleClick()) {
                    openHomeFragment(FragmentState.REPLACE);
                }
                break;

            case R.id.rbMyMusic:
//                if (isLogin()) {
                if (player != null && player.isPng()) {
                    setOriginalSongData();
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
                showDownloadNotification(false);
                if (checkMultipleClick()) {
                    openMyMusicFragment(FragmentState.REPLACE);
                }
//                }
                break;


            case R.id.ll_mini_player:
                if (checkMultipleClick()) {
                    if (mLayout != null) {
                        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
//                        setOriginalSongData();
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                            if (myPagerAdapter != null) {
//                                myPagerAdapter.notifyDataSetChanged();
//                            }
                        } else {
//                        setOriginalSongData();
                            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        }
                    }
                }
                break;

            case R.id.iv_close_player:
                if (checkMultipleClick()) {
                    if (mLayout != null) {
//                    setOriginalSongData();
                        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    }
                }
                break;

            case R.id.iv_leftview:
//                Log.e("iv_leftview", "iv_leftview");
                isHidden = true;
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                try {
                    if (player != null && player.isPng()) {
                        player.stopMedia();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case R.id.iv_up:
                if (botto_sheet_music_behaviour.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    botto_sheet_music_behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    botto_sheet_music_behaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.iv_share:
                callShareTrackApi(getShareParam(), songList.get(curruntPosition), parentLayout);
                break;

            case R.id.iv_favourite_bottom_sheet:
                if (isLogin()) {
                    if (!songList.get(curruntPosition).getIsLike()) {
                        callLikeTrackApi(getLikeTrackParam(), parentLayout, iv_like_main_player, songList, curruntPosition, this);
                    } else {
                        callUnlikeTrackApi(getLikeTrackParam(), parentLayout, iv_like_main_player, songList, curruntPosition, this);
                    }
                }
                break;
            case R.id.iv_like_bottom_sheet:
                if (isLogin()) {
                    if (!songList.get(curruntPosition).getIsFavourite()) {
                        callAddFavouriteTrackApi(getAddFavouriteParam(), parentLayout, iv_favourite_main_player, songList, curruntPosition, this);
                    } else {
                        callRemoveFavouriteTrackApi(getAddFavouriteParam(), parentLayout, iv_favourite_main_player, songList, curruntPosition, this);
                    }

                }
                break;

            case R.id.rl_download:
                if (checkMultipleClick()) {
                    if (isReadStorageAllowed(STORAGE_PERMISSION_CODE)) {
                        File myFile = new File(AppConstants.DEVICE_PATH + songList.get(curruntPosition).getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                        if (myFile.exists()) {
                            Toast.makeText(HomeActivity.this, R.string.this_song_is_already_downloaded, Toast.LENGTH_LONG).show();
                        } else {
                            if (rl_download != null) {
                                if (isReadStorageAllowed(STORAGE_PERMISSION_CODE)) {
                                    Toast.makeText(HomeActivity.this, R.string.this_song_is_downloading, Toast.LENGTH_LONG).show();
                                    downloadSong();
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.iv_add_play_list:
                if (isLogin()) {
                    Song mySong = songList.get(curruntPosition);
                    if (mySong.getType().toString().equalsIgnoreCase(TABLE_DOWNLOADED_SONG)) {
                        openDialogOffline(iv_add_play_list, mySong);
                    } else {
                        openDialog(iv_add_play_list, mySong.getSongID());
                    }
                }

                break;

            case R.id.iv_play_song_bottom:
                if (checkMultipleClick()) {
                    play();
                }

                break;

            case R.id.iv_play_song:
                if (checkMultipleClick()) {
                    play();
                }

                break;

            case R.id.iv_previous_song_bottom:
                if (checkMultipleClick()) {
                    if (player != null) {
                        player.skipToPrevious();
                    }
                }
                break;

            case R.id.iv_previous_song:
                if (checkMultipleClick()) {
                    if (player != null) {
                        player.skipToPrevious();
                    }
                }
                break;

            case R.id.iv_next_song_bottom:
                if (checkMultipleClick()) {
                    if (player != null) {
                        player.skipToNext();
                    }
                }
                break;


            case R.id.iv_next_song:
                if (checkMultipleClick()) {
                    if (player != null) {
                        player.skipToNext();
                    }
                }
                break;
            case R.id.iv_song_shuffle:
                if (isShuffle) {
                    isShuffle = false;
                    iv_song_shuffle.setImageResource(R.drawable.ic_shuffle_false);
                } else {
//                    if (isRepeat) {
//                        isRepeat = false;
//                        iv_song_repeat.setImageResource(R.drawable.ic_repeat_false);
//                    }

                    isShuffle = true;
//                    if (isShuffle) {
//                        Collections.shuffle(songList);
//                        if (myPagerAdapter != null){
//                            myPagerAdapter.updateList(songList);
//                        }
//                        MediaPlayerService.setList(songList,-1,HomeActivity.this);
////                        storage.storeAudio(songList);
////                        setViewPagerPosition();
//                    }
                    iv_song_shuffle.setImageResource(R.drawable.ic_shuffle_true);

                }
                player.setRepeatOrShuffle(isRepeat, isShuffle);
                break;
            case R.id.iv_song_repeat:
                if (isRepeat) {
                    isRepeat = false;
                    iv_song_repeat.setImageResource(R.drawable.ic_repeat_false);
                } else {
//                    if (isShuffle) {
//                        isShuffle = false;
//                        iv_song_shuffle.setImageResource(R.drawable.ic_shuffle_false);
//                    }
                    isRepeat = true;
                    iv_song_repeat.setImageResource(R.drawable.ic_repeat_true);

                }
                player.setRepeatOrShuffle(isRepeat, isShuffle);
                break;
        }
    }

    private void stopMusic() {
        if (player.isPng() && isPaused) {
            player.stopMedia();
        }
    }


    @Override
    public void setCurrenPosition(int audioIndex) {
        curruntPosition = audioIndex;
        setViewPagerPosition();
    }

    private void setViewPagerPosition() {
        if (viewpager != null && viewpager.getAdapter() != null) {
            Objects.requireNonNull(viewpager.getAdapter()).notifyDataSetChanged();
        }
        assert viewpager != null;
        viewpager.setCurrentItem(curruntPosition, true);
    }

    private void play() {
        if (isPaused) {
            if (player != null && !player.isPng()) {
                isPaused = false;
                player.resumeMedia();
            }else {
                if (player != null) {
                    isPaused = true;
                    player.pauseMedia();
                }
            }
        } else {
            if (player != null && player.isPng()) {
                isPaused = true;
                player.pauseMedia();
            }else {
                if (player != null) {
                    isPaused = false;
                    player.resumeMedia();
                }
            }
        }
    }

    private void setMarginToLayout(boolean margin) {
        if (ll_baner_visible != null) {
            if (margin) {
//                if (fragmentTAG.toString().equals(DiscoverFragment.class.getSimpleName()))
                ll_baner_visible.setVisibility(View.VISIBLE);
            } else {
                ll_baner_visible.setVisibility(View.GONE);
            }
        }
    }

    public void callSlideMenuApi() {
//        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.USER_TOKEN, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.FCMTOKEN));
        NetworkCall.getInstance().callSlideMenuApi(params, sCookie, new iResponseCallback<SlideMenuPojo>() {
            @Override
            public void sucess(SlideMenuPojo data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<SlideMenuDatum> slideMenuData = data.getData();
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.FULL_NAME, slideMenuData.get(0).getFullName().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_NAME, slideMenuData.get(0).getUserName().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_CITY, slideMenuData.get(0).getUserCity().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.PROFILE_PICTURE, slideMenuData.get(0).getProfilePicture().toString());
                    AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.LANGUAGE, data.getLanguage());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.BaseCount, data.getBaseCount());
                    if (!AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.IS_FirstTime).equals("true")) {
                        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.IS_FirstTime, "true");
                    }
//                    setNavigationDrawer();
//                    openHomeFragment(FragmentState.REPLACE);
                } else {
                }
//                setNavigationDrawer();
                NavigationDrawerFragment.getnewInstance().uploadImageProfile(HomeActivity.this);
//                openHomeFragment(FragmentState.REPLACE);
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(drawer_layout, baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<SlideMenuPojo> responseCall, Throwable T) {
                hideProgressDialog();
                showSnackBar(drawer_layout, getString(R.string.api_error_message));
            }
        });
    }

    private void changeLanguage() {
        Util.changeLanguage(getBaseContext());
        if (rbMyMusic != null && rbFavourite != null && rbBroadCast != null && rbHome != null) {
            rbHome.setText(getString(R.string.discover));
            rbBroadCast.setText(getString(R.string.recognize));
            rbMyMusic.setText(getString(R.string.downloads));
            rbFavourite.setText(getString(R.string.favourites));


        }
    }

    public void setNavigationDrawer() {
        final NavigationDrawerFragment fragment = NavigationDrawerFragment.newInstance(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_nav_container, fragment);
        transaction.commitAllowingStateLoss();
        drawer_layout.setScrimColor(Color.TRANSPARENT);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.drawable.ic_menu, R.string.app_name, R.string.app_name) {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                NavigationDrawerFragment.getnewInstance().uploadImageProfile(HomeActivity.this);
                closeKeyboard(drawer_layout);
            }
        };

        drawer_layout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        closeKeyboard(drawer_layout);
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            setOriginalSongData();
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        } else {
            int i = getSupportFragmentManager().getBackStackEntryCount();
            if (i > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
//                  setDefaultKeyMode(KeyEvent.KEYCODE_HOME);
//                onUserLeaveHint();
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        Log.e("HOME ","PRESSED");
        super.onUserLeaveHint();
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public void closeSideMenu() {
        super.closeSideMenu();
        closeDrawer();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void openDrawer() {
//        setNavigationDrawer();
//        NavigationDrawerFragment.getnewInstance().uploadImageProfile(HomeActivity.this);
        drawer_layout.openDrawer(Gravity.START);


    }

    @SuppressLint("WrongConstant")
    @Override
    public void closeDrawer() {
        drawer_layout.closeDrawer(Gravity.START);

    }

    @Override
    public void lockDrawer() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    @Override
    public void unlockDrawer() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    @Override
    public void onNavigationItemSelect(NavigationDrawerModel drawerModel, final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeDrawer();

            }
        }, 100);
        switch (drawerModel.getNavigationItem()) {
            case HOME:
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                openHomeFragment(FragmentState.REPLACE);
                break;
            case NOTIFICATION:
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                openNotificationFragment(true, FragmentState.REPLACE);
                break;
            case SETTINGS:
                openSettingsFragment(FragmentState.REPLACE);
                break;
            case ABOUT:
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                if (Util.changeLanguage(HomeActivity.this).equalsIgnoreCase("English")) {
                    openWebviewFragment(getString(R.string.about), "https://www.ca7s.com/ca7s/aboutus?lang=eng", FragmentState.REPLACE);

                } else {
                    openWebviewFragment(getString(R.string.about), "https://www.ca7s.com/ca7s/aboutus?lang=port", FragmentState.REPLACE);

                }
                break;
            case HELP:
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                if (Util.changeLanguage(HomeActivity.this).equalsIgnoreCase("English")) {
                    openWebviewFragment(getString(R.string.help), "https://www.ca7s.com/ca7s/help?lang=eng", FragmentState.REPLACE);

                } else {
                    openWebviewFragment(getString(R.string.help), "https://www.ca7s.com/ca7s/help?lang=port", FragmentState.REPLACE);

                }

                break;
            case T_AND_C:
                view.setClickable(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);

                    }
                }, 500);
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                if (Util.changeLanguage(HomeActivity.this).equalsIgnoreCase("English")) {
                    openWebviewFragment(getString(R.string.t_C), "https://www.ca7s.com/ca7s/terms_condition?lang=eng", FragmentState.REPLACE);

                } else {
                    openWebviewFragment(getString(R.string.t_C), "https://www.ca7s.com/ca7s/terms_condition?lang=port", FragmentState.REPLACE);

                }
                break;
            case RATE_US:
                showRateDialog(this);
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                break;

            case SHARE:
//                showSnackBar(drawer_layout, getString(R.string.under_development));
                String type_pt = "&hl=pt", type_us = "&hl=en", appLink = "http://play.google.com/store/apps/details?id=" + getPackageName();
                String selectedLAnguage = Util.changeLanguage(HomeActivity.this);
                String language = Locale.getDefault().getLanguage();
                if (selectedLAnguage.toString().equalsIgnoreCase("Portuguese")) {
                    language = "pt";
                } else if (selectedLAnguage.toString().equalsIgnoreCase("English")) {
                    language = "en";
                }
                if (language.toString().equalsIgnoreCase("pt")) {
                    appLink = appLink + type_pt;
                } else {
                    appLink = appLink + type_us;
                }
                ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText(appLink)
                        .startChooser();

                break;
            case LOGOUT:
                if (isLogin()) {
                    new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                            .setTitle(R.string.alert)
                            .setMessage(R.string.are_you_sure_you_want_to_logout)
                            .setNegativeButton(R.string.no_, null)
                            .setPositiveButton(R.string.yes_, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    logOut();
                                }
                            }).create().show();
                }
                break;
        }
    }

    public void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.rate_us))
                .setMessage(context.getString(R.string.please_rate_us_description))
                .setPositiveButton(context.getString(R.string.rate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String type_pt = "&hl=pt", type_us = "&hl=en", appLink = "https://play.google.com/store/apps/details?id=" + context.getPackageName();
//                        String link = "market://details?id=";
                        String selectedLAnguage = Util.changeLanguage(HomeActivity.this);
                        String language = Locale.getDefault().getLanguage();
                        if (selectedLAnguage.toString().equalsIgnoreCase("Portuguese")) {
                            language = "pt";
                        } else if (selectedLAnguage.toString().equalsIgnoreCase("English")) {
                            language = "en";
                        }
                        if (language.toString().equalsIgnoreCase("pt")) {
                            appLink = appLink + type_pt;
                        } else {
                            appLink = appLink + type_us;
                        }
                        if (context != null) {
                            // starts external action
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(appLink)));
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public void logOut() {
        AppLevelClass.getInstance().getPreferanceHelper().clearPreference();
        openAuthenticationActivity();
    }

    //To play audio
    public void playAudio(int audioIndex) {
//        Log.e("audioIndex 169", audioIndex + " " + serviceBound);
        //Check is service is active
        if (!isMyServiceRunning(MediaPlayerService.class)) {
            //Store Serializable audioList to SharedPreferences
//            Log.e("172", songList.size() + "");
            storage.storeAudio(songList);
            storage.storeAudioIndex(audioIndex);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
//            storage.storeAudio(songList);
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(AppLevelClass.Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    @Subscribe
    public void isNetworkAvailableOrNot(Boolean isConnected) {
        if (isConnected) {
            if (dialog != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });
            }
            isInternetOn = false;

        } else {
            if (!isInternetOn) {
                isInternetOn = true;
                showProgressDialog(getString(R.string.network_error_message));
            }
        }
    }

    public void playSongWithPostion(int position, ArrayList<Song> updatedList,String type) {
//        Log.e("Songwitpos : ", position + " : " + new Gson().toJson(updatedList));
        isHidden = false;
        MediaPlayerService.setList(updatedList, position, this,type);
        curruntPosition = position;
        songList = updatedList;
        setSongData(updatedList.get(position));
        myPagerAdapter.updateList(songList);
        setViewPagerPosition();
        onListUpdated(songList);
        if (seekbar_bottom != null && seekbar_main_player != null) {
            seekbar_main_player.setProgress(0);
            seekbar_bottom.setProgress(0);
        }

        if (tv_total_time != null && tv_play_time != null) {
            tv_total_time.setText("0:00");
            tv_play_time.setText("0:00");
        }
        //song commented
        playAudio(position);
//        if (songList != null && !songList.isEmpty() && myPagerAdapter != null && mNowPlayingAdapter != null) {
//
//        }

    }

    private MyMusicCallbackListener getReference() {
        return this;
    }


    @Override
    protected void onDestroy() {
        removeNotification(HomeActivity.this,MediaPlayerService.NOTIFICATION_ID);
        destroyMusicService();
        super.onDestroy();
        AppLevelClass.getInstance().getBus().unregister(this);

    }

    public void destroyMusicService() {
        try {

            if (NotifyAction != null) {
                unregisterReceiver(NotifyAction);
                isRegistered = false;
            }
            if (mLayout != null) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
            if (serviceBound) {
                unbindService(serviceConnection);
                serviceBound = false;
            }

            AppLevelClass.IsBack = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeNotification(Context context, int notificationId) {
        NotificationManager nMgr = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(notificationId);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
        callSlideMenuApi();
        // register connection status listener
        AppLevelClass.getInstance().setConnectivityListener(this);
        if (!serviceBound) {
            bindServiceCustom();
        }
        register_notify();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }



    private boolean checkIsAvailable() {

        boolean isAvailable = false;
        if (player != null && MediaPlayerService.mediaPlayer != null) {
            if (MediaPlayerService.mediaPlayer.isPlaying() && !MediaPlayerService.mediaPlayer.isPlaying()) {
                isAvailable = true;
            }
        }
        Log.e("player", "" + isAvailable);
        return isAvailable;
    }

    public void bindServiceCustom() {
//        if(isMyServiceRunning(MediaPlayerService.class)){
//        Log.e("onStart334", "Okay" + playerIntent);
        bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        serviceBound = true;
//        Log.e("onStart334", "Okay" + serviceBound);
//        }

    }


    private boolean checkMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }


    @Override
    public void onsetSongData(Song song, int audioIndex) {
        try {
            curruntPosition = audioIndex;
            upNextList = new ArrayList<>();
            upNextList.clear();
            ArrayList<Song> newList = new ArrayList<>();
            newList.clear();
            if (!songList.isEmpty() && audioIndex < songList.size() - 1) {
                upNextList = new ArrayList<>();
                upNextList.clear();
                for (int i = audioIndex; i < songList.size(); i++) {
                    if (i == audioIndex) {
                        newList.add(songList.get(i));
                    } else {
                        upNextList.add(songList.get(i));
                    }
                }

            }

            setViewPagerPosition();
            setSongData(songList.get(audioIndex));
            mNowPlayingAdapter.refreshAdapter(upNextList);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        songList = newList;
//        playSongWithPostion(0,songList);
    }

    private void setSongData(final Song song) {
        if (song != null) {
            if (txtSongNameBottom != null) {
                txtSongNameBottom.setText(song.getSongTitle());
                tv_song_name.setText(song.getSongTitle());
            }
            if (txtSongContentBottom != null) {
                txtSongContentBottom.setText(song.getSongArtist());
                tv_song_album_name.setText(song.getSongArtist());
            }

            if (tv_like_count != null) {
                if (song.getLikeCount() != 0) {
                    if (!song.getThirdparty_song()) {
                        tv_like_count.setText("" + song.getLikeCount());
                    } else {
                        tv_like_count.setText("");
                    }
                }
            }

            if (song.getType().toString().equals(AppConstants.TABLE_DOWNLOADED_SONG)) {
                if (iv_favourite_main_player != null && iv_like_main_player != null) {
                    iv_favourite_main_player.setVisibility(View.INVISIBLE);
                    iv_like_main_player.setVisibility(View.INVISIBLE);
                    rl_star.setVisibility(View.INVISIBLE);
                }
            } else {
                if (iv_favourite_main_player != null && iv_like_main_player != null) {
                    iv_favourite_main_player.setVisibility(View.VISIBLE);
                    iv_like_main_player.setVisibility(View.VISIBLE);
                    rl_star.setVisibility(View.VISIBLE);
                    if (iv_favourite_main_player != null) {

                        if (song.getIsFavourite()) {
                            iv_favourite_main_player.setImageResource(R.drawable.heart_theme_filled);
                        } else {
                            iv_favourite_main_player.setImageResource(R.drawable.heart_theme_unfilled);
                        }
                    }

                    if (iv_like_main_player != null) {
                        if (song.getIsLike()) {
                            iv_like_main_player.setImageResource(R.drawable.favourite_theme_filled);
                        } else {
                            iv_like_main_player.setImageResource(R.drawable.favorite_theme_unfilled);
                        }
                    }
                }
            }


            if (song.getFrom().toString().equals(AppConstants.FROM_MY_MUSIC) || song.getThirdparty_song()) {

                String playlistImage = song.getSongImagePath();
//                String playlistImage = profileData.get(0).getProfilePicture();
                if (playlistImage.contains("/index.php")) {
                    playlistImage = playlistImage.replace("/index.php", "");
                }
//                if (playlistImage != null && !playlistImage.isEmpty()) {
                if (iv_song_image_bottom != null && iv_song_image != null) {
                    if (playlistImage != null && !playlistImage.isEmpty()) {
                        Glide.with(this)
                                .load(playlistImage)
                                .placeholder(R.drawable.ic_top_placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                                .into(iv_song_image_bottom);
                        Glide.with(this)
                                .load(playlistImage)
                                .placeholder(R.drawable.ic_top_placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                                .into(iv_song_image);
                    }
                }
            } else {
                if (iv_song_image_bottom != null) {
                    if (HomeActivity.isDataSaverEnabled()) {

                    } else {
                        String playlistImage = song.getSongImageUrl();
                        if (playlistImage.contains("/index.php")) {
                            playlistImage = playlistImage.replace("/index.php", "");
                        }

                        if (playlistImage != null && !playlistImage.isEmpty()) {
                            if (!isFinishing()) {
                                Glide.with(this)
                                        .load(playlistImage)
                                        .placeholder(R.drawable.ic_top_placeholder)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                        .into(iv_song_image_bottom);
                                Glide.with(this)
                                        .load(playlistImage)
                                        .placeholder(R.drawable.ic_top_placeholder)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                        .into(iv_song_image);
                            }
                        }

                    }
                }

            }

            if (et_lyrics != null) {
                et_lyrics.setText(song.getLyrics());
            }
            File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
            if (myFile.exists()) {
                Log.i("TAG", "myfile-> " + myFile.getAbsolutePath());
                if (iv_song_download != null) {
                    iv_song_download.setImageResource(R.drawable.ic_done_theme);
                    download_progress_bar.setProgress(100);
                }
            } else {
                if (iv_song_download != null) {
                    iv_song_download.setImageResource(R.drawable.download_gray);
                    download_progress_bar.setProgress(0);
                }
            }
            if (iv_add_play_list != null) {
                if (song.getIsPlaylist() && !song.getType().equalsIgnoreCase(TABLE_DOWNLOADED_SONG)) {
                    iv_add_play_list.setImageResource(R.drawable.ic_playlist_add_theme);
                } else if (song.getType().equalsIgnoreCase(TABLE_DOWNLOADED_SONG)) {
                    iv_add_play_list.setImageResource(R.drawable.plus_gray);
                } else {
                    iv_add_play_list.setImageResource(R.drawable.plus_gray);

                }
            }
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    if (fragmentTAG.equalsIgnoreCase(SearchFragment.class.getSimpleName())) {

                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
//                        swipe_layout.setActivated(false);
            }
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            iv_share.setVisibility(View.VISIBLE);
            iv_play_song_bottom.setVisibility(View.VISIBLE);
            iv_play_song.setVisibility(View.VISIBLE);
            play_progressbar.setVisibility(View.INVISIBLE);
            play_progressbar_main.setVisibility(View.INVISIBLE);

        }
    }


    private HashMap<String, String> getAddToPlaylistParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, songList.get(curruntPosition).getSongID());
        params.put(ApiParameter.TRACK_TYPE, songList.get(curruntPosition).getTrackType());
        return params;
    }

    public HashMap<String, String> getAddFavouriteParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, songList.get(curruntPosition).getSongID());
        params.put(ApiParameter.TRACK_TYPE, songList.get(curruntPosition).getTrackType());
        if (songList.get(curruntPosition).getAlbumID() != null && !songList.get(curruntPosition).getAlbumID().isEmpty()) {
            params.put(ApiParameter.ALBUM_ID, songList.get(curruntPosition).getAlbumID());
        } else {
            params.put(ApiParameter.ALBUM_ID, "1");
        }
        return params;
    }

    public HashMap<String, String> getShareParam() {
        HashMap<String, String> params = new HashMap<>();
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        Log.d("user_id", user_id);
        if (!user_id.isEmpty()) {
            params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        } else {
            params.put(ApiParameter.USER_ID, "0");
        }
        params.put(ApiParameter.TITLE, songList.get(curruntPosition).getSongTitle());
       /* if (!songList.get(selectedMenuPosition).getLyrics().equals(""))
            params.put(ApiParameter.MUSIC_DESCRIPTION, songList.get(selectedMenuPosition).getLyrics());
        else*/
        params.put(ApiParameter.MUSIC_DESCRIPTION, songList.get(curruntPosition).getSongArtist());
        String imageUrl = songList.get(curruntPosition).getSongImageUrl();
//        if (imageUrl.contains("/index.php")) {
//            imageUrl = imageUrl.replace("/index.php", "");
//        }
        params.put(ApiParameter.MUSIC_THUMBNAIL, imageUrl);
        params.put(ApiParameter.MUSIC_URL, songList.get(curruntPosition).getSongURL());
        params.put(ApiParameter.TRACH_ID, songList.get(curruntPosition).getSongID());
        params.put(ApiParameter.THIRD_PARTY, songList.get(curruntPosition).getThirdparty_song().toString());

        Log.e("params", "" + params);

        return params;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isReadStorageAllowed(int pickImageRequestCode) {
        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, pickImageRequestCode);
            return false;
        }
    }

    public void getSharedMusic(String url) {
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.URL, url);
        params.put(ApiParameter.USER_ID, user_id);
        NetworkCall.getInstance().callPlayListApi(AppConstants.SHARED_MUSIC, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    if (!serviceBound) {
                        bindServiceCustom();
                    }
                    register_notify();
                    SharedSongModel sharedSongModel = new Gson().fromJson(data.toString(), SharedSongModel.class);
                    if (sharedSongModel.getData() != null) {
                        songList = new ArrayList<>();
                        SharedSongData sharedSongData = sharedSongModel.getData();
                        Song song = new Song();
                        song.setUser_id(String.valueOf(sharedSongData.getUserId()));
                        song.setSongID(sharedSongData.getTrackId());
                        song.setSongTitle(sharedSongData.getTitle());
                        song.setSongURL(sharedSongData.getMusicUrl());
                        song.setSongNumber(sharedSongData.getTrackId());
                        song.setSongImageUrl(sharedSongData.getMusicThumbnail());
                        song.setSongArtist(sharedSongData.getMusicDescription());
                        song.setIsLike(sharedSongData.isLike());
                        song.setIsFavourite(sharedSongData.isFavorite());
                        song.setIsPlaylist(false);
                        song.setFrom(AppConstants.FROM_HOME_TOP);
                        song.setLyrics("");
                        song.setAlbumID(sharedSongData.getTrackId());
                        File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                        File imagedirect = new File(getExternalCacheDir() + "/Images.nomedia");
                        if (myFile.exists()) {
                            song.setFrom(AppConstants.FROM_MY_MUSIC);
                            song.setSongPath(myFile.getPath());
                            song.setSongImagePath(imagedirect.getPath() + "/" + song.getSongID() + ".jpg");
                            song.setIsDownload(true);
                        } else {
                            song.setFrom(AppConstants.FROM_HOME_TOP);
                            song.setIsDownload(false);
                            song.setSongPath(sharedSongData.getMusicUrl());
                            song.setSongImagePath(song.getSongImageUrl());
                        }
                        song.setTrackType("1");
                        song.setThirdparty_song(sharedSongData.getThirdpartySong());
                        song.setLikeCount(sharedSongData.getLikeCount());
                        song.setAlbumID("1");
                        songList.add(song);

                        if (songList != null && !songList.isEmpty()) {
                            playSongWithPostion(0, songList,ApiParameter.FROM_NOTIFICATION);
                        }

                    }
                } else {
                    DebugLog.e("Status : " + status);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                DebugLog.e("Status : " + "onFailure");
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                DebugLog.e("Status : " + "onError");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                downloadSong();
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.to_download_song_you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
//        Log.e("onPageScrolled", "pos : " + curruntPosition + "    "+position);


    }

    @Override
    public void onPageSelected(final int position) {
//        Log.e("OnPageSelected : ", "onPageSelected : "+curruntPosition  +"     " + position);
//        if (fromNotification){
//            fromNotification = false;
//        }else {
        if (curruntPosition > position) {
            player.skipToPrevious();
        } else if (curruntPosition < position) {
            player.skipToNext();
        }
//        }
//        if (mNowPlayingAdapter != null){
//            myPagerAdapter.updateList(songList);
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

//            }
//        },500);


    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.e("onPageStateChanged : ", "onPageScrollStateChanged : " + state);

    }

    @Override
    public void onListShuffle(ArrayList<Song> shuffledList) {
        songList = shuffledList;
        if (mNowPlayingAdapter != null) {
            myPagerAdapter.updateList(songList);
        }

    }

    private void downloadSong() {
        String externalUrl = "";
        if (!songList.get(curruntPosition).getThirdparty_song()) {
            externalUrl = ApiParameter.BASE_MUSIC_URL;
        }
        Uri music_uri = Uri.parse(externalUrl + songList.get(curruntPosition).getSongURL());
        MyDownloadManager downloadManager = new MyDownloadManager(HomeActivity.this, this);
        downloadManager.DownloadData(music_uri, songList.get(curruntPosition));
    }

    private HashMap<String, String> getLikeTrackParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, songList.get(curruntPosition).getSongID());
        params.put(ApiParameter.TRACK_TYPE, songList.get(curruntPosition).getTrackType());
        return params;
    }


    @Override
    public void onItemClick(ViewPager parent, View view, int position) {
//        Log.e("onItemClick : ", "onItemClick");

    }

    @Override
    public void onImageSelected(int position, @NotNull Song selectedData) {
        if (position == curruntPosition) {
            showLyricsDialog(selectedData);
        }
    }

    @Override
    public void onSongSelected(int position, @NotNull Song selectedData,String type) {
        if (upNextList != null && !upNextList.isEmpty()) {
            playSongWithPostion(position, upNextList,type);
            botto_sheet_music_behaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            songList.set(position, selectedData);
            if (mNowPlayingAdapter != null) {
                myPagerAdapter.updateList(upNextList);
            }
        }
    }

    @Override
    public void onDotSelected(int position, @NotNull Song selectedData) {

    }

    @Override
    public void changeProgress(String position, int progress) {
//        Log.e("CHeck name ", " iD : " + position + "   " + songList.get(curruntPosition).getSongID());
        if (position.toString().equals(songList.get(curruntPosition).getSongID())) {
            if (download_progress_bar != null) {
                download_progress_bar.setProgress(progress);
                if (progress == 100) {
                    iv_song_download.setImageResource(R.drawable.ic_done_theme);
                }
            }
        }
    }

    @Override
    public void onListUpdated(ArrayList<Song> songs) {
        songList = songs;
        if (tv_like_count != null) {
            if (songs.size() > curruntPosition) {
                tv_like_count.setText("" + songList.get(curruntPosition).getLikeCount());
            } else {
                curruntPosition = 0;
                tv_like_count.setText("" + songList.get(curruntPosition).getLikeCount());
            }
        }
        if (myPagerAdapter != null) {
            myPagerAdapter.updateList(songList);
        }
        setSongData(songList.get(curruntPosition));
    }

      public void onImageViewUpdateForMainPlayer(ArrayList<Song> songs,Song song){
        if (songList != null && !songList.isEmpty() && song.getSongID().toString().equals(songList.get(curruntPosition).getSongID())) {
//            songList = songs;
            songList.set(curruntPosition,song);
            if (tv_like_count != null) {
                if (songs.size() > curruntPosition) {
                    tv_like_count.setText("" + songList.get(curruntPosition).getLikeCount());
                } else {
                    curruntPosition = 0;
                    tv_like_count.setText("" + songList.get(curruntPosition).getLikeCount());
                }
            }
            StorageUtil storageUtil = new StorageUtil(HomeActivity.this);
            storageUtil.storeAudio(songList);
            storageUtil.storeType(storageUtil.loadTYpe());
            if (myPagerAdapter != null) {
                myPagerAdapter.updateList(songList);
            }
            setSongData(songList.get(curruntPosition));
        }
    }

    @Override
    public void onUpdatePlayerList(int audioIndex, ArrayList<Song> audioLIst,String type) {
        songList = audioLIst;
        curruntPosition = audioIndex;
        MediaPlayerService.setList(audioLIst, -1, HomeActivity.this,type);
        setViewPagerPosition();
    }

    @Override
    public void onStartedMediaPlayer() {
        if (seekbar_bottom != null && seekbar_main_player != null) {
            seekbar_main_player.setProgress(0);
            seekbar_bottom.setProgress(0);
        }
        if (tv_total_time != null && tv_play_time != null) {
            tv_total_time.setText("0:00");
            tv_play_time.setText("0:00");
        }
        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
//            setOriginalSongData();
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            iv_play_song_bottom.setVisibility(View.VISIBLE);
            iv_play_song_bottom.setImageResource(R.drawable.ic_pause);
            iv_play_song.setImageResource(R.drawable.ic_pause);
            iv_play_song.setVisibility(View.VISIBLE);
            play_progressbar.setVisibility(View.INVISIBLE);
            play_progressbar_main.setVisibility(View.INVISIBLE);
        }
        onPlayMediaPlayer();

    }

    public void setSlidingState(Boolean collapsed) {
//        if (collapsed) {
//            if (player.isPng() || isPaused) {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }else {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//            }
//        } else {
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//        }
    }

    @Override
    public void onChangeSeekposition(final int totalprogress, final int completedProgress) {

        if (seekbar_bottom != null && seekbar_main_player != null && tv_play_time != null && tv_total_time != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    seekbar_main_player.setMax(totalprogress);
                    seekbar_bottom.setMax(totalprogress);
                    seekbar_bottom.setProgress(completedProgress);
                    seekbar_main_player.setProgress(completedProgress);
                    tv_total_time.setText("" + getSongTime(totalprogress));
                    tv_play_time.setText("" + getSongTime(completedProgress));
                }
            });
        }

        try {
            int total_progressin_sec = getTimeinInt(totalprogress);
            int complere_progress_in_sec = getTimeinInt(completedProgress);
            int last_progress = (total_progressin_sec - 2);
            if (complere_progress_in_sec > last_progress) {
                player.onCompletion(MediaPlayerService.mediaPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPlayMediaPlayer() {
        if (iv_play_song != null && iv_play_song_bottom != null) {
            isPaused = false;
            iv_play_song_bottom.setImageResource(R.drawable.ic_pause);
            iv_play_song.setImageResource(R.drawable.ic_pause);
        }
//        try{
//            if (player.isPng()) {
//                setOriginalSongData();
//                if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

    @Override
    public void onStopMediaPlayer() {
        if (iv_play_song != null && iv_play_song_bottom != null) {
            isPaused = true;
            iv_play_song_bottom.setImageResource(R.drawable.ic_play);
            iv_play_song.setImageResource(R.drawable.ic_play);
            if (mLayout != null) {
                isHidden = true;
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        }
    }



    @Override
    public void onPauseMediaPlayer() {
        if (iv_play_song != null && iv_play_song_bottom != null) {
            isPaused = true;
            iv_play_song_bottom.setImageResource(R.drawable.ic_play);
            iv_play_song.setImageResource(R.drawable.ic_play);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void pickImage() {
//        DebugLog.e("Pick Image");
        if (isReadStorageAllowed(PICK_IMAGE_REQUEST_CODE)) {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                if (bitmap != null) {
                    try {
                        OutputStream fOut = null;
                        File imagedirect = new File(getExternalCacheDir() + "/Images.nomedia" + System.currentTimeMillis() + ".jpg");
                        fOut = new FileOutputStream(imagedirect);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                        fOut.flush();
                        fOut.close();

                        MediaStore.Images.Media.insertImage(getContentResolver()
                                , imagedirect.getAbsolutePath(), imagedirect.getName(), imagedirect.getName());
                        imageURL = imagedirect.getPath();
                        if (iv_playlistBanner != null) {
                            Glide.with(HomeActivity.this)
                                    .load(imageURL)
                                    .placeholder(R.drawable.ic_top_placeholder)
                                    .override(200, 200)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                    .into(iv_playlistBanner);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("imageurl : ", "  " + imageURL);

                }

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    public void openDialog(final ImageView imageView, final String songNumber) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_playlists);
        dialog.show();

        ImageView iv_close_playlist = dialog.findViewById(R.id.iv_close_playlist);
        iv_close_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RelativeLayout rl_add_playlist = dialog.findViewById(R.id.rl_add_playlist);
        rl_add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openCreatePlayListDialog(imageView, songNumber);
            }
        });

        RecyclerView rv_play_list = dialog.findViewById(R.id.rv_play_list);
        TextView tv_no_data = dialog.findViewById(R.id.tv_no_data);

        getUserPlaylist(rv_play_list, tv_no_data, songNumber, dialog, imageView);

    }

    public void openCreatePlayListDialog(final ImageView imageView, final String songNumber) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_create_playlist);
        dialog.show();
        iv_playlistBanner = dialog.findViewById(R.id.iv_playlist_image);
        Glide.with(this)
                .load(R.drawable.ic_top_placeholder)
                .placeholder(R.drawable.ic_top_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(iv_playlistBanner);
        final EditText editText = dialog.findViewById(R.id.et_name);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        ImageView iv_close = dialog.findViewById(R.id.iv_close_dialog);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().isEmpty()) {
                    showSnackBar(parentLayout, getString(R.string.enter_playlist_name));
                    editText.requestFocus();
                } else {
                    createPlaylist(imageURL, editText.getText().toString(), imageView, songNumber);
                    imageURL = "";
                    dialog.dismiss();
                }
            }
        });

        iv_playlistBanner.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void getUserPlaylist(final RecyclerView rv_playlist, final TextView tv_no_data, final String songNumber, final Dialog dialog, final ImageView imageView) {
        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        NetworkCall.getInstance().callPlayListApi(AppConstants.USER_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DebugLog.e("Status : " + data.toString());
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    PlayListModel playListModel = new Gson().fromJson(data.toString(), PlayListModel.class);
                    if (playListModel != null && playListModel.getData() != null && playListModel.getData().getPlaylist() != null && !playListModel.getData().getPlaylist().isEmpty()) {
                        rv_playlist.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);
                        rv_playlist.setAdapter(new PlayListAdapter(HomeActivity.this, playListModel.getData().getPlaylist(), songNumber, dialog, imageView));
                    } else {
                        rv_playlist.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
//                    showSnackBar(parentLayout, message);
                } else {
//                    DebugLog.e("Status : " + status);
                    showSnackBar(parentLayout, getString(R.string.playlist_not_found));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(parentLayout, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                T.printStackTrace();
                hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                showSnackBar(parentLayout, getString(R.string.api_error_message));
            }
        });
    }


    public void createPlaylist(String imageURL, String name, final ImageView imageView, final String songNumber) {
        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.NAME, name);
        params.put(ApiParameter.IMAGE, imageURL);
        NetworkCall.getInstance().callPlayListApi(AppConstants.CREATE_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
                String playListID = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    playListID = data.get("playlist_id").getAsString();
                    showSnackBar(parentLayout, getString(R.string.playlist_created));
                    if (!songNumber.isEmpty()) {
                        addToPlaylist(imageView, songNumber, playListID);
                    }
                } else {
                    DebugLog.e("Status : " + status);
                    showSnackBar(parentLayout, getString(R.string.playlist_not_created));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                showSnackBar(parentLayout, baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                if (T.getMessage() != null && !T.getMessage().isEmpty()) {
                    showSnackBar(parentLayout, T.getMessage());
                }
//                T.printStackTrace();
            }
        });
    }

    public void updatePlaylist(String imageURL, String name, final String playlist_ID) {
        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.NAME, name);
        params.put(ApiParameter.IMAGE, imageURL);
        params.put(ApiParameter.PLAYLIST_ID, playlist_ID);
        NetworkCall.getInstance().callPlayListApi(AppConstants.UPDATE_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    showSnackBar(parentLayout, getString(R.string.playlist_updated_successfully));
                } else {
                    DebugLog.e("Status : " + status);
                    showSnackBar(parentLayout, message);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(parentLayout, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                showSnackBar(parentLayout, getString(R.string.api_error_message));
            }
        });
    }

    public void addToPlaylist(final ImageView imageView, final String song_number, String playlist_ID) {
        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.PLAYLIST_ID, playlist_ID);
        params.put(ApiParameter.TRACK_ID, song_number);

        NetworkCall.getInstance().callPlayListApi(AppConstants.ADD_SONG_IN_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    showSnackBar(parentLayout, getString(R.string.song_added_to_playlist));
                    imageView.setImageResource(R.drawable.ic_playlist_add_theme);
                } else {
                    DebugLog.e("Status : " + status);
                    showSnackBar(parentLayout, getString(R.string.already_in_playlist));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                showSnackBar(parentLayout, baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                showSnackBar(parentLayout, getString(R.string.api_error_message));
            }
        });
    }

    public void removeToPlaylist(final ImageView imageView, final String songNumber, String playlist_ID) {
        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.PLAYLIST_ID, playlist_ID);
        params.put(ApiParameter.TRACK_ID, songNumber);

        NetworkCall.getInstance().callPlayListApi(AppConstants.REMOVE_SONG_FROM_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    showSnackBar(parentLayout, getString(R.string.song_removed_from_playlist));
                    imageView.setImageResource(R.drawable.ic_add);
                } else {
                    DebugLog.e("Status : " + status);
                    showSnackBar(parentLayout, message);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(parentLayout, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                showSnackBar(parentLayout, getString(R.string.api_error_message));
            }
        });
    }


    public void removePlaylist(String playlist_ID) {
        showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.ID, playlist_ID);
        NetworkCall.getInstance().callPlayListApi(AppConstants.REMOVE_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    showSnackBar(parentLayout, getString(R.string.playlist_deleted));
                } else {
                    DebugLog.e("Status : " + status);
                    showSnackBar(parentLayout, message);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                showSnackBar(parentLayout, baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                showSnackBar(parentLayout, getString(R.string.api_error_message));
            }
        });
    }

    // Method to manually check connection status
    public void isConnected() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnackBar(parentLayout, getString(R.string.sorry_no_internet));
        }
    }

    // Method to manually check connection status
    public static Boolean isDataSaverEnabled() {
        Boolean isEnabled = false;
        String interModeType = String.valueOf(ConnectivityReceiver.getNetworkType(getInstance()));
        String dataMode = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.DATA_MODE);
        if (interModeType.toString().equals("0")) {
            if (dataMode.toString().equals("true")) {
                return true;
            }
        }
        return isEnabled;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    public void getNetworkType(int typeEthernet,String nameOFData) {
        Log.e("getNetworkType  :  ",""+typeEthernet+"        "+nameOFData);
        if (typeEthernet == ConnectivityManager.TYPE_MOBILE) {
            showDataModeDialog(getString(R.string.data_saver_message));
        }else /*if (typeEthernet == ConnectivityManager.TYPE_WIFI){*/
        {  AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DATA_MODE, "false");
            AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DONT_SHOW, "false");
        }
        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.INTERNET_MODE, String.valueOf(typeEthernet));
    }

    public void showDataModeDialog(String message) {
        final CheckBox dontShowAgain;
//Dialog code
        AlertDialog.Builder adb = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater adbInflater = LayoutInflater.from(HomeActivity.this);
        View eulaLayout = adbInflater.inflate(R.layout.data_saver_layout, null);
        dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
        adb.setView(eulaLayout);
        adb.setTitle(getString(R.string.attention));
        adb.setMessage(Html.fromHtml(message));
        adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "false";
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DATA_MODE, "true");
                if (dontShowAgain.isChecked()) checkBoxResult = "true";
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DONT_SHOW, checkBoxResult);
                isDataSaverDialogShowing = false;
                dialog.dismiss();
                return;
            }
        });

        adb.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "false";
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DATA_MODE, "false");
                if (dontShowAgain.isChecked()) checkBoxResult = "true";
                AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.DONT_SHOW, checkBoxResult);
                isDataSaverDialogShowing = false;
                dialog.dismiss();
                return;
            }
        });
        String skipMessage = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.DONT_SHOW);
        String isDataModeOn = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.DATA_MODE);
        if (!skipMessage.equals("true")) {
            if (!isDataSaverDialogShowing) {
                if (isDataModeOn.toString().equalsIgnoreCase("true")){

                }else {
                    if (!isDataSaverEnabled()) {
                        adb.show();
                        isDataSaverDialogShowing = true;
                    }
                }

            }
        }
    }

    public void updateUpnextList(@NotNull ArrayList<Song> list) {
        int newPosition = (curruntPosition + 1);
        if (newPosition < songList.size()) {
            for (int i = 0; i < list.size(); i++) {
                songList.set(newPosition, list.get(i));
                newPosition++;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StorageUtil storageUtil = new StorageUtil(HomeActivity.this);
            String type = storageUtil.loadTYpe();
            player.setList(songList, -1, HomeActivity.this,type);
            if (myPagerAdapter != null) {
                myPagerAdapter.updateList(songList);
            }
//            Log.e("After Update : ", ""+new Gson().toJson(songList));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Log.e("onConfigurationChanged",""+newConfig);
        // your code here, you can use newConfig.locale if you need to check the language
        // or just re-set all the labels to desired string resource
    }

    public void showBottomMenu(boolean b) {
//        if (b){
//            if (player != null && player.isPng()) {
//                if (mLayout != null && mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED && mLayout.getPanelState()
//                        != SlidingUpPanelLayout.PanelState.EXPANDED) {
//                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                }
//            }
//        }else {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//
//        }
    }

    public void getUserPlaylistOffline(final RecyclerView rv_playlist, final TextView tv_no_data, final Song song, final Dialog dialog, final ImageView imageView) {
        ArrayList<DownloadPlaylistModel> playListModel = databaseHandler.getAllPLaylist();
        if (playListModel != null && !playListModel.isEmpty()) {
            rv_playlist.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
            rv_playlist.setAdapter(new DownloadPlayListAdapter(HomeActivity.this, playListModel, dialog, song, imageView, databaseHandler));
        } else {
            rv_playlist.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }

    }

    public void openDialogOffline(final ImageView imageView, final Song song) {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_playlists);
        dialog.show();

        ImageView iv_close_playlist = dialog.findViewById(R.id.iv_close_playlist);
        iv_close_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RelativeLayout rl_add_playlist = dialog.findViewById(R.id.rl_add_playlist);
        rl_add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openCreatePlayListDialogOffline(imageView, song);
            }
        });
        RecyclerView rv_play_list = dialog.findViewById(R.id.rv_play_list);
        TextView tv_no_data = dialog.findViewById(R.id.tv_no_data);
        getUserPlaylistOffline(rv_play_list, tv_no_data, song, dialog, imageView);

    }

    public void openCreatePlayListDialogOffline(final ImageView imageView, final Song song) {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_create_playlist);
        dialog.show();
        iv_playlistBanner = dialog.findViewById(R.id.iv_playlist_image);
        Glide.with(HomeActivity.this)
                .load(R.drawable.ic_top_placeholder)
                .placeholder(R.drawable.ic_top_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(iv_playlistBanner);
        final EditText editText = dialog.findViewById(R.id.et_name);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        btn_add.setText(getString(R.string.create_playlist));
        ImageView iv_close = dialog.findViewById(R.id.iv_close_dialog);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().isEmpty()) {
                    showSnackBar(parentLayout, getString(R.string.enter_playlist_name));
                    editText.requestFocus();
                } else {
//                        if (imageURL == null || imageURL.isEmpty()){
//                            pickImage();
//                            homeActivity.showSnackBar(getView(),getString(R.string.please_select_playlist_image));
//                            return;
//                        }
                    Util.hideKeyboard(HomeActivity.this);
                    DownloadPlaylistModel downloadPlaylistModel = new DownloadPlaylistModel();
                    downloadPlaylistModel.setName(editText.getText().toString());
                    downloadPlaylistModel.setImage(imageURL);
                    downloadPlaylistModel.setCreatedAt(Util.getCurrentDateTime());
                    song.setIsPlaylist(true);
                    ArrayList<Song> mSongs = new ArrayList<>();
                    mSongs.add(song);
                    String values = new Gson().toJson(mSongs);
                    downloadPlaylistModel.setValue(values);
                    databaseHandler.addPlaylist(downloadPlaylistModel);
                    imageView.setImageResource(R.drawable.ic_playlist_add_theme);
                    showSnackBar(parentLayout, getString(R.string.song_added_to_playlist));
                    imageURL = "";
                    dialog.dismiss();
                }
            }
        });

        iv_playlistBanner.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
//                if (type.toString().equals(AppConstant.ADD)) {
                pickImage();
//                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
