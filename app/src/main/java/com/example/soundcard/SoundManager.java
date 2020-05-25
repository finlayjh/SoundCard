package com.example.soundcard;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.media.SoundPool.OnLoadCompleteListener;
import android.content.Context;

public class SoundManager{
    private static SoundPool soundPool;
    private char currentLetter = '\0';
    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private int[] soundIDs = new int[26];
    private boolean isLoaded = false, plays = false;

    public SoundManager(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(26)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else{
            soundPool = new SoundPool(26, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoaded = true;
            }
        });

        for (int i = 0; i<alphabet.length; i++){
            int sID = context.getResources().getIdentifier("sound_" + alphabet[i], "raw", context.getPackageName());
            soundIDs[i] = soundPool.load(context, sID, 1);
        }

    }

    public void setCurrentLetter(char c){
        currentLetter = c;
    }

    public void playSound() {
        if (isLoaded && !plays) {
            soundPool.play(soundIDs[(Character.toLowerCase(currentLetter))-'a'], 1, 1, 1, 0, 1f);
            //plays = true;
        }
    }

    protected void onDestroy(){
        soundPool.release();
        soundPool = null;
    }
}
