package com.music.ca7s.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.genericbottomsheet.GenericBottomModel;
import com.music.ca7s.genericbottomsheet.GenericBottomSheetDialog;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.profile.ViewProfileDatum;
import com.music.ca7s.model.profile.ViewProfilePojo;
import com.music.ca7s.utils.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;

public class ViewProfileFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.imgShowProfile)
    CircleImageView imgShowProfile;
    @BindView(R.id.txtShowProName)
    TextView txtShowProName;
    @BindView(R.id.txtShowProCity)
    TextView txtShowProCity;
    @BindView(R.id.txtShowEmail)
    TextView txtShowEmail;
    @BindView(R.id.txtShowProNumber)
    TextView txtShowProNumber;
    @BindView(R.id.txtShowCity)
    TextView txtShowCity;
    @BindView(R.id.txtShowProGender)
    TextView txtShowProGender;
    @BindView(R.id.txtShowProBirthdate)
    TextView txtShowProBirthdate;
    @BindView(R.id.btnfollow)
    Button btnfollow;
    @BindView(R.id.btnUnfollow)
    Button btnUnfollow;
    @BindView(R.id.llFollowBtns)
    LinearLayout llFollowBtns;
    private String sCookie;


    String sUserId, status = "";

    public void setsUserId(String sUserId) {
        this.sUserId = sUserId;
    }

    public static ViewProfileFragment newInstance(String sUserId) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setsUserId(sUserId);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        txtTopbarTitle.setText(R.string.profile);
        imgTopbarRight.setVisibility(View.GONE);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        homeActivity.lockDrawer();
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        callViewProfileApi();

        if (sUserId.equalsIgnoreCase(AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID)))
            llFollowBtns.setVisibility(View.GONE);

    }

    private void callViewProfileApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.VIEW_ID, sUserId);

        NetworkCall.getInstance().callgetViewProfilePicApi(params, sCookie, new iResponseCallback<ViewProfilePojo>() {
            @Override
            public void sucess(ViewProfilePojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    List<ViewProfileDatum> dataList = data.getData();

//                    if (HomeActivity.isDataSaverEnabled()){
//
//                    }else {
                    String playlistImage = dataList.get(0).getProfilePicture();
                    if (playlistImage.contains("/index.php")){
                        playlistImage = playlistImage.replace("/index.php","");
                    }
                    if (playlistImage != null && !playlistImage.isEmpty()) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .load(playlistImage)
                                .placeholder(R.drawable.ic_top_placeholder)
                                .transform(new CircleCrop())
                                .into(imgShowProfile);
//                        CropCircleTransformation(getActivity())
                    }
//                    }

                    txtShowProName.setText(dataList.get(0).getFullName());
                    txtShowEmail.setText(dataList.get(0).getEmail());
                    if (dataList.get(0).getUserGender().equalsIgnoreCase("male"))
                        txtShowProGender.setText(getString(R.string.gender_) + " " + getString(R.string.male));
                    if (dataList.get(0).getUserGender().equalsIgnoreCase("female"))
                        txtShowProGender.setText(getString(R.string.gender_) + getString(R.string.female));
                    if (!dataList.get(0).getMobileNumber().isEmpty()) {
                        txtShowProNumber.setText(dataList.get(0).getMobileNumber());
                    }

                    if (!dataList.get(0).getUserCity().isEmpty()) {
                        txtShowProCity.setText(dataList.get(0).getUserCity());
                        txtShowCity.setText(dataList.get(0).getUserCity());
                    }

                    if (!dataList.get(0).getBirthDate().isEmpty()) {
                        txtShowProBirthdate.setText(dataList.get(0).getBirthDate());
                    }
                    status = dataList.get(0).getUserStatus();
                    if (!dataList.get(0).getUserStatus().isEmpty() && dataList.get(0).getUserStatus().equalsIgnoreCase("request")) {
                        btnfollow.setText(R.string.request);
                        btnUnfollow.setVisibility(View.GONE);
                    } else if (dataList.get(0).getUserStatus().equalsIgnoreCase("following")) {
                        btnUnfollow.setText(R.string.unfollow);
                        btnfollow.setVisibility(View.GONE);
                    } else if (dataList.get(0).getUserStatus().equalsIgnoreCase("Follow")) {
                        btnfollow.setText(R.string.follow);
                        btnUnfollow.setVisibility(View.GONE);
                    } else {

                    }


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
            public void onError(Call<ViewProfilePojo> responseCall, Throwable T) {
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


    @OnClick({R.id.iv_close_player, R.id.btnfollow, R.id.btnUnfollow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.btnfollow:
                if (status.equals("request")) {
                    openBottomSheetDialog(getString(R.string.are_you_sure_you_want_to_remove), sUserId);
                } else {
                    callFollowApi(sUserId);
                }
                break;
            case R.id.btnUnfollow:
                callUnfollowFriend(sUserId);
                break;
        }
    }

    private void openBottomSheetDialog(final String header, final String following_id) {
        List<GenericBottomModel> modelList = new ArrayList<>();
        String[] arrayAction = {getString(R.string.remove), getString(R.string.cancel)};
        for (int i = 0; i < 2; i++) {
            GenericBottomModel model = new GenericBottomModel();
            model.setId(i + "");
            model.setItemText(arrayAction[i]);
            modelList.add(model);
        }

        homeActivity.openBottomSheet(header, modelList, new GenericBottomSheetDialog.RecyclerItemClick() {
            @Override
            public void onItemClick(GenericBottomModel genericBottomModel) {

                if (genericBottomModel.getId().equals("0")) {
//                    DebugLog.e("call here remove friend api : " + following_id);
                    callUnfollowFriend(following_id);
                }
            }
        });
    }

    private void callRemoveFriend(String following_id) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, following_id);

        NetworkCall.getInstance().callgetRemoveFriendApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    //  homeActivity.showSnackBar(getView(), data.getMessage());
                    callViewProfileApi();
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

    //follow user api
    private void callFollowApi(String sFollowId) {

        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, sFollowId);

        NetworkCall.getInstance().callFollowApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    //   homeActivity.showSnackBar(getView(), data.getMessage());
                    callViewProfileApi();

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

    private void callUnfollowFriend(String following_id) {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.FOLLOW_ID, following_id);

        NetworkCall.getInstance().callUnfollowApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    // homeActivity.showSnackBar(getView(), data.getMessage());
                    callViewProfileApi();

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

}
