package com.music.ca7s.utils.downloadutils;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.music.ca7s.mediaplayer.Song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.music.ca7s.contant.ApiParameter.ID;

public class DownloadingService extends IntentService {
    public static String PROGRESS_UPDATE_ACTION = DownloadingService.class
            .getName() + ".progress_update";

    private static final String ACTION_CANCEL_DOWNLOAD = DownloadingService.class
            .getName() + "action_cancel_download";

    private boolean mIsAlreadyRunning;
    private boolean mReceiversRegistered;

    private ExecutorService mExec;
    private CompletionService<NoResultType> mEcs;
    private LocalBroadcastManager mBroadcastManager;
    private List<DownloadTask> mTasks;

    private static final long INTERVAL_BROADCAST = 800;
    private long mLastUpdate = 0;

    public DownloadingService() {
        super("DownloadingService");
        mExec = Executors.newFixedThreadPool( /* only 5 at a time */5);
        mEcs = new ExecutorCompletionService<NoResultType>(mExec);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mTasks = new ArrayList<DownloadTask>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mIsAlreadyRunning) {
            publishCurrentProgressOneShot(true);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mIsAlreadyRunning) {
            return;
        }
        mIsAlreadyRunning = true;

        Song files = new Song();
        final Collection<DownloadTask> tasks = mTasks;
        int index = 0;
            DownloadTask yt1 = new DownloadTask(files.getSongID(), files);
            tasks.add(yt1);

        for (DownloadTask t : tasks) {
            mEcs.submit(t);
        }
        // wait for finish
        int n = tasks.size();
        for (int i = 0; i < n; ++i) {
            NoResultType r;
            try {
                r = mEcs.take().get();
                if (r != null) {
                    // use you result here
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        // send a last broadcast
        publishCurrentProgressOneShot(true);
        mExec.shutdown();
    }

    private void publishCurrentProgressOneShot(boolean forced) {
        if (forced
                || System.currentTimeMillis() - mLastUpdate > INTERVAL_BROADCAST) {
            mLastUpdate = System.currentTimeMillis();
            final List<DownloadTask> tasks = mTasks;
            String[] positions = new String[tasks.size()];
            int[] progresses = new int[tasks.size()];
            for (int i = 0; i < tasks.size(); i++) {
                DownloadTask t = tasks.get(i);
                positions[i] = t.mPosition;
                progresses[i] = t.mProgress;
            }
            publishProgress(positions, progresses);
        }
    }

    private void publishCurrentProgressOneShot() {
        publishCurrentProgressOneShot(false);
    }

    private synchronized void publishProgress(String[] positions,
                                              int[] progresses) {
        Intent i = new Intent();
        i.setAction(PROGRESS_UPDATE_ACTION);
        i.putExtra("position", positions);
        i.putExtra("progress", progresses);
        i.putExtra("oneshot", true);
        mBroadcastManager.sendBroadcast(i);
    }

    // following methods can also be used but will cause lots of broadcasts
    private void publishCurrentProgress() {
        final Collection<DownloadTask> tasks = mTasks;
        for (DownloadTask t : tasks) {
            publishProgress(t.mPosition, t.mProgress);
        }
    }

    private synchronized void publishProgress(String position, int progress) {
        Intent i = new Intent();
        i.setAction(PROGRESS_UPDATE_ACTION);
        i.putExtra("progress", progress);
        i.putExtra("position", position);
        mBroadcastManager.sendBroadcast(i);
    }

  public class DownloadTask implements Callable<NoResultType> {
        private String mPosition;
        private int mProgress;
        private boolean mCancelled;
        private final Song mFile;
        private Random mRand = new Random();

        public DownloadTask(String position, Song file) {
            mPosition = position;
            mFile = file;
        }

        @Override
        public NoResultType call() throws Exception {
            while (mProgress < 100 && !mCancelled) {
                mProgress += mRand.nextInt(5);
                Thread.sleep(mRand.nextInt(500));

                // publish progress
                publishCurrentProgressOneShot();

                // we can also call publishProgress(int position, int
                // progress) instead, which will work fine but avoid broadcasts
                // by aggregating them

                // publishProgress(mPosition,mProgress);
            }
            return new NoResultType();
        }

        public int getProgress() {
            return mProgress;
        }

        public String getPosition() {
            return mPosition;
        }

        public void cancel() {
            mCancelled = true;
        }
    }


    private void registerReceiver() {
        unregisterReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadingService.ACTION_CANCEL_DOWNLOAD);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mCommunicationReceiver, filter);
        mReceiversRegistered = true;
    }

    private void unregisterReceiver() {
        if (mReceiversRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    mCommunicationReceiver);
            mReceiversRegistered = false;
        }
    }

    private final BroadcastReceiver mCommunicationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    DownloadingService.ACTION_CANCEL_DOWNLOAD)) {
                final String id = intent.getStringExtra(ID);
                if (!id.isEmpty()) {
                    for (DownloadTask task : mTasks) {
                        if (task.mFile.getSongID().equals(id)) {
                            task.cancel();
                            break;
                        }
                    }
                }
            }
        }
    };

    class NoResultType {

    }
}
