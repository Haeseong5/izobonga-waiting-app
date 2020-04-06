package com.example.izobonga_waiting_app.service;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.izobonga_waiting_app.R;

public class MediaPlayerService {

    public MediaPlayer mediaPlayer;
    private Context context;

    public MediaPlayerService (Context context){
        this.context = context;
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, R.raw.sound_alarm_file);
        }

    }

    public void start(){
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }
//    public void stop(){
//        if (mediaPlayer != null){
//            mediaPlayer.stop(); //정지
//            mediaPlayer.reset(); //초기화
//        }
//    }
//    public void remove(){
//        if (mediaPlayer!=null){
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
}
