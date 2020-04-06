package com.example.izobonga_waiting_app.service;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.example.izobonga_waiting_app.R;

import java.util.Locale;

public class TTSService {

    public TextToSpeech tts;
    private Context context;

    public TTSService(Context context){
        this.context = context;
        if (tts == null){
            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    tts.setLanguage(Locale.KOREA);
                }
            });
        }
    }

    public void speak(String ticket){
        if (tts != null){
            String message = String.format(context.getString(R.string.tts_message),ticket);
            tts.setSpeechRate(1.0f); //읽는 속도 설정 : 기본
            tts.setPitch((float) 1);      // 음량
//        tts.setSpeechRate(""); //톤 조절
            tts.speak(message , TextToSpeech.QUEUE_FLUSH, null);
        }
    }

//    public void removeTTS(){
//        if(tts != null){
//            tts.stop();
//            tts.shutdown();
//            tts = null;
//        }
//    }
}
