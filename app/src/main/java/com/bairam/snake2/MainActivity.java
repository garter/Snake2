package com.bairam.snake2;

import com.bairam.snake2.classes.AndroidGame;


public class MainActivity extends AndroidGame {

    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
