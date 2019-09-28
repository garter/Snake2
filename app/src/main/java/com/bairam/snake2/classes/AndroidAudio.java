package com.bairam.snake2.classes;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.bairam.snake2.Audio;
import com.bairam.snake2.Music;
import com.bairam.snake2.Sound;

import java.io.IOException;

public class AndroidAudio implements Audio {

    AssetManager mAssetManager;
    SoundPool mSoundPool;

    public AndroidAudio(Activity activity){
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAssetManager = activity.getAssets();
        mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music newMusic(String fileName) {
        try{
            AssetFileDescriptor assetDescriptor = mAssetManager.openFd(fileName);
            return new AndroidMusic(assetDescriptor);
        }catch (IOException e){
            throw new RuntimeException("Невозможно загрузить музыку '" + fileName + "'");
        }
    }

    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = mAssetManager.openFd(filename);
            int soundId = mSoundPool.load(assetDescriptor, 0);
            return new AndroidSound(mSoundPool, soundId);
        }catch (IOException e){
            throw new RuntimeException("Невозможно загрузить звук '" + filename + "'");
        }
    }
}
