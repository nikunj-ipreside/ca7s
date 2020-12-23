package com.music.ca7s.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.OnLoadMoreListener;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.homeadapters.PlayListSongAdapter;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.listener.RecyclerItemListener;
import com.music.ca7s.listener.RecyclerItemSongListenerListener;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.mediaplayer.MediaPlayerService;
import com.music.ca7s.mediaplayer.MyDownloadManager;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.DownloadPlaylistModel;
import com.music.ca7s.model.PlayListModel;
import com.music.ca7s.model.toptracklist.GetTopData;
import com.music.ca7s.model.toptracklist.GetTopPojo;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.music.ca7s.utils.AppConstants.DOWNLOADED_PLAYLIST;
import static com.music.ca7s.utils.AppConstants.FROM_FAVOURITE_PLAYLIST;
import static com.music.ca7s.utils.AppConstants.GET_SONG_FROM_PLAYLIST;
import static com.music.ca7s.utils.AppConstants.NEW;
import static com.music.ca7s.utils.AppConstants.RISING;
import static com.music.ca7s.utils.AppConstants.TABLE_DOWNLOADED_SONG;
import static com.music.ca7s.utils.AppConstants.TOP;

public class PlayListFragment extends BaseFragment implements OnLoadMoreListener, DownloadSongListener, RecyclerItemListener,
        RecyclerItemSongListenerListener, SearchView.OnQueryTextListener {
    private static final int STORAGE_PERMISSION_CODE = 1;
    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.rvPlayList)
    RecyclerView rvPlayList;
    BottomSheetBehavior sheetBehavior, sheetBehaviorPlalist;
    @BindView(R.id.bottom_sheet_music)
    LinearLayout bottomSheet;
    @BindView(R.id.bottom_sheet_playList)
    LinearLayout bootomSheetPlayList;
    @BindView(R.id.imgVPSong)
    ImageView imgVPSong;
    @BindView(R.id.tv_song_name)
    TextView txtSongName;
    @BindView(R.id.txtSongContent)
    TextView txtSongContent;
    @BindView(R.id.imgDownArrow)
    ImageButton imgDownArrow;
    @BindView(R.id.iv_favourite_bottom_sheet)
    ImageView iv_favourite_bottom_sheet;
    @BindView(R.id.iv_like_bottom_sheet)
    ImageView iv_like_bottom_sheet;
    @BindView(R.id.iv_song_download)
    ImageView chkSongDown;
    @BindView(R.id.iv_add_play_list)
    ImageView iv_add_play_list;
    @BindView(R.id.tv_add_playlist)
    TextView tv_add_playlist;
    @BindView(R.id.chkShare)
    ImageView chkShare;
    @BindView(R.id.ll_like)
    LinearLayout ll_like;
    @BindView(R.id.ll_favourite)
    LinearLayout ll_favourite;
    @BindView(R.id.ll_download)
    LinearLayout ll_download;
    @BindView(R.id.ll_addtoplaylist)
    LinearLayout ll_addtoplaylist;
    @BindView(R.id.ll_sharesong)
    LinearLayout ll_sharesong;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
//    @BindView(R.id.edt_search)
//    SearchView edt_search;

    @BindView(R.id.tv_main)
    TextView tv_main;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.top_image)
    ImageView top_image;

    private Boolean isScreenVisible = true;


    //All Search Functionality
    @BindView(R.id.ll_expanded)
    RelativeLayout ll_expanded;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.btn_play)
    Button btn_play;
    private boolean isHideToolbarView = false;
    int page = 1;
    int lastPage = 1;
    private String sCookie;
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Song> fiteredList = new ArrayList<>();
    private int selectedMenuPosition = 0;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    public PlayListSongAdapter mAdapter;
    public boolean isLoadmore = false;
    private long mLastClickTime = 0;
    private ImageView iv_search_edit;
    private androidx.appcompat.widget.SearchView searchView;
    private String songNumber = "";
    public static PlayListFragment playListFragment;

    public void setListModel(PlayListModel listModel) {
        this.listModel = listModel;
        type = listModel.getType();
        screenID = listModel.getScreenID();
        genreId = listModel.getGenreID();
        screenName = listModel.getName();
        if (listModel.getGenre_name() != null && !listModel.getGenre_name().isEmpty()) {
            genre_name = listModel.getGenre_name();
        } else {
            genre_name = listModel.getName();
        }
    }

    PlayListModel listModel;

    private String type = "";
    private String screenName = "";
    private int screenID = 0;
    private int genreId = 0;
    private String genre_name = "";


    public static PlayListFragment newInstance(PlayListModel listModel) {
        PlayListFragment fragment = new PlayListFragment();
        fragment.setListModel(listModel);
        return fragment;
    }

    @Override
    public void onListUpdated(ArrayList<Song> songs) {
        songList = songs;
        setDataAdapter();
    }

    //    @SuppressLint("ValidFragment")
