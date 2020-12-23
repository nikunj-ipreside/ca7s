package com.music.ca7s.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.music.ca7s.R;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.ImagePicker;
import com.music.ca7s.utils.Util;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerAddArtworkFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.imgAddArtWork)
    ImageView imgAddArtWork;

    private static final int PICK_IMAGE_REQUEST_CODE = 32;
    private static final int PICK_IMAGE_PERMISSION_CODE = 32;
    @BindView(R.id.imgPicArtWork)
    ImageView imgPicArtWork;
    @BindView(R.id.rlAddArtWork)
    RelativeLayout rlAddArtWork;
    public static MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    public static Context context;

    public static ViewPagerAddArtworkFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerAddArtworkFragment fragment = new ViewPagerAddArtworkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_v_add_artwork, container, false);
        context= getActivity();

        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    public static void setData(){
        try {
            mmr.setDataSource(getRealPathFromURI(Uri.parse(AddMusicFragment.songUrl)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AddMusicFragment.dataByt != null) {
            try {
                AddMusicFragment.songPic = Util.getPathFromBitmap(homeActivity,AddMusicFragment.dataByt);
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(AddMusicFragment.songPic)
                        .override(400,400)
//                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                        .into(imgPicArtWork);

            }catch (Exception e){
                e.printStackTrace();
            }
            }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.rlAddArtWork)
    public void onViewClicked() {
        if (isReadStorageAllowed())
            pickImage();
    }

    //Check permission is granted or not
    private boolean isReadStorageAllowed() {

        //If permission is granted returning true
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_PERMISSION_CODE);
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.e("Requestcode:" + requestCode);
        if (requestCode == PICK_IMAGE_PERMISSION_CODE) {

            if (grantResults.length > 0) {
                boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (cameraPermission && readExternalFile && writeExternalFile) {
                    pickImage();
                } else {
                    homeActivity.showSnackBar(getView(), getString(R.string.to_upload_profile_pic_you_have_to_allow_storage_permission));
                }

            } else {
                homeActivity.showSnackBar(getView(), getString(R.string.to_upload_profile_pic_you_have_to_allow_storage_permission));
            }
        }
    }

    private void pickImage() {
        DebugLog.e("Pick Image");
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
        startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), resultCode, data);
                            if (bitmap != null) {
                                AddMusicFragment.dataByt = bitmap;
                                AddMusicFragment.songPic = Util.getPathFromBitmap(homeActivity, AddMusicFragment.dataByt);
                                Glide.with(getActivity())
                                        .load(AddMusicFragment.songPic)
                                        .override(400, 400)
//                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(18)))
                                        .into(imgPicArtWork);

                            }
                        }
                    }, 1000);

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}
