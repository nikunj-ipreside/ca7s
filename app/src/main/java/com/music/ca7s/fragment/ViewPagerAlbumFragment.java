package com.music.ca7s.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.homeadapters.FavouriteCollectionAdapter;
import com.music.ca7s.adapter.viewholder.AlbumHolder;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.FavouriteRecylerViewItemClickListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.PlayListModel;
import com.music.ca7s.model.favourite.album.AlbumData;
import com.music.ca7s.model.favourite.album.FavAlbumPojo;
import com.music.ca7s.utils.DebugLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class ViewPagerAlbumFragment extends BaseFragment implements FavouriteRecylerViewItemClickListener {


    Unbinder unbinder;
    @BindView(R.id.rvAlbum)
    RecyclerView rvAlbum;
    int numbOfColumb = 3;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
    private String sCookie;
    private FavouriteCollectionAdapter mFavouriteCollectionAdapter;
    private ArrayList<AlbumData> mAlbumList = new ArrayList<AlbumData>();
    public static ViewPagerAlbumFragment newInstance() {
        Bundle args = new Bundle();
        ViewPagerAlbumFragment fragment = new ViewPagerAlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_v_album, container, false);
//        View view = inflater.inflate(R.layout.row_home_genre, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(true);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        mFavouriteCollectionAdapter = new FavouriteCollectionAdapter(mAlbumList,homeActivity,this);
        rvAlbum.setLayoutManager(new GridLayoutManager(getActivity(), numbOfColumb));
        rvAlbum.setNestedScrollingEnabled(false);
        rvAlbum.setAdapter(mFavouriteCollectionAdapter);

        callFavouriteAlbumApi();
    }

    private void callFavouriteAlbumApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        NetworkCall.getInstance().callFavouriteAlbumApi(params, sCookie, new iResponseCallback<FavAlbumPojo>() {
            @Override
            public void sucess(FavAlbumPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (txtNodata != null && rvAlbum != null) {
                    if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                        mAlbumList = data.getList().getData();
                        txtNodata.setVisibility(View.GONE);
                        rvAlbum.setVisibility(View.VISIBLE);
                        if (mFavouriteCollectionAdapter != null) {
                            mFavouriteCollectionAdapter.refreshAdapter(mAlbumList);
                        }
//                    List<AlbumData> dataList = data.getList().getData();
//                    setAdapter(dataList);
                    } else {
                        txtNodata.setVisibility(View.VISIBLE);
                        rvAlbum.setVisibility(View.GONE);
                        homeActivity.showSnackBar(getView(), data.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                if (txtNodata != null && rvAlbum != null) {
                    homeActivity.hideProgressDialog();
                    txtNodata.setVisibility(View.VISIBLE);
                    rvAlbum.setVisibility(View.GONE);
                    if (baseModel != null)
                        if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                            homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                        }
                }

            }

            @Override
            public void onError(Call<FavAlbumPojo> responseCall, Throwable T) {
                if (txtNodata != null && rvAlbum != null) {
                    homeActivity.hideProgressDialog();
                    txtNodata.setVisibility(View.VISIBLE);
                    rvAlbum.setVisibility(View.GONE);
                    if (T.getMessage() != null) {
                        homeActivity.showSnackBar(getView(), T.getMessage());
                    }
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setAdapter(List<AlbumData> dataList) {
        if (dataList.size() > 0) {
            txtNodata.setVisibility(View.GONE);
            rvAlbum.setVisibility(View.VISIBLE);
            GenericAdapter<AlbumData, AlbumHolder> adapter =
                    new GenericAdapter<AlbumData, AlbumHolder>(R.layout.row_home_genre, AlbumHolder.class, dataList) {
                        @Override
                        public Filter getFilter() {
                            return null;
                        }

                        @Override
                        public void loadMore() {

                        }

                        @Override
                        public void setViewHolderData(AlbumHolder holder, final AlbumData data, int position) {
                            if (HomeActivity.isDataSaverEnabled()){

                            }else {
                                String playlistImage = data.getImageIcon();
                                if (playlistImage.contains("/index.php")){
                                    playlistImage = playlistImage.replace("/index.php","");
                                }
                                if (playlistImage != null && !playlistImage.isEmpty()) {
                                    Glide.with(getActivity()).
                                            load(playlistImage)
                                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                            .into(holder.imgGenrePic);
                                }
                            }

                            holder.txtGenreName.setText(data.getType());

                            holder.llRowGenreRoot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppLevelClass.page = 0;
                                    PlayListModel listModel = new PlayListModel();
                                    listModel.setScreenID(0);
                                    listModel.setGenreID(data.getId());
                                    listModel.setType(AppConstants.FAVOURITE);
                                    listModel.setName(getString(R.string.favourite)+" "+data.getType());
                                    homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);

                                }
                            });

                        }
                    };
            if (rvAlbum != null)
                rvAlbum.setAdapter(adapter);
        } else {
            if (txtNodata != null)
                txtNodata.setVisibility(View.VISIBLE);
            if (txtNodata != null)
                rvAlbum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCollectionClicked(int position, @NotNull AlbumData data) {
        AppLevelClass.page = 0;
        PlayListModel listModel = new PlayListModel();
        listModel.setScreenID(0);
        listModel.setType(AppConstants.FAVOURITE);
        listModel.setName(getString(R.string.favourite)+" "+data.getType());
        listModel.setGenreID(data.getId());
        homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
    }

    @Override
    public void onSongsClicked(int position, @NotNull AlbumData selectedData) {

    }

    @Override
    public void onPlayListCollected(int position, @NotNull AlbumData selectedData) {

    }
}
