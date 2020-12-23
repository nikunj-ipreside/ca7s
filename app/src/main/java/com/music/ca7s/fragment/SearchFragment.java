package com.music.ca7s.fragment;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.IACRCloudListener;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.dialog.NoResultFragmentDialog;
import com.music.ca7s.dialog.TapScanFragmentDialog;
import com.music.ca7s.enumeration.FragmentState;
import com.music.ca7s.listener.OnTryAginLIstener;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.WaveView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.music.ca7s.AppLevelClass.isSearchedDialog;


public class SearchFragment extends BaseFragment implements IACRCloudListener, OnTryAginLIstener {


    Unbinder unbinder;
    @BindView(R.id.iv_close_player)
    ImageView imgTopbarLeft;
    @BindView(R.id.txtTopbarTitle)
    TextView txtTopbarTitle;
    @BindView(R.id.imgTopbarRight)
    ImageView imgTopbarRight;
    @BindView(R.id.relativeTopBar)
    RelativeLayout relativeTopBar;
    ImageView imgZoominBg;
    @BindView(R.id.iv_start_image)
    ImageView iv_start_image;
    @BindView(R.id.iv_start_image_gif)
    GifImageView iv_start_image_gif;
    @BindView(R.id.tv_tap)
    TextView tv_tap;

    @BindView(R.id.iv_close)
    ImageView iv_close;


    @BindView(R.id.iv_bottom)
    WaveView iv_bottom;

    private MediaRecorder mRecorder;
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 3;

    private int image= 0;
    private Animation startRotateAnimation;
    private Animation mShakeAnimation;
    private Animation zoomInAnimation;
    private Animation zoomOutAnimation;
    private Animation sizingAnimation;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tv_tap.setText(getString(R.string.tap_to_scan));
            stopRecroding();
            setWaves();
        }
    };



    Handler handler;
    private String sCookie;
    private String uID;


    private ACRCloudClient mClient;
    private ACRCloudConfig mConfig;

    private TextView mVolume, mResult, tv_time;

    private boolean mProcessing = false;
    private boolean initState = false;

    private String path = "";

    private long startTime = 0;
    private long stopTime = 0;
    private String[] waves;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        unbinder = ButterKnife.bind(this, view);

        if (AppLevelClass.getInstance().getTutorialPrefrences().getString(SharedPref.SEARCH_SCREEN).equalsIgnoreCase("1")) {

        } else {
            AppLevelClass.getInstance().getTutorialPrefrences().putString(SharedPref.SEARCH_SCREEN, "1");
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.tooltip_layout_center_bootom);
            TextView tv_msg = dialog.findViewById(R.id.tooltip_text);
            tv_msg.setText(R.string.tap_to_scan);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = 100;   //x position
            wmlp.y = 600;
            dialog.show();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity.setSlidingState(false);
        txtTopbarTitle.setText(R.string.recognize);
        imgTopbarRight.setImageResource(R.drawable.ic_history);
        imgTopbarRight.setVisibility(View.GONE);
