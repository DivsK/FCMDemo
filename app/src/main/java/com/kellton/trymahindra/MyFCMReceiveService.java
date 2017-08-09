package com.kellton.trymahindra;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * <h1><font color="orange">MyFCMReceiveService</font></h1>
 * Service class to receive notifications.
 * Created by Divya Khanduri on 25/7/17.
 */

public class MyFCMReceiveService extends FirebaseMessagingService {
    private final String TAG = MyFCMReceiveService.class.getSimpleName();

    /**
     * handles notifications received
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //creating default notification
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("Name"), remoteMessage.getData().get("Phone-No"));
        }

        // Also if you intend on generating your own custom notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendBroadcast method below.

        sendBroadcast(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("Name"), remoteMessage.getData().get("Phone-No"));
    }

    /**
     * sending default notification
     */
    private void sendNotification(String body, String name, String phone) {
        Log.e("hello", "sendNotification");
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("body", body);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * sending notification via broadcast
     */
    public void sendBroadcast(String body, String name, String phone) {
        Intent intent = new Intent("com.kellton.trymahindra_FCM-MESSAGE");
        intent.putExtra("body", body);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);

    }
}
