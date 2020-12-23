package com.music.ca7s.mediaplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.DownloadSongListener;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.localdatabase.DatabaseHandler;
import com.music.ca7s.utils.DebugLog;
import com.music.ca7s.utils.Util;
import com.music.ca7s.utils.UtilFileKt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Sudesh on 22-10-2019.
 */

public class MyDownloadManager {

    private DownloadManager downloadManager;
    private Context context;
    private long downLoadId = 0;
    private DownloadSongListener downloadSongListener;
    private File imagedirect;
    private String TAG = "MyDownloadManager";

    public MyDownloadManager(Context context, DownloadSongListener downloadSongListener) {
        this.context = context;
        this.downloadSongListener = downloadSongListener;
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);
        //onDownloadCompliteListener = (OnDownloadCompliteListener) context;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("onRecieve : ",intent.getAction()+"   "+intent.getData());
            Toast toast = Toast.makeText(context,
                    R.string.downloading_complite, Toast.LENGTH_SHORT);
            downloadSongListener.onDownloadSuccess();
            context.unregisterReceiver(downloadReceiver);
//            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }
    };


    public void DownloadData(Uri uri, final Song title) {
        if (!title.getThirdparty_song()) {
            title.setSongURL(ApiParameter.BASE_MUSIC_URL + title.getSongURL());
        }
        File direct = new File(context.getExternalCacheDir() + "/CA7S.nomedia");
        imagedirect = new File(context.getExternalCacheDir() + "/Images.nomedia");
        boolean success = true;
        boolean imagesuccess = true;
        if (!direct.exists()) {
            success = direct.mkdir();
        }
        if (!imagedirect.exists()) {
            imagesuccess = imagedirect.mkdir();
        }
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);
        if (imagesuccess) {
            String playlistImage = title.getSongImageUrl();
            if (playlistImage.contains("/index.php")) {
                playlistImage = playlistImage.replace("/index.php", "");
            }
            if (playlistImage != null && !playlistImage.isEmpty()) {
                Glide.with(context)
                        .asBitmap().load(playlistImage)
                        .listener(new RequestListener<Bitmap>() {
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
//                                      Toast.makeText(cxt,getResources().getString(R.string.unexpected_error_occurred_try_again),Toast.LENGTH_SHORT).show();
                                          return false;
                                      }

                                      @Override
                                      public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                          UtilFileKt.saveImage(context, bitmap, imagedirect.getPath() + "/" + title.getSongID() + ".jpg");
                                          return false;
                                      }
                                  }
                        ).submit();
            }
        } else {
            success = imagedirect.mkdirs();
//            Log.i("TAG", "Folder Created failed");
        }
        String songDbPath = direct.getPath() + "/" + title.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia";
//        Log.e("songDbPath : ",songDbPath);
        title.setSongPath(songDbPath);
        title.setSongNumber(title.getSongID());
        title.setSongURL(title.getSongURL());
        title.setIsPlaylist(false);
        title.setLyrics(title.getLyrics());
        title.setSongImagePath(imagedirect.getPath() + "/" + title.getSongID() + ".jpg");
        title.setSongArtist(title.getSongArtist());
        title.setSongImageUrl(title.getSongImageUrl());
        title.setFrom(AppConstants.FROM_MY_MUSIC);
        title.setCreatedAt(Util.getCurrentDateTime());
//        downloadZipFileRx(title);
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        databaseHandler.addSongItem(title, AppConstants.TABLE_DOWNLOADED_SONG);
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
//        Log.e("Main_user : ", user_id + "");
        if (user_id != null && !user_id.isEmpty()) {
//            Log.e("user_id : ", user_id + "");
            if (title.getUser_id() != null && !title.getUser_id().isEmpty() && !title.getUser_id().toString().equalsIgnoreCase(user_id)) {
//                Log.e("getUser_id : ", title.getUser_id() + "");
                hitDownloadCountApi(title.getSongID());
            }
        } else {
            hitDownloadCountApi(title.getSongID());
        }

        if (success) {
//            Log.i("TAG", "Folder Created Sucess");
            // Do something on success
            long downloadReference;
            downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            //Setting title of request
            request.setTitle(title.getSongTitle());
            //Setting description of request
            request.setDescription("Downloading...");
            File newFile = new File(songDbPath);
            Uri mySongUri = Uri.fromFile(newFile);
            Log.e("mySongUri : ", " " + mySongUri);
            request.setDestinationUri(mySongUri);

            Toast toast = Toast.makeText(context,
                    R.string.downloading_started, Toast.LENGTH_SHORT);
            toast.show();
            //Enqueue download and save the referenceId
//            downloadReference = downloadManager.enqueue(request);
            downLoadId = downloadManager.enqueue(request);
            final Timer myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downLoadId);
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    final int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    cursor.close();
                    final int total = (int) (bytes_total / 1000) % 60;
                    final int downloaded = (int) (bytes_downloaded / 1000) % 60;
                    final int dl_progress = (int) ((bytes_downloaded * 1f / bytes_total) * 100);
