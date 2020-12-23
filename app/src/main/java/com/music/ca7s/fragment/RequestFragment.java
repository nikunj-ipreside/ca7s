package com.music.ca7s.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.viewholder.RequestHolder;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.request_list.RequestData;
import com.music.ca7s.model.request_list.RequestPojo;
import com.music.ca7s.utils.DebugLog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;

public class RequestFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.rvRequest)
    RecyclerView rvRequest;
    private String sCookie;


    public static RequestFragment newInstance() {

        Bundle args = new Bundle();

        RequestFragment fragment = new RequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        unbinder = ButterKnife.bind(this, view);
        imgTopbarRight.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.lockDrawer();
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        txtTopbarTitle.setText(R.string.request_list);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);


        callRequestListApi();
        LinearLayoutManager lLayout = new LinearLayoutManager(getContext());
        rvRequest.setLayoutManager(lLayout);

    }

    private void callRequestListApi() {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        NetworkCall.getInstance().callgetRequestListApi(params, sCookie, new iResponseCallback<RequestPojo>() {
            @Override
            public void sucess(RequestPojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<RequestData> dataList = data.getList().getData();

                    setAdapter(dataList);


                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
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
            public void onError(Call<RequestPojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setAdapter(final List<RequestData> dataList) {
        GenericAdapter<RequestData, RequestHolder> adapter =
                new GenericAdapter<RequestData, RequestHolder>(R.layout.row_request_list, RequestHolder.class, dataList) {
                    @Override
                    public Filter getFilter() {
                        return null;
                    }

                    @Override
                    public void loadMore() {

                    }

                    @Override
                    public void setViewHolderData(RequestHolder holder, final RequestData data, int position) {

                        String playlistImage = data.getProfilePicture();
                        if (playlistImage.contains("/index.php")){
                            playlistImage = playlistImage.replace("/index.php","");
                        }
                        if (playlistImage != null && !playlistImage.isEmpty()) {
                            Glide.with(getActivity())
                                    .asBitmap()
                                    .load(playlistImage)
                                    .placeholder(R.drawable.ic_top_placeholder)
                                    .transform(new CircleCrop())
                                    .into(holder.imgRequestPic);
//                            CropCircleTransformation(getActivity())
                        }

                        holder.txtName.setText(data.getFullName());

                        holder.txtAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callRequestAcceptApi(data.getFollowId());
                            }
                        });


                        holder.txtDeclain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callRequestRejectApi(data.getFollowId());

                            }
                        });


                    }
                };

        rvRequest.setAdapter(adapter);
    }


    private void callRequestAcceptApi(Integer followId) {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, followId.toString());

        NetworkCall.getInstance().callgetRequestAcceptApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    callRequestListApi();
                 //   homeActivity.showSnackBar(getView(), data.getMessage());
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());

            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }


    private void callRequestRejectApi(Integer followId) {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, followId.toString());

        NetworkCall.getInstance().callgetRequestRejectApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    callRequestListApi();
              //      homeActivity.showSnackBar(getView(), data.getMessage());
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
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
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }

    @OnClick(R.id.iv_close_player)
    public void onViewClicked() {
        homeActivity.onBackPressed();
    }
}