//        iv_bottom.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
//        iv_bottom.setWaveColor(ContextCompat.getColor(getActivity(), R.color.colorAccent_light));
        setWaves();
        sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        uID = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/" + AppConstants.songRecordedUrl;
        path = Environment.getExternalStorageDirectory().toString()
                    + "/acrcloud/model";

            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            this.mConfig = new ACRCloudConfig();
            this.mConfig.acrcloudListener = this;
            // If you implement IACRCloudResultWithAudioListener and override "onResult(ACRCloudResult result)", you can get the Audio data.
            //this.mConfig.acrcloudResultWithAudioListener = this;

            this.mConfig.context = getContext();
            this.mConfig.host = "identify-ap-southeast-1.acrcloud.com";
            this.mConfig.dbPath = path; // offline db path, you can change it with other path which this app can access.
            this.mConfig.accessKey = "581fd0f0ee35ff8ea38202a80c1a77a8";
            this.mConfig.accessSecret = "NVsTiw1YAj0PA9Z7eyIRj5p3aLemzWsYEI1klHD3";
            this.mConfig.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTPS; // PROTOCOL_HTTP
            this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
            //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
            //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;

            this.mClient = new ACRCloudClient();
            // If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
            // the function initWithConfig is used to load offline db, and it may cost long time.
            this.initState = this.mClient.initWithConfig(this.mConfig);
            if (this.initState) {
            this.mClient.startPreRecord(3000); //start prerecord, you can call "this.mClient.stopPreRecord()" to stop prerecord.
            }
    }


    public  Drawable getImage(String name) {
        return homeActivity.getResources().getDrawable(homeActivity.getResources().getIdentifier(name, "drawable", homeActivity.getPackageName()));
    }

    @Override
    public void onStart() {
        super.onStart();
        homeActivity.showBottomMenu(false);
    }

    @Override
    public void onSearchAgain() {
        startRecording();
    }

    public void start(){
        if (!this.initState) {
            return;
        }
        if (!mProcessing) {
            mProcessing = true;
//            startAnimation();
            startImageAnimation();
            if (this.mClient == null || !this.mClient.startRecognize()) {
                    mProcessing = false;
            }
            startTime = System.currentTimeMillis();
        }
    }

    public void stop(){
        if (mProcessing && this.mClient != null) {
            this.mClient.stopRecordToRecognize();
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
        }
        mProcessing = false;
        stopTime = System.currentTimeMillis();
        setWaves();
    }

    private void startImageAnimation(){
        if (iv_start_image != null) {
            if (mProcessing) {
                tv_tap.setText(getString(R.string.listening));
                setWaves();
            } else {
                tv_tap.setText(getString(R.string.tap_to_scan));
                setWaves();
            }
        }
    }

    private void setWaves(){
       if (mProcessing){
               iv_bottom.play();
           iv_start_image_gif.setVisibility(View.VISIBLE);
           iv_start_image.setVisibility(View.GONE);
           iv_close.setVisibility(View.VISIBLE);
       }else {
           if (iv_bottom.isPlaying()) {
               iv_bottom.pause();
           }
           tv_tap.setText(getString(R.string.tap_to_scan));
           iv_start_image_gif.setVisibility(View.GONE);
           iv_start_image.setVisibility(View.VISIBLE);
           iv_close.setVisibility(View.GONE);
       }
    }

    @Override
    public void onResume() {
        super.onResume();
        homeActivity.showBottomMenu(false);

    }

    // Old api
    @Override
    public void onResult(String result) {
        if (this.mClient != null) {
            this.mClient.cancel();
            mProcessing = false;
        }
        String tres = "\n";
        try {
            JSONObject j = new JSONObject(result);
            JSONObject j1 = j.getJSONObject("status");
            int j2 = j1.getInt("code");
            if(j2 == 0){
                JSONObject metadata = j.getJSONObject("metadata");
                //
                if (metadata.has("humming")) {
                    JSONArray hummings = metadata.getJSONArray("humming");
                    for(int i=0; i<hummings.length(); i++) {
                        JSONObject tt = (JSONObject) hummings.get(i);
                        String title = tt.getString("title");
                        JSONArray artistt = tt.getJSONArray("artists");
                        JSONObject art = (JSONObject) artistt.get(0);
                        String artist = art.getString("name");
                        tres = tres + (i+1) + ".  " + title + "\n";

                    }
                }
                if (metadata.has("music")) {
                    JSONArray musics = metadata.getJSONArray("music");
//                    Log.e("MUSIC DATA : ",musics.toString());
                    for(int i=0; i<musics.length(); i++) {
                        JSONObject tt = (JSONObject) musics.get(i);
                        String title = tt.getString("title");
                        JSONArray artistt = tt.getJSONArray("artists");
                        JSONObject art = (JSONObject) artistt.get(0);

                                String artist = art.getString("name");
//                        String imageUrl=art.getString("imgUrl");
                        String imageUrl="";
                        tres = tres + (i+1) + ".  Title: " + title + "    Artist: " + artist + "\n";

                        Log.d("title",title);

//                        List<TapScanData> tapScanData = new ArrayList<TapScanData>();
                        TapScanFragmentDialog dialog = TapScanFragmentDialog.newInstance(title,artist,imageUrl,homeActivity);
                        if (!isSearchedDialog) {
                            isSearchedDialog = true;
                            dialog.show(getActivity().getSupportFragmentManager(), title);

                        }
                        tv_tap.setText(getString(R.string.tap_to_scan));
                        stopRecroding();
                        setWaves();
                    }
                }
                if (metadata.has("streams")) {
                    JSONArray musics = metadata.getJSONArray("streams");
                    for(int i=0; i<musics.length(); i++) {
                        JSONObject tt = (JSONObject) musics.get(i);
                        String title = tt.getString("title");
                        String channelId = tt.getString("channel_id");
                        tres = tres + (i+1) + ".  Title: " + title + "    Channel Id: " + channelId + "\n";
                    }
                }
                if (metadata.has("custom_files")) {
                    JSONArray musics = metadata.getJSONArray("custom_files");
                    for(int i=0; i<musics.length(); i++) {
                        JSONObject tt = (JSONObject) musics.get(i);
                        String title = tt.getString("title");
                        tres = tres + (i+1) + ".  Title: " + title + "\n";
                    }
                }
                tres = tres + "\n\n" + result;



            }else{
                tres = result;
            }
        } catch (JSONException e) {
            tres = result;
            e.printStackTrace();
        }

        try {
            JSONObject j = new JSONObject(result);
            JSONObject j1 = j.getJSONObject("status");
            String message = j1.get("msg").toString();
            if (message.equalsIgnoreCase("No result")){
                NoResultFragmentDialog dialog = NoResultFragmentDialog.newInstance(homeActivity.getString(R.string.no_results),this);
                dialog.show(getActivity().getSupportFragmentManager(), homeActivity.getString(R.string.no_results));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Log.d("result",""+tres);
//        mResult.setText(tres);
    }


    @Override
    public void onVolumeChanged(double volume) {
        long time = (System.currentTimeMillis() - startTime) / 1000;
//        mVolume.setText(getResources().getString(R.string.volume) + volume + "\n\nRecord Time: " + time + " s");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        unbinder.unbind();
    }


    @OnClick({R.id.iv_close_player, R.id.imgTopbarRight,  R.id.iv_start_image,R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_player:
                homeActivity.openDrawer();
                break;
            case R.id.imgTopbarRight:
                homeActivity.openSearchHistoryFragment(null, FragmentState.REPLACE);
                break;
            case R.id.iv_start_image:// start recording
                onSearchAgain();
//                startRecording();
//                startAnimation();
                break;

            case R.id.iv_close:
                stop();
                break;

        }
    }

    // start recording
    private void startRecording() {
        if (CheckPermissions()) {
            // start timer for 15 second record
            handler = new Handler();
            handler.postDelayed(runnable, 10000);
            start();
        } else {
            RequestPermissions();
        }
    }

    // stop recording and call TapToScan API
    private void stopRecroding() {
        stop();;
    }

    // check for permission
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    // request for permission
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        startRecording();
                    }
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if (this.mClient != null) {
            this.mClient.release();
            this.initState = false;
            this.mClient = null;
//            this.mClient.stopPreRecord();
        }
    }
}
