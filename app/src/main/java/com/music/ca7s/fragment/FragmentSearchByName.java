package com.music.ca7s.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.newsearchedata.SearchedByID;
import com.music.ca7s.model.newsearchedata.SearchedDataByID;
import com.music.ca7s.model.search_song.HistoryData;
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

public class FragmentSearchByName extends BaseFragment implements  OnLoadMoreListener {
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

    public static FragmentSearchByName newInstance(String id, String searchName) {
        FragmentSearchByName fragment = new FragmentSearchByName();
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
            lAdapter = new ListAdapter(FragmentSearchByName.this);
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

    }

    private void loadData(int pageno, boolean isSilent) {
        SearchApi();
    }

    private void SearchApi(){
        HashMap<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("third_part_title",searchName);
        searchData(params);
    }

    private void searchData(HashMap<String, String> params) {
        NetworkCall.getInstance().callSearchSongBYIDApi(params,geCooKie(), new iResponseNewCallback<SearchedByID>() {
            @Override
            public void sucess(SearchedByID data) {
                DebugLog.e("searchData : " + new Gson().toJson(data));
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
                                    songList.add(song);
                                }
                                setDataInList(songList);

                            }
                        }

                } else {
                    songList = new ArrayList<>();
                    setDataInList(songList);
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }

            }

            @Override
            public void onFailure(String baseModel) {
                songList= new ArrayList<>();
                songList.clear();
                setDataInList(songList);
                homeActivity.showSnackBar(getView(), baseModel.toString());

            }

            @Override
            public void onError(Call<SearchedByID> responseCall, Throwable T) {
                songList= new ArrayList<>();
                songList.clear();
                setDataInList(songList);
                if (T.getMessage() != null && !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }
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
