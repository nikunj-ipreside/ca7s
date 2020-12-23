package com.music.ca7s.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.adapter.HomeVPagerAdapter;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iApiService;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.upload_music.UploadMusicPojo;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ProgressRequestBody;
import com.music.ca7s.utils.RetroClient;
import com.music.ca7s.utils.Util;
import com.music.ca7s.utils.UtilFileKt;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMusicFragment extends BaseFragment implements ProgressRequestBody.UploadCallbacks {
    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    @BindView(R.id.tlAddMusic)
    TabLayout tlAddMusic;
    @BindView(R.id.vpagerAddMusic)
    ViewPager vpagerAddMusic;
    @BindView(R.id.tv_limit)
    TextView tv_limit;

    private List<BaseFragment> fragmentList;
    private HomeVPagerAdapter homeVPagerAdapter;
    private String sCookie;

    public static AddMusicFragment newInstance() {

        Bundle args = new Bundle();

        AddMusicFragment fragment = new AddMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // upload song param value
    public static String privacy = "2";
    public static String genreID = "";
    public static String songUrl = "";
    public static String songPic = "";
    public static String songTitle = "";
    public static String songYear = "";
    public static String albumName = "";
    public static String artistName = "";
    public static String edtLyrics = "";
    public static Bitmap dataByt = null;
    int[] location = new int[2];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_music, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        txtTopbarTitle.setText(R.string.add_music);
        imgTopbarRight.setImageResource(R.drawable.ic_save);
        imgTopbarLeft.setImageResource(R.drawable.ic_back);
        homeActivity.lockDrawer();
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        hitApiForLimit();
        setupTabandFragment();

        vpagerAddMusic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                DebugLog.e("onPageScrolled position : " + position + "   positionOffsetPixels  : " + positionOffsetPixels);
                homeActivity.closeKeyboard(getView());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public String geCooKie(){
        return AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
    }

    private void hitApiForLimit() {
        iApiService apiService= RetroClient.getApiService();
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        HashMap<String,String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID,user_id);
        Call<JsonObject> call = apiService.getUploadLimit(params,geCooKie());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//              Log.e("jsonObject : ",response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equals("success")) {
                        if (tv_limit != null) {
                            if (response.body().has("data")) {
                                tv_limit.setText(homeActivity.getString(R.string.youravailableuploadlimitis) + " " + response.body().get("data").getAsFloat() + " mb/100 mb");
                                tv_limit.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, Throwable t) {
                if (t != null && t.getMessage() != null && t.getMessage().isEmpty()){
                    homeActivity.showSnackBar(getView(),t.getMessage());
                }
               t.printStackTrace();
            }
        });
    }

    private void setupTabandFragment() {
        tlAddMusic.addTab(tlAddMusic.newTab().setText(getString(R.string.add_music)));
        tlAddMusic.addTab(tlAddMusic.newTab().setText(R.string.add_artwork));
        tlAddMusic.addTab(tlAddMusic.newTab().setText(getString(R.string.add_lyrics)));
        setAdapterViewPager();

        if (AppLevelClass.getInstance().getTutorialPrefrences().getString(SharedPref.ADD_ART_WORK_SCREEN).equalsIgnoreCase("1")) {

        } else {
            AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.ADD_ART_WORK_SCREEN, "1");

            Point point = new Point();
            point.x = location[0];
            point.y = location[1];
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.tooltip_layout_center);
            TextView tv_msg = dialog.findViewById(R.id.tooltip_text);
            tv_msg.setText(R.string.you_can_add_artwork);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = 100;   //x position
            wmlp.y = 300;
            dialog.show();
        }

    }

    private void setAdapterViewPager() {
            fragmentList = getFragmentList();
            homeVPagerAdapter = new HomeVPagerAdapter(getChildFragmentManager(), fragmentList);
        vpagerAddMusic.setAdapter(homeVPagerAdapter);
        vpagerAddMusic.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlAddMusic));
        tlAddMusic.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpagerAddMusic));
    }

    private List<BaseFragment> getFragmentList() {
        List<BaseFragment> fragments = new ArrayList<>();

        ViewPagerAddMusicFragment addMusicFragment = new ViewPagerAddMusicFragment();
        fragments.add(addMusicFragment);

        ViewPagerAddArtworkFragment artWorkFragment = new ViewPagerAddArtworkFragment();
        fragments.add(artWorkFragment);

        ViewPagerAddLyricsFragment addLyricsFragment = new ViewPagerAddLyricsFragment();
        fragments.add(addLyricsFragment);

        return fragments;
    }

    void selectPage(int pageIndex) {
        tlAddMusic.setScrollPosition(pageIndex, 0f, true);
        vpagerAddMusic.setCurrentItem(pageIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        try {
            if (ProgressRequestBody.in != null)
                ProgressRequestBody.in.close();
        } catch (IOException e) {
            e.printStackTrace();
            DebugLog.e("IOException : " + e.toString());
        }
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.onBackPressed();
                break;
            case R.id.imgTopbarRight:
                if (isValidate()) {
                    selectPage(0);
                    callUploadMusicApi();
                }
                break;
        }
    }

    private boolean isValidate() {


        if (songUrl.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song));
            return false;
        }

        if (songTitle.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song_title));
            return false;
        }

        if (albumName.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_album_name));
            return false;
        }

        if (artistName.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_artist_name));
            return false;
        }
        if (songYear.length() ==0){
            homeActivity.showSnackBar(getView(), getString(R.string.song_year_is_required));
            return false;
        }
        if (songYear.length() < 4){
            homeActivity.showSnackBar(getView(), getString(R.string.enter_valid_release_year));
            return false;
        }
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (Integer.parseInt(songYear) > year){
            homeActivity.showSnackBar(getView(), getString(R.string.release_year_should_not_be_greater));
            return false;
        }

        if (genreID.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song_type));
            return false;
        }

       /* if (songPic.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song_pic));
            return false;
        }*/

       /* if (et_lyrics.length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_lyrics));
            return false;
        }*/
        return true;
    }

    private void callUploadMusicApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        File file = new File(songUrl);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("filefield", file.getName(), fileBody);
