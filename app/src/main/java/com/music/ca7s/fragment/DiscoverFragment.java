package com.music.ca7s.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.TrendingModel;
import com.music.ca7s.adapter.generic_adapter.SlidingImageAdapter;
import com.music.ca7s.adapter.homeadapters.CatTopAdapter;
import com.music.ca7s.adapter.homeadapters.NewReleaseAdapter;
import com.music.ca7s.adapter.homeadapters.RisingStarAdapter;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.apicall.iResponseNewCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.RecylerViewItemClickListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.OfflineSearchModel;
import com.music.ca7s.model.PlayListModel;
import com.music.ca7s.model.TagClass;
import com.music.ca7s.model.genre_list.GenreDatum;
import com.music.ca7s.model.genre_list.GenrePojo;
import com.music.ca7s.model.slidemenu.SlideMenuDatum;
import com.music.ca7s.model.slidemenu.SlideMenuPojo;
import com.music.ca7s.model.toplist.TopListDatum;
import com.music.ca7s.model.toplist.TopListPojo;
import com.music.ca7s.tagview.TagView;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;
import com.music.ca7s.utils.connectivity_check.ConnectionStateReceiver;
import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class DiscoverFragment extends BaseFragment implements RecylerViewItemClickListener {
    private ImageView imgTopbarLeft, imgTopbarRight,imgSearchSong;
    private TextView tv_count, txtTopbarTitle;
    private RelativeLayout rv_top, rv_new, rv_rising;
    private LinearLayout ll_banner,ll_trending;
    private TextView tv_browse_ca7, tv_browse_new_release, tv_browse_rising_stars;
    private EditText edtSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_top_genre, rv_new_releases, rv_rising_stars;
    private RecyclerView.LayoutManager top_layout_manager, new_layout_manager, risingLayoutManager;
    private ArrayList<GenreDatum> genreList = new ArrayList<GenreDatum>();
    private ArrayList<GenreDatum> newList = new ArrayList<GenreDatum>();
    private ArrayList<GenreDatum> risingList = new ArrayList<GenreDatum>();
    private ArrayList<TagClass> trendingList = new ArrayList<TagClass>();
    private ArrayList<TagClass> trendingListFull = new ArrayList<TagClass>();
    private CatTopAdapter genreAdapter;
    private NewReleaseAdapter releaseAdapter;
    private RisingStarAdapter risingAdapter;
    private TagView tagGroup;
    private String selectedId = "";
    private String selectedTitle ="",selectedArtist="";
    private int colorAccent= Color.parseColor("#ab0092");
    private int colorWhite = Color.parseColor("#ffffff");

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Timer swipeTimer;
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private DatabaseHandler databaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getContext());
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity.setSlidingState(true);
//        for (int i=0;i<6;i++){
        trendingList = new ArrayList<>();
        trendingListFull = new ArrayList<>();
//        }
        imgSearchSong =  view.findViewById(R.id.imgSearchSong);
         pager = view.findViewById(R.id.pager);
         indicator = view.findViewById(R.id.indicator);
        rv_top = view.findViewById(R.id.rv_top);
        ll_banner = view.findViewById(R.id.ll_banner);
        ll_trending = view.findViewById(R.id.ll_trending);
        rv_new = view.findViewById(R.id.rv_new);
        rv_rising = view.findViewById(R.id.rv_rising);
        tv_browse_ca7 = view.findViewById(R.id.tv_browse_ca7);
        tv_browse_new_release = view.findViewById(R.id.tv_browse_new_release);
        tv_browse_rising_stars = view.findViewById(R.id.tv_browse_rising_stars);
        edtSearch = view.findViewById(R.id.edtSearch);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        rv_top_genre = view.findViewById(R.id.rv_top_genre);
        rv_new_releases = view.findViewById(R.id.rv_new_releases);
        rv_rising_stars = view.findViewById(R.id.rv_rising_stars);
        //TagView
        tagGroup = (TagView) view.findViewById(R.id.tag_group);
        rv_top.setOnClickListener(this);
        rv_new.setOnClickListener(this);
        rv_rising.setOnClickListener(this);
        tv_browse_ca7.setOnClickListener(this);
        tv_browse_new_release.setOnClickListener(this);
        tv_browse_rising_stars.setOnClickListener(this);
        edtSearch.setOnClickListener(this);
        imgSearchSong.setOnClickListener(this);

        //TextView
        tv_count = view.findViewById(R.id.tv_count);
        txtTopbarTitle = view.findViewById(R.id.txtTopbarTitle);
        txtTopbarTitle.setText(getString(R.string.discover));
        //ImageView
        imgTopbarLeft = view.findViewById(R.id.iv_close_player);
        imgTopbarRight = view.findViewById(R.id.imgTopbarRight);
        imgTopbarLeft.setOnClickListener(this);
        imgTopbarRight.setOnClickListener(this);

