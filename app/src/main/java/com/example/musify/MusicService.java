package com.example.musify;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

public class MusicService extends Service {
//    private MyBinder binder = new MyBinder();
    private static final String CHANNEL_ID = "My Music Channel Id";
    private static final int NOTIFICATION_ID = 100;
    private static final int PENDINGCODE = 1000;


    public MusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotifivationChannel();
        showNotification();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "work", Toast.LENGTH_SHORT).show();
        return null;
    }


    @SuppressLint("MissingPermission")
    public void showNotification() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.applogo, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        Intent intentnotify = new Intent(this, MainActivity.class);
        intentnotify.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, PENDINGCODE, intentnotify, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Music")
                .setContentTitle("App")
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.applogo)
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);
    }




    private void createNotifivationChannel(){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Mini Player",NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


}

//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent intentnotify = new Intent(context, MainActivity.class);
//        intentnotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pi = PendingIntent.getActivity(this,PENDINGCODE,intentnotify,PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setLargeIcon(largeIcon)
//                .setSmallIcon(R.drawable.applogo)
//                .setContentText("Music")
//                .setSubText("App")
//                .setContentIntent(pi)
//                .build();
//        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"Mini Player",NotificationManager.IMPORTANCE_HIGH));
//        nm.notify(NOTIFICATION_ID,notification);


//    public class MyBinder extends Binder
//    {
//        MusicService getBoundService(){
//            return MusicService.this;
//        }
//    }