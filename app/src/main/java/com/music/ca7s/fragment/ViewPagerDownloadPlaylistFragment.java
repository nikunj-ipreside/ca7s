package com.music.ca7s.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.google.gson.reflect.TypeToken;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.homeadapters.DowloadedPlayListAdapterMain;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.listener.onPositionSelected;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.DownloadPlaylistModel;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ImagePicker;
import com.music.ca7s.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.music.ca7s.utils.AppConstants.TABLE_DOWNLOADED_SONG;
import static com.music.ca7s.utils.UtilFileKt.getSortedListForOffline;

public class ViewPagerDownloadPlaylistFragment extends BaseFragment  implements onPositionSelected {
    private RelativeLayout rl_add_playlist;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE_PLAYLIST = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 3;
    private ArrayList<DownloadPlaylistModel> songList= new ArrayList<>();
    ProgressDialog dialog;
    private RecyclerView rvMyMusic;
    private TextView txtNodata,tv_songname;
    private DowloadedPlayListAdapterMain mDownloadedAdapter;
    private DatabaseHandler databaseHandler;
    private DownloadPlaylistModel selectedPlaylist;
    private ImageView iv_playlistBanner;
    private String imageURL = "";
    public static  ViewPagerDownloadPlaylistFragment downloadPlaylistFragment;
    private EditText edtSearchName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_playlist, container, false);
        downloadPlaylistFragment = this;
        databaseHandler = new DatabaseHandler(getContext());
        setUserVisibleHint(false);
        return view;
    }

    public static ViewPagerDownloadPlaylistFragment getInstance(){
        return downloadPlaylistFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity.setSlidingState(true);
        edtSearchName = view.findViewById(R.id.edtSearchName);
        rl_add_playlist = view.findViewById(R.id.rl_add_playlist);
        rvMyMusic = view.findViewById(R.id.rvMyMusic);
        txtNodata = view.findViewById(R.id.txtNodata);
        tv_songname = view.findViewById(R.id.tv_songname);
        rl_add_playlist.setOnClickListener(this);
        rvMyMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        mDownloadedAdapter = new DowloadedPlayListAdapterMain(homeActivity,songList,songList,this);
        rvMyMusic.setHasFixedSize(true);
        rvMyMusic.setAdapter(mDownloadedAdapter);
        if (isReadStorageAllowed()){
            setData();
        }

        edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDownloadedAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Util.hideKeyboard(homeActivity);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReadStorageAllowed()){
            setData();
        }
    }

    public  void setData() {
        songList = new ArrayList<>();
        songList = databaseHandler.getAllPLaylist();
        setAdapter();
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

    private boolean isReadStorageAllowed() {

        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
            return false;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setData();
            } else {
                Toast.makeText(getActivity(), getString(R.string.you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void setAdapter() {
        if (songList != null && !songList.isEmpty()) {
            txtNodata.setVisibility(View.GONE);
            rvMyMusic.setVisibility(View.VISIBLE);
            mDownloadedAdapter = new DowloadedPlayListAdapterMain(homeActivity,songList,songList,this);
            rvMyMusic.setHasFixedSize(true);
            rvMyMusic.setAdapter(mDownloadedAdapter);
        } else {
            txtNodata.setVisibility(View.VISIBLE);
            rvMyMusic.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

    }
    private void loadData(int pageno, boolean isSilent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onItemSelected(int pos) {
        AppLevelClass.page = 0;
        com.music.ca7s.model.PlayListModel listModel = new com.music.ca7s.model.PlayListModel();
        listModel.setScreenID(1);
        listModel.setType(TABLE_DOWNLOADED_SONG);
//        listModel.setType(AppConstants.DOWNLOADED_PLAYLIST);
        listModel.setName(getString(R.string.my_music)+" "+songList.get(pos).getName());
        listModel.setGenre_name(songList.get(pos).getName());
        listModel.setGenreID(Integer.parseInt(String.valueOf(songList.get(pos).getId())));
        listModel.setImage(songList.get(pos).getImage());
        listModel.setPlayListId(String.valueOf(songList.get(pos).getId()));
        homeActivity.openPlayListFragment(listModel, FragmentState.REPLACE);
    }

    @Override
    public void onMenuSelected(String type,int pos) {
        selectedPlaylist = songList.get(pos);
        if (type.toString().equals(AppConstants.DELETE)){
            removePlaylist(selectedPlaylist.getId());
        }else if (type.toString().equals(AppConstants.UPDATE)){
            openCreatePlayListDialog(AppConstants.EDIT);
        }
    }


    private void pickImage() {
        DebugLog.e("Pick Image");
        if (isReadStorageAllowed()) {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getContext());
            startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
//                if (requestCode == Activity.RESULT_OK) {
                    Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
//                bitmapProfilePic = bitmap;
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        try {
                            String path = Environment.getExternalStorageDirectory().toString();
                            OutputStream fOut = null;
                            File imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia" + System.currentTimeMillis() + ".jpg");
//                        File file = new File(path, imagedirect.getPath()+"/"+System.currentTimeMillis()+".jpg");
                            fOut = new FileOutputStream(imagedirect);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                            fOut.flush();
                            fOut.close();

                            MediaStore.Images.Media.insertImage(getContext().getContentResolver()
                                    , imagedirect.getAbsolutePath(), imagedirect.getName(), imagedirect.getName());
                            imageURL = imagedirect.getPath();

                            if (imageURL != null && !imageURL.isEmpty()) {
                                if (iv_playlistBanner != null) {
                                    Glide.with(getContext())
                                            .load(imageURL)
                                            .placeholder(R.drawable.ic_top_placeholder)
                                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                                            .into(iv_playlistBanner);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("imageurl : ", "  " + imageURL);
                    }
//                }
                break;
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//                break;
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
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    editText.setCursorVisible(true);
                    editText.setSelection(editText.getText().toString().length());
                }
            }
        });
        Button btn_add = dialog.findViewById(R.id.btn_add);
        if (type.toString().equals(AppConstants.ADD)) {
            btn_add.setText(getString(R.string.create_playlist));
        }
        else {
            btn_add.setText(getString(R.string.update));
            editText.setText(selectedPlaylist.getName());
            imageURL = selectedPlaylist.getImage();
            homeActivity.loadImage(selectedPlaylist.getImage(),iv_playlistBanner);
        }
        ImageView iv_close = dialog.findViewById(R.id.iv_close_dialog);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Image on Button : ",imageURL+"");
                if (editText.getText().toString().trim().isEmpty()){
                    homeActivity.showSnackBar(getView(),getString(R.string.enter_playlist_name));
                    editText.requestFocus();
                }else {
                    Util.hideKeyboard(homeActivity);
                    if (type.toString().equals(AppConstants.ADD)) {
                        DownloadPlaylistModel playlistModel = new DownloadPlaylistModel();
                        playlistModel.setCreatedAt(Util.getCurrentDateTime());
                        ArrayList<Song> mySong = new ArrayList<>();
                        String values = new Gson().toJson(mySong);
                        playlistModel.setValue(values);
                        playlistModel.setImage(imageURL);
                        playlistModel.setName(editText.getText().toString());
                        createPlaylist(playlistModel);
                        imageURL = "";
                    }
                    else {
                        DownloadPlaylistModel playlistModel = new DownloadPlaylistModel();
                        playlistModel.setCreatedAt(selectedPlaylist.getCreatedAt());
                        playlistModel.setId(selectedPlaylist.getId());
                        playlistModel.setValue(selectedPlaylist.getValue());
                        playlistModel.setImage(imageURL);
                        playlistModel.setName(editText.getText().toString());
                        updatePlaylist(playlistModel);
                        imageURL = "";
                    }
                    dialog.dismiss();
                    setData();
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

    public void createPlaylist(DownloadPlaylistModel downloadPlaylistModel){
      databaseHandler.addPlaylist(downloadPlaylistModel);
      imageURL = "";
      setData();
    }

    public void updatePlaylist(DownloadPlaylistModel playlist_ID){
       databaseHandler.updatePlaylist(playlist_ID);
       imageURL = "";
    }

    public void removePlaylist(int playlist_ID){
       databaseHandler.deletePlaylist(playlist_ID);
       setData();
    }
}
