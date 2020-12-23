package com.music.ca7s.mediaplayer;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.music.ca7s.AppLevelClass;
import com.music.ca7s.R;
import com.music.ca7s.activity.HomeActivity;
import com.music.ca7s.apicall.NetworkCall;
import com.music.ca7s.apicall.iResponseCallback;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;
import com.music.ca7s.listener.MyMusicCallbackListener;
import com.music.ca7s.model.BaseModel;
import com.music.ca7s.utils.AppConstants;
import com.music.ca7s.utils.DebugLog;

import retrofit2.Call;

import static com.music.ca7s.AppLevelClass.Broadcast_ACTION;
import static com.music.ca7s.AppLevelClass.Broadcast_NOTIFY;
import static com.music.ca7s.AppLevelClass.Broadcast_PLAY_NEW_AUDIO;
import static com.music.ca7s.AppLevelClass.PAUSE;
import static com.music.ca7s.AppLevelClass.PLAY;
import static com.music.ca7s.AppLevelClass.PlayAction;
import static com.music.ca7s.AppLevelClass.RESUME;
import static com.music.ca7s.AppLevelClass.context;


public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {
    public static final String ACTION_PLAY = "com.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.audioplayer.ACTION_NEXT";
    public static final String ACTION_NEXT_COMPLETE = "com.audioplayer.ACTION_NEXT_COMPLETE";
    public static final String ACTION_STOP = "com.audioplayer.ACTION_STOP";
    public static final String ACTION_DESTROY = "com.audioplayer.ACTION_DESTROY";
    PendingIntent play_pauseAction = null;
    //AudioPlayer notification ID
    public static final int NOTIFICATION_ID = 101;
    public static MediaPlayer mediaPlayer;
    public static Boolean isInProcess = false;
    public static long maxdur, curdur;
    //Used to pause/resume MediaPlayer
    public static int resumePosition;
    private static Bitmap imageBitmap;
    //AudioFocus
    public static AudioManager audioManager;
    //List of available Audio files
    public static ArrayList<Song> audioList = new ArrayList<Song>();
    public static int audioIndex = -1;
    public static Song activeAudio; //an object on the currently playing audio
    //Handle incoming phone calls
    public static boolean ongoingCall = false;
    public static PhoneStateListener phoneStateListener;
    public static TelephonyManager telephonyManager;
    public static MediaSessionCompat mediaSession;
    public static MediaControllerCompat.TransportControls transportControls;
    public static NotificationCompat.Builder notificationBuilder;
    // Binder given to clients
    public final IBinder iBinder = new LocalBinder();
    //MediaSession
    private MediaSessionManager mediaSessionManager;
    public static MyMusicCallbackListener listener;
    RemoteViews contentView;
//    PendingIntent play_pauseAction;

    private Boolean isRepeat = false;
    private Boolean isShuffle = false;
    private String CHANNEL_ID = "music";
    public static MediaPlayerService  myMediaPlayerService = null;



    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        myMediaPlayerService = this;
        // Perform one-time setup procedures
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver();
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio();
        register_actionreciver();
        handler = new Handler();
    }

    /**
     * ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs
     */
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("CHECK DATA2 : ","  "+intent+"   "+intent.getAction());
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };
    /**
     * Play new Audio
     */
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("CHECK DATA1 : ","  "+intent+"   "+intent.getAction());
//            Log.e("98", "playNewAudio");
            //Get the new media index form SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            audioList = storage.loadAudio();
            audioIndex = storage.loadAudioIndex();
            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }
            //A PLAY_NEW_AUDIO action received
            //reset mediaPlayer to play the new Audio

            if (mediaPlayer != null) {
                stopMedia();
                mediaPlayer.reset();
            }

            initMediaPlayer();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);
        }
    };
    private BroadcastReceiver ActionPlay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_PAUSE)) {
                if (contentView != null) {
                    contentView.setImageViewResource(R.id.imgPlay, R.drawable.ic_play);
                }
                AppLevelClass.ispaused = true;
                buildNotification(PlaybackStatus.PAUSED);
            } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_PLAY)) {
                if (contentView != null) {
                    contentView.setImageViewResource(R.id.imgPlay, R.drawable.ic_pause);
                }
                AppLevelClass.ispaused = true;
                buildNotification(PlaybackStatus.PLAYING);
            } else if (AppLevelClass.NotiFyAction != null && AppLevelClass.NotiFyAction.equalsIgnoreCase(ACTION_PLAY)) {
                if (contentView != null) {
                    contentView.setImageViewResource(R.id.imgPlay, R.drawable.ic_pause);
                }
                AppLevelClass.ispaused = true;
                buildNotification(PlaybackStatus.PLAYING);
            }
        }

    };

    public void pauseMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isInProcess = true;
            resumePosition = mediaPlayer.getCurrentPosition();
            listener.onPauseMediaPlayer();
            buildNotification(PlaybackStatus.PAUSED);
        }
    }

    public void resumeMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && listener != null) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
            listener.onPlayMediaPlayer();
            updateseekbarposition();
            buildNotification(PlaybackStatus.PLAYING);
        }
    }

    public Boolean isPng() {
        if (mediaPlayer != null) {
            try {
                return mediaPlayer.isPlaying();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateseekbarposition() {
        if (mediaPlayer != null) {
            listener.onChangeSeekposition(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            handler.postDelayed(runnable, 1000);
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Do what ever you want
            try {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    updateseekbarposition();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Service lifecycle methods
     */
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e("onStartCommand : ","  "+intent+"   "+intent.getAction());
        try {
            //Load data from SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            audioList = storage.loadAudio();
            audioIndex = storage.loadAudioIndex();

            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
//                Log.e("audioIndex 123", audioIndex + "");
            } else {
                stopSelf();
            }
        } catch (NullPointerException e) {
            stopSelf();
//            Log.e("e", e + "");
            e.printStackTrace();
//            Log.e("e", "Exception=====================Exception========================Exception");
        }

        //Request audio focus
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }

        if (mediaSessionManager == null) {
            try {
                initMediaSession();
//                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }

        }
        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        removeNotification();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        //clear cached playlist
        new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();

        removeAudioFocus();
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        removeNotification();
        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);
        unregisterReceiver(ActionPlay);


    }

    /**
     * MediaPlayer callback methods
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed.
//        if (HomeActivity.playAll) {
//        Log.e("onSeekComplete: 304", "Ok");
        hitStreamApi();
        if (isRepeat) {
            if (mediaPlayer != null){
                try {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            initMediaPlayer();
        } else {
            if (audioIndex < audioList.size()) {
                transportControls.skipToNext();
                AppLevelClass.NotiFyAction = ACTION_NEXT_COMPLETE;
                Intent broadcastIntent = new Intent(Broadcast_NOTIFY);
                sendBroadcast(broadcastIntent);
            } else if (audioIndex == audioList.size()) {
                StorageUtil storageUtil = new StorageUtil(this);
                audioList = storageUtil.loadAudio();
                audioIndex = 0;
                setList(audioList, audioIndex, this,storageUtil.loadTYpe());
                initMediaPlayer();
            }
        }
//        } else {
//            stopMedia();
//            removeNotification();
//            //stop the service
//            stopSelf();
//        }
    }

    private void hitStreamApi() {
        String user_id = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.USER_ID);
        Log.e("Main_user : ", user_id + "");
        if (user_id != null && !user_id.isEmpty()) {
            Log.e("user_id : ", user_id + "");
            if (activeAudio.getUser_id() != null && !activeAudio.getUser_id().isEmpty() && !activeAudio.getUser_id().toString().equalsIgnoreCase(user_id)) {
                Log.e("getUser_id : ", activeAudio.getUser_id() + "");
                hitDownloadCountApi(activeAudio.getSongID());
            }
        } else {
            hitDownloadCountApi(activeAudio.getSongID());
        }
    }

    private static void hitDownloadCountApi(String songID) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", songID);
        String sCookie = AppLevelClass.getInstance().getPreferanceHelper().getString(SharedPref.LARAVEL_COOCKIE);
        NetworkCall.getInstance().callDownloadandStreamApi(AppConstants.STREAM, hashMap, sCookie, new iResponseCallback<JsonObject>() {
            @Override
            public void sucess(JsonObject data) {
//                DebugLog.e("data : " + data.toString());
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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        playMedia();


    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
//        Log.e("Here : ","447");

        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusState) {

        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (!AppLevelClass.ispaused) {
                    if (mediaPlayer == null) initMediaPlayer();
                    else if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
//                    mediaPlayer.setVolume(2.0f, 2.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }

                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();

                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer != null && mediaPlayer.isPlaying())
//                    mediaPlayer.setVolume(0.1f, 0.1f);
                    break;
        }
    }

    /**
     * AudioFocus
     */
    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        Boolean isTrue =false;
        try {
            if (audioManager != null) {
                isTrue = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
            }
        }catch (Exception e){
            isTrue = false;
            e.printStackTrace();
        }

        return isTrue;
    }

    /**
     * MediaPlayer actions
     */
    public void initMediaPlayer() {
//        if (mediaPlayer == null)
        mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.stop();
        mediaPlayer.reset();
//        mediaPlayer.release();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String url = "";
            activeAudio = audioList.get(audioIndex);
            if (activeAudio.getFrom().toString().equals(AppConstants.FROM_MY_MUSIC)) {
                url = activeAudio.getSongPath();
//                if (url.contains(AppConstants.TEMPRARY_MUSIC_EXTENTION)){
//                    url = url.replace(AppConstants.TEMPRARY_MUSIC_EXTENTION,AppConstants.PERMANENT_MUSIC_EXTENTION);
//                }
            } else {
                String externalUrl = "";
                if (!activeAudio.getThirdparty_song()) {
                    externalUrl = ApiParameter.BASE_MUSIC_URL;
                }
                url = externalUrl + activeAudio.getSongURL();
            }
            Log.e("audio Name324", url + "");
            Uri mp3 = Uri.parse(url);
            Log.e("audio mp3", mp3 + "");
            mediaPlayer.setDataSource(getApplicationContext(), mp3);
//            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
//                }
//            });
            imageBitmap = null;
            imageBitmap = getBitmapFromURL(activeAudio.getSongImageUrl());
            buildNotification(PlaybackStatus.PLAYING);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("336", "Exception============================Exception==============================Exception");
            stopSelf();
        }
        if (listener != null) {
            StorageUtil storageUtil = new StorageUtil(getApplicationContext());
            ArrayList<Song> mySongs =storageUtil.loadAudio();
            String type = storageUtil.loadTYpe();

            listener.onUpdatePlayerList(audioIndex, mySongs,type);
            listener.onsetSongData(activeAudio, audioIndex);
            listener.setCurrenPosition(audioIndex);

        }


    }

    public void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            maxdur = mediaPlayer.getDuration();
            listener.onStartedMediaPlayer();
            listener.onPlayMediaPlayer();
            if (mediaPlayer.isPlaying()) {
                getCurrentProgress();
            }
            isInProcess = true;
            updateseekbarposition();
            buildNotification(PlaybackStatus.PLAYING);
        }

    }

    public void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            isInProcess = false;
            listener.onStopMediaPlayer();
