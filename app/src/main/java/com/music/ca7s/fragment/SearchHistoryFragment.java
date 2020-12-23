package com.music.ca7s.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.OnLoadMoreListener;
import com.music.ca7s.R;
import com.music.ca7s.TrendingModel;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.HistoryRecyclerItemTouchHelper;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.viewholder.SearchHistoryHolder;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.apicall.iResponseNewCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.mediaplayer.MyDownloadManager;
import com.music.ca7s.mediaplayer.StorageUtil;
import com.music.ca7s.model.history_new_model.HistoryNewModel;
import com.music.ca7s.model.history_new_model.HistoryNewModelData;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.search_song.HistoryData;
import com.music.ca7s.model.search_song.SearchHistoryPojo;
import com.music.ca7s.model.search_song.international.InternationalData;
import com.music.ca7s.model.search_song.international.InternationalPojo;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.DebugLog;

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

public class SearchHistoryFragment extends BaseFragment implements  HistoryRecyclerItemTouchHelper.RecyclerItemTouchHelperListener, OnLoadMoreListener, DownloadSongListener {
    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.rvSearchHistory)
    RecyclerView rvSearchHistory;
    @BindView(R.id.iv_progress)
    ProgressBar iv_progress;
    private String sCookie;

    private Song downloadSongModel = new Song();
    private int DOWNLOAD_CODE = 54;

    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Song> tempSongList = new ArrayList<>();
    List<HistoryData> historyListData;
    int page = 1;
    int totalPage;
    int currentPage;
    GenericAdapter<Song, SearchHistoryHolder> adapter;

    private String searchName;

    private HistoryAdapter mAdapter;

    private long mLastClickTime = 0;
    public ListAdapter lAdapter;
    public boolean isLoadmore = false;

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public static SearchHistoryFragment newInstance(String searchName) {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        fragment.setSearchName(searchName);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(true);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        imgTopbarRight.setVisibility(View.GONE);
        homeActivity.lockDrawer();
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvSearchHistory.setLayoutManager(lLayout);
        if (searchName != null) {
            lAdapter = new ListAdapter(SearchHistoryFragment.this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvSearchHistory.setLayoutManager(mLayoutManager);
            rvSearchHistory.setHasFixedSize(true);
            rvSearchHistory.setAdapter(lAdapter);
            rvSearchHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (lAdapter.getItemCount() - 2)) {
                        lAdapter.showLoading();
                    }
                }
            });

        }

        //DebugLog.e("onResume");
        if (searchName == null) {
            txtTopbarTitle.setText(R.string.history);
            callSearchHistoryApi(page);
        } else {
            txtTopbarTitle.setText(R.string.search_result);
            page = AppLevelClass.page;
            loadData(page, true);

        }

    }

    private void loadData(int pageno, boolean isSilent) {
//        if (!AppLevelClass.isFromNowPlaing) {
            callSearchInternaltionalApi(pageno, isSilent);
//        } else {
//            AppLevelClass.isFromNowPlaing = false;
//            Additem();
//        }
    }

    public void Additem() {
        lAdapter.addAll(songList);


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof HistoryAdapter.MyViewHolder) {
            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }

    private void callSearchHistoryApi(int pagination) {
        iv_progress.setVisibility(View.VISIBLE);
//        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();

//        if(AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID) == null){
//            params.put(ApiParameter.USER_ID,"1");
//        }else {
            params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
//        }

        params.put(ApiParameter.PAGE, String.valueOf(pagination));

        NetworkCall.getInstance().callSearchHistoryApi(params, sCookie, new iResponseCallback<SearchHistoryPojo>() {
            @Override
            public void sucess(SearchHistoryPojo data) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
//                    historyListData = data.getList().getData();
//                    totalPage = data.getList().getLastPage();
//                    currentPage = data.getList().getCurrentPage();
                    tempSongList = new ArrayList<>();
                    List<HistoryData> historyData = data.getList().getData();
                    for (int i = 0; i < historyData.size(); i++) {
                        HistoryData trackData = historyData.get(i);
                        Song song = new Song();
                        song.setUser_id(String.valueOf(trackData.getUserId()));
                        song.setSongID(trackData.getId() + "");
                        song.setSongTitle(trackData.getSerachText());
                        song.setSongURL(trackData.getStream_url());
                        song.setSongNumber((i + 1) + "");
                        song.setSongImageUrl(trackData.getImageUrl());
                        if (trackData.getArtistName() != null)
                            song.setSongArtist(trackData.getArtistName());
                        else
                            song.setSongArtist("");
                        if (trackData.getLyrics() != null)
                            song.setLyrics(trackData.getLyrics());
                        else
                            song.setLyrics("");
//                        song.setIsLike(trackData.getIsLike());
//                        song.setIsFavourite(trackData.getIsFavorite());
                        song.setFrom(AppConstants.FROM_SEARCH_RESULT);
                        song.setAlbumID(trackData.getTrackId());
                        // song.setIsDownload(topData.getis());
//                        song.setAlbumID(listModel.getPlayListID());
//                        song.setTrackType("1");
//                        song.setLikeCount(trackData.getLikeCount());
                        songList.add(song);

                    }

                    setAdapter(data.getList().getData(), songList);
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<SearchHistoryPojo> responseCall, Throwable T) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }


    private void callSearchInternaltionalApi(int pageno, boolean isSilent) {
        iv_progress.setVisibility(View.VISIBLE);
//        if (!isSilent)
//            homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        if(user_id.isEmpty()){
            params.put(ApiParameter.USER_ID, "1");
        }else {

            params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        }
        params.put(ApiParameter.SEARCH_TEXT, searchName);
        params.put(ApiParameter.PAGE, String.valueOf(pageno));
        params.put(ApiParameter.PER_PAGE, "10");

        Log.d("search",searchName);

        NetworkCall.getInstance().callSearchInternationalApi(params, sCookie, new iResponseCallback<HistoryNewModel>() {
            @Override
            public void sucess(HistoryNewModel data) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                Log.d("data",""+new Gson().toJson(data));
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    try{
                    List<HistoryNewModelData> dataList = data.getList().getList();
                    tempSongList = new ArrayList<>();
                    if (dataList != null && dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {
                            HistoryNewModelData trackData = dataList.get(i);
                            Song song = new Song();
                            song.setUser_id(String.valueOf(trackData.getUserId()));
                            song.setSongID(trackData.getId() + "");
                            song.setSongTitle(trackData.getTitle());
                            song.setSongAlbum(trackData.getAlbumName());
                            song.setSongURL(trackData.getStreamUrl());
                            song.setSongNumber(trackData.getId() + "");
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
                                song.setSongPath(trackData.getStreamUrl());
                                song.setSongImagePath(trackData.getArtworkUrl());
                            }
                            if (trackData.getArtworkUrl() != null && !trackData.getArtworkUrl().isEmpty()) {
                                song.setSongImageUrl(trackData.getArtworkUrl());
                            }else {
                                if (trackData.getImageUrl() != null && !trackData.getImageUrl().isEmpty()) {
                                    song.setSongImageUrl(trackData.getImageUrl());
                                }else {

                                }
                            }
                            song.setAlbumID(trackData.getId());
                            song.setThirdparty_song(trackData.getThirdparty_song());
                            if (trackData.getArtistName() != null)
                                song.setSongArtist(trackData.getArtistName());
                            else {
                                    song.setSongArtist(trackData.getTitle());
                            }
                            if (trackData.getLyrics() != null)
                                song.setLyrics(trackData.getLyrics());
                            else
                                song.setLyrics("");

                            if (i == 0) {
                                callTrendingApi(song.getSongID());
                            }

                            tempSongList.add(song);
                            songList.add(song);
                        }
                        if (isLoadmore) {
                            lAdapter.dismissLoading();
                            lAdapter.addItemMore(tempSongList);
                            lAdapter.setMore(true);
                        } else {
                            rvSearchHistory.setVisibility(View.VISIBLE);
                            lAdapter.addAll(tempSongList);

                        }
                    } else {
                        if (!isLoadmore) {
                            rvSearchHistory.setVisibility(View.GONE);
                            //txtNodata.setVisibility(View.VISIBLE);
                        } else {
                            lAdapter.dismissLoading();
                            page--;
                            lAdapter.setMore(true);
                            homeActivity.showSnackBar(getView(), getResources().getString(R.string.no_more_data));
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                    if (songList.size() > 0) {
                        lAdapter.dismissLoading();
                        page--;
                        lAdapter.setMore(true);
                    } else {
                        homeActivity.onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

//                homeActivity.onBackPressed();

            }

            @Override
            public void onError(Call<HistoryNewModel> responseCall, Throwable T) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
//                homeActivity.onBackPressed();

                Log.d("error","" +T);

            }
        });
    }

    private void callTrendingApi(String id){
        HashMap<String,String> params = new HashMap<>();
        params.put("id",id);
        setTrendingApi(params);
    }

    private void setTrendingApi(HashMap<String,String> hashMap) {
//        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callSetTrendingApi(hashMap,geCooKie(), new iResponseNewCallback<TrendingModel>() {
            @Override
            public void sucess(TrendingModel data) {
                DebugLog.e("setTrendingApi : " + data.getStatus());
//                homeActivity.hideProgressDialog();
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {

                } else {
//                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
//                homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);

            }

            @Override
            public void onFailure(String baseModel) {
//                homeActivity.hideProgressDialog();
//                homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);
//                homeActivity.showSnackBar(getView(), baseModel.toString());

            }

            @Override
            public void onError(Call<TrendingModel> responseCall, Throwable T) {
//                homeActivity.hideProgressDialog();
//                homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);
//                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setAdapter(final List<HistoryData> historyData, final List<Song> songList) {
        // new edited
        mAdapter = new HistoryAdapter(getActivity(), songList);
        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvSearchHistory.setLayoutManager(lLayout);
        rvSearchHistory.setAdapter(mAdapter);
        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new HistoryRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        ///new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSearchHistory);
    }

    @OnClick(R.id.iv_close_player)
    public void onViewClicked() {
        homeActivity.onBackPressed();

    }

    @Override
    public void onLoadMore() {
        isLoadmore = true;
        page++;
        loadData(page, true);
    }


    /*********Adapter***********/
    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
        private Context context;
        public List<Song> SongListData;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout viewBackground, viewForeground;
            public TextView txtTitle;
            public ImageView imgLogo;

            public MyViewHolder(View view) {
                super(view);
                txtTitle = view.findViewById(R.id.txtTitle);
                viewBackground = view.findViewById(R.id.view_background);
                viewForeground = view.findViewById(R.id.view_foreground);
                imgLogo = view.findViewById(R.id.imgLogo);
            }
        }


        public HistoryAdapter(Context context, List<Song> dataList) {
            this.context = context;
            this.SongListData = dataList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_notification, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.txtTitle.setText(SongListData.get(position).getSongTitle());
            if (HomeActivity.isDataSaverEnabled()){

            }else {
                String playlistImage = SongListData.get(position).getSongImageUrl();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity())
                            .load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(holder.imgLogo);
                }
            }

        }

        @Override
        public int getItemCount() {
            return SongListData.size();
        }

        public void removeItem(int position) {
            callSingleHistoryApi(SongListData.get(position).getSongID().toString(), position);
//            mAdapter.SongListData.remove(position);
//            mAdapter.notifyItemRemoved(position);
        }

    }

    private void callSingleHistoryApi(String strSongID, final int position) {
        iv_progress.setVisibility(View.VISIBLE);
//        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.HISTORY_ID, strSongID);

        NetworkCall.getInstance().callDeleteSingleHistoryApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    mAdapter.SongListData.remove(position);
                    mAdapter.notifyItemRemoved(position);
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                iv_progress.setVisibility(View.GONE);
//                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }


    private boolean checkMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
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
            if (viewType == VIEW_ITEM) {
                return new ListAdapter.StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_playlist, parent, false));
            } else {
                return new ListAdapter.ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false));
            }
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

        public void addAll(List<Song> lst) {
            itemList.clear();
            itemList.addAll(lst);
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
                    ListAdapter.StudentViewHolder offerHolder = (ListAdapter.StudentViewHolder) holder;
                    bindStudentViewHolder(offerHolder, position);
                    break;
                default:
                    break;
            }

        }

        public Song getItem(int position) {

            return itemList.get(position);
        }

        public void bindStudentViewHolder(final ListAdapter.StudentViewHolder holder, final int position) {
            final Song data = getItem(position);
            if (HomeActivity.isDataSaverEnabled()){

            }else {
                String playlistImage ="";
                if (data.getSongImageUrl() != null && !data.getSongImageUrl().isEmpty()){
                    playlistImage = data.getSongImageUrl();
                }else {
                    playlistImage = data.getImage_url();
                }
//                if (playlistImage.contains("/index.php")){
//                    playlistImage = playlistImage.replace("/index.php","");
//                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity()).
                            load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(holder.imgPlayList);
                }
            }

            holder.txtPlayListName.setText(data.getSongTitle());
            holder.txtPlayListContent.setText(data.getSongArtist());

            if (data.getFrom().equalsIgnoreCase(AppConstants.FROM_FAVOURITE_PLAYLIST)) {
                holder.imgDot.setVisibility(View.GONE);
            }
            holder.imgDot.setVisibility(View.GONE);
            holder.llRowRoot.setId(position);

            holder.llRowRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    homeActivity.playSongWithPostion(id,songList,AppConstants.FROM_SEARCH_RESULT);
                }
            });
            holder.iv_download.setVisibility(View.VISIBLE);
            if (getItem(position).getIsDownload()){
                holder.iv_download.setImageResource(R.drawable.ic_done_theme);
            }else {
                holder.iv_download.setImageResource(R.drawable.ic_download_theme_color);
            }
            holder.iv_download.setId(position);
            holder.iv_download.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    downloadSongModel = getItem(id);
                    if (!downloadSongModel.getIsDownload()) {
                        if (isReadStorageAllowed(DOWNLOAD_CODE)) {
                            downloadSong();
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class StudentViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_song_image)
            public ImageView imgPlayList;
            @BindView(R.id.tv_song_name)
            public TextView txtPlayListName;
            @BindView(R.id.txtSongContent)
            public TextView txtPlayListContent;
            @BindView(R.id.imgDot)
            public ImageView imgDot;
            @BindView(R.id.iv_download)
            public ImageView iv_download;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isReadStorageAllowed(int pickImageRequestCode) {
        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(homeActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(homeActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, pickImageRequestCode);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == DOWNLOAD_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                downloadSong();
            } else {
                Toast.makeText(homeActivity, getString(R.string.to_download_song_you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void downloadSong() {
        String externalUrl = "";
        if (!downloadSongModel.getThirdparty_song()) {
            externalUrl = ApiParameter.BASE_MUSIC_URL;
        }
        Uri music_uri = Uri.parse(externalUrl + downloadSongModel.getSongURL());
        MyDownloadManager downloadManager = new MyDownloadManager(homeActivity, this);
        downloadManager.DownloadData(music_uri, downloadSongModel);
    }

    @Override
    public void onDownloadSuccess() {
        homeActivity.onDownloadSuccess();
    }

    @Override
    public void changeProgress(String position, int progress) {

    }
}
