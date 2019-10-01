package com.bairam.snake2.classes;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bairam.snake2.Audio;
import com.bairam.snake2.FileIO;
import com.bairam.snake2.Game;
import com.bairam.snake2.Graphics;
import com.bairam.snake2.Input;
import com.bairam.snake2.Screen;

public class AndroidGame extends AppCompatActivity implements Game {

    AndroidFastRenderView mRenderView; //в котором мы будем рисовать и который будет управлять потоком основного цикла.
    Graphics mGraphics;
    Audio mAudio;
    Input mInput;
    FileIO mFileIO;
    Screen mScreen; //хранит текущий Screen.
    PowerManager.WakeLock mWakeLock; //нужен для того, чтобы экран не гаснул

    public static final String TAG = "GLGame";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;

        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565);

        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        mRenderView = new AndroidFastRenderView(this, frameBuffer);
        mGraphics = new AndroidGraphics(getAssets(), frameBuffer);
        mFileIO = new AndroidFileIO(getAssets());
        mAudio = new AndroidAudio(this);
        mInput = new AndroidInput(this, mRenderView, scaleX, scaleY);
        mScreen = getStartScreen();

        setContentView(mRenderView);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mScreen.resume();
        mRenderView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        mRenderView.pause();
        mScreen.pause();

        if (isFinishing()){
            mScreen.dispose();
        }
    }

    @Override
    public Input getInput() {
        return mInput;
    }

    @Override
    public FileIO getFileIO() {
        return mFileIO;
    }

    @Override
    public Graphics getGraphics() {
        return mGraphics;
    }

    @Override
    public Audio getAudio() {
        return mAudio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        mScreen.pause();
        mScreen.dispose();
        screen.resume();
        screen.update(0);
        mScreen = screen;

    }

    @Override
    public Screen getCurrentScreen() {
        return mScreen;
    }

    @Override
    public Screen getStartScreen() {
        return null;
    }


}