//            removeNotification();
        }
    }

    public void skipToNext() {
        audioList = new StorageUtil(getApplicationContext()).loadAudio();
        if (audioList != null) {
            if (audioIndex == audioList.size() - 1) {
                //if last in playlist
                audioIndex = 0;
                activeAudio = audioList.get(audioIndex);
            } else {
                //get next in playlist
                if (isShuffle) {
                    audioIndex = new Random().nextInt(audioList.size());
                    activeAudio = audioList.get(audioIndex);
                } else {
                    if (audioList.size() > audioIndex) {
                        audioIndex = (audioIndex + 1);
                        activeAudio = audioList.get(audioIndex);
                    }
                }
            }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //reset mediaPlayer
            mediaPlayer.reset();
        }
        initMediaPlayer();
        }
    }

    public void skipToPrevious() {
        audioList = new StorageUtil(getApplicationContext()).loadAudio();
        if (audioIndex == 0) {
            //if first in playlist
            //set index to the last of audioList
            audioIndex = audioList.size() - 1;
            activeAudio = audioList.get(audioIndex);
        } else {
            //get previous in playlist
            if (isShuffle) {
                audioIndex = new Random().nextInt(audioList.size());
            } else {
                if (audioIndex < audioList.size() ) {
                    activeAudio = audioList.get(--audioIndex);
                }
            }
        }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //reset mediaPlayer
            mediaPlayer.reset();
        }
        initMediaPlayer();
    }

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    /**
     * Handle PhoneState changes
     */
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                pauseMedia();
                                if (!AppLevelClass.ispaused) {
//                                    resumeMedia();
                                }
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * MediaSession and Notification actions
     */
    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        //Set mediaSession's MetaData

        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();

                updateMetaData();

                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();

                updateMetaData();

                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
