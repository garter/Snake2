package com.bairam.snake2.classes;

import android.content.Context;
import android.view.View;

import com.bairam.snake2.Input;
import com.bairam.snake2.TouchHandler;

import java.util.List;

public class AndroidInput implements Input {

    AccelerometerHandler mAccelerometerHandler;
    KeyBoardHandler mKeyBoardHandler;
    TouchHandler mTouchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY){
        mAccelerometerHandler = new AccelerometerHandler(context);
        mKeyBoardHandler = new KeyBoardHandler(view);
        mTouchHandler = new Multitouchhandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isKeyPressed(int keyKode) {
        return mKeyBoardHandler.isKeyPressed(keyKode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return mTouchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return mTouchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return mTouchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return mAccelerometerHandler.mAccelX;
    }

    @Override
    public float getAccelY() {
        return mAccelerometerHandler.mAccelY;
    }

    @Override
    public float getAccelZ() {
        return mAccelerometerHandler.mAccelZ;
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return mKeyBoardHandler.getKeyEvents();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return mTouchHandler.getTouchEvents();
    }
}