//        initializeMusicService();

        top_layout_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        new_layout_manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        risingLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rv_top_genre.setLayoutManager(top_layout_manager);
        rv_new_releases.setLayoutManager(new_layout_manager);
        rv_rising_stars.setLayoutManager(risingLayoutManager);

        genreAdapter = new CatTopAdapter(genreList, getContext(), this);
        rv_top_genre.setAdapter(genreAdapter);

        releaseAdapter = new NewReleaseAdapter(newList, getContext(), this);
        rv_new_releases.setAdapter(releaseAdapter);

        risingAdapter = new RisingStarAdapter(risingList, getContext(), this);
        rv_rising_stars.setAdapter(risingAdapter);

        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(TagClass tag, int position, List<TagClass> mTags) {
                if (tag.getName().toString().equals(getString(R.string.more))){
                    setAllTags(trendingListFull);
                }else if (tag.getName().toString().equals(getString(R.string.less))){
                    setAllTags(trendingList);
                }else {
                    selectedId = String.valueOf(tag.getSongID());
                    selectedTitle = tag.getName();
                    edtSearch.setText(tag.getName());
                    edtSearch.setSelection(tag.getName().length());//to set cursor position
                }

            }
        });
//
//        llBottomBar.setOnTouchListener(new OnSwipeTouchListener(homeActivity) {
//            public void onSwipeTop() {
//                Toast.makeText(homeActivity, "top", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeRight() {
//                Toast.makeText(homeActivity, "right", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeLeft() {
//                Toast.makeText(homeActivity, "left", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeBottom() {
//                Toast.makeText(homeActivity, "bottom", Toast.LENGTH_SHORT).show();
//            }
//
//            public boolean onTouch(View v, MotionEvent event) {
//                return v.onTouchEvent(event);
//            }
//        });



