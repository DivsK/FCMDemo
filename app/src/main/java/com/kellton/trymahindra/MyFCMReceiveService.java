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
 * Created by ubuntu on 25/7/17.
 */

public class MyFCMReceiveService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("hello","onMessageReceived");
        //Displaying data in log
        //It is optional
        Log.d("Message--", "From: " + remoteMessage.getFrom());
        Log.d("Message Body--", "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("Name").toString(), remoteMessage.getData().get("Phone-No").toString());
        sendBroadcast(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String body, String name, String phone) {
        Log.e("hello","sendNotification");
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

    public  void sendBroadcast(String title,String message)
    {
        Intent intent=new Intent("com.kellton.trymahindra_FCM-MESSAGE");
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);

    }
}
