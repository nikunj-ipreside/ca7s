package com.music.ca7s.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.music.ca7s.R;
import com.music.ca7s.TrendingModel;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.homeadapters.SearchAdapter;
import com.music.ca7s.adapter.homeadapters.SearchHistoryAdapter;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseNewCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.listener.RecyclerItemSearchedSongListenerListener;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.mediaplayer.MyDownloadManager;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.OfflineSearchModel;
import com.music.ca7s.model.SearchData;
import com.music.ca7s.model.SearchModel;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class NewSearchFragment extends BaseFragment implements RecyclerItemSearchedSongListenerListener, DownloadSongListener {
    private int DOWNLOAD_CODE = 54;
    private Song downloadSongModel = new Song();
    private TextView txtTopBarTitle;
    private ImageView topBarLeft,topBarRight,iv_close;
    private EditText edtSearch;
    private RecyclerView rv_search,rv_search_history;
    private SearchAdapter mSearchAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private ArrayList<SearchData> mSearchList=new ArrayList<>();
    private ArrayList<OfflineSearchModel> mSearchHistoryList = new ArrayList<>();
    private String title = "";
    private String id ="";
    private DatabaseHandler databaseHandler;
    private Timer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getContext());
        return inflater.inflate(R.layout.fragment_new_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity.setSlidingState(true);
        txtTopBarTitle = view.findViewById(R.id.txtTopbarTitle);
        txtTopBarTitle.setText(getString(R.string.search));
        topBarLeft = view.findViewById(R.id.iv_close_player);
        topBarLeft.setOnClickListener(this);
        topBarRight = view.findViewById(R.id.imgTopbarRight);
        topBarRight.setOnClickListener(this);
        topBarRight.setVisibility(View.GONE);
        topBarLeft.setImageResource(R.drawable.ic_back);
        iv_close = view.findViewById(R.id.iv_close);
        iv_close.setVisibility(View.GONE);
        iv_close.setOnClickListener(this);
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setOnClickListener(this);
        rv_search = view.findViewById(R.id.rv_search);
        rv_search_history = view.findViewById(R.id.rv_search_history);

        setSearchHistoryData();

        mSearchAdapter = new SearchAdapter(mSearchList,homeActivity,this);

        rv_search.setAdapter(mSearchAdapter);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // user is typing: reset already started timer (if existing)
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // user typed: start the timer
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                       new Handler(Looper.getMainLooper()).post(new Runnable() {
                           @Override
                           public void run() {
                               if (editable.toString().trim().length()>0){
                                   iv_close.setVisibility(View.VISIBLE);
                                   if (editable.toString().trim().length() > 1) {
                                       SearchApi(editable.toString());
                                   }
                               }
                               else {
                                   clearList();
                                   iv_close.setVisibility(View.GONE);
                               }
                           }
                       });

                    }
                }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (edtSearch.getText().toString().trim().length()>1){

                        title = edtSearch.getText().toString().trim();
//        }
                        if (title != null && !title.isEmpty()) {
                            OfflineSearchModel offlineSearchModel = new OfflineSearchModel();
                            offlineSearchModel.setId(new Random(323540).nextInt());
                            offlineSearchModel.setName(title);
                            offlineSearchModel.setCreatedAt(Util.getCurrentDateTime());
                            databaseHandler.addOfflinelist(offlineSearchModel);
                        }
                        callTrendingApi("0");
                        Util.hideKeyboard(homeActivity);

//                        edtSearch.setFocusable(true);
//                        edtSearch.setFocusableInTouchMode(true);
//                        String search = edtSearch.getText().toString().trim();
//                        OfflineSearchModel offlineSearchModel = new OfflineSearchModel();
//                        offlineSearchModel.setId(new Random(323540).nextInt());
//                        offlineSearchModel.setName(search);
//                        offlineSearchModel.setCreatedAt(Util.getCurrentDateTime());
//                        databaseHandler.addOfflinelist(offlineSearchModel);
//                        clearList();
//                        setAdapter(mSearchList);
//                        edtSearch.setText("");
//                        homeActivity.openSearchHistoryFragment(search, FragmentState.REPLACE);
////                        homeActivity.openNewSearchHistoryFragment("0",search, FragmentState.REPLACE);
////                        SearchApi(search);
//                        Util.hideKeyboard(homeActivity);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void setSearchHistoryData() {
        mSearchHistoryList = databaseHandler.getAllOfflineSearch();
        Collections.sort(mSearchHistoryList, new Comparator<OfflineSearchModel>() {

            public int compare(OfflineSearchModel s1, OfflineSearchModel s2) {
                String songDate1 = s1.getCreatedAt();
                String songDate2 = s2.getCreatedAt();
                //ascending order
                return songDate2.compareTo(songDate1);
                //descending order
                //return StudentName2.compareTo(StudentName1);
            }});

        mSearchHistoryAdapter = new SearchHistoryAdapter(mSearchHistoryList,homeActivity,this);
        rv_search_history.setAdapter(mSearchHistoryAdapter);
    }


    private void SearchApi(String keyword){
        HashMap<String,String> params = new HashMap<>();
        params.put("keyword",keyword);
        searchData(params);
    }

    private void searchData(HashMap<String, String> params) {
        NetworkCall.getInstance().callSearchSongApi(params,geCooKie(), new iResponseNewCallback<SearchModel>() {
            @Override
            public void sucess(SearchModel data) {
//                DebugLog.e("searchData : " + data.getStatus());

                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    if (data.getData() != null && !data.getData().isEmpty()){
                        mSearchList= new ArrayList<>();
                        mSearchList.clear();
                        for (int i =0 ;i<data.getData().size();i++) {
                            if (mSearchList.size() < 70) {
                                mSearchList.add(data.getData().get(i));
                            }
                        }
                        setDataInList(mSearchList);
                    }
                } else {
                    setDataInList(mSearchList);
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }

            }

            @Override
            public void onFailure(String baseModel) {
                mSearchList= new ArrayList<>();
                mSearchList.clear();
                setDataInList(mSearchList);
                homeActivity.showSnackBar(getView(), baseModel.toString());

            }

            @Override
            public void onError(Call<SearchModel> responseCall, Throwable T) {
                T.printStackTrace();
                mSearchList= new ArrayList<>();
                mSearchList.clear();
                setDataInList(mSearchList);
                if (T.getMessage() != null && !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(),T.getMessage());
                }
                T.printStackTrace();
            }
        });

    }

    private void callTrendingApi(String id){
        HashMap<String,String> params = new HashMap<>();
        params.put("id",id);
        setTrendingApi(params);
    }

    private void setTrendingApi(HashMap<String,String> hashMap) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callSetTrendingApi(hashMap,geCooKie(), new iResponseNewCallback<TrendingModel>() {
            @Override
            public void sucess(TrendingModel data) {
//                DebugLog.e("setTrendingApi : " + data.getStatus());
                homeActivity.hideProgressDialog();
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {

                } else {
//                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
                edtSearch.setText("");
                clearList();
                setAdapter(mSearchList);
                homeActivity.openSearchHistoryFragment(title, FragmentState.REPLACE);
//                homeActivity.openNewSearchHistoryFragment(id,title, FragmentState.REPLACE);

            }

            @Override
            public void onFailure(String baseModel) {
                homeActivity.hideProgressDialog();
                homeActivity.openSearchHistoryFragment(title, FragmentState.REPLACE);
//                homeActivity.openNewSearchHistoryFragment(id,title, FragmentState.REPLACE);
//                homeActivity.showSnackBar(getView(), baseModel.toString());

            }

            @Override
            public void onError(Call<TrendingModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.openSearchHistoryFragment(title, FragmentState.REPLACE);
//                homeActivity.openNewSearchHistoryFragment(id,title, FragmentState.REPLACE);
//                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));

            }
        });

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;

            case R.id.imgTopbarRight:

                break;

            case R.id.iv_close:
                edtSearch.setText("");
                break;

            case R.id.edtSearch:
                edtSearch.setFocusable(true);
                edtSearch.setFocusableInTouchMode(true);
                edtSearch.requestFocus();
                break;
        }
    }

    private void setDataInList(ArrayList<SearchData> mSearchList){
//        clearList();
        setAdapter(mSearchList);
    }

    private void clearList(){
       mSearchList = new ArrayList<>();
       mSearchList.clear();
       setAdapter(mSearchList);
    }

    private void setAdapter(ArrayList<SearchData> mSearchList){
        if (mSearchAdapter != null){
            mSearchAdapter.refreshAdapter(mSearchList);
        }
    }

    @Override
    public void onClickedOnHistoryItem(int pos, OfflineSearchModel song) {
        edtSearch.setText(song.getName());
    }

    @Override
    public void onRemove(int pos, OfflineSearchModel song) {
        databaseHandler.deleteOfflineSearch(song.getName());
        setSearchHistoryData();

    }

    @Override
    public void onClickedonSong(int pos, SearchData song) {
        id = song.id;
//        if (song.getTitle() != null && !song.getTitle().isEmpty()) {
//            title = song.getTitle();
//        }else {
            title = edtSearch.getText().toString().trim();
//        }
        if (title != null && !title.isEmpty()) {
            OfflineSearchModel offlineSearchModel = new OfflineSearchModel();
            offlineSearchModel.setId(Integer.parseInt(song.id));
            offlineSearchModel.setName(title);
            offlineSearchModel.setCreatedAt(Util.getCurrentDateTime());
            databaseHandler.addOfflinelist(offlineSearchModel);
        }
        callTrendingApi(song.id);
        Util.hideKeyboard(homeActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDownloadClick(int pos, SearchData searchData) {
        downloadSongModel.setSongID(searchData.id);
        downloadSongModel.setSongTitle(searchData.songTitle);
        downloadSongModel.setSongAlbum(searchData.songTitle);
        downloadSongModel.setSongURL(searchData.streamUrl);
        downloadSongModel.setSongImageUrl(searchData.imageUrl);
        downloadSongModel.setSongNumber(searchData.id);
        downloadSongModel.setSongPath(searchData.streamUrl);
        downloadSongModel.setSongImagePath(searchData.imageUrl);
        downloadSongModel.setIsLike(false);
        downloadSongModel.setIsFavourite(false);
        downloadSongModel.setDownload(false);
        downloadSongModel.setTrackType(searchData.type);
        downloadSongModel.setAlbumID(searchData.id);
        downloadSongModel.setFrom(ApiParameter.FROM_SEARCH);
        downloadSongModel.setIsPlaylist(false);
        downloadSongModel.setThirdparty_song(searchData.thirdpartySong);
        downloadSongModel.setLikeCount(0);
        downloadSongModel.setCreatedAt(Util.getCurrentDateTime());
        downloadSongModel.setType(searchData.type);
        downloadSongModel.setPlayList_id(searchData.id);
        downloadSongModel.setUser_id(searchData.userId);
        downloadSongModel.setDownloades_song(BigInteger.valueOf(0));
        downloadSongModel.setStremmed_song(BigInteger.valueOf(0));
        if (isReadStorageAllowed(DOWNLOAD_CODE)){
            downloadSong();
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
