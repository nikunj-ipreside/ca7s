package com.music.ca7s.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.music.ca7s.R;
import com.music.ca7s.activity.SplashActivity;
import com.music.ca7s.utils.DebugLog;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent;

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        DebugLog.e("From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            DebugLog.e("Message data payload: " + remoteMessage.getData());
           /* notificationModel.setNotification_key(remoteMessage.getData().get(Parameter.NOTIFICATION_KEY));
            notificationModel.setNotification_id(remoteMessage.getData().get(Parameter.NOTIFICATION_ID));
            notificationModel.setNotification_title(remoteMessage.getData().get(Parameter.NOTIFICATION_TITLE));
            notificationModel.setNotification_details(remoteMessage.getData().get(Parameter.NOTIFICATION_DETAILS));
            notificationModel.setNotification_type(remoteMessage.getData().get(Parameter.NOTIFICATION_TYPE));
            notificationModel.setNotification_deeplink(remoteMessage.getData().get(Parameter.NOTIFICATION_DEEPLINK));
            notificationModel.setClick_action(remoteMessage.getData().get(Parameter.CLICK_ACTION));
*/
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            DebugLog.e("Message Notification payload: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        final String NOTIFICATION_CHANNEL_ID = "10001";
        intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, getRequestCode(), intent, 0);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "CA7S", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(getRequestCode() /* Request Code */, notificationBuilder.build());
    }
}