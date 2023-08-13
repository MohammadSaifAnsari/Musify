package com.example.musify;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button playbtn,nextbtn,prevbtn,fastforwardbtn,fastbackwardbtn;
    TextView txtname,txtstart,txtstop;
    SeekBar seekmusic;
    ImageView imageView;

    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaplayer = MyMediaPlayer.getInstance();
    int x=0;

//    MusicService musicService  = new MusicService();
//    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);



        playbtn = findViewById(R.id.playbtn1);
        nextbtn = findViewById(R.id.btnnext);
        prevbtn = findViewById(R.id.btnprev);
        txtname = findViewById(R.id.txt_sn1);
        txtstart = findViewById(R.id.txt_start);
        txtstop = findViewById(R.id.txt_stop);
        seekmusic = findViewById(R.id.seekbar_1);
        imageView = findViewById(R.id.imgview1);
        fastforwardbtn = findViewById(R.id.btnfast_forward);
        fastbackwardbtn = findViewById(R.id.btnfast_back);



        txtname.setSelected(true);

        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("list");

        setResourcesWithMusic();



        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaplayer!=null){
                    seekmusic.setProgress(mediaplayer.getCurrentPosition());
                    txtstart.setText(updateTime(mediaplayer.getCurrentPosition()+""));

                    if(mediaplayer.isPlaying()){
                        playbtn.setBackgroundResource(R.drawable.baseline_pause);
                        imageView.setRotation(x++);
                    }else{
                        playbtn.setBackgroundResource(R.drawable.baseline_play);
                        imageView.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,10);
            }
        });



        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaplayer!=null && fromUser){
                    mediaplayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        txtname.setText(currentSong.getTitle());

        txtstop.setText(updateTime(currentSong.getDuration()));

        playbtn.setOnClickListener(v-> pausePlay());
        nextbtn.setOnClickListener(v-> playNextSong());
        prevbtn.setOnClickListener(v-> playPreviousSong());
        fastbackwardbtn.setOnClickListener(v->fastbackward());
        fastforwardbtn.setOnClickListener(v->fastforward());

        playMusic();



    }

    private  void  fastbackward(){
        if (mediaplayer.isPlaying())
                {
                    mediaplayer.seekTo(mediaplayer.getCurrentPosition()-10000);
                }
    }

    private  void  fastforward(){
        if (mediaplayer.isPlaying())
        {
            mediaplayer.seekTo(mediaplayer.getCurrentPosition()+10000);
        }
    }


    private void playMusic(){

        mediaplayer.reset();
        try {
            mediaplayer.setDataSource(currentSong.getPath());
            mediaplayer.prepare();
            mediaplayer.start();
            seekmusic.setProgress(0);
            seekmusic.setMax(mediaplayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextSong(){
        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaplayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaplayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaplayer.isPlaying()) {
            mediaplayer.pause();
        }else {
            mediaplayer.start();
        }
    }


    public String updateTime (String duration)
    {
        String time = "";
        int min = (Integer.parseInt(duration)/1000)/60;
        int sec = Integer.parseInt(duration)/1000%60;

        time+=min+":";
        if (sec<10)
        {
            time+="0";
        }
        time+=sec;
        return time;
    }


//    private ServiceConnection serviceConnection = new ServiceConnection(){
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
//            musicService = binder.getBoundService();
//            isConnected = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            isConnected  = false;
//        }
//    };
}
