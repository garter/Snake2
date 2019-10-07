package com.bairam.snake2;

import android.graphics.Color;

import java.util.List;

public class GameScreen extends Screen {

    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }

    GameState mState = GameState.Ready;
    World mWorld;
    int oldScore = 0;
    String score = "0";

    public GameScreen(Game game){
        super(game);
        mWorld = new World();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (mState == GameState.Ready){
            updateReady(touchEvents);
        }
        if (mState == GameState.Running){
            updateRunning(touchEvents, deltaTime);
        }
        if (mState == GameState.Paused){
            updatePaused(touchEvents);
        }
        if (mState == GameState.GameOver){
            updateGameOver(touchEvents);
        }

    }

    private void updateReady(List<Input.TouchEvent> touchEvents){
        if (touchEvents.size() > 0){
            mState = GameState.Running;
        }
    }

    private void updateRunning(List<Input.TouchEvent> touchEvents, float deltaTime){
        int len = touchEvents.size();
        for (int i = 0; i < len; i++){
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP){
                if (event.x < 64 && event.y < 64){
                    if (Settings.soundEnabled)
                        Assets.click.play(1);
                    mState = GameState.Paused;
                    return;
                }
            }
            if (event.type == Input.TouchEvent.TOUCH_DOWN){
                if (event.x < 64 && event.y > 416){
                    mWorld.mSnake.turnLeft();
                }
                if (event.x > 256 && event.y > 416){
                    mWorld.mSnake.turnRight();
                }
            }
        }

        mWorld.update(deltaTime);
        if (mWorld.mGameOver){
            if (Settings.soundEnabled)
                Assets.click.play(1);
            mState = GameState.GameOver;
        }
        if (oldScore != mWorld.mScore){
            oldScore = mWorld.mScore;
            score = " " + oldScore;
            if (Settings.soundEnabled)
                Assets.eat.play(1);
        }
    }

    private void updatePaused(List<Input.TouchEvent> touchEvents){
        int len = touchEvents.size();
        for (int i = 0; i < len; i++){
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP){
                if(event.x > 80 && event.x <= 240){
                    if(event.y > 100 && event.y <= 148){
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        mState = GameState.Running;
                        return;
                    }
                    if(event.y > 148 && event.y < 196) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        game.setScreen(new MainMenuScreen(game));
                        return;
                    }
                }
            }
        }
    }

    private void updateGameOver(List<Input.TouchEvent> touchEvents){
        int len = touchEvents.size();
        for (int i = 0; i < len; i++){
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                        event.y >= 200 && event.y <= 264) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0);
        drawWorld(mWorld);
        if(mState == GameState.Ready)
            drawReadyUI();
        if(mState == GameState.Running)
            drawRunningUI();
        if(mState == GameState.Paused)
            drawPausedUI();
        if(mState == GameState.GameOver)
            drawGameOverUI();

        drawText(g, score, g.getWidth() / 2 - score.length()*20 / 2, g.getHeight() - 42);
    }

    private void drawWorld(World world){
        Graphics g = game.getGraphics();
        Snake snake = world.mSnake;
        SnakePart head = snake.mParts.get(0);
        Stain stain = world.mStain;

        Pixmap stainPixmap = null;
        if (stain.type == Stain.TYPE_1)
            stainPixmap = Assets.stain1;
        if(stain.type == Stain.TYPE_2)
            stainPixmap = Assets.stain2;
        if(stain.type == Stain.TYPE_3)
            stainPixmap = Assets.stain3;
        int x = stain.x * 32;
        int y = stain.y * 32;
        g.drawPixmap(stainPixmap, x, y);

        int len = snake.mParts.size();
        for (int i = 1; i < len; i++){
            SnakePart part = snake.mParts.get(i);
            x = part.x * 32;
            y = part.y * 32;
            g.drawPixmap(Assets.tail, x, y);
        }

        Pixmap headPixmap = null;
        if(snake.direction == Snake.UP)
            headPixmap = Assets.headUp;
        if(snake.direction == Snake.LEFT)
            headPixmap = Assets.headLeft;
        if(snake.direction == Snake.DOWN)
            headPixmap = Assets.headDown;
        if(snake.direction == Snake.RIGHT)
            headPixmap = Assets.headRight;
        x = head.x * 32 + 16;
        x = head.y * 32 + 16;
        g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
    }

    private void drawReadyUI(){
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.ready, 47, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawRunningUI(){
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    }

    private void drawPausedUI(){
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.pause, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI(){
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.gameOver, 62, 100);
        g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    public void drawText(Graphics g, String line, int x, int y){
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);
            if (character == ' ') {
                x += 20;
                continue;
            }
            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20; }

            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override
    public void pause() {
        if (mState == GameState.Running)
            mState = GameState.Paused;

        if (mWorld.mGameOver){
            Settings.addScore(mWorld.mScore);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
