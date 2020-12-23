package com.music.ca7s.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.adapter.homeadapters.BrowseAllAdapter;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.RecylerViewItemClickListener;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.PlayListModel;
import com.music.ca7s.model.genre_list.GenreDatum;
import com.music.ca7s.model.genre_list.GenrePojo;
import com.music.ca7s.utils.DebugLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;

public class BrowseAllFragment extends BaseFragment implements RecylerViewItemClickListener {
    private ImageView imgTopbarLeft, imgTopbarRight;
    private TextView tv_count, txtTopbarTitle,tv_data_not_found;

    //    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_browse_all;
    private RecyclerView.LayoutManager top_layout_manager;
    private ArrayList<GenreDatum> genreList = new ArrayList<GenreDatum>();
    private BrowseAllAdapter browseAllAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity.setSlidingState(true);
        rv_browse_all = view.findViewById(R.id.rv_bowse_all);
        tv_data_not_found = view.findViewById(R.id.tv_data_not_found);
        //TextView
        tv_count = view.findViewById(R.id.tv_count);
        txtTopbarTitle = view.findViewById(R.id.txtTopbarTitle);
        txtTopbarTitle.setText(AppLevelClass.SCREENTYPE);
        //ImageView
        imgTopbarLeft = view.findViewById(R.id.iv_close_player);
        imgTopbarRight = view.findViewById(R.id.imgTopbarRight);
        imgTopbarRight.setVisibility(View.GONE);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        imgTopbarLeft.setOnClickListener(this);
        imgTopbarRight.setOnClickListener(this);

        genreList = AppLevelClass.BROWSELIST;
//        genreList.remove(0);
//        Log.e("GenreList : ",new Gson().toJson(genreList)+"\n"+new Gson().toJson(AppLevelClass.BROWSELIST));
        top_layout_manager = new GridLayoutManager(getContext(), 3);
        rv_browse_all.setLayoutManager(top_layout_manager);
        browseAllAdapter = new BrowseAllAdapter(genreList, getContext(),AppLevelClass.SCREENTYPE, this);
        rv_browse_all.setAdapter(browseAllAdapter);

        if (genreList != null && !genreList.isEmpty()) {
            tv_data_not_found.setVisibility(View.GONE);
            rv_browse_all.setVisibility(View.VISIBLE);
        }
        else {
            tv_data_not_found.setVisibility(View.VISIBLE);
            rv_browse_all.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTopCa7Clicked(int position, @NotNull GenreDatum selectedData) {

    }

    @Override
    public void onNewRelase(int position, @NotNull GenreDatum selectedData) {

    }

    @Override
    public void onRisingStar(int position, @NotNull GenreDatum selectedData) {
//        AppLevelClass.isFromNowPlaing = false;
        AppLevelClass.page = 0;
        PlayListModel listModel = new PlayListModel();
        if (AppLevelClass.SCREENTYPE.equals(getString(R.string.top_ca7s))) {

            if (position == 0){
                listModel.setScreenID(0);
                listModel.setType(AppConstants.TOP);
                listModel.setName(getString(R.string.top_ca7s));
                listModel.setGenre_name(getString(R.string.top_ca7s));
                listModel.setGenreID(0);//change
            }else {
                listModel.setScreenID(1);
                listModel.setType(AppConstants.TOP_GENRE);
                listModel.setName(getString(R.string.top) + " " + selectedData.getType());
                listModel.setGenre_name(selectedData.getType());
                listModel.setImage(selectedData.getImageIcon());
                listModel.setGenreID(selectedData.getId());//change
            }
        }
        else if (AppLevelClass.SCREENTYPE.equals(getString(R.string.new_releases))){
            if (position == 0){
                listModel.setScreenID(0);
                listModel.setType(AppConstants.NEW);
                listModel.setName(getString(R.string.new_releases));
                listModel.setGenreID(0);//change
                listModel.setGenre_name(getString(R.string.new_releases));
            }else {
                listModel.setScreenID(1);
                listModel.setType(AppConstants.NEW_GENRE);
                listModel.setName(getString(R.string.new_releases) + " " + selectedData.getType());
                listModel.setGenreID(selectedData.getId());//change
                listModel.setImage(selectedData.getImageIcon());
                listModel.setGenre_name(selectedData.getType());
            }
        }else if (AppLevelClass.SCREENTYPE.equals(getString(R.string.rising_stars))){
            if (position == 0){
                listModel.setScreenID(0);
                listModel.setType(AppConstants.RISING);
                listModel.setName(getString(R.string.rising_stars));
                listModel.setGenreID(0);//change
                listModel.setGenre_name((getString(R.string.rising_stars)));
            }else {
                listModel.setScreenID(1);
                listModel.setType(AppConstants.RISING_GENRE);
                listModel.setName(getString(R.string.rising_stars) + " " + selectedData.getType());
                listModel.setImage(selectedData.getImageIcon());
                listModel.setGenreID(selectedData.getId());//change
                listModel.setGenre_name(selectedData.getType());
            }
        }
        homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
    }

    private void callGenreListApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callGenreListApi(geCooKie(), new iResponseCallback<GenrePojo>() {
            @Override
            public void sucess(GenrePojo data) {
                homeActivity.hideProgressDialog();
//                swipeRefreshLayout.setRefreshing(false);
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    genreList = new ArrayList<GenreDatum>();
                    genreList.addAll(data.getData());
                    if (browseAllAdapter != null) {
                        browseAllAdapter.refreshAdapter(genreList);
                    }
                    rv_browse_all.setVisibility(View.VISIBLE);
                    tv_data_not_found.setVisibility(View.GONE);
                } else {
                    rv_browse_all.setVisibility(View.GONE);
                    tv_data_not_found.setVisibility(View.VISIBLE);
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                rv_browse_all.setVisibility(View.GONE);
                tv_data_not_found.setVisibility(View.VISIBLE);
//                swipeRefreshLayout.setRefreshing(false);
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }

            }

            @Override
            public void onError(Call<GenrePojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                rv_browse_all.setVisibility(View.GONE);
                tv_data_not_found.setVisibility(View.VISIBLE);
//                swipeRefreshLayout.setRefreshing(false);
                if (T.getMessage() != null & !T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }
                T.printStackTrace();
            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;

            case R.id.imgTopbarRight:
                homeActivity.setFragment(new NotificationFragment(), FragmentState.REPLACE);
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