//        RequestBody songURL1 = RequestBody.create(MediaType.parse(songUrl), file);
        RequestBody userID1 = RequestBody.create(MediaType.parse("text/plain"),
                AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        RequestBody songTitle1 = RequestBody.create(MediaType.parse("text/plain"), songTitle);
        RequestBody albumName1 = RequestBody.create(MediaType.parse("text/plain"), albumName);
        RequestBody genreID1 = RequestBody.create(MediaType.parse("text/plain"), genreID);
        RequestBody year1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Calendar.YEAR));
        RequestBody artistName1 = RequestBody.create(MediaType.parse("text/plain"), artistName);
        RequestBody edtLyrics1 = RequestBody.create(MediaType.parse("text/plain"), edtLyrics);
        RequestBody edtYear = RequestBody.create(MediaType.parse("text/plain"), songYear);
        RequestBody privacy1 = RequestBody.create(MediaType.parse("text/plain"), privacy);
        String encodedString = "";
        if (dataByt != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            dataByt.compress(Bitmap.CompressFormat.PNG, 100, stream);
            encodedString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        }
        RequestBody songPic1 = RequestBody.create(MediaType.parse("text/plain"), encodedString);
        RequestBody songPic2 = RequestBody.create(MediaType.parse("text/plain"), "0");

        NetworkCall.getInstance().callUploadMusicApi(filePart, userID1,
                songTitle1, albumName1, genreID1, year1, artistName1, edtLyrics1, privacy1, songPic1,songPic2,edtYear, sCookie, new iResponseCallback<JsonObject>() {
                    @Override
                    public void sucess(JsonObject jsonData) {
//                        UploadMusicPojo data = new Gson().fromJson(jsonData.toString(),UploadMusicPojo.class);
//                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        homeActivity.hideProgressDialog();
                        DebugLog.e("Status : " + jsonData.toString());
                        String status = "";
                        String message = "";

                        if (jsonData.has("status")) {
                            status = jsonData.get("status").getAsString();
                        }

                        if (jsonData.has("message")) {
                            message = jsonData.get("message").getAsString();
                        }
                        if (status.equals("success")) {
                            //ViewPagerAddMusicFragment.donutProgress.setDonut_progress("100");
                            privacy = "0";
                            genreID = "";
                            songUrl = "";
                            songPic = "";
                            songTitle = "";
                            albumName = "";
                            artistName = "";
                            edtLyrics = "";
                            dataByt = null;
                            homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.song_uploaded_successfully));
                            ViewPagerAddMusicFragment.donutProgress.setDonut_progress(0+"");
                            homeActivity.onBackPressed();
                        } else {
                            if (!message.isEmpty()) {
                                homeActivity.showSnackBar(getView(), message);
                            }
                        }
                    }

                    @Override
                    public void onFailure(BaseModel baseModel) {
                        homeActivity.hideProgressDialog();
                        if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                            homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                        }
                        ViewPagerAddMusicFragment.donutProgress.setVisibility(View.GONE);
//                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }

                    @Override
                    public void onError(Call<JsonObject> responseCall, Throwable T) {
                        homeActivity.hideProgressDialog();
                        homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
                        ViewPagerAddMusicFragment.donutProgress.setVisibility(View.GONE);
//                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        DebugLog.e("Status : " + T.toString());

                    }
                });
    }

    private void showSuccessMessage(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
        //Setting message manually and performing action on button click
        builder.setMessage(string)
                .setCancelable(false)
                .setPositiveButton(homeActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      dialog.dismiss();
                      homeActivity.onBackPressed();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(homeActivity.getString(R.string.alert));
        alert.show();
}


    @Override
    public void onProgressUpdate(int percentage) {
//        DebugLog.e("/****************** percentage : " + percentage);

        ViewPagerAddMusicFragment.donutProgress.setDonut_progress(percentage + "");
    }

    @Override
    public void onError() {
        DebugLog.e("/****************** onError*****************/");

    }

    @Override
    public void onFinish() {
        //DebugLog.e("/****************** onFinish : " + 100);

    }
}
