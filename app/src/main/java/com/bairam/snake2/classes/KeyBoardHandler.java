package com.bairam.snake2.classes;

import android.view.KeyEvent;
import android.view.View;

import com.bairam.snake2.Input;

import java.util.ArrayList;
import java.util.List;

public class KeyBoardHandler implements View.OnKeyListener {

    boolean[] pressedKeys = new boolean[128]; //текущее состояние (нажата или не нажата) каждой клавиши в данном массиве
    Pool<Input.KeyEvent> mKeyEventPool; //хранит экземпляры наших классов KeyEvent.
    List<Input.KeyEvent> mKeyEventsBuffer = new ArrayList<Input.KeyEvent>(); // хранит KeyEvent, которые пока не были обработаны классом Game.
    List<Input.KeyEvent> mKeyEvents = new ArrayList<Input.KeyEvent>(); //хранит KeyEvent, которые мы вернем при вызове KeyboardHandler.getKeyEvents()

    public KeyBoardHandler(View view){
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>() {
            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };
        mKeyEventPool = new Pool<Input.KeyEvent>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_MULTIPLE){
            return false;
        }

        synchronized (this){
            Input.KeyEvent keyEvent1 = mKeyEventPool.newObject();
            keyEvent1.keyKode = keyCode;
            keyEvent1.keyChar = (char) keyEvent.getUnicodeChar();

            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                keyEvent1.type = Input.KeyEvent.KEY_DOWN;
                if (keyCode > 0 && keyCode < 127){
                    pressedKeys[keyCode] = true;
                }
            }
            if (keyEvent.getAction() == KeyEvent.ACTION_UP){
                keyEvent1.type = Input.KeyEvent.KEY_UP;
                if (keyCode > 0 && keyCode < 127){
                    pressedKeys[keyCode] = false;
                }
            }
            mKeyEventsBuffer.add(keyEvent1);
        }

        return false;
    }

    //о том, нажата данная клавиша или нет
    public boolean isKeyPressed(int keyCode){
        //предварительно проверив границы диапазона
        if (keyCode < 0 || keyCode > 127){
            return false;
        }
        return pressedKeys[keyCode];
    }

    public List<Input.KeyEvent> getKeyEvents(){
        synchronized (this){
            int len = mKeyEvents.size();
            for (int i = 0; i < len; i++){
                mKeyEventPool.free(mKeyEvents.get(i));
            }
            mKeyEvents.clear();
            mKeyEvents.addAll(mKeyEventsBuffer);
            mKeyEventsBuffer.clear();
            return mKeyEvents;
        }
    }
}