//                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
//                Log.e("531", position + "");
                curdur = position;
            }
        });
    }

    private void updateMetaData() {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_back); //replace with medias albumArt
        if (imageBitmap == null) {

        } else {
            albumArt = imageBitmap;
        }
        // Update the current metadata
        if (activeAudio != null)
            mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, activeAudio.getSongTitle())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, activeAudio.getSongArtist())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, activeAudio.getSongAlbum())
                    .build());
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            imageBitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
        return imageBitmap;
    }

    private void buildNotification(PlaybackStatus playbackStatus) {
        if (activeAudio != null && activeAudio.getSongImageUrl() != null && !activeAudio.getSongImageUrl().isEmpty()) {
            getBitmapAsyncAndDoWork(playbackStatus, activeAudio.getSongImageUrl());
        }
    }

    // Load bitmap from image url on background thread and display image notification
    private void getBitmapAsyncAndDoWork(PlaybackStatus playbackStatus, String imageUrl) {

        final Bitmap[] bitmap = {null};

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        bitmap[0] = resource;
                        if (bitmap[0] != null) {
                            displayImageNotification(playbackStatus, bitmap[0]);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public int checkPlayAction(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            return 1;
        }else {
            return 0;
        }
    }

    private void displayImageNotification(PlaybackStatus playbackStatus, Bitmap bitmap) {
        /**
         * Notification actions -> playbackAction()
         *  0 -> Play
         *  1 -> Pause
         *  2 -> Next track
         *  3 -> Previous track
         *  4 -> Stop
         */
        if (activeAudio != null) {
            int notificationAction = R.drawable.ic_play;//needs to be initialized
            //Build a new notification according to the current state of the MediaPlayer
            if (playbackStatus == PlaybackStatus.PLAYING) {
                notificationAction = R.drawable.ic_pause;
                play_pauseAction = playbackAction(1);
            } else {
                play_pauseAction = playbackAction(0);
            }
            contentView = new RemoteViews(getPackageName(), R.layout.layout_song_notification);
            contentView.setImageViewBitmap(R.id.imgSongImage, bitmap);
            contentView.setTextViewText(R.id.txtTitle, activeAudio.getSongTitle());
            contentView.setTextViewText(R.id.txtDescription, activeAudio.getSongArtist());
            contentView.setImageViewResource(R.id.imgPlay, notificationAction);
            contentView.setOnClickPendingIntent(R.id.imgPrevious, playbackAction(3));
            contentView.setOnClickPendingIntent(R.id.imgPlay,play_pauseAction);
            contentView.setOnClickPendingIntent(R.id.imgNext, playbackAction(2));
            contentView.setOnClickPendingIntent(R.id.imgclose, playbackAction(4));
            long[] vibration = new long[]{0L};

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            createChannel(notificationManager);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setContentText(getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setColor(Color.parseColor("#ffffff"))
                    .setAutoCancel(false)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setOngoing(true)
                    .setSound(null)
                    .setVibrate(vibration)
                    .setContent(contentView);
//
//            Notification notification = mBuilder.build();
//                    notificationManager.notify(NOTIFICATION_ID, notification);

            CharSequence name = getString(R.string.app_name);
            String description = activeAudio.getSongTitle();

            NotificationChannel channel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.setSound(null,null);
                channel.setVibrationPattern(vibration);
                mBuilder.setChannelId(channel.getId());
                mBuilder.setSound(null);
                mBuilder.setVibrate(vibration);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

//
            String listData = new Gson().toJson(audioList);
            Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
            notificationIntent.putExtra(ApiParameter.FROM_NOTIFICATION, "yes");
            notificationIntent.putExtra(ApiParameter.INDEX, audioIndex);
            notificationIntent.putExtra(ApiParameter.LIST, listData);
            if (mediaPlayer != null) {
                notificationIntent.putExtra(ApiParameter.SEEKTO, mediaPlayer.getCurrentPosition());
            }else {
                notificationIntent.putExtra(ApiParameter.SEEKTO, 0);
            }
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(),NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.build().flags |= Notification.FLAG_NO_CLEAR;
            mBuilder.setContentIntent(pendingNotificationIntent);

            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    public void createChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getString(R.string.app_name));
        notificationManager.createNotificationChannel(channel);
    }

    private void showImageForNotificationfromUrl(RemoteViews contentView, int imgSongImage, String finalPlaylistImage) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(finalPlaylistImage);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(input);
            contentView.setImageViewBitmap(imgSongImage, imageBitmap);
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            return;
        }
    }

    private void showImageForNotificationfromPath(RemoteViews contentView, int imgSongImage, String imageUrl) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            File bitmapFile = new File(imageUrl);
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getPath());
            contentView.setImageViewBitmap(imgSongImage, bitmap);
        } catch (Exception e) {
            // Log exception
            e.printStackTrace();
            return;
        }
    }

    public PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                if (contentView != null) {
                    contentView.setImageViewResource(R.id.imgPlay, R.drawable.ic_pause);
                }
                play_pauseAction = PendingIntent.getService(this, 1, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
//                buildNotification(PlaybackStatus.PLAYING);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                if (contentView != null) {
                    contentView.setImageViewResource(R.id.imgPlay, R.drawable.ic_play);
                }
                play_pauseAction = PendingIntent.getService(this, 0, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
//                buildNotification(PlaybackStatus.PAUSED);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
//                buildNotification(PlaybackStatus.PLAYING);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
//                buildNotification(PlaybackStatus.PLAYING);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 4:
                // Previous track
                playbackAction.setAction(ACTION_STOP);
//                buildNotification(PlaybackStatus.PAUSED);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    public void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
//            if (!AppLevelClass.ispaused) {
            transportControls.play();
            AppLevelClass.NotiFyAction = ACTION_PLAY;
            Intent broadcastIntent = new Intent(Broadcast_NOTIFY);
            sendBroadcast(broadcastIntent);
//            }
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
            AppLevelClass.NotiFyAction = ACTION_PAUSE;
            Intent broadcastIntent = new Intent(Broadcast_NOTIFY);
            sendBroadcast(broadcastIntent);
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
            AppLevelClass.NotiFyAction = ACTION_NEXT;
            Intent broadcastIntent = new Intent(Broadcast_NOTIFY);
            sendBroadcast(broadcastIntent);

        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
            AppLevelClass.NotiFyAction = ACTION_PREVIOUS;
            Intent broadcastIntent = new Intent(Broadcast_NOTIFY);
            sendBroadcast(broadcastIntent);

        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
            AppLevelClass.NotiFyAction = ACTION_STOP;
            Intent broadcastIntent = new Intent(Broadcast_NOTIFY);
            sendBroadcast(broadcastIntent);
        }

    }

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    private void register_actionreciver() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Broadcast_ACTION);
        registerReceiver(ActionPlay, filter);
    }

    //To get current progress of playing audio
    public void getCurrentProgress() {
        int mediaMax_new = mediaPlayer.getDuration();
        maxdur = mediaMax_new;
    }

    public Song getSongData(Context mContext) {
        context = mContext;
        StorageUtil storageUtil = new StorageUtil(mContext);
        audioIndex = storageUtil.loadAudioIndex();
        audioList = storageUtil.loadAudio();
        if (audioList != null && !audioList.isEmpty() && audioList.size() > audioIndex) {
            activeAudio = audioList.get(audioIndex);
            return activeAudio;
        }else {
            return null;
        }
    }

    public void setSongPosition(int i) {
        mediaPlayer.seekTo(i);
    }

    public static void setCallBackListener(MyMusicCallbackListener reference) {
        listener = reference;
    }

    public void setRepeatOrShuffle(Boolean isRepeat, Boolean isShuffle) {
        this.isRepeat = isRepeat;
        this.isShuffle = isShuffle;
    }

    public static void setList(ArrayList<Song> songList, int position, Context context1,String type) {
        Log.e("SetList : ","HJere");
        context = context1;
        audioList = songList;
        StorageUtil storageUtil = new StorageUtil(context1);
        storageUtil.storeAudio(songList);
        storageUtil.storeType(type);
        if (position < 0) {
//            throw new IllegalArgumentException("id must be greater than zero:" + position);
        } else {
            audioIndex = position;
            activeAudio = audioList.get(position);
            storageUtil.storeAudioIndex(position);
        }
        audioList = storageUtil.loadAudio();
    }

    public void updateListAfterRemove(ArrayList<Song> songList, Context context1, String songNumber, String type) {
        context = context1;
        StorageUtil storageUtil = new StorageUtil(context1);
        if (storageUtil.loadTYpe().equals(type)) {
            Log.e("updateList : ", "HJere");
            ArrayList<Song> newaudioList = storageUtil.loadAudio();
            for (int i = 0; i < newaudioList.size(); i++) {
                if (newaudioList.get(i).getSongID().toString().equals(songNumber)) {
                    newaudioList.remove(i);
                    storageUtil.storeAudio(newaudioList);
                    break;
                }
            }
            audioIndex = storageUtil.loadAudioIndex();
            audioList = storageUtil.loadAudio();
            if (isInProcess && mediaPlayer.isPlaying() && !songNumber.isEmpty() && songNumber.toString().equalsIgnoreCase(activeAudio.getSongID())) {
                mediaPlayer.stop();
                if (songList.size() > audioIndex) {
                    storageUtil.storeAudioIndex(audioIndex);
                } else {
                    audioIndex = 0;
                    storageUtil.storeAudioIndex(audioIndex);
                }
                initMediaPlayer();
            }
        }
    }

    /**
     * Service Binder
     */
    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaPlayerService.this;
        }
    }

}
