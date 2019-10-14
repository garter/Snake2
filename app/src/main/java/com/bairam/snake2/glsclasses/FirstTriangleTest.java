package com.bairam.snake2.glsclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.bairam.snake2.Game;
import com.bairam.snake2.Screen;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class FirstTriangleTest extends GLGame {
    @Override
    public Screen getStartScreen() {
        return new IndexedScreen(this);
    }

    class FirstTriangleScreen extends Screen{

        GLGraphics mGLGraphics;
        FloatBuffer vertices;

        public FirstTriangleScreen(Game game){
            super(game);

            mGLGraphics = ((GLGame)game).getGlGraphics();

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3*2*4);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertices = byteBuffer.asFloatBuffer();
            vertices.put(new float[]{
               0.0f, 0.0f,
               319.0f, 0.0f,
               160.0f, 479.0f
            });
            vertices.flip();
        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getKeyEvents();
            game.getInput().getTouchEvents();
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = mGLGraphics.getGL();
            gl.glViewport(0, 0, mGLGraphics.getWidth(), mGLGraphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, 320, 0, 480, 1, -1);

            gl.glColor4f(0, 1, 1, 1);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertices);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
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

    class ColoredTriangleScreen extends Screen{

        final int VERTEX_SIZE = (2 + 4) * 4;
        GLGraphics mGLGraphics;
        FloatBuffer vertices;

        public ColoredTriangleScreen(Game game){
            super(game);

            mGLGraphics = ((GLGame)game).getGlGraphics();

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3 * VERTEX_SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertices = byteBuffer.asFloatBuffer();
            vertices.put(new float[]{
                    0.0f, 0.0f, 1, 0, 0, 1,
                    319.0f, 0.0f, 0, 1, 0, 1,
                    160.0f, 479.0f, 0, 0, 1, 1
            });
            vertices.flip();
        }

        @Override
        public void update(float deltaTime) {

        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = mGLGraphics.getGL();
            gl.glViewport(0, 0, mGLGraphics.getWidth(), mGLGraphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, 320, 0, 480, 1, -1);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            vertices.position(0);
            gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
            vertices.position(2);
            gl.glColorPointer(4, GL10.GL_FLOAT, VERTEX_SIZE, vertices);

            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
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

    class TexturedTriangleScreen extends Screen{

        final int VERTEX_SIZE = (2 + 2) * 4;
        GLGraphics mGLGraphics;
        FloatBuffer vertices;
        int textureId;

        public TexturedTriangleScreen(Game game){
            super(game);
            mGLGraphics = ((GLGame)game).getGlGraphics();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect( 3 * VERTEX_SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertices = byteBuffer.asFloatBuffer();
            vertices.put(new float[]{
               0.0f, 0.0f, 0.0f, 1.0f,
               319.0f, 0.0f, 1.0f, 1.0f,
                    160.0f, 479.0f, 0.5f, 0.0f
            });
            vertices.flip();
            textureId = loadTexture("background.png");
        }

        public int loadTexture(String fileName){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(game.getFileIO().readAsset(fileName));
                GL10 gl = mGLGraphics.getGL();

                int texturaIds[] = new int[1];
                gl.glGenTextures(1, texturaIds, 0);
                int texturaId = texturaIds[0];
                gl.glBindTexture(GL10.GL_TEXTURE_2D, texturaId);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
                bitmap.recycle();
                return texturaId;
            }catch (IOException e){
                e.printStackTrace();
                throw new RuntimeException("couldn't load asset '" + fileName + "'");
            }
        }
        @Override
        public void update(float deltaTime) {

        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = mGLGraphics.getGL();
            gl.glViewport(0, 0, mGLGraphics.getWidth(), mGLGraphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, 320, 0, 480, 1, -1);

            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            vertices.position(0);
            gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
            vertices.position(2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
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

    class IndexedScreen extends Screen{

        final int VERTEX_SIZE = (2 + 2) * 4;
        GLGraphics mGLGraphics;
        FloatBuffer vertices;
        ShortBuffer indices;
        Texture mTexture;

        public IndexedScreen(Game game){
            super(game);
            mGLGraphics = ((GLGame)game).getGlGraphics();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * VERTEX_SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertices = byteBuffer.asFloatBuffer();
            vertices.put(new float[]{
                    100.0f, 100.0f, 0.0f, 1.0f,
                    228.0f, 100.0f, 1.0f, 1.0f,
                    228.0f, 228.0f, 1.0f, 0.0f,
                    100.0f, 228.0f, 0.0f, 0.0f
            });
            vertices.flip();

            byteBuffer = ByteBuffer.allocateDirect(6 * 2);
            byteBuffer.order(ByteOrder.nativeOrder());
            indices = byteBuffer.asShortBuffer();
            indices.put(new short[]{
                    0, 1, 2, 2, 3, 0
            });
            indices.flip();

            mTexture = new Texture((GLGame)game, "background.png");
        }

        @Override
        public void update(float deltaTime) {

        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = mGLGraphics.getGL();
            gl.glViewport(0, 0, mGLGraphics.getWidth(), mGLGraphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, 320, 0, 480, 1, -1);

            gl.glEnable(GL10.GL_TEXTURE_2D);
            mTexture.bind();

            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            vertices.position(0);
            gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
            vertices.position(2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);

            gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indices);
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
