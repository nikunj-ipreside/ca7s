package com.music.ca7s.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.OnLoadMoreListener;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.viewholder.SongsHolder;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.listener.RecyclerItemListener;
import com.music.ca7s.mediaplayer.MyDownloadManager;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.favourite.song.FavSongPojo;
import com.music.ca7s.model.favourite.song.FavoriteList;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.DebugLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class ViewPagerSongsFragment extends BaseFragment implements OnLoadMoreListener, DownloadSongListener, RecyclerItemListener {
    Unbinder unbinder;
    @BindView(R.id.rvSongs)
    RecyclerView rvSongs;
    private String sCookie;
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Song> TempsongList = new ArrayList<>();
    private static final int STORAGE_PERMISSION_CODE = 1;
    private long mLastClickTime = 0;
    private int selectedMenuPosition =0;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
//    public boolean isLoadmore = false;

    int page = 1;
    private ListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private OnLoadMoreListener onLoadMoreListener;
    protected Handler handler;
    GenericAdapter<Song, SongsHolder> adapter;

    private ImageView iv_cover,iv_close_sheet,iv_remove,iv_playlist,iv_share,iv_like,iv_favourite;
    private LinearLayout ll_remove,ll_addtoplaylist,ll_sharesong,bottom_sheet_downloaded,ll_like,ll_favourite;
    private BottomSheetBehavior downloaded_menu_sheet_behaviour;
    private Song mSelectedSong = null;
    private TextView tv_songname,tv_songartist,tv_remove,tv_addplaylist,tv_share;

    public static ViewPagerSongsFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerSongsFragment fragment = new ViewPagerSongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_v_songs, container, false);
