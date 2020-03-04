package com.ali.pinksafety;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID = 1;
    SessionManager session;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        generateNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());

    }

    private void generateNotification(String body, String title) {
        /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.google.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); */
        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setLights(Color.RED, 3000, 3000)
                .setAutoCancel(true)
                .setVibrate(new long[] { 5000 })
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        if(NOTIFICATION_ID > 1073741824){
            NOTIFICATION_ID = 0;
        }
        notificationManager.notify(NOTIFICATION_ID++, notificationBuilder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
        session = new SessionManager(this);
        session.storeToken(s);
    }
}