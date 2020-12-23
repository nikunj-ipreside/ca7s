package com.music.ca7s.fragment;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.genericbottomsheet.GenericBottomModel;
import com.music.ca7s.genericbottomsheet.GenericBottomSheetDialog;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.model.genre_list.GenreDatum;
import com.music.ca7s.model.genre_list.GenrePojo;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.RealPathUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;
import static com.music.ca7s.utils.Util.convertBase64ToBitmap;
import static com.music.ca7s.utils.UtilFileKt.getFilePathFromUri;

public class ViewPagerAddMusicFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.imgAddSong)
    ImageView imgAddSong;
    @BindView(R.id.edtSongTitle)
    public EditText edtSongTitle;
    @BindView(R.id.edtAlbumName)
    public EditText edtAlbumName;
    @BindView(R.id.edtArtistName)
    public EditText edtArtistName;
    @BindView(R.id.edtSongType)
    EditText edtSongType;
    @BindView(R.id.edtSongYear)
    EditText edtSongYear;
    @BindView(R.id.rlSongType)
    RelativeLayout rlSongType;
    @BindView(R.id.rbPrivate)
    RadioButton rbPrivate;
    @BindView(R.id.rbPublic)
    RadioButton rbPublic;
    @BindView(R.id.rbOnlyMe)
    RadioButton rbOnlyMe;
    @BindView(R.id.rgPrivacy)
    RadioGroup rgPrivacy;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.spinner2)
    Spinner spinner2;

    public static DonutProgress donutProgress;
    @BindView(R.id.imgPlush)
    ImageView imgPlush;

    private String sCookie;

    List<GenreDatum> listData;
    private static final int PICK_AUDIO_REQUEST_CODE = 323;
    private static final int PICK_AUDIO_PERMISSION_CODE = 232;
    Uri songUri;
    int[] location = new int[2];
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public static ViewPagerAddMusicFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerAddMusicFragment fragment = new ViewPagerAddMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_v_addmusic, container, false);

        unbinder = ButterKnife.bind(this, view);

        donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
        if (AppLevelClass.getInstance().getTutorialPrefrences().getString(SharedPref.ADD_MUSIC_FILE_SCREEN).equalsIgnoreCase("1")) {

        } else {
            AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.ADD_MUSIC_FILE_SCREEN, "1");

            Point point = new Point();
            point.x = location[0];
            point.y = location[1];
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.tooltip_layout_center);
            TextView tv_msg = dialog.findViewById(R.id.tooltip_text);
            tv_msg.setText(R.string.upload_music_file);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = 100;   //x position
            wmlp.y = 600;
            dialog.show();
        }


        if (!AddMusicFragment.songUrl.isEmpty()) {
            imgPlush.setVisibility(View.GONE);
            donutProgress.setVisibility(View.VISIBLE);
        } else {
            imgPlush.setVisibility(View.VISIBLE);
            donutProgress.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        homeActivity.setSlidingState(true);

        checkPrivacy();
        int lastyear = 1900;
        int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = currentyear;i>lastyear;i--){
            list.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(homeActivity,
                android.R.layout.simple_list_item_1, list);
        spinner2.setPrompt("");
        spinner2.setAdapter(dataAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtSongYear.setText(list.get(position)+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rbOnlyMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPrivacy();
            }
        });
        rbPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPrivacy();
            }
        });
        rbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPrivacy();
            }
        });

        edtSongTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddMusicFragment.songTitle = edtSongTitle.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSongYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddMusicFragment.songYear = edtSongYear.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtAlbumName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddMusicFragment.albumName = edtAlbumName.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtArtistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddMusicFragment.artistName = edtArtistName.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.imgAddSong, R.id.rlSongType,R.id.edtSongType, R.id.rbPrivate, R.id.rbPublic, R.id.rbOnlyMe, R.id.btnSave,R.id.edtSongYear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgAddSong:
