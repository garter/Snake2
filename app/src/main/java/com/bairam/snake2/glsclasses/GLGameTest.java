package com.bairam.snake2.glsclasses;

import com.bairam.snake2.Game;
import com.bairam.snake2.Screen;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class GLGameTest extends GLGame {

    @Override
    public Screen getStartScreen() {
        return new TestScreen(this);
    }

    class TestScreen extends Screen{

        GLGraphics glGraphics;
        Random rand = new Random();

        public TestScreen(Game game){
            super(game);
            glGraphics = ((GLGame) game).getGlGraphics();
        }
        @Override
        public void update(float deltaTime) {

        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = glGraphics.getGL();
            gl.glClearColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.8f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void dispose() {

        }
    }
}
