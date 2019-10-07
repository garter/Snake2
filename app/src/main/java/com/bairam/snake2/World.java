package com.bairam.snake2;

import java.util.Random;

public class World {
    static final int WORLD_WIDTH = 10;
    static final int WORLD_HEIGHT = 13;
    static final int SCORE_INCREMENT = 10;
    static final float TICK_INITIAL = 0.5f;
    static final float TICK_DECREMENT = 0.05f;

    public Snake mSnake;
    public Stain mStain;
    public boolean mGameOver = false;
    public int mScore = 0;

    boolean fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    Random mRandom = new Random();
    float tikTime = 0;
    static float tik = TICK_INITIAL;

    public World(){
        mSnake = new Snake();
        placeStain();
    }

    private void placeStain(){
        for (int x = 0; x < WORLD_WIDTH; x++){
            for (int y = 0; y < WORLD_HEIGHT; y++){
                fields[x][y] = false;
            }
        }

        int len = mSnake.mParts.size();
        for (int i = 0; i < len; i++){
            SnakePart part = mSnake.mParts.get(i);
            fields[part.x][part.y] = true;
        }

        int stainX = mRandom.nextInt(WORLD_WIDTH);
        int stainY = mRandom.nextInt(WORLD_HEIGHT);
        while (true){
            if (fields[stainX][stainY] == false)
                break;
            stainX += 1;
            if (stainX >= WORLD_WIDTH){
                stainX = 0;
                stainY +=1;
                if (stainY >= WORLD_HEIGHT){
                    stainY = 0;
                }
            }
        }

        mStain = new Stain(stainX, stainY, mRandom.nextInt(3));
    }

    public void update(float deltaTime){
        if (mGameOver){
            return;
        }

        tikTime += deltaTime;

        while (tikTime > tik){
            tikTime -= tik;
            mSnake.advance();
            if (mSnake.checkBitten()){
                mGameOver = true;
                return;
            }

            SnakePart head = mSnake.mParts.get(0);
            if (head.x == mStain.x && head.y == mStain.y){
                mScore += SCORE_INCREMENT;
                mSnake.eat();
                if (mSnake.mParts.size() == WORLD_WIDTH * WORLD_HEIGHT){
                    mGameOver = true;
                    return;
                }else {
                    placeStain();
                }

                if (mScore % 100 == 0 && tik - TICK_DECREMENT > 0){
                    tik -= TICK_DECREMENT;
                }
            }
        }
    }
}
