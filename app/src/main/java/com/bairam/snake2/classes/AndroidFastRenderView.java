package com.bairam.snake2.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {

    AndroidGame game;
    Bitmap mFramBuffer;
    Thread renderThread = null;
    SurfaceHolder mHolder;
    volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame game, Bitmap framBuffer) {
        super(game);
        this.game = game;
        mFramBuffer = framBuffer;
        mHolder = getHolder();
    }

    @Override
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while (running){
            if (!mHolder.getSurface().isValid())
                continue;

            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;

            startTime = System.nanoTime();

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);

            Canvas canvas = mHolder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(mFramBuffer, null, dstRect, null);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void resume(){
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void pause(){
        running = false;
        while (true){
            try {
                renderThread.join();
                break;
            }catch (InterruptedException e){

            }
        }
    }
}
