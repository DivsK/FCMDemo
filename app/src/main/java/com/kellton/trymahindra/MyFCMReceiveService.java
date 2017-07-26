package com.kellton.trymahindra;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ubuntu on 25/7/17.
 */

public class MyFCMReceiveService extends FirebaseMessagingService {
    public final String TAG = "FROM--->";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("hello","onMessageReceived");
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d("msgBody--", "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("Name").toString(), remoteMessage.getData().get("Phone-No").toString());
        createNotification();
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

    private void createNotification() {
        Log.e("hello","onMessageReceived");

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);

        //Dial intent
        Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:09882527868"));
        dial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentDial = PendingIntent.getActivity(this, 0, dial, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btn_call, pendingIntentDial);

        //Message intent
        Uri uri = Uri.parse("smsto:09882527868");
        Intent message = new Intent(Intent.ACTION_SENDTO, uri);
        message.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentMessage = PendingIntent.getActivity(this, 0, message, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btn_message, pendingIntentMessage);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.notification)
                .setTicker("custom notification")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(contentView);

        contentView.setTextViewText(R.id.tv_title, "title");
        contentView.setTextViewText(R.id.tv_text, "text");
        contentView.setImageViewResource(R.id.iv_icon, R.drawable.notification);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }
}
