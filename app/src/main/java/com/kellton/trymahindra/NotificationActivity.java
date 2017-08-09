package com.kellton.trymahindra;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * <h1><font color="orange">NotificationActivity</font></h1>
 * Activity to receive notifications through a broadcast.
 * Created by Divya Khanduri on 25/7/17.
 */
public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverHandler, new IntentFilter("com.kellton.trymahindra_FCM-MESSAGE"));
        setContentView(R.layout.activity_notification);
        TextView mTextView = (TextView) findViewById(R.id.textView);
        if (getIntent().getExtras() != null) {
            // displaying content in activity from notification
            mTextView.setText("Name" + getIntent().getStringExtra("name") + "\n" + "Phone" + getIntent().getStringExtra("phone"));

        }
    }

    /**
     * creating custom notification
     */
    private void createNotification(String title, String name, Context context) {
        Log.e("hello", "onMessageReceived");
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
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


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.drawable.notification)
                .setTicker("Custom FCM Notification")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(contentView);

        contentView.setTextViewText(R.id.tv_title, title);
        contentView.setTextViewText(R.id.tv_text, name);
        contentView.setImageViewResource(R.id.iv_icon, R.drawable._white_star);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }

    private BroadcastReceiver broadcastReceiverHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("body");
            String name = intent.getStringExtra("name");
            createNotification(title, name, context);
        }
    };
}
