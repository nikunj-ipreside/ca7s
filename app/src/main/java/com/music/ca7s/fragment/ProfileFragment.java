package com.music.ca7s.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.profile.ProfileDatum;
import com.music.ca7s.model.profile.ProfilePojo;
import com.music.ca7s.utils.DebugLog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;

public class ProfileFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.imgProfilePic)
    CircleImageView imgProfilePic;
    @BindView(R.id.txtProfileName)
    TextView txtProfileName;
    @BindView(R.id.txtProfileCity)
    TextView txtProfileCity;
    @BindView(R.id.txtFolloweres)
    TextView txtFolloweres;
    @BindView(R.id.txtFollowing)
    TextView txtFollowing;
    @BindView(R.id.txtEditProfile)
    TextView txtEditProfile;
    @BindView(R.id.linearFullCurve)
    LinearLayout linearFullCurve;
    @BindView(R.id.txtFavourite)
    TextView txtFavourite;
    @BindView(R.id.txtMyMusic)
    TextView txtMyMusic;
    @BindView(R.id.txtAddMusic)
    TextView txtAddMusic;
    @BindView(R.id.linearCurveBg)
    LinearLayout linearCurveBg;
    @BindView(R.id.linearFollowing)
    LinearLayout linearFollowing;
    @BindView(R.id.linearFollowers)
    LinearLayout linearFollowers;
    private String sCookie;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.rl_addmusic)
    LinearLayout rl_addmusic;
    @BindView(R.id.rl_downloads)
    LinearLayout rl_downloads;
    @BindView(R.id.rl_favourite)
    LinearLayout rl_favourite;
    String count;
    List<ProfileDatum> profileData;

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        unbinder = ButterKnife.bind(this, view);
        homeActivity.closeDrawer();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        txtTopbarTitle.setText(R.string.profile);
        imgTopbarRight.setImageResource(R.drawable.ic_pending_requiest);
        homeActivity.unlockDrawer();
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        callGetProfileApi();
    }

    private void callGetProfileApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));

        NetworkCall.getInstance().callgetProfileApi(params, sCookie, new iResponseCallback<ProfilePojo>() {
            @Override
            public void sucess(ProfilePojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    profileData = data.getData();

                    String playlistImage = profileData.get(0).getProfilePicture();
                    if (playlistImage.contains("/index.php")){
                        playlistImage = playlistImage.replace("/index.php","");
                    }

                    if (playlistImage.contains("/public/public")){
                        playlistImage = playlistImage.replace("/public/public","/public");
                    }
                    if (playlistImage != null && !playlistImage.isEmpty()) {
                        Glide.with(getActivity())
                                .asBitmap()
                                .load(playlistImage)
                                .placeholder(R.drawable.ic_top_placeholder)
                                .transform(new CircleCrop())
                                .into(imgProfilePic);
//                        CropCircleTransformation(getActivity())
                    }

                    txtProfileName.setText(profileData.get(0).getFullName());
                    txtProfileCity.setText(profileData.get(0).getUserCity());
                    txtFolloweres.setText(profileData.get(0).getFollowers().toString());
                    txtFollowing.setText(profileData.get(0).getFollowing().toString());
                    count =data.getRequestCount();
                    if (!count.equals("") && !count.equals("0")) {
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText(count);
                    } else {
                        tv_count.setVisibility(View.GONE);
                    }
                } else {
                    homeActivity.showSnackBar(getView(), data.getMessage());
                    if (data.getMessage() != null && !data.getMessage().isEmpty() && data.getMessage().toString().equalsIgnoreCase("re-login or try again.")){
                        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_ID,"");
                        AppLevelClass.getInstance().getPreferanceHelper().putBoolean(SharedPref.IS_LOGIN,false);
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                    }
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                if ( baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty() && baseModel.getMessage().toString().equalsIgnoreCase("re-login or try again.")){
                    AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.USER_ID,"");
                    AppLevelClass.getInstance().getPreferanceHelper().putBoolean(SharedPref.IS_LOGIN,false);
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                }

            }

            @Override
            public void onError(Call<ProfilePojo> responseCall, Throwable T) {
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

    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight, R.id.linearFollowers, R.id.linearFollowing, R.id.txtEditProfile, R.id.rl_favourite,
            R.id.rl_downloads, R.id.rl_addmusic, R.id.imgProfilePic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.openDrawer();
                break;
            case R.id.imgTopbarRight:
                homeActivity.openRequestListFragment(FragmentState.REPLACE);
                break;
            case R.id.linearFollowers:

                if(isLogin()){
                    homeActivity.openFollowersFragment(FragmentState.REPLACE);
                }
                break;
            case R.id.linearFollowing:

                if(isLogin()){
                    homeActivity.openFollowingFragment(FragmentState.REPLACE);
                }
                break;
            case R.id.txtEditProfile:

                if(isLogin()){
                    homeActivity.openEditProfileFragment(profileData, FragmentState.REPLACE);
                }
                break;
            case R.id.rl_favourite:
                homeActivity.rbFavourite.setChecked(true);
                if(isLogin()){
                    homeActivity.openFavouriteFragment(FragmentState.REPLACE);
                }

                break;
            case R.id.rl_downloads:
                homeActivity.rbMyMusic.setChecked(true);
                homeActivity.openMyMusicFragment(FragmentState.REPLACE);
                break;
            case R.id.rl_addmusic:
                homeActivity.openUploadedMusicFragment(FragmentState.REPLACE);
                break;

            case R.id.imgProfilePic:
                if(isLogin()){
                    homeActivity.openViewProfileFragment(profileData.get(0).getUserId().toString(), FragmentState.REPLACE);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
