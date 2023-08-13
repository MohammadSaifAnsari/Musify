package com.example.musify;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MusicNotification {
    public static final String CHANNEL_ID = "music channel1";
    public  static final  String CHANNEL_PREV = "prevaction";
    public  static final  String CHANNEL_NEXT = "nextaction";
    public  static final  String CHANNEL_PLAY = "playaction";

    public static Notification notification;



    @SuppressLint("MissingPermission")
    public static void musicNotification(Context context, AudioModel audioModel, int playbutton, int pos, int size){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag1");


        notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(audioModel.getTitle())
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManagerCompat.notify(2,notification);

    }

}
