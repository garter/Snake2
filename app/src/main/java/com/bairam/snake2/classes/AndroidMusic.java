package com.bairam.snake2.classes;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.bairam.snake2.Music;

import java.io.IOException;

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {

    MediaPlayer mMediaPlayer;
    boolean isPrepared = false;

    public AndroidMusic(AssetFileDescriptor assetFileDescriptor){
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            isPrepared = true;
            mMediaPlayer.setOnCompletionListener(this);
        }catch (Exception e){
            throw new RuntimeException("Couldn't load music");
        }
    }

    @Override
    public void play() {
        if (mMediaPlayer.isPlaying()){
            return;
        }
        try {
                synchronized (this){
                    if (!isPrepared) {
                        mMediaPlayer.prepare();
                    }
                    mMediaPlayer.start();
                }
            }catch (IllegalStateException ie){
                ie.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        synchronized (this){
            isPrepared = false;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    @Override
    public void setVolume(float volume) {
        mMediaPlayer.setVolume(volume, volume);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
            return !isPrepared;
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    @Override
    public void dispose() {
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this){
            isPrepared = false;
        }
    }
}