//    public PlayListFragment(PlayListModel playListModel){
//        this.listModel=playListModel;
//    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        playListFragment = this;
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(true);
        imgTopbarRight.setVisibility(View.GONE);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        homeActivity.lockDrawer();

        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        txtTopbarTitle.setText(genre_name);
        tv_main.setText(screenName);
        mAdapter = new PlayListSongAdapter(songList, songList, homeActivity, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvPlayList.setLayoutManager(mLayoutManager);
        rvPlayList.setHasFixedSize(true);
        rvPlayList.setAdapter(mAdapter);
        rvPlayList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (mAdapter.getItemCount() - 2)) {
                    onLoadMore();
                }
            }
        });
        page = AppLevelClass.page;
        if (page == 0) {
            page = 1;
        }
        loadData(page, false);
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvPlayList.getLayoutManager();

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:

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

        sheetBehaviorPlalist = BottomSheetBehavior.from(bootomSheetPlayList);
        sheetBehaviorPlalist.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehaviorPlalist.setHideable(false);//Important to add
        sheetBehaviorPlalist.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehaviorPlalist.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        ll_search.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        ll_search.setVisibility(View.VISIBLE);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = (androidx.appcompat.widget.SearchView) view.findViewById(R.id.searchview);
        iv_search_edit = view.findViewById(R.id.iv_search_edit);

        if (screenID == 0 && type.toString().equals(TOP)) {
            tv_title.setText(getString(R.string.created_by_top));
            Glide.with(getContext())
                    .load(R.drawable.ic_top_placeholder)
                    .override(200, 200)
                    .placeholder(R.drawable.ic_top_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                    .into(top_image);
        } else if (screenID == 0 && type.toString().equals(NEW)) {
            tv_title.setText(getString(R.string.created_by_new));
            Glide.with(getContext())
                    .load(R.drawable.ic_new)
                    .override(200, 200)
                    .placeholder(R.drawable.ic_top_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                    .into(top_image);
        } else if (screenID == 0 && type.toString().equals(RISING)) {
            tv_title.setText(getString(R.string.created_by_rising));
            Glide.with(getContext())
                    .load(R.drawable.ic_rising)
                    .override(200, 200)
                    .placeholder(R.drawable.ic_top_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                    .into(top_image);
        }
//        else if (screenID == 1 && type.toString().equals(TOP)) {
//            Glide.with(getContext())
//                    .load(R.drawable.ic_top_placeholder)
//                    .override(200, 200)
//                    .placeholder(R.drawable.ic_top_placeholder)
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
//                    .into(top_image);
//        }
        else {
            if (HomeActivity.isDataSaverEnabled()) {
                Glide.with(getContext())
                        .load(R.drawable.ic_top_placeholder)
                        .override(200, 200)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                        .into(top_image);
            } else {
                if (listModel != null && listModel.getImage() != null && !listModel.getImage().isEmpty()) {
                    Glide.with(getContext())
                            .load(listModel.getImage())
                            .override(200, 200)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(top_image);
                } else {
                    Glide.with(getContext())
                            .load(R.drawable.ic_top_placeholder)
                            .override(200, 200)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(top_image);
                }
            }
        }

        if (screenID == 1 && type.toString().equals(RISING)) {
            String language = Util.changeLanguage(homeActivity);

            if (language.toString().equals("Portuguese")) {
                if (listModel.getPt() != null && !listModel.getPt().isEmpty()) {
                    tv_title.setText(listModel.getPt());
                }

            } else {
                if (listModel.getEn() != null && !listModel.getEn().isEmpty()) {
                    tv_title.setText(listModel.getEn());
                }
            }
        }

        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchEditText.setTextColor(getResources().getColor(R.color.colorBlack));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Util.hideKeyboard(homeActivity);
                    return true;
                }
                return false;
            }
        });
