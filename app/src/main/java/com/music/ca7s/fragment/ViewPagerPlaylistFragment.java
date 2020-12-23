package com.music.ca7s.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.homeadapters.PlayListAdapterMain;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.listener.onPositionSelected;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.playlist.PlayListModel;
import com.music.ca7s.model.playlist.Playlist;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ImagePicker;
import com.music.ca7s.utils.Util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;


public class ViewPagerPlaylistFragment extends BaseFragment implements onPositionSelected {
    Unbinder unbinder;
    @BindView(R.id.rvPlayList1)
    RecyclerView rvPlayList;
    private String sCookie;
    private ArrayList<Playlist> songList = new ArrayList<Playlist>();
    private ImageView iv_playlistBanner;
    private String imageURL = "";
    @BindView(R.id.txtNodata)
    TextView txtNodata;
    private long mLastClickTime = 0;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE_PLAYLIST = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    public PlayListAdapterMain mAdapter;
    private RelativeLayout rl_add_playlist;
    private onPositionSelected listener;
    private Playlist selectedPlaylist;
    public static ViewPagerPlaylistFragment viewPagerPlaylistFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_v_playlist, container, false);
        setUserVisibleHint(false);
        listener = this;
        viewPagerPlaylistFragment = this;
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = this;
        homeActivity.setSlidingState(true);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        mAdapter = new PlayListAdapterMain(getContext(),songList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvPlayList.setLayoutManager(mLayoutManager);
        rvPlayList.setHasFixedSize(true);
        rvPlayList.setAdapter(mAdapter);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl_add_playlist = view.findViewById(R.id.rl_add_playlist);
        rl_add_playlist.setOnClickListener(this);
    }

    private boolean isReadStorageAllowed(int code) {
        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, code);
            return false;
        }
    }

    public void getUserPlaylist(){
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        NetworkCall.getInstance().callPlayListApi(AppConstants.USER_PLAYLIST,params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                homeActivity. hideProgressDialog();
                String status = "";
                String message = "";
                status =  data.get("status").getAsString();
                message = data.get("message").getAsString();
//                DebugLog.e("Status : " + data.toString());
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    PlayListModel playListModel = new Gson().fromJson(data.toString(),PlayListModel.class);
                    if (playListModel != null && playListModel.getData() != null && playListModel.getData().getPlaylist() !=null  && !playListModel.getData().getPlaylist().isEmpty()){
                        songList = playListModel.getData().getPlaylist();
                        if (rvPlayList != null) {
                            rvPlayList.setVisibility(View.VISIBLE);
                            txtNodata.setVisibility(View.GONE);
                            if (mAdapter != null) {
                                mAdapter.refreshAdapter(songList);
                            }
                        }
                    }
                    else {
                        if (rvPlayList != null) {
                            rvPlayList.setVisibility(View.GONE);
                            txtNodata.setVisibility(View.VISIBLE);
                        }
                    }
//                    homeActivity.showSnackBar(getView(), message);
                } else {
//                    DebugLog.e("Status : " + status);
                    songList = new ArrayList<>();
                    songList.clear();
                    if (rvPlayList != null) {
                        rvPlayList.setVisibility(View.GONE);
                        txtNodata.setVisibility(View.VISIBLE);
                    }
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.playlist_not_found));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
//                DebugLog.e("Status : " + "onError");
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserPlaylist();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getActivity(), getString(R.string.to_download_song_you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean checkMultipleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.rl_add_playlist:
               openCreatePlayListDialog(AppConstants.ADD);
                break;
        }
    }



    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemSelected(int pos) {
        selectedPlaylist = songList.get(pos);
        AppLevelClass.page = 0;
        com.music.ca7s.model.PlayListModel listModel = new com.music.ca7s.model.PlayListModel();
        listModel.setScreenID(1);
        listModel.setType(AppConstants.GET_SONG_FROM_PLAYLIST);
        listModel.setName(getString(R.string.favourite)+" "+selectedPlaylist.getName());
        listModel.setGenre_name(selectedPlaylist.getName());
        listModel.setGenreID(Integer.parseInt(String.valueOf(selectedPlaylist.getId())));
        listModel.setPlayListId(String.valueOf(selectedPlaylist.getId()));
        listModel.setImage(selectedPlaylist.getImage());
        homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
    }

    @Override
    public void onMenuSelected(String type,int pos) {
        selectedPlaylist = songList.get(pos);
        if (type.toString().equals(AppConstants.DELETE)){
            removePlaylist(String.valueOf(selectedPlaylist.getId()));
        }else if (type.toString().equals(AppConstants.UPDATE)){
            openCreatePlayListDialog(AppConstants.EDIT);
        }
    }


    private void pickImage() {
        DebugLog.e("Pick Image");
        if (isReadStorageAllowed(STORAGE_PERMISSION_CODE_PLAYLIST)) {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getContext());
            startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
                if (bitmap != null) {
                    try{
                        OutputStream fOut = null;
                        File imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia"+System.currentTimeMillis()+".jpg");
                        fOut = new FileOutputStream(imagedirect);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                        fOut.flush();
                        fOut.close();
                        MediaStore.Images.Media.insertImage(getContext().getContentResolver()
                                ,imagedirect.getAbsolutePath(),imagedirect.getName(),imagedirect.getName());
                        imageURL = imagedirect.getPath();
                        if (iv_playlistBanner != null){
                            Glide.with(getContext())
                                    .load(imageURL)
                                    .placeholder(R.drawable.ic_top_placeholder)
                                    .override(200,200)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                    .into(iv_playlistBanner);
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("imageurl : ","  "+imageURL);

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = homeActivity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public void openCreatePlayListDialog(final String type){
        final Dialog dialog = new Dialog(homeActivity);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_create_playlist);
        dialog.show();
        iv_playlistBanner = dialog.findViewById(R.id.iv_playlist_image);
        Glide.with(getContext())
                .load(R.drawable.ic_top_placeholder)
                .placeholder(R.drawable.ic_top_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(iv_playlistBanner);
        final EditText editText = dialog.findViewById(R.id.et_name);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        if (type.toString().equals(AppConstants.ADD)) {
            btn_add.setText(getString(R.string.create_playlist));
        }
        else {
            btn_add.setText(getString(R.string.update));
            editText.setText(selectedPlaylist.getName());
            homeActivity.loadImage(selectedPlaylist.getImage(),iv_playlistBanner);
        }
        ImageView iv_close = dialog.findViewById(R.id.iv_close_dialog);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().isEmpty()){
                    homeActivity.showSnackBar(getView(),getString(R.string.enter_playlist_name));
                    editText.requestFocus();
                }else {
                    Util.hideKeyboard(homeActivity);
                    if (type.toString().equals(AppConstants.ADD)) {
//                        if (imageURL == null || imageURL.isEmpty()){
//                            pickImage();
//                            homeActivity.showSnackBar(getView(),getString(R.string.please_select_playlist_image));
//                            return;
//                        }
                        createPlaylist(imageURL, editText.getText().toString());
                        imageURL = "";
                    }
                    else {
                        updatePlaylist(imageURL,editText.getText().toString());
                        imageURL = "";
                    }
                    dialog.dismiss();
                }
            }
        });

        iv_playlistBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (type.toString().equals(AppConstants.ADD)) {
                    pickImage();
//                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void createPlaylist(String imageURL, String name){
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.NAME, name);
        params.put(ApiParameter.IMAGE, imageURL);
        NetworkCall.getInstance().callPlayListApi(AppConstants.CREATE_PLAYLIST,params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                homeActivity.hideProgressDialog();
                String status = "";
                String message = "";
                String playListID ="";
//                try {
                status =  data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
//                    playListID = data.get("playlist_id").getAsString();
                    getUserPlaylist();
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.playlist_created));
                } else {
//                    DebugLog.e("Status : " + status);
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.playlist_not_created));
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                if (T.getMessage() != null && ! T.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), T.getMessage());
                }