//        trendingAdapter =new TrendingAdapter(trendingList,getContext(),this);
//        rv_trending.setAdapter(trendingAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectionStateReceiver.isConnected(getActivity())) {
                    callGenreListApi();
                    callgetTrendingApi();
                } else {
                    homeActivity.showSnackBar(getView(), getString(R.string.network_error_message));
                }
            }
        });

        if (ConnectionStateReceiver.isConnected(getActivity())) {
            callGenreListApi();
            callgetTrendingApi();
            callFollowListApi();
            if (!AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.IS_UPDATE).equalsIgnoreCase(AppConstants.sFalse)) {
                String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
                if (!user_id.isEmpty()) {
                    callSlideMenuApi();
                }
//                homeActivity.setNavigationDrawer();
            }
        } else {
            homeActivity.showSnackBar(getView(), getString(R.string.network_error_message));
        }


    }


    private void setAdapter(final List<TopListDatum> listData) {

        if(getContext() != null) {
            if (listData != null && !listData.isEmpty()){
                setupViewPager(listData);
                ll_banner.setVisibility(View.VISIBLE);
            }else {
                ll_banner.setVisibility(View.GONE);
            }

        }

    }

    private void setupViewPager(final List<TopListDatum> listData){
        pager.setAdapter(new SlidingImageAdapter(getContext(),listData));
        indicator.setViewPager(pager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = listData.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                if(pager != null) {
                    pager.setCurrentItem(currentPage++, true);
                }
            }
        };

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 5000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
//
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listData.get(currentPage).getUrl()));
//            startActivity(browserIntent);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }

        });
    }

    private void setAllTags(ArrayList<TagClass> trendingList) {
        if (tagGroup != null) {
            tagGroup.removeAll();
            tagGroup.addTags(trendingList);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTopCa7Clicked(int position, @NotNull GenreDatum selectedData) {
        if (position == 0) {
//            AppLevelClass.isFromNowPlaing = false;
            AppLevelClass.page = 0;
            PlayListModel listModel = new PlayListModel();
            listModel.setScreenID(0);
            listModel.setType(AppConstants.TOP);
            listModel.setName(getString(R.string.top_ca7s));
            listModel.setGenre_name(getString(R.string.top_ca7s));
            listModel.setGenreID(0);//change
            homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
        } else {
//            AppLevelClass.isFromNowPlaing = false;
            AppLevelClass.page = 0;
            PlayListModel listModel = new PlayListModel();
            listModel.setScreenID(1);
            listModel.setType(AppConstants.TOP_GENRE);
            listModel.setName(getString(R.string.top)+" "+selectedData.getType());
            listModel.setGenre_name(selectedData.getType());
            listModel.setImage(selectedData.getImageIcon());
            listModel.setGenreID(selectedData.getId());//change
            homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
        }
    }

    @Override
    public void onNewRelase(int position, @NotNull GenreDatum selectedData) {
        if (position == 0) {
//            AppLevelClass.isFromNowPlaing = false;
            AppLevelClass.page = 0;
            PlayListModel listModel = new PlayListModel();
            listModel.setScreenID(0);
            listModel.setType(AppConstants.NEW);
            listModel.setName(getString(R.string.new_releases));
            listModel.setGenreID(0);//change
            listModel.setGenre_name(getString(R.string.new_releases));
            homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
        } else {
            PlayListModel listModel = new PlayListModel();
            listModel.setScreenID(1);
            listModel.setType(AppConstants.NEW_GENRE);
            listModel.setName(getString(R.string.new_releases)+" "+selectedData.getType());
            listModel.setGenreID(selectedData.getId());//change
            listModel.setGenre_name(selectedData.getType());
            listModel.setImage(selectedData.getImageIcon());
            homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
        }

    }

    @Override
    public void onRisingStar(int position, @NotNull GenreDatum selectedData) {
        if (position == 0) {
//            AppLevelClass.isFromNowPlaing = false;
            AppLevelClass.page = 0;
            PlayListModel listModel = new PlayListModel();
            listModel.setScreenID(0);
            listModel.setType(AppConstants.RISING);
            listModel.setName(getString(R.string.rising_stars));
            listModel.setGenreID(0);//change
            listModel.setGenre_name((getString(R.string.rising_stars)));
            homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
        } else {
            PlayListModel listModel = new PlayListModel();
            listModel.setScreenID(1);
            listModel.setType(AppConstants.RISING_GENRE);
            listModel.setName(getString(R.string.rising_stars)+" "+selectedData.getType());
            listModel.setGenreID(selectedData.getId());//change
            listModel.setGenre_name(selectedData.getType());
            listModel.setImage(selectedData.getImageIcon());
            listModel.setEn(selectedData.getEn());
            listModel.setPt(selectedData.getPt());
            homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
        }
    }

    public void callSlideMenuApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.USER_TOKEN, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.FCMTOKEN));

        NetworkCall.getInstance().callSlideMenuApi(params, geCooKie(), new iResponseCallback<SlideMenuPojo>() {
            @Override
            public void sucess(SlideMenuPojo data) {

//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<SlideMenuDatum> slideMenuData = data.getData();

                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.FULL_NAME, slideMenuData.get(0).getFullName().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_NAME, slideMenuData.get(0).getUserName().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_CITY, slideMenuData.get(0).getUserCity().toString());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.PROFILE_PICTURE, slideMenuData.get(0).getProfilePicture().toString());
                    AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.LANGUAGE, data.getLanguage());
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.BaseCount, data.getBaseCount());
//                    count = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.BaseCount);
//                    if (!count.equals("") && !count.equals("0")) {
////                        tv_count.setVisibility(View.VISIBLE);
////                        tv_count.setText(count);
//                    } else {
////                        tv_count.setVisibility(View.GONE);
//                    }
//                    openSearchFragment(FragmentState.REPLACE);
                } else {
//                    showSnackBar(drawerLayout, data.getMessage());
//                    homeActivity.logOut();
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {

                if(baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<SlideMenuPojo> responseCall, Throwable T) {
                if (T.getMessage() != null && !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }
                T.printStackTrace();
            }
        });
    }

    private void callFollowListApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.PAGE, "1");

        NetworkCall.getInstance().callTopListApi(params, geCooKie(), new iResponseCallback<TopListPojo>() {
            @Override
            public void sucess(TopListPojo data) {
                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
//                DebugLog.e("Status : " + data.getStatus());

                Log.d("data",""+data.getData());

                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<TopListDatum> listData = data.getData();

//                    Log.d("FollowData : ",""+new Gson().toJson(listData));

                    setAdapter(listData);

                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<TopListPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }


    private void callGenreListApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callGenreListApi(geCooKie(), new iResponseCallback<GenrePojo>() {
            @Override
            public void sucess(GenrePojo data) {
                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    genreList = new ArrayList<GenreDatum>();
                    GenreDatum genreDatum = new GenreDatum();
                    genreDatum.setId(0);
                    genreDatum.setImageIcon("");
                    genreDatum.setType(homeActivity.getString(R.string.top));
                    genreList.add(genreDatum);
                    genreList.addAll(data.getData());

                    if (genreAdapter != null) {
                        genreAdapter.refreshAdapter(genreList);
                    }

                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if(baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<GenrePojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

        NetworkCall.getInstance().callnewReleaseGenreListApi(geCooKie(), new iResponseCallback<GenrePojo>() {
            @Override
            public void sucess(GenrePojo data) {
                swipeRefreshLayout.setRefreshing(false);
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    newList = new ArrayList<GenreDatum>();
                    GenreDatum genreDatum1 = new GenreDatum();
                    genreDatum1.setId(0);
                    genreDatum1.setImageIcon("");
                    genreDatum1.setType(homeActivity.getString(R.string.new_releases));
                    newList.add(genreDatum1);
                    newList.addAll(data.getData());
                    if (releaseAdapter != null) {
                        releaseAdapter.refreshAdapter(newList);
                    }
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<GenrePojo> responseCall, Throwable T) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

        NetworkCall.getInstance().callgetRisingStarApi(geCooKie(), new iResponseCallback<GenrePojo>() {
            @Override
            public void sucess(GenrePojo data) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
//                DebugLog.e("getRisingSta : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    risingList = new ArrayList<GenreDatum>();
                    GenreDatum genreDatum2 = new GenreDatum();
                    genreDatum2.setId(0);
                    genreDatum2.setImageIcon("");
                    genreDatum2.setType(homeActivity.getString(R.string.rising_stars));
                    risingList.add(genreDatum2);
                    risingList.addAll(data.getData());
                    if (risingAdapter != null) {
                        risingAdapter.refreshAdapter(risingList);
                    }

                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<GenrePojo> responseCall, Throwable T) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });



    }

    private void callgetTrendingApi() {
//        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callAllTrendingApi(geCooKie(), new iResponseNewCallback<TrendingModel>() {
            @Override
            public void sucess(TrendingModel data) {
                swipeRefreshLayout.setRefreshing(false);
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                  trendingList = new ArrayList<>();
                  trendingListFull = new ArrayList<>();
                  for (int i = 0;i<data.getData().size();i++){
                      if (i<=5){
                          if (i<=4){
                              trendingList.add(new TagClass(R.drawable.drawable_text_round_background, data.getData().get(i).getSongTitle(), colorAccent,data.getData().get(i).getId()));
                          }else {
                              trendingList.add(new TagClass(R.drawable.drawable_text_round_background_filled, homeActivity.getString(R.string.more), colorWhite,0));
                          }
                      }
                      trendingListFull.add(new TagClass(R.drawable.drawable_text_round_background, data.getData().get(i).getSongTitle(), colorAccent,data.getData().get(i).getId()));
                  }
                    trendingListFull.add(new TagClass(R.drawable.drawable_text_round_background_filled, homeActivity.getString(R.string.less), colorWhite,0));
                  if (data.getData() != null && !data.getData().isEmpty()){
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              setAllTags(trendingList);
                          }
                      },500);

                      ll_trending.setVisibility(View.VISIBLE);
                  }
                  else {
                      ll_trending.setVisibility(View.GONE);
                      trendingList = new ArrayList<>();
                      trendingList.clear();
                      setAllTags(trendingList);
                  }
                } else {
                    ll_trending.setVisibility(View.GONE);
                    trendingList = new ArrayList<>();
                    trendingList.clear();
                    setAllTags(trendingList);
//                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(String baseModel) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                ll_trending.setVisibility(View.GONE);
                trendingList = new ArrayList<>();
                trendingList.clear();
                setAllTags(trendingList);
//                homeActivity.showSnackBar(getView(), baseModel.toString());

            }

            @Override
            public void onError(Call<TrendingModel> responseCall, Throwable T) {
//                homeActivity.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                ll_trending.setVisibility(View.GONE);
                trendingList = new ArrayList<>();
                trendingList.clear();
                setAllTags(trendingList);
//                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rbHome:
                homeActivity.setFragment(new DiscoverFragment(), FragmentState.REPLACE);
                break;
            case R.id.rbBroadCast:
                homeActivity.setFragment(new SearchFragment(), FragmentState.REPLACE);
                break;
            case R.id.rbMyMusic:
                homeActivity.setFragment(new MyMusicFragment(), FragmentState.REPLACE);
                break;

            case R.id.rbFavourite:
                homeActivity.setFragment(new FavouriteFragment(), FragmentState.REPLACE);
                break;

            case R.id.iv_close_player:
                homeActivity.openDrawer();
                break;
            case R.id.tv_browse_ca7:
                AppLevelClass.SCREENTYPE = getString(R.string.top_ca7s);
                AppLevelClass.BROWSELIST = genreList;
                homeActivity.setFragment(new BrowseAllFragment(), FragmentState.REPLACE);
                break;

            case R.id.tv_browse_new_release:
                AppLevelClass.SCREENTYPE = getString(R.string.new_releases);
                AppLevelClass.BROWSELIST = newList;
                homeActivity.setFragment(new BrowseAllFragment(), FragmentState.REPLACE);
                break;

            case R.id.tv_browse_rising_stars:
                AppLevelClass.SCREENTYPE = getString(R.string.rising_stars);
                AppLevelClass.BROWSELIST = risingList;
                homeActivity.setFragment(new BrowseAllFragment(), FragmentState.REPLACE);
                break;

            case R.id.imgTopbarRight:
                homeActivity.setFragment(NotificationFragment.newInstance(false), FragmentState.REPLACE);
                break;

            case R.id.edtSearch:
                homeActivity.setFragment(new NewSearchFragment(), FragmentState.REPLACE);
                break;

            case R.id.imgSearchSong:
                if (edtSearch.getText().toString().trim().isEmpty()){
                    homeActivity.showSnackBar(getView(),getString(R.string.field_should_not_empty));
                }
                else {
                    OfflineSearchModel offlineSearchModel = new OfflineSearchModel();
                    offlineSearchModel.setId(Integer.parseInt(selectedId));
                    offlineSearchModel.setName(selectedTitle);
                    offlineSearchModel.setCreatedAt(Util.getCurrentDateTime());
                    databaseHandler.addOfflinelist(offlineSearchModel);
                    edtSearch.setText("");
                    callTrendingApi(selectedId);
//                    homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);
                }
                break;
//            case R.id.ll_insta:
//                openOnWeb(homeActivity, AppConstants.INSTAGRAM_URL);
//                break;
//
//            case R.id.ll_facebook:
//                openOnWeb(homeActivity,AppConstants.FACEBOOK_URL);
//                break;
        }
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
                homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);

            }

            @Override
            public void onFailure(String baseModel) {
                homeActivity.hideProgressDialog();
                homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);
//                homeActivity.showSnackBar(getView(), baseModel.toString());

            }

            @Override
            public void onError(Call<TrendingModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.openNewSearchHistoryFragment(selectedId,selectedTitle, FragmentState.REPLACE);
//                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));

            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
