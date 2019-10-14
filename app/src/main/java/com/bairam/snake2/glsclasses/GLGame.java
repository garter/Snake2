package com.bairam.snake2.glsclasses;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bairam.snake2.Audio;
import com.bairam.snake2.FileIO;
import com.bairam.snake2.Game;
import com.bairam.snake2.Graphics;
import com.bairam.snake2.Input;
import com.bairam.snake2.Screen;
import com.bairam.snake2.classes.AndroidAudio;
import com.bairam.snake2.classes.AndroidFileIO;
import com.bairam.snake2.classes.AndroidInput;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class GLGame extends AppCompatActivity implements Game, GLSurfaceView.Renderer {
    enum GLGameSate{
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    GLSurfaceView glView;
    GLGraphics glGraphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    GLGameSate state = GLGameSate.Initialized;
    Object stateChanged = new Object();
    long startTime = System.nanoTime();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glView = new GLSurfaceView(this);
        glView.setRenderer(this);
        setContentView(glView);

        glGraphics = new GLGraphics(glView);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, glView, 1, 1);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glGraphics.setGL(gl);
        synchronized (stateChanged){
            if (state == GLGameSate.Initialized)//Если приложение запущено впервые
                screen = getStartScreen(); //вернуться к стартовому экрану игры.
            state = GLGameSate.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLGameSate sate = null;
        synchronized (stateChanged){
            sate = state;
        }

        if (sate == GLGameSate.Running){
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if (sate == GLGameSate.Paused){
            screen.pause();
            synchronized (stateChanged){
                this.state = GLGameSate.Idle;
                stateChanged.notifyAll();
            }
        }

        if (sate == GLGameSate.Finished){
            screen.pause();
            screen.dispose();
            synchronized (stateChanged){
                this.state = GLGameSate.Idle;
                stateChanged.notifyAll();
            }
        }
    }

    @Override
    protected void onPause() {

        synchronized (stateChanged){
            if (isFinishing()){
                state = GLGameSate.Finished;
            }else {
                state = GLGameSate.Paused;
            }

            while (true){
                try {
                    stateChanged.wait();
                    break;
                }catch (InterruptedException e){

                }
            }
        }
        glView.onPause();
        super.onPause();
    }

    public GLGraphics getGlGraphics(){
        return glGraphics;
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }
}