//                        Log.e("Total Checking : ",downloaded+"     "+total+"     "+dl_progress);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dl_progress == 100) {
                                myTimer.cancel();
                            }
                            if (downloaded == 0 && total == 0 && dl_progress == 0) {

                            } else {
                                downloadSongListener.changeProgress(title.getSongID(), dl_progress);
                            }
//                                progressBar.setProgress();
                        }
                    });
                }
            }, 100, 1000);
            Log.d("url", title.getSongURL());


        } else {
            success = direct.mkdirs();
            Log.i("TAG", "Folder Created faild");
            // Do something else on failure
        }

    }

    private void hitDownloadCountApi(String songID) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", songID);
        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        NetworkCall.getInstance().callDownloadandStreamApi(AppConstants.DOWNLOAD, hashMap, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
                DebugLog.e("data : " + data.toString());
            }

            @Override
            public void onFailure(BaseModel baseModel) {

            }

            @Override
            public void onError(Call<JsonObject> responseCall, Throwable T) {
                T.printStackTrace();

            }


        });

    }

    @SuppressLint("CheckResult")
    private void downloadZipFileRx(Song song) {
        RetrofitInterface downloadService = createService(RetrofitInterface.class, ApiParameter.BASE_URL);
        Log.e("downloadZip", song.getSongURL());
        downloadService.downloadFileByUrlRx(song.getSongURL())
                .flatMap(new io.reactivex.functions.Function<Response<ResponseBody>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                        return saveToDiskRx(responseBodyResponse);
                    }
                })
//                .flatMap(processResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e("Object :  ", "o : " + o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .subscribe(handleResult());

    }

    public interface RetrofitInterface {

        // Regular Retrofit 2 GET request
        @Streaming
        @GET
        Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);


        // Retrofit 2 GET request for rxjava
        @Streaming
        @GET
        Observable<Response<ResponseBody>> downloadFileByUrlRx(@Url String fileUrl);
    }


    private Function<Response<ResponseBody>, Observable<Song>> processResponse() {
        return new Function<Response<ResponseBody>, Observable<Song>>() {
            @Override
            public Observable<Song> apply(Response<ResponseBody> input) {
                return saveToDiskRx(input);
            }
        };
    }

//    Function<Response<ResponseBody>, Observable<Song>>() {
//        @Override
//        public Observable<Song> call(Response<ResponseBody> responseBodyResponse) {
//            return saveToDiskRx(responseBodyResponse);
//        }
//    }

    private Observable<Song> saveToDiskRx(final Response<ResponseBody> response) {
        return Observable.create(new ObservableOnSubscribe<Song>() {
            @Override
            public void subscribe(ObservableEmitter<Song> emitter) throws Exception {
                Log.e("response :  ", "body : " + response.body());
                Log.e("emitter :  ", "body : " + emitter.serialize());
            }
        });
//        return Observable.create(new Observable.OnSubscribe<>() {
//            @Override
//            public void call(Subscriber<? super Song> subscriber) {
////                try {
//                    Log.e("response :  ","body : "+response.body());
////                    String header = response.headers().get("Content-Disposition");
////                    String filename = header.replace("attachment; filename=", "");
////                    File destinationFile = new File("/data/data/" + context.getPackageName() + "/ca7s/" + filename);
////                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
////                    bufferedSink.writeAll(response.body().source());
////                    bufferedSink.close();
////                    subscriber.onNext(destinationFile);
////                    subscriber.onCompleted();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                    subscriber.onError(e);
////                }
//            }
//        });
    }

    private Observer<Object> handleResult() {
        return new Observer<Object>() {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "Error " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object file) {
                Log.d(TAG, "File downloaded to " + file.toString());
            }
        };
    }

    public <T> T createService(Class<T> serviceClass, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }
}