//        View view = inflater.inflate(R.layout.row_songs, container, false);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_like = view.findViewById(R.id.ll_like);
        ll_favourite = view.findViewById(R.id.ll_favourite);
        iv_like = view.findViewById(R.id.iv_like);
        iv_favourite = view.findViewById(R.id.iv_favourite);
        tv_songname = view.findViewById(R.id.tv_songname);
        tv_songartist = view.findViewById(R.id.tv_songartist);
        tv_remove = view.findViewById(R.id.tv_remove);
        tv_addplaylist = view.findViewById(R.id.tv_addplaylist);
        iv_cover = view.findViewById(R.id.iv_cover);
        iv_close_sheet = view.findViewById(R.id.iv_close_sheet);
        iv_remove = view.findViewById(R.id.iv_remove);
        iv_playlist = view.findViewById(R.id.iv_playlist);
        iv_share = view.findViewById(R.id.iv_share);
        tv_share = view.findViewById(R.id.tv_share);
        ll_remove = view.findViewById(R.id.ll_remove);
        ll_addtoplaylist = view.findViewById(R.id.ll_addtoplaylist);
        ll_sharesong = view.findViewById(R.id.ll_sharesong);
        bottom_sheet_downloaded = view.findViewById(R.id.bottom_sheet_downloaded);
        bottom_sheet_downloaded.setOnClickListener(this);
        iv_close_sheet.setOnClickListener(this);
        ll_remove.setOnClickListener(this);
        ll_addtoplaylist.setOnClickListener(this);
        ll_sharesong.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        ll_favourite.setOnClickListener(this);
        tv_addplaylist.setText(getString(R.string.add_to_playlist));
        iv_remove.setImageResource(R.drawable.download_gray);
        tv_remove.setText(getString(R.string.download_song));

        ll_like.setVisibility(View.VISIBLE);
        ll_favourite.setVisibility(View.VISIBLE);


        downloaded_menu_sheet_behaviour = BottomSheetBehavior.from(bottom_sheet_downloaded);
        downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        downloaded_menu_sheet_behaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
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

    @Override
    public void onDownloadSuccess() {
        homeActivity.onDownloadSuccess();
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
    public void onListUpdated(ArrayList<Song> songs) {
        songList = songs;
        mAdapter.addAll(songList);
        if (rvSongs != null) {
            loadData(page, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                downloadSong();
            } else {
                Toast.makeText(getActivity(), getString(R.string.to_download_song_you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadSong() {
        String externalUrl="";
        if (!songList.get(selectedMenuPosition).getThirdparty_song()){
            externalUrl = ApiParameter.BASE_MUSIC_URL;
        }
        Uri music_uri = Uri.parse(externalUrl+songList.get(selectedMenuPosition).getSongURL());
        MyDownloadManager downloadManager = new MyDownloadManager(getActivity(), this);
        downloadManager.DownloadData(music_uri, songList.get(selectedMenuPosition));
    }



    public HashMap<String, String> getShareParam() {
        HashMap<String, String> params = new HashMap<>();
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        Log.d("user_id",user_id);
        if(!user_id.isEmpty()) {
            params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        }else{
            params.put(ApiParameter.USER_ID,"0");
        }
        params.put(ApiParameter.TITLE, songList.get(selectedMenuPosition).getSongTitle());
       /* if (!songList.get(selectedMenuPosition).getLyrics().equals(""))
            params.put(ApiParameter.MUSIC_DESCRIPTION, songList.get(selectedMenuPosition).getLyrics());
        else*/
        params.put(ApiParameter.MUSIC_DESCRIPTION, mSelectedSong.getSongArtist());
        String imageUrl = songList.get(selectedMenuPosition).getSongImageUrl();
        if (imageUrl.contains("/index.php")) {
            imageUrl = imageUrl.replace("/index.php", "");
        }
        params.put(ApiParameter.MUSIC_THUMBNAIL, imageUrl);
        params.put(ApiParameter.MUSIC_URL, songList.get(selectedMenuPosition).getSongURL());
        params.put(ApiParameter.TRACH_ID, songList.get(selectedMenuPosition).getSongID());
        params.put(ApiParameter.THIRD_PARTY,songList.get(selectedMenuPosition).getThirdparty_song().toString());

        Log.d("params",""+params);

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
        }
        else {
            params.put(ApiParameter.ALBUM_ID, "1");
        }
        return params;
    }


    private void setSongData(final int id) {
        final Song song = mSelectedSong;
        if (song != null) {
            DebugLog.e("Song URL : " + song.getSongURL());
            if (tv_songname != null)
                tv_songname.setText(song.getSongTitle());
            if (tv_songartist != null)
                tv_songartist.setText(song.getSongArtist());
            if (iv_favourite != null) {
                if (song.getIsFavourite()) {
                    iv_favourite.setImageResource(R.drawable.heart_theme_filled);
                }else {
                    iv_favourite.setImageResource(R.drawable.heart_theme_unfilled);
                }
            }
            if (iv_like != null) {
                if (song.getIsLike()){
                    iv_like.setImageResource(R.drawable.favourite_theme_filled);
                }else {
                    iv_like.setImageResource(R.drawable.favorite_theme_unfilled);
                }
            }
            if (iv_playlist != null) {
                if(song.getIsPlaylist()){
                    iv_playlist.setImageResource(R.drawable.ic_playlist_add_theme);
                }else {
                    iv_playlist.setImageResource(R.drawable.plus_gray);
                }
            }
            File myFile = new File(AppConstants.DEVICE_PATH  + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION+".nomedia");

            if (myFile.exists()) {
                Log.i("TAG", "myfile-> " + myFile.getAbsolutePath());
                if (iv_remove != null) {
                    iv_remove.setImageResource(R.drawable.ic_done_theme);
                }
            } else {
                if (iv_remove != null) {
                    iv_remove.setImageResource(R.drawable.export_download_gray);
                }

            }
            if (iv_cover != null)
                if (HomeActivity.isDataSaverEnabled()){
                    Glide.with(getActivity())
                            .load(R.drawable.ic_top_placeholder)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .error(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(iv_cover);
                }else {
                    String playlistImage =song.getSongImageUrl();
                    if (playlistImage.contains("/index.php")){
                        playlistImage = playlistImage.replace("/index.php","");
                    }
                    if (playlistImage != null && !playlistImage.isEmpty()) {
                        Glide.with(getActivity())
                                .load(song.getSongImageUrl())
                                .placeholder(R.drawable.ic_top_placeholder)
                                .error(R.drawable.ic_top_placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                .into(iv_cover);
                    }
                }

            if (ll_like != null && ll_favourite != null){
                if (song.getFrom().toString().equals(AppConstants.FROM_MY_MUSIC) || song.getThirdparty_song()) {
                    ll_like.setVisibility(View.GONE);
                    ll_favourite.setVisibility(View.GONE);
                }else {
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
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_close_sheet :
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;

            case R.id.ll_remove:
                        if (checkMultipleClick()) {
                            downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                            File myFile = new File(AppConstants.DEVICE_PATH + mSelectedSong.getSongID() +AppConstants.TEMPRARY_MUSIC_EXTENTION+".nomedia");
                            if (myFile.exists()) {
                                Toast.makeText(getActivity(), R.string.this_song_is_already_downloaded, Toast.LENGTH_LONG).show();
                                iv_remove.setImageResource(R.drawable.ic_done_theme);
                            } else {
                                if (iv_remove != null) {
                                    iv_remove.setImageResource(R.drawable.ic_done_theme);
                                    if (isReadStorageAllowed()) {
                                        Toast.makeText(getActivity(), R.string.this_song_is_downloading, Toast.LENGTH_LONG).show();
                                        downloadSong();
                                    }
                                }
                            }

                        }
                break;

            case  R.id.ll_favourite:
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);

                if(isLogin()){
                    if (songList.get(selectedMenuPosition).getIsFavourite()) {
                        homeActivity.callRemoveFavouriteTrackApi1(getAddFavouriteParam(), getView(), iv_favourite,songList,selectedMenuPosition,this);

                    } else {
                        homeActivity.callAddFavouriteTrackApi1(getAddFavouriteParam(), getView(), iv_favourite,songList,selectedMenuPosition,this);
                    }
                }
                break;

            case R.id.ll_like:
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                if(isLogin()){
                    if (songList.get(selectedMenuPosition).getIsLike()) {
                        homeActivity.callUnlikeTrackApi(getLikeTrackParam(), getView(), iv_like, songList,selectedMenuPosition,this);
                    } else {
                        homeActivity.callLikeTrackApi(getLikeTrackParam(), getView(), iv_like,songList,selectedMenuPosition,this);


                    }
                }
                break;


            case R.id.ll_addtoplaylist:
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                if(isLogin()){
                    homeActivity.openDialog(iv_playlist,songList.get(selectedMenuPosition).getSongID());
//                  homeActivity.openCreatePlayListDialog(iv_playlist,songList.get(selectedMenuPosition).getSongID());
//                    homeActivity.callAddToPlaylistApi1(getAddToPlaylistParam(), getView(), iv_like,songList,selectedMenuPosition,this);
                }
                break;

            case R.id.ll_sharesong:
                homeActivity.callShareTrackApi(getShareParam(), songList.get(selectedMenuPosition), getView());
                if (downloaded_menu_sheet_behaviour.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;

            case R.id.bottom_sheet_downloaded:
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(true);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        handler = new Handler();
        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvSongs.setLayoutManager(lLayout);

        mAdapter = new ListAdapter(ViewPagerSongsFragment.this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvSongs.setLayoutManager(mLayoutManager);
        rvSongs.setHasFixedSize(true);


        rvSongs.setAdapter(mAdapter);
        rvSongs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (mAdapter.getItemCount() - 2)) {
//                    mAdapter.showLoading();
//                }
            }
        });


    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TempsongList.clear();
        page = 1;
//        isLoadmore = false;
        loadData(page, false);
    }

    private void loadData(int pageno, boolean isSilent) {
        callFavouriteSongApi(pageno, isSilent);
    }

    private void callFavouriteSongApi(final int pageno, boolean isSilent) {
//        if (!isSilent)
//            homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        Log.i("TAG", "page :-> " + page);
        params.put(ApiParameter.PAGE, String.valueOf(pageno));
        NetworkCall.getInstance().callFavouriteSongApi(params, sCookie, new iResponseCallback<FavSongPojo>() {
            @Override
            public void sucess(FavSongPojo data) {
//                homeActivity.hideProgressDialog();
                DebugLog.e("data songs : " + " "+new Gson().toJson(data));
                try {
                    if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                        if (data.getList() != null && data.getList().getFavoriteList() != null && !data.getList().getFavoriteList().isEmpty()) {
                            if (pageno == 1) {
                                songList = new ArrayList<>();
                            }
                            List<FavoriteList> dataList = data.getList().getFavoriteList();
                            if (dataList != null && dataList.size() > 0) {
                                for (int i = 0; i < dataList.size(); i++) {
                                    FavoriteList topData = dataList.get(i);
                                    Song song = new Song();
                                    song.setUser_id(String.valueOf(topData.getUserId()));
                                    song.setSongID(topData.getId());
                                    song.setSongTitle(topData.getTitle());
                                    song.setSongURL(topData.getStreamUrl());
                                    song.setSongNumber(topData.getId());
                                    song.setSongImageUrl(topData.getImageUrl());
                                    song.setSongArtist(topData.getArtistName());
                                    song.setIsLike(topData.getIsLike());
                                    song.setThirdparty_song(topData.getThirdparty_song());
                                    song.setIsFavourite(topData.getIsFavorite());
                                    song.setFrom(AppConstants.FROM_FAVOURITE_SONGS);
//                        song.setAlbumID("");
                                    song.setTrackType("1");
                                    song.setLikeCount(topData.getLikeCount());
                                    song.setAlbumID(topData.getId());
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
                                    songList.add(song);
                                    TempsongList.add(song);
                                    mAdapter.addAll(songList);
                                }
                                setAdapterData();
//                            if (isLoadmore) {
//                                if (rvSongs != null) {
//                                    mAdapter.dismissLoading();
//                                    mAdapter.addItemMore(songList);
//                                    mAdapter.setMore(true);
//                                }
//                            } else {
//                                if (rvSongs != null)
//                                    rvSongs.setVisibility(View.VISIBLE);
//                                if (txtNodata != null)
//                                    txtNodata.setVisibility(View.GONE);
//
//
//                            }


                            } else {
                                setAdapterData();
//                            if (!isLoadmore) {
//                                if (rvSongs != null)
//                                    // rvPlayList.setVisibility(View.GONE);
//                                    if (txtNodata != null)
//                                        txtNodata.setVisibility(View.GONE);
//                                mAdapter.setMore(false);
//                            } else {
//                                mAdapter.dismissLoading();
//                                page--;
//                                mAdapter.setMore(true);
////                                homeActivity.showSnackBar(getView(), getResources().getString(R.string.no_more_data));
//                            }
                            }
                        } else {
                            setAdapterData();
                        }
                    } else {
                        if (data.getMessage() != null) {
                            homeActivity.showSnackBar(getView(), data.getMessage());
                        }
                        setAdapterData();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
//                homeActivity.hideProgressDialog();
                Log.e("Base model ",baseModel.getMessage()+"");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

                setAdapterData();

//                if (baseModel != null)
//                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<FavSongPojo> responseCall, Throwable T) {
//                homeActivity.hideProgressDialog();
                if (T.getMessage() != null){
                    homeActivity.showSnackBar(getView(),T.getMessage());
                }

                setAdapterData();

                T.printStackTrace();

//                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });


    }

    private void setAdapterData() {
        if (songList != null && !songList.isEmpty()){
            if (rvSongs != null)
                rvSongs.setVisibility(View.VISIBLE);
            if (txtNodata != null)
                txtNodata.setVisibility(View.GONE);
        }
        else {
            if (rvSongs != null)
                rvSongs.setVisibility(View.GONE);
            if (txtNodata != null)
                txtNodata.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onLoadMore() {
//        isLoadmore = true;
        page++;
        loadData(page, true);
    }


    @Override
    public void changeProgress(String position, int progress) {

    }

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;

        private ArrayList<Song> itemList;

        private OnLoadMoreListener onLoadMoreListener;
        private boolean isMoreLoading = true;


        public ListAdapter(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
            itemList = new ArrayList<>();
        }

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == VIEW_ITEM) {
                return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_playlist, parent, false));
//            }
//            else {
//                return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false));
//            }
        }

        public void showLoading() {
            if (isMoreLoading && itemList != null && onLoadMoreListener != null) {
                isMoreLoading = false;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        itemList.add(null);
                        notifyItemInserted(itemList.size() - 1);
                        onLoadMoreListener.onLoadMore();
                    }
                });
            }
        }

        public void setMore(boolean isMore) {
            this.isMoreLoading = isMore;
        }

        public void dismissLoading() {
            if (itemList != null && itemList.size() > 0) {
                itemList.remove(itemList.size() - 1);
                notifyItemRemoved(itemList.size());
            }
        }

        public void addAll(ArrayList<Song> lst) {
           itemList = lst;
            notifyDataSetChanged();
        }

        public void addItemMore(List<Song> lst) {
            int sizeInit = itemList.size();
            itemList.addAll(lst);
            notifyItemRangeChanged(sizeInit, itemList.size());
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            switch (holder.getItemViewType()) {
                case 1:
                    StudentViewHolder offerHolder = (StudentViewHolder) holder;
                    bindStudentViewHolder(offerHolder, position);
                    break;
                default:
                    break;
            }

        }

        public Song getItem(int position) {

            return itemList.get(position);
        }

        public void bindStudentViewHolder(final StudentViewHolder holder, final int position) {
            final Song data = getItem(position);
            if (HomeActivity.isDataSaverEnabled()){
                Glide.with(getActivity()).
                        load(R.drawable.ic_top_placeholder)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .error(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                        .into(holder.imgVPSongs);
            }else {
                String playlistImage = data.getSongImageUrl();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity()).
                            load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .error(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(holder.imgVPSongs);
                }
            }
//            holder.txtSongNumber.setText(String.valueOf(position + 1));

            holder.txtSongName.setText(data.getSongTitle());
            holder.txtSongContent.setText(data.getSongArtist());
            holder.imgDot.setVisibility(View.VISIBLE);
            holder.llRowRoot.setId(position);

            holder.imgDot.setId(position);

            holder.llRowRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    homeActivity.playSongWithPostion(id,itemList,AppConstants.SONG_ALBUM);
                }
            });

            holder.imgDot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedPosition = view.getId();
                    mSelectedSong = itemList.get(selectedPosition);
                    setSongData(selectedPosition);
                    downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class StudentViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_song_image)
            public ImageView imgVPSongs;
            @BindView(R.id.tv_song_name)
            public TextView txtSongName;
            @BindView(R.id.txtSongContent)
            public TextView txtSongContent;
            @BindView(R.id.imgDot)
            public ImageView imgDot;
            @BindView(R.id.llRowRoot)
            public LinearLayout llRowRoot;


            public StudentViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        public class ProgressViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar pBar;

            public ProgressViewHolder(View v) {
                super(v);
                pBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            }
        }
    }

}
