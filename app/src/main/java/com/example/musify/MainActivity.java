package com.example.musify;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView noTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    ListAdapter adapter;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        noTextView = findViewById(R.id.no_songs_text);


        //Adding toolbar to main menu
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.music_tool);
        setSupportActionBar(toolbar);


        //Checking audio permission is given or not
        if (checkAudioPermission() == false) {
            requestAudioPermission();
            return;
        }


        //Checking notification permission is given or not
        if (checkNotificationPermission() == false) {
            requestNotificationPermission();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(songData.getPath()).exists())
                songsList.add(songData);
        }

//        AudioModel songData = songsList.get(0);
//
//        MusicNotification.musicNotification(MainActivity.this,songData,R.drawable.baseline_play,1,songsList.size());


        if (songsList.size() == 0) {
            noTextView.setVisibility(View.VISIBLE);
        } else {
            // Applying recyclerview

            //initialising the adapter class
            adapter = new ListAdapter(songsList, getApplicationContext());
            //adding layout manager to our recycler view
            LinearLayoutManager manager = new LinearLayoutManager(this);
            //setting layout manager to our recycler view
            recyclerView.setLayoutManager(manager);
            //setting adapter to our recycler view
            recyclerView.setAdapter(adapter);
        }

//        miniplayertext.setSelected(true);
//        minilist = songsList;
//        setMiniResourcesWithMusic();

    }

    //Menu Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("What do you want to listen to?");
        //To remove cursor from search view
        //searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText.toLowerCase());
                return true;
            }
        });

        return true;
    }

    private void filterList(String text) {
        ArrayList<AudioModel> filterlist = new ArrayList<AudioModel>();
        for (AudioModel song : songsList) {
            if (song.getTitle().toLowerCase().contains(text)){
                filterlist.add(song);
            }
        }
        if (filterlist.isEmpty()){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.filterList(filterlist);
            recyclerView.setAdapter(adapter);
        }
    }


    //Menu Bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.music_setting) {
            Toast.makeText(this, "Opening Setting", Toast.LENGTH_SHORT).show();
            startService();
        }
        return true;
    }


    //Checking permission is given or not
    boolean checkAudioPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //Requesting permission to user
    void requestAudioPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO)) {
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 123);
    }

    boolean checkNotificationPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestNotificationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)) {
            Toast.makeText(MainActivity.this, "NOTIFICATION PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 121);
    }


    //For coming back to main activity
    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    public  void startService(){
        Intent intent = new Intent(this,MusicService.class);
        ContextCompat.startForegroundService(this,intent);
    }
    public  void stopService(){
        Intent intent = new Intent(this,MusicService.class);
        stopService(intent);
    }


//    private  void createChannel(){
//        NotificationChannel channel = new NotificationChannel(MusicNotification.CHANNEL_ID,"Music", NotificationManager.IMPORTANCE_LOW);
//        notificationManager = getSystemService(NotificationManager.class);
//        if (notificationManager != null){
//            notificationManager.createNotificationChannel(channel);
//        }
//
//    }

}

