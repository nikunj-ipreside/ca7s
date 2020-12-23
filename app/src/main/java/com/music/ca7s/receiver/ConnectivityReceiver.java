package com.music.ca7s.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.music.ca7s.AppLevelClass;

public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        // At activity startup we manually check the internet status and change
        // the text status
        Log.e("onRecieve : ",""+intent+"    "+intent.getType());
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager
                        cm = (ConnectivityManager) context.getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.getNetworkType(activeNetwork.getType(),activeNetwork.getTypeName());
                    }
                }
            }
        },3000);

        if (networkInfo != null && networkInfo.isConnected()) {

        } else {
            if (connectivityReceiverListener != null) {
                connectivityReceiverListener.onNetworkConnectionChanged(false);
            }
        }

    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) AppLevelClass.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static int getNetworkType(Context context){
        ConnectivityManager
                cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getType();
        }
        return 5;
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
        void getNetworkType(int typeEthernet,String nameOfDataType);
    }
}
