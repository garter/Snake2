package com.bairam.snake2;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public List<SnakePart> mParts = new ArrayList<SnakePart>();
    public int direction;

    public Snake(){
        direction = UP;
        mParts.add(new SnakePart(5, 6));
        mParts.add(new SnakePart(5, 7));
        mParts.add(new SnakePart(5, 8));
    }

    public void turnLeft(){
        direction += 1;
        if (direction > RIGHT){
            direction = UP;
        }
    }

    public void turnRight(){
        direction -= 1;
        if (direction < UP){
            direction = RIGHT;
        }
    }

    public void eat(){
        SnakePart end = mParts.get(mParts.size() - 1);
        mParts.add(new SnakePart(end.x, end.y));
    }

    public void advance(){
        SnakePart head = mParts.get(0);

        int len = mParts.size() - 1;
        for (int i = len; i > 0; i--){
            SnakePart before = mParts.get(i - 1);
            SnakePart part = mParts.get(i);
            part.x = before.x;
            part.y = before.y;
        }

        if (direction == UP){
            head.y -= 1;
        }
        if (direction == LEFT){
            head.x -= 1;
        }
        if (direction == DOWN){
            head.y += 1;
        }
        if (direction == RIGHT){
            head.x += 1;
        }

        if (head.x < 0){
            head.x = 9;
        }
        if (head.x > 9){
            head.x = 0;
        }
        if (head.y < 0){
            head.y = 12;
        }
        if (head.y > 12){
            head.y = 0;
        }
    }

    public boolean  checkBitten(){
        int len = mParts.size();
        SnakePart head = mParts.get(0);
        for (int i = 1; i < len; i++){
            SnakePart part = mParts.get(i);
            if (part.x == head.x && part.y == head.y){
                return true;
            }
        }
        return false;
    }
}
