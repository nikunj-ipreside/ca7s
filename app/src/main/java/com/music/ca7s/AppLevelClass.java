package com.music.ca7s;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.AppGlideModule;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.genre_list.GenreDatum;
import com.music.ca7s.receiver.ConnectivityReceiver;
import com.music.ca7s.utils.PreferanceHelper;
import com.music.ca7s.utils.TutorialPrefrences;
import com.music.ca7s.utils.Util;
import com.squareup.otto.Bus;
import com.yariksoffice.lingver.Lingver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.internal.ListenerClass;
import io.fabric.sdk.android.Fabric;

public class AppLevelClass extends Application {
    private static AppLevelClass application;
    private PreferanceHelper preferanceHelper;
    private TutorialPrefrences tutorialPrefrences;
    private Bus bus;
    public static synchronized AppLevelClass getInstance() {
        return application;
    }
    public static int page = 1;
    public SharedPreferences sharedPref;
    public static List<Song> DownloadingsongList = new ArrayList<>();
    public static Context context;
    public static String SCREENTYPE = "";
    public static ArrayList<GenreDatum> BROWSELIST = new ArrayList<>();
    public static  Boolean ispaused=true;
    public static ImageView iv_static_star_LIke= null;


    //Music Service

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.audioplayer.PlayNewAudio";
    public static final String Broadcast_ACTION = "com.audioplayer.actions";
    public static final String Broadcast_NOTIFY = "com.audioplayer.notify";
    public  static HashMap<String,Object> previous_song=null;
    public static String PlayAction=null;
    public static String NotiFyAction=null;
    public static String AUDIOINDEX = "audioIndex";
    public static String AUDIONAME = "audioName";
    public static String AUDIOPROGRESS = "audioProgress";
    public static String PLAY = "play";
    public static String RESUME = "resume";
    public static String PAUSE = "pause";
    public static String STOP = "stop";
    public static Boolean isDataSaverDialogShowing = false;
    public static boolean IsBack=false;
    public static boolean isSearchedDialog = false;

    public PreferanceHelper getPreferanceHelper() {
        return preferanceHelper;
    }

    public TutorialPrefrences getTutorialPrefrences(){
        return tutorialPrefrences;
    }


    public Bus getBus() {
        return bus;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        application = this;
        bus = new Bus();
        preferanceHelper = new PreferanceHelper(getApplicationContext());
        tutorialPrefrences =  new TutorialPrefrences(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AppLevelClass.getInstance().getPreferanceHelper().putString(SharedPref.FCMTOKEN, refreshedToken);
        String selectedLAnguage = tutorialPrefrences.getString(SharedPref.LANGUAGE);
        String language =Locale.getDefault().getLanguage();
        if (selectedLAnguage.toString().equalsIgnoreCase("Portuguese")){
            language = "pt";
        }else if (selectedLAnguage.toString().equalsIgnoreCase("English")){
            language = "en";
        }

        Lingver.init(this, language);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