//        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.gray));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(this);

        iv_search_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(homeActivity);
            }
        });

    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        mAdapter.getFilter().filter(text);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        String text = s;
        mAdapter.getFilter().filter(text);
        searchView.onActionViewCollapsed();
        searchView.clearFocus();
        return false;
    }

    public void updateData(Song song){
        if (songList != null && !songList.isEmpty()){
            for (int i =0;i<songList.size();i++){
                if (songList.get(i).getSongID().toString().equals(song.getSongID())){
                    songList.set(i,song);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isScreenVisible = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        isScreenVisible = false;
    }

    @Override
    public void onDownloadSuccess() {
        homeActivity.onDownloadSuccess();
    }

    @Override
    public void onStop() {
        super.onStop();
        isScreenVisible = false;
//        DebugLog.e("onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isScreenVisible = false;
//        DebugLog.e("onDestroy");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.iv_close_player, R.id.imgDownArrow, R.id.ll_like, R.id.ll_favourite, R.id.ll_download, R.id.ll_addtoplaylist, R.id.ll_sharesong, R.id.bottom_sheet_music, R.id.btn_play, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.iv_search:
                ll_search.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_play:
                if (songList != null && !songList.isEmpty()) {
                    homeActivity.playSongWithPostion(0, songList,type);
                } else {

                }
                break;
            case R.id.imgDownArrow:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.ll_sharesong:
//                chkShare.setChecked(false);
                homeActivity.callShareTrackApi(getShareParam(), songList.get(selectedMenuPosition), getView());
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.ll_like:
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (isLogin()) {
                    if (songList.get(selectedMenuPosition).getIsLike()) {
                        homeActivity.callUnlikeTrackApi(getLikeTrackParam(), getView(), iv_like_bottom_sheet, songList, selectedMenuPosition, this);
                    } else {
                        homeActivity.callLikeTrackApi(getLikeTrackParam(), getView(), iv_like_bottom_sheet, songList, selectedMenuPosition, this);
                    }
                }
                break;
            case R.id.ll_favourite:
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                if (isLogin()) {
                    if (songList.get(selectedMenuPosition).getIsFavourite()) {
                        homeActivity.callRemoveFavouriteTrackApi1(getAddFavouriteParam(), getView(), iv_favourite_bottom_sheet, songList, selectedMenuPosition, this);

                    } else {
                        homeActivity.callAddFavouriteTrackApi1(getAddFavouriteParam(), getView(), iv_favourite_bottom_sheet, songList, selectedMenuPosition, this);
                    }
                }
                break;
            case R.id.ll_addtoplaylist:
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (isLogin()) {
                    Song mySong = songList.get(selectedMenuPosition);
                    if (type.toString().equalsIgnoreCase(GET_SONG_FROM_PLAYLIST) && mySong.getIsPlaylist()) {
                        removeToPlaylist(mySong.getSongID(), mySong.getPlayList_id());
                    } else if (type.toString().equalsIgnoreCase(TABLE_DOWNLOADED_SONG) && mySong.getIsPlaylist()) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
                        DownloadPlaylistModel playlistModel = new DownloadPlaylistModel();
                        playlistModel = databaseHandler.getPlaylistData(listModel.getGenreID());
                        String values = playlistModel.getValue();
                        Gson gson = new Gson();
                        ArrayList<Song> myLIst = gson.fromJson(values, new TypeToken<ArrayList<Song>>() {
                        }.getType());
                        for (int i = 0; i < myLIst.size(); i++) {
                            if (mySong.getSongID().equalsIgnoreCase(myLIst.get(i).getSongID())) {
                                myLIst.remove(i);
                                break;
                            }
                        }
                        String neValues = new Gson().toJson(myLIst);
                        playlistModel.setValue(neValues);
                        databaseHandler.updatePlaylist(playlistModel);
                        loadData(0, false);
                    } else if (type.toString().equalsIgnoreCase(TABLE_DOWNLOADED_SONG) && !mySong.getIsPlaylist()) {
                        homeActivity.openDialogOffline(iv_add_play_list, mySong);
                    } else {
                        homeActivity.openDialog(iv_add_play_list, songList.get(selectedMenuPosition).getSongID());
                    }
                }
                break;
            case R.id.bottom_sheet_music:
                break;
        }
    }

    private boolean isReadStorageAllowed() {

        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                downloadSong();
            } else {
                Toast.makeText(getActivity(), getString(R.string.to_download_song_you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadSong() {
        String externalUrl = "";
        if (!songList.get(selectedMenuPosition).getThirdparty_song()) {
            externalUrl = ApiParameter.BASE_MUSIC_URL;
        }
        Uri music_uri = Uri.parse(externalUrl + songList.get(selectedMenuPosition).getSongURL());
        MyDownloadManager downloadManager = new MyDownloadManager(getActivity(), this);
        downloadManager.DownloadData(music_uri, songList.get(selectedMenuPosition));
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
        params.put(ApiParameter.TITLE, songList.get(selectedMenuPosition).getSongTitle());
       /* if (!songList.get(selectedMenuPosition).getLyrics().equals(""))
            params.put(ApiParameter.MUSIC_DESCRIPTION, songList.get(selectedMenuPosition).getLyrics());
        else*/
        params.put(ApiParameter.MUSIC_DESCRIPTION, screenName);
        String imageUrl = songList.get(selectedMenuPosition).getSongImageUrl();
        if (imageUrl.contains("/index.php")) {
            imageUrl = imageUrl.replace("/index.php", "");
        }
        params.put(ApiParameter.MUSIC_THUMBNAIL, imageUrl);
        params.put(ApiParameter.MUSIC_URL, songList.get(selectedMenuPosition).getSongURL());
        params.put(ApiParameter.TRACH_ID, songList.get(selectedMenuPosition).getSongID());
        params.put(ApiParameter.THIRD_PARTY, songList.get(selectedMenuPosition).getThirdparty_song().toString());

        Log.e("params", "" + params);

        return params;
    }

    private HashMap<String, String> getLikeTrackParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, songList.get(selectedMenuPosition).getSongID());
        params.put(ApiParameter.TRACK_TYPE, songList.get(selectedMenuPosition).getTrackType());
        return params;
    }

    private HashMap<String, String> getAddToPlaylistParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, songList.get(selectedMenuPosition).getSongID());
        params.put(ApiParameter.TRACK_TYPE, songList.get(selectedMenuPosition).getTrackType());
        return params;
    }

    public HashMap<String, String> getAddFavouriteParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.TRACK_ID, songList.get(selectedMenuPosition).getSongID());
        params.put(ApiParameter.TRACK_TYPE, songList.get(selectedMenuPosition).getTrackType());
        if (songList.get(selectedMenuPosition).getAlbumID() != null && !songList.get(selectedMenuPosition).getAlbumID().isEmpty()) {
            params.put(ApiParameter.ALBUM_ID, songList.get(selectedMenuPosition).getAlbumID());
        } else {
            params.put(ApiParameter.ALBUM_ID, "1");
        }
        return params;
    }


    private void setSongData(final int id) {
        final Song song = songList.get(id);
        if (song != null) {
//            DebugLog.e("Song URL : " + song.getSongURL());
            if (txtSongName != null)
                txtSongName.setText(song.getSongTitle());
            if (txtSongContent != null)
                txtSongContent.setText(song.getSongArtist());
            if (iv_favourite_bottom_sheet != null) {
                if (song.getIsFavourite()) {
                    iv_favourite_bottom_sheet.setImageResource(R.drawable.heart_theme_filled);
                } else {
                    iv_favourite_bottom_sheet.setImageResource(R.drawable.heart_theme_unfilled);
                }
            }
            if (iv_like_bottom_sheet != null) {
                if (song.getIsLike()) {
                    iv_like_bottom_sheet.setImageResource(R.drawable.favourite_theme_filled);
                } else {
                    iv_like_bottom_sheet.setImageResource(R.drawable.favorite_theme_unfilled);
                }
            }
            if (iv_add_play_list != null) {
                if (song.getIsPlaylist()) {
                    iv_add_play_list.setImageResource(R.drawable.ic_playlist_add_theme);
                    tv_add_playlist.setText(homeActivity.getString(R.string.remove));
                } else {
                    tv_add_playlist.setText(homeActivity.getString(R.string.add_to_playlist));
                    iv_add_play_list.setImageResource(R.drawable.plus_gray);
                }
            }
            File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");

            if (myFile.exists()) {
                Log.i("TAG", "myfile-> " + myFile.getAbsolutePath());
                if (chkSongDown != null) {
                    chkSongDown.setImageResource(R.drawable.ic_done_theme);
                }
            } else {
                if (chkSongDown != null) {
                    chkSongDown.setImageResource(R.drawable.export_download_gray);
                }

            }
            if (ll_download != null) {
                ll_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkMultipleClick()) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                            if (myFile.exists()) {
                                Toast.makeText(getActivity(), R.string.this_song_is_already_downloaded, Toast.LENGTH_LONG).show();
                                chkSongDown.setImageResource(R.drawable.ic_done_theme);
                            } else {
                                if (chkSongDown != null) {
                                    chkSongDown.setImageResource(R.drawable.ic_done_theme);
                                    if (isReadStorageAllowed()) {
                                        Toast.makeText(getActivity(), R.string.this_song_is_downloading, Toast.LENGTH_LONG).show();
                                        downloadSong();
                                    }
                                }
                            }

                        }
                    }
                });
            }
            if (imgVPSong != null)
                if (HomeActivity.isDataSaverEnabled()) {

                } else {
                    String playlistImage = song.getSongImageUrl();
                    if (playlistImage.contains("/index.php")) {
                        playlistImage = playlistImage.replace("/index.php", "");
                    }
                    if (playlistImage != null && !playlistImage.isEmpty()) {
                        Glide.with(getActivity())
                                .load(playlistImage)
                                .placeholder(R.drawable.ic_top_placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                .into(imgVPSong);
                    }
                }

            if (ll_like != null && ll_favourite != null) {
                if (song.getFrom().toString().equals(AppConstants.FROM_MY_MUSIC) || song.getThirdparty_song()) {
                    ll_like.setVisibility(View.GONE);
                    ll_favourite.setVisibility(View.GONE);
                } else {
                    ll_like.setVisibility(View.VISIBLE);
                    ll_favourite.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private boolean checkMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
//        DebugLog.e("onStart");
    }

    private void loadData(int pageno, boolean isSilent) {
        try {
            if (type.toString().equals(TABLE_DOWNLOADED_SONG)) {
                DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
                DownloadPlaylistModel playlistModel = new DownloadPlaylistModel();
                playlistModel = databaseHandler.getPlaylistData(listModel.getGenreID());
                String values = playlistModel.getValue();
                Gson gson = new Gson();
                ArrayList<Song> myLIst = gson.fromJson(values, new TypeToken<ArrayList<Song>>() {
                }.getType());
                songList = myLIst;
                for (int i = 0; i < songList.size(); i++) {
                    Song updatedSong = songList.get(i);
                    updatedSong.setPlayList_id(String.valueOf(playlistModel.getId()));
                    updatedSong.setType(TABLE_DOWNLOADED_SONG);
                    myLIst.set(i, updatedSong);
                }
                setDataAdapter();
            } else {
                callTopApi(pageno, isSilent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callTopApi(final int pageno, boolean isSilent) {
        if (!isSilent)
            homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.PAGE, String.valueOf(pageno));
        Log.i("TAG", "PAG NO:-> " + pageno + "");
        params.put(ApiParameter.TYPE, listModel.getType());
        params.put(ApiParameter.GENRE_ID, String.valueOf(genreId));
        params.put(ApiParameter.PLAYLIST_ID, listModel.getPlayListId());


        NetworkCall.getInstance().callGetTopApi(type, screenID, params, sCookie, new iResponseCallback<GetTopPojo>() {
            @Override
            public void sucess(GetTopPojo data) {
                try {
//                DebugLog.e("Status : " + new Gson().toJson(data) + " ");
                    if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                        List<GetTopData> listData = data.getList().getData();
                        if (page == 1) {
                            songList = new ArrayList<>();
                            songList.clear();
                        }
                        if (listData != null && listData.size() > 0) {
                            for (int i = 0; i < listData.size(); i++) {
                                GetTopData topData = listData.get(i);
                                if (type.equals(AppConstants.FAVOURITE)) {
                                    if (topData.getIsFavorite()) {
                                        Song song = new Song();
                                        song.setUser_id(String.valueOf(topData.getUserId()));
                                        song.setSongID(topData.getId());
                                        song.setSongTitle(topData.getTitle());
                                        song.setSongURL(topData.getStreamUrl());
                                        song.setSongNumber((i + 1) + "");
                                        song.setSongImageUrl(topData.getImageUrl());
                                        song.setSongArtist(topData.getArtistName());
                                        song.setIsLike(topData.getIsLike());
                                        song.setIsFavourite(topData.getIsFavorite());
                                        song.setIsPlaylist(false);
                                        song.setType(listModel.getType());
                                        song.setPlayList_id(listModel.getPlayListId());
                                        File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                                        File imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia");
                                        if (myFile.exists()) {
                                            song.setFrom(AppConstants.FROM_MY_MUSIC);
                                            song.setSongPath(myFile.getPath());
                                            song.setSongImagePath(imagedirect.getPath() + "/" + song.getSongID() + ".jpg");
                                            song.setIsDownload(true);
                                        } else {
                                            song.setFrom(AppConstants.FROM_HOME_TOP);
                                            song.setIsDownload(false);
                                            song.setSongPath(topData.getStreamUrl());
                                            song.setSongImagePath(song.getSongImageUrl());
                                        }
                                        if (topData.getLyrics() != null)
                                            song.setLyrics(topData.getLyrics());
                                        else
                                            song.setLyrics("");
                                        song.setAlbumID(topData.getAlbum_id());
                                        song.setTrackType("1");
                                        song.setThirdparty_song(topData.getThirdparty_song());
                                        song.setLikeCount(topData.getLikeCount());
                                        songList.add(song);
                                    }
                                } else if (type.equals(AppConstants.GET_SONG_FROM_PLAYLIST)) {
                                    Song song = new Song();
                                    song.setUser_id(String.valueOf(topData.getUserId()));
                                    song.setSongID(topData.getId());
                                    song.setSongTitle(topData.getTitle());
                                    song.setSongURL(topData.getStreamUrl());
                                    song.setSongNumber((i + 1) + "");
                                    song.setSongImageUrl(topData.getImageUrl());
                                    song.setSongArtist(topData.getArtistName());
                                    song.setIsLike(topData.getIsLike());
                                    song.setIsFavourite(topData.getIsFavorite());
                                    song.setIsPlaylist(true);
                                    song.setFrom(AppConstants.FROM_HOME_TOP);
                                    song.setType(listModel.getType());
                                    song.setPlayList_id(listModel.getPlayListId());
                                    if (topData.getLyrics() != null)
                                        song.setLyrics(topData.getLyrics());
                                    else
                                        song.setLyrics("");
                                    song.setAlbumID(String.valueOf(genreId));
                                    File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                                    File imagedirect = new File(Objects.requireNonNull(getContext()).getExternalCacheDir() + "/Images.nomedia");
                                    if (myFile.exists()) {
                                        song.setFrom(AppConstants.FROM_MY_MUSIC);
                                        song.setSongPath(myFile.getPath());
                                        song.setSongImagePath(imagedirect.getPath() + "/" + song.getSongID() + ".jpg");
                                        song.setIsDownload(true);
                                    } else {
                                        song.setFrom(AppConstants.FROM_HOME_TOP);
                                        song.setIsDownload(false);
                                        song.setSongPath(topData.getStreamUrl());
                                        song.setSongImagePath(song.getSongImageUrl());
                                    }
                                    song.setTrackType("1");
                                    song.setThirdparty_song(topData.getThirdparty_song());
                                    song.setLikeCount(topData.getLikeCount());
                                    song.setAlbumID(topData.getAlbum_id());
                                    songList.add(song);
                                } else {
                                    Song song = new Song();
                                    song.setUser_id(String.valueOf(topData.getUserId()));
                                    song.setSongID(topData.getId());
                                    song.setSongTitle(topData.getTitle());
                                    song.setSongURL(topData.getStreamUrl());
                                    song.setSongNumber((i + 1) + "");
                                    song.setSongImageUrl(topData.getImageUrl());
                                    song.setSongArtist(topData.getArtistName());
                                    song.setIsLike(topData.getIsLike());
                                    song.setIsFavourite(topData.getIsFavorite());
                                    song.setIsPlaylist(topData.getIsPlaylist());
                                    song.setFrom(AppConstants.FROM_HOME_TOP);
                                    song.setType(listModel.getType());
                                    song.setPlayList_id(listModel.getPlayListId());
                                    if (topData.getLyrics() != null)
                                        song.setLyrics(topData.getLyrics());
                                    else
                                        song.setLyrics("");
                                    song.setAlbumID(String.valueOf(genreId));
                                    File myFile = new File(AppConstants.DEVICE_PATH + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                                    File imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia");
                                    if (myFile.exists()) {
                                        song.setFrom(AppConstants.FROM_MY_MUSIC);
                                        song.setSongPath(myFile.getPath());
                                        song.setSongImagePath(imagedirect.getPath() + "/" + song.getSongID() + ".jpg");
                                        song.setIsDownload(true);
                                    } else {
                                        song.setFrom(AppConstants.FROM_HOME_TOP);
                                        song.setIsDownload(false);
                                        song.setSongPath(topData.getStreamUrl());
                                        song.setSongImagePath(song.getSongImageUrl());
                                    }
                                    song.setTrackType("1");
                                    song.setThirdparty_song(topData.getThirdparty_song());
                                    song.setLikeCount(topData.getLikeCount());
                                    song.setAlbumID(topData.getAlbum_id());
                                    songList.add(song);
                                }

                            }
                            isLoadmore = false;
                            setDataAdapter();
                            lastPage = data.getList().getLastPage();
                            if (lastPage > 2 && page < 3) {
                                if (isScreenVisible) {
                                    page++;
                                    callTopApi(page, true);
                                }
                            } else {

                            }
                        } else {
                            isLoadmore = false;
                            setDataAdapter();
                        }
                        homeActivity.hideProgressDialog();
                    } else {
                        homeActivity.hideProgressDialog();
                        homeActivity.showSnackBar(getView(), data.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<GetTopPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                T.printStackTrace();
                DebugLog.i("LOG:-> " + "callGenreTrackApi");
                if (T != null && T.getMessage() != null && !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                } else {
                    homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
                }
            }
        });
    }

    public void setDataAdapter() {
        if (rvPlayList != null && txtNodata != null) {
            if (songList != null && !songList.isEmpty()) {
                rvPlayList.setVisibility(View.VISIBLE);
                txtNodata.setVisibility(View.GONE);
            } else {
                rvPlayList.setVisibility(View.GONE);
                txtNodata.setVisibility(View.VISIBLE);
            }

            if (mAdapter != null) {
                mAdapter.refreshAdapter(songList);
            }
        }
    }

    @Override
    public void onLoadMore() {
        if (page < lastPage) {
            isLoadmore = true;
            page++;
            loadData(page, true);
        }
    }

    @Override
    public void changeProgress(String position, int progress) {

    }


    @Override
    public void onFavouriteButtonClicked(int pos, Song song, ImageView iv_like_bottom_sheet) {
        selectedMenuPosition = pos;
        if (isLogin()) {
            if (song.getIsLike()) {
                homeActivity.callUnlikeTrackApi(getLikeTrackParam(), getView(), iv_like_bottom_sheet, songList, pos, this);
            } else {
                homeActivity.callLikeTrackApi(getLikeTrackParam(), getView(), iv_like_bottom_sheet, songList, pos, this);
            }
        }
    }


    @Override
    public void onImageDotClicked(int pos, Song song) {
        selectedMenuPosition = pos;
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        setSongData(pos);
    }

    @Override
    public void onItemClicked(int pos, ArrayList<Song> song) {
        searchView.onActionViewCollapsed();
        searchView.clearFocus();
        homeActivity.playSongWithPostion(pos, song,type);
    }

    public void removeToPlaylist(final String songNumber5, String playlist_ID) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.PLAYLIST_ID, playlist_ID);
        params.put(ApiParameter.TRACK_ID, songNumber5);

        NetworkCall.getInstance().callPlayListApi(AppConstants.REMOVE_SONG_FROM_PLAYLIST, params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                homeActivity.hideProgressDialog();
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
                    homeActivity.showSnackBar(getView(), getString(R.string.song_removed_from_playlist));
                    page = 1;
                    songNumber = songNumber5;
                    if (MediaPlayerService.isInProcess) {
                        if (homeActivity.player != null) {
                            homeActivity.player.updateListAfterRemove(songList, homeActivity, songNumber,type);
                            songNumber = "";
                        }
                    }
                    loadData(page, true);

                } else {
                    DebugLog.e("Status : " + status);
                    homeActivity.showSnackBar(getView(), message);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }

}
