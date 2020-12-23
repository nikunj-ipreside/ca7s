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
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseNewCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.listener.RecyclerItemSearchedSongListenerListener;
import com.music.ca7s.mediaplayer.MyDownloadManager;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.SearchData;
import com.music.ca7s.model.SearchModel;
import com.music.ca7s.model.newsearchedata.SearchedByID;
import com.music.ca7s.model.newsearchedata.SearchedDataByID;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class NewSearchHistoryFragment extends BaseFragment implements  OnLoadMoreListener , DownloadSongListener {
    private int DOWNLOAD_CODE = 54;
    private Song downloadSongModel = new Song();
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
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    private String sCookie;

    private ArrayList<SearchedDataByID> mSearchList=new ArrayList<>();

    private ArrayList<Song> songList = new ArrayList<>();
    List<HistoryData> historyListData;
    int page = 1;
    int totalPage;
    int currentPage;
    String searchName="";
    private String id="0";

    private long mLastClickTime = 0;
    public ListAdapter lAdapter;
    public boolean isLoadmore = false;

    public void setSearchName(String id, String searchName1) {
        AppLevelClass.page = 0;
        this.searchName = searchName1;
        this.id = id;
    }

    public static NewSearchHistoryFragment newInstance(String id,String searchName) {
        NewSearchHistoryFragment fragment = new NewSearchHistoryFragment();
        fragment.setSearchName(id,searchName);
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
            lAdapter = new ListAdapter(NewSearchHistoryFragment.this);
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
//                        lAdapter.showLoading();
                    }
                }
            });

        }

    }

    private void loadData(int pageno, boolean isSilent) {
        SearchApi();
    }

    private void SearchApi(){
        HashMap<String,String> params = new HashMap<>();
//        params.put("id",id);
        params.put("id","0");
        params.put("third_part_title",searchName);
        searchData(params);
    }

    private void searchData(HashMap<String, String> params) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callSearchSongBYIDApi(params,geCooKie(), new iResponseNewCallback<SearchedByID>() {
            @Override
            public void sucess(SearchedByID data) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("searchData : " + new Gson().toJson(data));
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    if (data.getData() != null && !data.getData().isEmpty()){
                        mSearchList= new ArrayList<>();
                        mSearchList.clear();
                        mSearchList = data.getData();
                        songList = new ArrayList<>();

                        if (mSearchList != null && !mSearchList.isEmpty()){
                            for (int i=0;i<mSearchList.size();i++){
                                   SearchedDataByID trackData = mSearchList.get(i);
                                    Song song = new Song();
                                    song.setUser_id(String.valueOf(trackData.getUserId()));
                                    song.setSongID(trackData.getId() + "");
                                    song.setSongTitle(trackData.getSongTitle());
                                    song.setSongAlbum(trackData.getAlbumName());
                                    song.setSongURL(trackData.getStreamUrl());
                                    song.setSongNumber((i + 1) + "");
                                    song.setSongImageUrl(trackData.getImageUrl());
                                File myFile = new File(AppConstants.DEVICE_PATH  + song.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION+".nomedia");
                                File  imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia");
                                if (myFile.exists()) {
                                    song.setFrom(AppConstants.FROM_MY_MUSIC);
                                    song.setSongPath(myFile.getPath());
                                    song.setSongImagePath(imagedirect.getPath() + "/" + song.getSongID() + ".jpg");
                                    song.setIsDownload(true);
                                } else {
                                    song.setFrom(AppConstants.FROM_HOME_TOP);
                                    song.setIsDownload(false);
                                    song.setSongPath(trackData.getStreamUrl());
                                    song.setSongImagePath(song.getSongImageUrl());
                                }
                                    song.setThirdparty_song(trackData.isThirdpartySong());
                                    if (trackData.getSongTitle() != null)
                                        song.setSongArtist(trackData.getSongTitle());
                                    else
                                        song.setSongArtist("");
                                        song.setLyrics("");
                                    song.setFrom(AppConstants.FROM_SEARCH_RESULT);
                                    song.setAlbumID(String.valueOf(trackData.getGenreId()));
                                    if (songList.size() < 90) {
                                        songList.add(song);
                                    }
                                }
                                setDataInList(songList);

                            }
                        }

                } else {
                    songList = new ArrayList<>();
                    setDataInList(songList);
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.sorry_song_not_found));
                }

            }

            @Override
            public void onFailure(String baseModel) {
                homeActivity.hideProgressDialog();
                songList= new ArrayList<>();
                songList.clear();
                setDataInList(songList);
                homeActivity.showSnackBar(getView(),homeActivity.getString(R.string.sorry_song_not_found));

            }

            @Override
            public void onError(Call<SearchedByID> responseCall, Throwable T) {
                songList= new ArrayList<>();
                songList.clear();
                setDataInList(songList);
                if (T.getMessage() != null && !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }
                homeActivity.hideProgressDialog();
            }
        });

    }

    private void setDataInList(ArrayList<Song> mSearchList){
//        clearList();
        setAdapter(mSearchList);
    }

    private void clearList(){
        mSearchList = new ArrayList<>();
        mSearchList.clear();
    }

    private void setAdapter(ArrayList<Song> mSearchList){
        if (rvSearchHistory != null && tv_no_data != null) {
            if (lAdapter != null) {
                if (mSearchList != null && !mSearchList.isEmpty()) {
                    lAdapter.addAll(mSearchList);
                    rvSearchHistory.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                } else {
                    rvSearchHistory.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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




    @Override
    public void onResume() {
        super.onResume();
            txtTopbarTitle.setText(R.string.search_result);
            page = AppLevelClass.page;
            loadData(page, false);
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
                return new ListAdapter.StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_histoty, parent, false));
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
                String playlistImage = data.getSongImageUrl();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
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
            holder.llRowRoot.setId(position);

            holder.llRowRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    homeActivity.playSongWithPostion(id,songList,AppConstants.FROM_SEARCH_RESULT);
                }
            });

            holder.iv_download.setId(position);

            holder.iv_download.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    downloadSongModel = getItem(id);
                    if (isReadStorageAllowed(DOWNLOAD_CODE)){
                        downloadSong();
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