//                T.printStackTrace();
            }
        });
    }

    public void updatePlaylist(String imageURL, String name){
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.NAME, name);
        params.put(ApiParameter.IMAGE, imageURL);
        params.put(ApiParameter.PLAYLIST_ID, String.valueOf(selectedPlaylist.getId()));
        NetworkCall.getInstance().callPlayListApi(AppConstants.UPDATE_PLAYLIST,params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                homeActivity.hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status =  data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.playlist_updated_successfully));
                    getUserPlaylist();
                } else {
                    DebugLog.e("Status : " + status);
                    homeActivity.showSnackBar(getView(), message);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                homeActivity. hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }

    public void removePlaylist(String playlist_ID){
        homeActivity.showProgressDialog(getString(R.string.loading));
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        params.put(ApiParameter.ID, playlist_ID);
        NetworkCall.getInstance().callPlayListApi(AppConstants.REMOVE_PLAYLIST,params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                homeActivity.hideProgressDialog();
                String status = "";
                String message = "";
//                try {
                status = data.get("status").getAsString();
                message = data.get("message").getAsString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                DebugLog.e("Status : " + status);
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    getUserPlaylist();
                    homeActivity.showSnackBar(getView(), homeActivity.getString(R.string.playlist_deleted));
                } else {
                    DebugLog.e("Status : " + status);
                    homeActivity.showSnackBar(getView(), message);
                }
            }
            @Override
            public void onFailure(BaseModel baseModel) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onFailure");
                homeActivity.showSnackBar(getView(), baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + "onError");
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });
    }


}