//                homeActivity.showSnackBar(getView(), getString(R.string.under_development));
                if (isReadStorageAllowed())
                    pickAudio();
                break;
            case R.id.edtSongYear:
                if (spinner2.getAdapter() != null){
                    spinner2.performClick();
                }
                break;
            case R.id.rlSongType:
                callGenreListApi();
                break;
            case R.id.edtSongType:
                callGenreListApi();
                break;
            case R.id.rbPrivate:
                checkPrivacy();
                break;
            case R.id.rbPublic:
                checkPrivacy();
                break;
            case R.id.rbOnlyMe:
                checkPrivacy();
                break;

            case R.id.btnSave:
                if (isValidate())
                    saveAllData();
                break;
        }
    }

    private void checkPrivacy() {
        // private  = 1, public = 0 , only me  = 2
        if (rbPrivate.isChecked()) {
            AddMusicFragment.privacy = "1";
        } else if (rbPublic.isChecked()) {
            AddMusicFragment.privacy = "2";
        } else if (rbOnlyMe.isChecked()) {
            AddMusicFragment.privacy = "3";
        }
    }

    // open orderBy dropdown
    private void openBottomSheetDialog(final String header) {
        List<GenericBottomModel> modelList = new ArrayList<>();

        for (int i = 0; i < listData.size(); i++) {
            GenericBottomModel model = new GenericBottomModel();
            model.setId(listData.get(i).getId().toString());
            model.setItemText(listData.get(i).getType());
            modelList.add(model);
        }
        String data = new Gson().toJson(modelList);

        Log.e("openSheetData : ",""+data);
        if (modelList != null && !modelList.isEmpty()) {
            homeActivity.openBottomSheet(header, modelList, new GenericBottomSheetDialog.RecyclerItemClick() {
                @Override
                public void onItemClick(GenericBottomModel genericBottomModel) {
                    edtSongType.setText(genericBottomModel.getItemText());
                    AddMusicFragment.genreID = genericBottomModel.getId();
                }
            });
        }
    }

    private void callGenreListApi() {
        homeActivity.showProgressDialog(getString(R.string.loading));
        NetworkCall.getInstance().callGenreListForUploadSongApi(sCookie, new iResponseCallback<GenrePojo>() {
            @Override
            public void sucess(GenrePojo data) {
                homeActivity.hideProgressDialog();
                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    listData = data.getData();
                    openBottomSheetDialog(getString(R.string.genre));
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
            public void onError(Call<GenrePojo> responseCall, Throwable T) {
                homeActivity.hideProgressDialog();
                homeActivity.showSnackBar(getView(), getString(R.string.api_error_message));
            }
        });

    }

    //Check permission is granted or not
    private boolean isReadStorageAllowed() {

        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_AUDIO_PERMISSION_CODE);
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == PICK_AUDIO_PERMISSION_CODE) {

            if (grantResults.length > 0) {
                boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (readExternalFile && writeExternalFile) {
                    pickAudio();
                } else {
                    homeActivity.showSnackBar(getView(), getString(R.string.to_upload_profile_pic_you_have_to_allow_storage_permission));
                }

            } else {
                homeActivity.showSnackBar(getView(), getString(R.string.to_upload_profile_pic_you_have_to_allow_storage_permission));
            }
        }
    }

    private void pickAudio() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,PICK_AUDIO_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_AUDIO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        songUri = data.getData();
                        String uriString = songUri.toString();
                        String path = getFilePathFromUri(homeActivity,songUri);
                        Log.e("ioooo",path);
                        if (!path.isEmpty()) {
                            AddMusicFragment.songUrl = path;
                            Log.e("Path : ","  "+AddMusicFragment.songUrl);
                            try {
                                mmr.setDataSource(AddMusicFragment.songUrl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            mmr.setDataSource(AddMusicFragment.songUrl);
//                        mmr.getFrameAtTime();
                            String encodeToString = "";
                            if (mmr.getEmbeddedPicture()!=null && mmr.getEmbeddedPicture().length > 0){
                                encodeToString = Base64.encodeToString(mmr.getEmbeddedPicture(), Base64.DEFAULT);
                                AddMusicFragment.dataByt =convertBase64ToBitmap(encodeToString);
                            }
                            edtSongTitle.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                            edtAlbumName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
                            edtArtistName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

                            AddMusicFragment.songTitle = edtSongTitle.getText().toString();
                            AddMusicFragment.albumName = edtAlbumName.getText().toString();
                            AddMusicFragment.artistName = edtArtistName.getText().toString();

                            DebugLog.e("METADATA_KEY_ALBUM: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                            DebugLog.e("METADATA_KEY_ALBUMARTIST: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
                            DebugLog.e("METADATA_KEY_ARTIST: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                            imgPlush.setVisibility(View.GONE);
                            donutProgress.setVisibility(View.VISIBLE);
                        }else {

                        }

                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    /**
     * Gets the corresponding path to a file from the given content:// URI
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver The content resolver to use to perform the query.
     * @return the file path as a string
     */
    private String getFilePathFromContentUri(Uri selectedVideoUri,
                                             ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



    private boolean isValidate() {

        if (songUri == null) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song));
            return false;
        }

        if (edtSongTitle.getText().length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song_title));
            return false;
        }

        if (edtAlbumName.getText().length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_album_name));
            return false;
        }

        if (edtArtistName.getText().length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_artist_name));
            return false;
        }

        if (edtSongType.getText().length() == 0) {
            homeActivity.showSnackBar(getView(), getString(R.string.validate_empty_song_type));
            return false;
        }

        return true;
    }

    private void saveAllData() {
        homeActivity.showSnackBar(getView(), getString(R.string.information_save));
        AddMusicFragment.songTitle = edtSongTitle.getText().toString();
        AddMusicFragment.albumName = edtAlbumName.getText().toString();
        AddMusicFragment.artistName = edtArtistName.getText().toString();
    }
}
