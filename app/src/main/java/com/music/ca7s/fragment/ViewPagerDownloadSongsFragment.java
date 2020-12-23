package com.music.ca7s.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.OnLoadMoreListener;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.adapter.homeadapters.DownloadPlayListAdapter;
import com.music.ca7s.adapter.homeadapters.DownloadedSongsAdapter;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.contant.RecylerViewSongItemClickListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.DownloadPlaylistModel;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ImagePicker;
import com.music.ca7s.utils.Util;
import com.music.ca7s.utils.downloadutils.DownloadingService;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.music.ca7s.utils.AppConstants.DOWNLOADED_PLAYLIST;
import static com.music.ca7s.utils.AppConstants.TABLE_DOWNLOADED_SONG;

public class ViewPagerDownloadSongsFragment extends BaseFragment implements OnLoadMoreListener, DownloadSongListener, RecylerViewSongItemClickListener {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE_PLAYLIST = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 3;
    private ArrayList<Song> songList= new ArrayList<>();
    ProgressDialog dialog;
    private RecyclerView rvMyMusic;
    private TextView txtNodata,tv_songname,tv_songartist,tv_remove,tv_addplaylist,tv_share;
    private DownloadedSongsAdapter mDownloadedAdapter;
    private DatabaseHandler databaseHandler;
    private ImageView iv_cover,iv_close_sheet,iv_remove,iv_playlist,iv_share;
    private LinearLayout ll_remove,ll_addtoplaylist,ll_sharesong,bottom_sheet_downloaded;
    private BottomSheetBehavior downloaded_menu_sheet_behaviour;
    private Song mSelectedSong = null;
    private int selectedPosition = 0;
    private ImageView iv_playlistBanner;
    private String imageURL = "";
    private EditText edtSearchName;
    private DownloadPlaylistModel selectedPlaylist;
    public static ViewPagerDownloadSongsFragment mViewPagerDownloadFragment=null;
    private boolean mReceiversRegistered = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_song, container, false);
        mViewPagerDownloadFragment = this;
        databaseHandler = new DatabaseHandler(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity.setSlidingState(true);
        homeActivity.showDownloadNotification(false);
        edtSearchName = view.findViewById(R.id.edtSearchName);
        rvMyMusic = view.findViewById(R.id.rvMyMusic);
        txtNodata = view.findViewById(R.id.txtNodata);
        tv_songname = view.findViewById(R.id.tv_songname);
        tv_songartist = view.findViewById(R.id.tv_songartist);
        tv_remove = view.findViewById(R.id.tv_remove);
        tv_addplaylist = view.findViewById(R.id.tv_addplaylist);
        iv_cover = view.findViewById(R.id.iv_cover);
        iv_close_sheet = view.findViewById(R.id.iv_close_sheet);
        iv_remove = view.findViewById(R.id.iv_remove);
        iv_playlist = view.findViewById(R.id.iv_playlist);
        iv_share = view.findViewById(R.id.iv_share);
        tv_share = view.findViewById(R.id.tv_share);
        ll_remove = view.findViewById(R.id.ll_remove);
        ll_addtoplaylist = view.findViewById(R.id.ll_addtoplaylist);
        ll_sharesong = view.findViewById(R.id.ll_sharesong);
        bottom_sheet_downloaded = view.findViewById(R.id.bottom_sheet_downloaded);
        bottom_sheet_downloaded.setOnClickListener(this);
        iv_close_sheet.setOnClickListener(this);
        ll_remove.setOnClickListener(this);
        ll_addtoplaylist.setOnClickListener(this);
        ll_sharesong.setOnClickListener(this);
        rvMyMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        mDownloadedAdapter = new DownloadedSongsAdapter(songList,songList,homeActivity,this);
        rvMyMusic.setAdapter(mDownloadedAdapter);
        if (isReadStorageAllowed()){
           setData();
        }

//        if (savedInstanceState == null) {
//            Intent intent = new Intent(getContext(), DownloadingService.class);
////            intent.putParcelableArrayListExtra("files", new ArrayList<File>(Arrays.asList(files)));
//            getContext().startService(intent);
//        }
//
//        registerReceiver();

        downloaded_menu_sheet_behaviour = BottomSheetBehavior.from(bottom_sheet_downloaded);
        downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        downloaded_menu_sheet_behaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

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

    private void setData() {
        songList = databaseHandler.getAllSong(AppConstants.TABLE_DOWNLOADED_SONG);
        setAdapter();
    }

//    private com.music.ca7s.utils.downloadutils.File getFile(long id) {
//        return new com.music.ca7s.utils.downloadutils.File(id, "https://someurl/" + id);
//    }


//    private void registerReceiver() {
//        unregisterReceiver();
//        IntentFilter intentToReceiveFilter = new IntentFilter();
//        intentToReceiveFilter
//                .addAction(DownloadingService.PROGRESS_UPDATE_ACTION);
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
//                mDownloadingProgressReceiver, intentToReceiveFilter);
//        mReceiversRegistered = true;
//    }
//
//    private void unregisterReceiver() {
//        if (mReceiversRegistered) {
//            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(
//                    mDownloadingProgressReceiver);
//            mReceiversRegistered = false;
//        }
//    }

//    // don't call notifyDatasetChanged() too frequently, have a look at
//    // following url http://stackoverflow.com/a/19090832/1112882
//    protected void onProgressUpdate(int position, int progress) {
//      Log.e("onProgressUpdate : ",""+position+"    "+progress);
//    }
//
//    protected void onProgressUpdateOneShot(int[] positions, int[] progresses) {
////        Log.e("UpdateOneShot : ",""+positions+"    "+progresses);
//        if (positions != null) {
//            for (int i = 0; i < positions.length; i++) {
//                int position = positions[i];
//                int progress = progresses[i];
//                onProgressUpdate(position, progress);
//            }
//        }
//    }

//    private final BroadcastReceiver mDownloadingProgressReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(
//                    DownloadingService.PROGRESS_UPDATE_ACTION)) {
//                final boolean oneShot = intent
//                        .getBooleanExtra("oneshot", false);
//                if (oneShot) {
//                    final int[] progresses = intent
//                            .getIntArrayExtra("progress");
//                    final int[] positions = intent.getIntArrayExtra("position");
//                    onProgressUpdateOneShot(positions, progresses);
//                } else {
//                    final int progress = intent.getIntExtra("progress", -1);
//                    final int position = intent.getIntExtra("position", -1);
//                    if (position == -1) {
//                        return;
//                    }
//                    onProgressUpdate(position, progress);
//                }
//            }
//        }
//    };

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_close_sheet :
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;

            case R.id.ll_remove:
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (mSelectedSong != null){
                    databaseHandler.deleteSongItem(mSelectedSong,AppConstants.TABLE_DOWNLOADED_SONG);
                    setData();
                    File file = new File(AppConstants.DEVICE_PATH +mSelectedSong.getSongID()+AppConstants.TEMPRARY_MUSIC_EXTENTION+".nomedia");
                    if (file.exists()){
                        file.delete();
                    }
                }
                break;

            case R.id.ll_addtoplaylist:
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (mSelectedSong != null){
                  openDialog(iv_playlist,mSelectedSong);
                }
                break;

            case R.id.ll_sharesong:
                if (mSelectedSong != null){
                    homeActivity.callShareTrackApi(getShareParam(), mSelectedSong, getView());
                }
                downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;

            case R.id.bottom_sheet_downloaded:
                break;
        }
    }

    public void openDialog(final ImageView imageView, final Song song){
        final Dialog dialog = new Dialog(homeActivity);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_playlists);
        dialog.show();

        ImageView iv_close_playlist  = dialog.findViewById(R.id.iv_close_playlist);
        iv_close_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RelativeLayout rl_add_playlist = dialog.findViewById(R.id.rl_add_playlist);
        rl_add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openCreatePlayListDialog(imageView,song);
            }
        });
        RecyclerView rv_play_list = dialog.findViewById(R.id.rv_play_list);
        TextView tv_no_data = dialog.findViewById(R.id.tv_no_data);
        getUserPlaylist(rv_play_list,tv_no_data,song,dialog,imageView);

    }

    public void openCreatePlayListDialog(final ImageView imageView,final Song song){
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
            btn_add.setText(getString(R.string.create_playlist));
        ImageView iv_close = dialog.findViewById(R.id.iv_close_dialog);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().isEmpty()){
                    homeActivity.showSnackBar(getView(),getString(R.string.enter_playlist_name));
                    editText.requestFocus();
                }else {
//                        if (imageURL == null || imageURL.isEmpty()){
//                            pickImage();
//                            homeActivity.showSnackBar(getView(),getString(R.string.please_select_playlist_image));
//                            return;
//                        }
                        Util.hideKeyboard(homeActivity);
                        DownloadPlaylistModel downloadPlaylistModel = new DownloadPlaylistModel();
                        downloadPlaylistModel.setName(editText.getText().toString());
                        downloadPlaylistModel.setImage(imageURL);
                        downloadPlaylistModel.setCreatedAt(Util.getCurrentDateTime());
                        song.setIsPlaylist(true);
                        ArrayList<Song> mSongs = new ArrayList<>();
                        mSongs.add(song);
                        String values = new Gson().toJson(mSongs);
                        downloadPlaylistModel.setValue(values);
                        imageURL ="";
                        databaseHandler.addPlaylist(downloadPlaylistModel);
                        imageView.setImageResource(R.drawable.ic_playlist_add_theme);
                        homeActivity.showSnackBar(getView(),getString(R.string.song_added_to_playlist));

                    dialog.dismiss();
                }
            }
        });

        iv_playlistBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (type.toString().equals(AppConstant.ADD)) {
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


    private void pickImage() {
//        DebugLog.e("Pick Image");
        if (isReadStorageAllowed()) {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getContext());
            startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);
        }
    }

    public void createPlaylist(String imageURL, String name){
        ArrayList<Song> songs = new ArrayList<Song>();
        String value = new Gson().toJson(songs);
        String image = imageURL;
        DownloadPlaylistModel downloadPlaylistModel = new DownloadPlaylistModel();
        downloadPlaylistModel.setName(name);
        downloadPlaylistModel.setImage(image);
        downloadPlaylistModel.setValue(value);
        downloadPlaylistModel.setCreatedAt(Util.getCurrentDateTime());
    }


    public void getUserPlaylist(final RecyclerView rv_playlist, final TextView tv_no_data, final Song song, final Dialog dialog, final ImageView imageView){
      ArrayList<DownloadPlaylistModel> playListModel = databaseHandler.getAllPLaylist();
                    if (playListModel != null && !playListModel.isEmpty()){
                        rv_playlist.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);
                        rv_playlist.setAdapter(new DownloadPlayListAdapter(homeActivity,playListModel,dialog,song,imageView,databaseHandler));
                    }
                    else {
                        rv_playlist.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }

    }



    public HashMap<String, String> getShareParam() {
        HashMap<String, String> params = new HashMap<>();
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        Log.d("user_id",user_id);
        if(!user_id.isEmpty()) {
            params.put(ApiParameter.USER_ID, AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID));
        }else{
            params.put(ApiParameter.USER_ID,"0");
        }
        params.put(ApiParameter.TITLE, mSelectedSong.getSongTitle());
        params.put(ApiParameter.MUSIC_DESCRIPTION, mSelectedSong.getSongArtist());
        String imageUrl = mSelectedSong.getSongImageUrl();
        if (imageUrl.contains("/index.php")) {
            imageUrl = imageUrl.replace("/index.php", "");
        }
        params.put(ApiParameter.MUSIC_THUMBNAIL, imageUrl);
        params.put(ApiParameter.MUSIC_URL, mSelectedSong.getSongURL());
        params.put(ApiParameter.TRACH_ID, mSelectedSong.getSongID());
        params.put(ApiParameter.THIRD_PARTY,mSelectedSong.getThirdparty_song().toString());

        Log.d("params",""+params);

        return params;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadData(int pageno, boolean isSilent) {

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
//        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setData();
            } else {
                Toast.makeText(getActivity(), getString(R.string.you_have_to_allow_storage_permission), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
//                bitmapProfilePic = bitmap;
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    try{
                        String path = Environment.getExternalStorageDirectory().toString();
                        OutputStream fOut = null;
                        File imagedirect = new File(getContext().getExternalCacheDir() + "/Images.nomedia"+System.currentTimeMillis()+".jpg");
//                        File file = new File(path, imagedirect.getPath()+"/"+System.currentTimeMillis()+".jpg");
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
                                    .override(150,150)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                    .into(iv_playlistBanner);
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }

//                    imageURL = new File(getRealPathFromURI(selectedImageURI)).getPath();
//                    Log.e("imageurl : ","  "+imageURL);

//                    imageURL = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

                }

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onDestroyView() {
//        unregisterReceiver();
        super.onDestroyView();

    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onSongSelected(int position, @NotNull Song selectedData,String type) {
                homeActivity.playSongWithPostion(position,songList,type);
    }

    @Override
    public void onDotSelected(int position, @NotNull Song selectedData) {
        selectedPosition = position;
        mSelectedSong = selectedData;
//        Log.e("onDotSelected : ",new Gson().toJson(mSelectedSong));
        setSongData(selectedData);
        downloaded_menu_sheet_behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setSongData(Song selectedData) {
        if (iv_cover != null){
            if (HomeActivity.isDataSaverEnabled()){

            }else {
                String playlistImage = selectedData.getSongImageUrl();
                if (playlistImage.contains("/index.php")){
                    playlistImage = playlistImage.replace("/index.php","");
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(getActivity())
                            .load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                            .into(iv_cover);
                }
            }
            tv_songname.setText(selectedData.getSongTitle());
            tv_songartist.setText(selectedData.getSongArtist());
            if (mSelectedSong.getIsPlaylist()){
                iv_playlist.setImageResource(R.drawable.ic_playlist_add_theme);
                tv_addplaylist.setText(getString(R.string.added_to_playist));
            }else {
                iv_playlist.setImageResource(R.drawable.plus_gray);
                tv_addplaylist.setText(getString(R.string.add_to_playlist));
            }
        }
    }

    @Override
    public void onImageSelected(int position, @NotNull Song selectedData) {

    }



    private void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void setAdapter() {
        if (songList != null && !songList.isEmpty()) {
          Collections.sort(songList, new Comparator<Song>() {

                public int compare(Song s1, Song s2) {
                    String songDate1 = s1.getCreatedAt();
                    String songDate2 = s2.getCreatedAt();

                    //ascending order
                    return songDate2.compareTo(songDate1);

                    //descending order
                    //return StudentName2.compareTo(StudentName1);
                }});

          for (int i=0;i<songList.size();i++){
              Song newSong = new Song();
              newSong = songList.get(i);
              newSong.setType(TABLE_DOWNLOADED_SONG);
              songList.set(i,newSong);
          }


            txtNodata.setVisibility(View.GONE);
            rvMyMusic.setVisibility(View.VISIBLE);
            if (mDownloadedAdapter != null){
                mDownloadedAdapter.refreshAdapter(songList);
            }
        } else {
            txtNodata.setVisibility(View.VISIBLE);
            rvMyMusic.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeProgress(String position, int progress) {

    }

    @Override
    public void onDownloadSuccess() {
//        Log.e("DOwnloaded ","onDownloadSuccess");
//        if (getContext() != null){
//            Log.e("DOwnloaded ","getContext");
//            if (isReadStorageAllowed()){
//                setData();
//            }
////            MyMusicFragment.newInstance().setupTabandFragment();
//        }
//        if (isReadStorageAllowed()){
//            setData();
//        }
    }

    public void onRefreshAfterDownload(){
//        Log.e("DOwnloaded ","onDownloadSuccess");
        if (getContext() != null){
//            Log.e("DOwnloaded ","getContext");
            if (isReadStorageAllowed()){
                setData();
                homeActivity.showDownloadNotification(false);
            }
//            MyMusicFragment.newInstance().setupTabandFragment();
        }
    }
}
