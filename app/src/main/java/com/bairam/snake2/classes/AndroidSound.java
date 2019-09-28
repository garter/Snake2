package com.bairam.snake2.classes;

import android.media.SoundPool;

import com.bairam.snake2.Sound;

public class AndroidSound implements Sound {

    int mSoundId;
    SoundPool mSoundPool;

    public AndroidSound(SoundPool soundPool, int soundId){
        mSoundId = soundId;
        mSoundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        mSoundPool.play(mSoundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        mSoundPool.unload(mSoundId);
    }
}
