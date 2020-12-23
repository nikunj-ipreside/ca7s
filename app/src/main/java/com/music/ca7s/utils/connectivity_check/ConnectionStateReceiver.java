package com.music.ca7s.utils.connectivity_check;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.music.ca7s.AppLevelClass;


public class ConnectionStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppLevelClass.getInstance().getBus().post(isConnected(context));
    }


    public static Boolean isConnected(Context context) {
        NetworkType type = checkStatus(context);
        if (type == NetworkType.WIFI || type == NetworkType.MOBILE_DATA) {
            return true;
        } else {
            return false;
        }
    }

    public static NetworkType checkStatus(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            return NetworkType.WIFI;
        } else if (mobile.isConnectedOrConnecting()) {
            return NetworkType.MOBILE_DATA;
        } else {
            return NetworkType.NO_NETWORK;
        }
    }


    private enum NetworkType {
        WIFI, MOBILE_DATA, NO_NETWORK
    }
}
