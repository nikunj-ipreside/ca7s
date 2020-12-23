package com.music.ca7s.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonObject;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.fragment.BaseFragment;
import com.music.ca7s.fragment.DiscoverFragment;
import com.music.ca7s.fragment.PlayListFragment;
import com.music.ca7s.genericbottomsheet.GenericBottomModel;
import com.music.ca7s.genericbottomsheet.GenericBottomSheetDialog;
import com.music.ca7s.listener.RecyclerItemListener;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseAvtivity extends AppCompatActivity implements View.OnClickListener {

    // commit before adding music player code

    ShareDialog shareDialog;
    ProgressDialog dialog;
    public static String fragmentTAG = DiscoverFragment.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        /*CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        HttpCookie cookie = new HttpCookie("session_id", "aa");
        cookie.setDomain("ca7s/");
        cookie.setPath("/");
        cookie.setVersion(0);
        try {
            cookieManager.getCookieStore().add(new URI("http://13.127.166.64/"), cookie);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

    }

    void fragmentChange(Fragment fragment, FragmentState fragmentState) {
        closeSideMenu();
        fragmentTAG = fragment.getClass().getSimpleName();
         setCurrentFragmentName(fragmentTAG);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentState == FragmentState.ADD) {
            transaction.add(R.id.fragment_container_main, fragment);
        } else {
            transaction.replace(R.id.fragment_container_main, fragment);
        }
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }


    public void setCurrentFragmentName(String fragmentTAG){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.changeLanguage(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.changeLanguage(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public void closeSideMenu(){

    }

    @Override
    public void onClick(View view) {

    }

    //GetDeviceID
    public String deviceId() {
        String android_id;
        try {
            android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            return android_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Hide Keyboard
    public void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSnackBar(View view, String msg) {
        try {
            if (msg != null && !msg.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }catch (Exception e){
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }
    }

    public void showProgressDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog.setMessage(message);
            dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public Boolean isLogin(){
        Boolean isLogin = false;
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        if(!user_id.isEmpty()){
            isLogin = true;
        }
        else {
            AppLevelClass.getInstance().getPreferanceHelper().putBoolean(SharedPref.IS_LOGIN,false);
            startActivity(new Intent(this, AuthenticationActivity.class));
            finishAffinity();
        }
        return isLogin;

    }

    public void openBottomSheet(@Nullable String header, List<GenericBottomModel> modelList, GenericBottomSheetDialog.RecyclerItemClick callback) {
        final GenericBottomSheetDialog dialog = new GenericBottomSheetDialog();
        dialog.setDatalist(modelList);
        if (header != null) {
            dialog.setHeader(header);
        }
        dialog.setCallback(callback);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }


    public void callShareTrackApi(HashMap<String, String> params, final Song song, final View view) {
        showProgressDialog(getString(R.string.loading));
        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        NetworkCall.getInstance().callShareTrackApi(params, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
//                DebugLog.e("data : " + data.toString());
                String status = data.get("status").getAsString();
                String message = data.get("message").getAsString();
                int code = data.get("code").getAsInt();
                if (status.equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    String generated_url = data.get("generated_url").getAsString();
//                    Log.e("TAg", "getGeneratedUrl :-> " + data.get("generated_url").getAsString());
//                    Log.e("Song Url :","  "+song.getSongImageUrl());
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "CA7S");
                        i.putExtra(Intent.EXTRA_TEXT, generated_url);
                        startActivity(Intent.createChooser(i, "choose one"));
//                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                        StrictMode.setThreadPolicy(policy);
//
//                        String imageUrl = song.getSongImageUrl();
//                        if (imageUrl.contains("index.php/")){
//                            imageUrl = imageUrl.replace("index.php/","");
//                        }
//
//                            URL url = new URL(imageUrl);
//                        Bitmap  image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                        String sAux = generated_url;
//                        prepareShareIntent(image,sAux);
                        hideProgressDialog();
                    } catch (Exception e) {
                        hideProgressDialog();
                        e.printStackTrace();
                        //e.toString();
                    }
                } else {
                    hideProgressDialog();
                    showSnackBar(view, message);
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(view, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                T.printStackTrace();
                hideProgressDialog();
                if (T.getMessage() != null){
                    showSnackBar(view,T.getMessage().toString());
                }
            }


        });

    }

    private void prepareShareIntent(Bitmap bmp, String message) {
        Uri bmpUri = getLocalBitmapUri(bmp); // see previous remote images section
        // Construct share intent as described above based on bitmap
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            message = String.valueOf(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            message = String.valueOf(Html.fromHtml(message));
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
//        Log.e("bmpurl", "=>" + bmpUri);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, message);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//        intent.putExtra(Intent.EXTRA_SUBJECT, message); // set your subject here
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//        intent.putExtra(Intent.EXTRA_TITLE,message);
//        intent.putExtra(Intent.CATEGORY_OPENABLE,message);
        intent.setType("*/*");
//        intent.setType("image/*");

//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//        shareIntent.putExtra(Intent.EXTRA_TITLE,message);
//        shareIntent.setType("image/png");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        hideProgressDialog();
        startActivity(Intent.createChooser(intent, "Share Opportunity"));

    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ca7"+".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public void callUnlikeTrackApi(HashMap<String, String> params, final View view,
                                   final ImageView chkSongLike, final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callUnlikeTrackApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    showSnackBar(view, getString(R.string.unliked));
                    Song  mysong = callback.get(position);
                    mysong.setLikeCount((mysong.getLikeCount()-1));
                    mysong.setIsLike(false);
                    callback.set(position,mysong);
                    chkSongLike.setImageResource(R.drawable.favorite_theme_unfilled);
                    listener.onListUpdated(callback);
                    BaseFragment.homeActivity.onImageViewUpdateForMainPlayer(callback,mysong);
                    PlayListFragment.playListFragment.updateData(mysong);
//                    PlayListFragment.playListFragment.onListUpdated(callback);
                } else {
                    showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(view, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }

    public void callLikeTrackApi(HashMap<String, String> params, final View view,final ImageView chkSongLike, final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callLikeTrackApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    showSnackBar(view, getString(R.string.liked));
                    Song  mysong = callback.get(position);
                    mysong.setLikeCount((mysong.getLikeCount()+1));
                    mysong.setIsLike(true);
                    callback.set(position,mysong);
                    chkSongLike.setImageResource(R.drawable.favourite_theme_filled);
                    listener.onListUpdated(callback);
                    BaseFragment.homeActivity.onImageViewUpdateForMainPlayer(callback,mysong);
//                    if (BaseFragment.homeActivity.getSupportFragmentManager().getFragments())
                    PlayListFragment.playListFragment.updateData(mysong);
                } else {
                    showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                showSnackBar(view, baseModel.getMessage().toString());

            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }


    public void callAddFavouriteTrackApi(HashMap<String, String> params, final View view, final ImageView chkSongFavourite,final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callAddFavouriteTrackApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    chkSongFavourite.setImageResource(R.drawable.heart_theme_filled);
                    showSnackBar(view, getString(R.string.added_to_favourite));
                    Song  mysong = callback.get(position);
                    mysong.setIsFavourite(true);
                    callback.set(position,mysong);
//                    listener.onListUpdated(callback);

                    BaseFragment.homeActivity.onImageViewUpdateForMainPlayer(callback,mysong);
//                    if (BaseFragment.homeActivity.getSupportFragmentManager().getFragments())
                    PlayListFragment.playListFragment.updateData(mysong);
                } else {
                    showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(view, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                T.printStackTrace();
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }

    public void callAddFavouriteTrackApi1(HashMap<String, String> params, final View view, final ImageView chkSongFavourite,final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callAddFavouriteTrackApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    chkSongFavourite.setImageResource(R.drawable.heart_theme_filled);
                    showSnackBar(view, getString(R.string.added_to_favourite));
                    Song  mysong = callback.get(position);
                    mysong.setIsFavourite(true);
                    callback.set(position,mysong);
//                    listener.onListUpdated(callback);

                    BaseFragment.homeActivity.onImageViewUpdateForMainPlayer(callback,mysong);
//                    if (BaseFragment.homeActivity.getSupportFragmentManager().getFragments())
                    PlayListFragment.playListFragment.updateData(mysong);
                } else {
                    showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                showSnackBar(view, baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }


    public void callRemoveFavouriteTrackApi(HashMap<String, String> params, final View view, final ImageView chkSongFavourite,final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {
        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callRemoveFavouriteTrackApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    chkSongFavourite.setImageResource(R.drawable.heart_theme_unfilled);
                    showSnackBar(view, getString(R.string.removed_to_favourite));
                    Song  mysong = callback.get(position);
                    mysong.setIsFavourite(false);
                    callback.set(position,mysong);
//                    listener.onListUpdated(callback);

                    BaseFragment.homeActivity.onImageViewUpdateForMainPlayer(callback,mysong);
//                    if (BaseFragment.homeActivity.getSupportFragmentManager().getFragments())
                    PlayListFragment.playListFragment.updateData(mysong);
                } else {
                    showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(view, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                T.printStackTrace();
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }

    public void callRemoveFavouriteTrackApi1(HashMap<String, String> params, final View view, final ImageView chkSongFavourite,final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callRemoveFavouriteTrackApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getStatus());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    chkSongFavourite.setImageResource(R.drawable.heart_theme_unfilled);
                    showSnackBar(view, getString(R.string.removed_to_favourite));
                    Song  mysong = callback.get(position);
                    mysong.setIsFavourite(false);
                    callback.set(position,mysong);
//                    listener.onListUpdated(callback);

                    BaseFragment.homeActivity.onImageViewUpdateForMainPlayer(callback,mysong);
//                    if (BaseFragment.homeActivity.getSupportFragmentManager().getFragments())
                    PlayListFragment.playListFragment.updateData(mysong);
                } else {
                    showSnackBar(view, data.getMessage());
                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                showSnackBar(view, baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                T.printStackTrace();
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }


    public void callAddToPlaylistApi(HashMap<String, String> params, final View view, final ImageView chkPlaylist,final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callAddToPlaylistApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getMessage());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    Song  mysong = callback.get(position);
                    if (mysong.getIsPlaylist()) {
                        chkPlaylist.setImageResource(R.drawable.ic_playlist_add_theme);
                        showSnackBar(view, getResources().getString(R.string.add_to_playlist_song));
                        mysong.setIsPlaylist(true);
                    } else {
                        mysong.setIsPlaylist(false);
                        chkPlaylist.setImageResource(R.drawable.ic_add);
                        showSnackBar(view, getResources().getString(R.string.remove_to_playlist));
                    }

                    callback.set(position,mysong);
                    listener.onListUpdated(callback);
                } else {

                    showSnackBar(view, data.getMessage());

                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                if (baseModel != null && baseModel.getMessage() != null && !baseModel.getMessage().isEmpty()) {
                    showSnackBar(view, baseModel.getMessage().toString());
                }
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }

    public void callAddToPlaylistApi1(HashMap<String, String> params, final View view, final ImageView chkPlaylist,final ArrayList<Song> callback, final int position, final RecyclerItemListener listener) {

        showProgressDialog(getString(R.string.loading));

        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);

        NetworkCall.getInstance().callAddToPlaylistApi(params, sCookie, new iResponseCallback<BaseModel>() {
            @Override
            public void sucess(BaseModel data) {
                hideProgressDialog();
//                DebugLog.e("Status : " + data.getMessage());
                if (data.getStatus().equalsIgnoreCase(ApiParameter.SUCCESS)) {
                    Song  mysong = callback.get(position);
                    if (mysong.getIsPlaylist()) {
                        chkPlaylist.setImageResource(R.drawable.ic_playlist_add_theme);
                        showSnackBar(view, getResources().getString(R.string.add_to_playlist_song));
                        mysong.setIsPlaylist(true);
                    } else {
                        mysong.setIsPlaylist(false);
                        chkPlaylist.setImageResource(R.drawable.ic_add);
                        showSnackBar(view, getResources().getString(R.string.remove_to_playlist));
                    }

                    callback.set(position,mysong);
                    listener.onListUpdated(callback);
                } else {

                    showSnackBar(view, data.getMessage());

                }
            }

            @Override
            public void onFailure(BaseModel baseModel) {
                hideProgressDialog();
                showSnackBar(view, baseModel.getMessage().toString());
            }

            @Override
            public void onError(Call<BaseModel> responseCall, Throwable T) {
                hideProgressDialog();
                showSnackBar(view, getString(R.string.api_error_message));
            }
        });

    }


    public void loadImage(String url, ImageView imageView) {
        if (url != null && !url.isEmpty() && imageView != null) {
            String playlistImage =url;
            if (playlistImage.contains("/index.php")){
                playlistImage = playlistImage.replace("/index.php","");
            }
            if (playlistImage != null && !playlistImage.isEmpty()) {
                Glide.with(this)
                        .load(playlistImage)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(26)))
                        .into(imageView);
            }
        }
    }
}

