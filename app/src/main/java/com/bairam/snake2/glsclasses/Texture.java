package com.bairam.snake2.glsclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.bairam.snake2.FileIO;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public class Texture {
    GLGraphics mGLGraphics;
    FileIO mFileIO;
    String mFileNaame;
    int textureId;
    int minFilter;
    int magFilter;

    public Texture(GLGame glGame, String fileNaame){
        mGLGraphics = glGame.getGlGraphics();
        mFileIO = glGame.getFileIO();
        mFileNaame = fileNaame;
        load();
    }

    private void load(){
        GL10 gl = mGLGraphics.getGL();
        int textureIds[] = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];

        InputStream in = null;
        try {
            in = mFileIO.readAsset(mFileNaame);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        }catch (IOException e){
            throw new RuntimeException("Couldn't load texture '" + mFileNaame +"'", e);
        }finally {
            if (in != null){
                try {
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void reload(){
        load();
        bind();
        setFilters(minFilter, magFilter);
        mGLGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    public void setFilters(int minFilter, int magFilter){
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        GL10 gl = mGLGraphics.getGL();
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind(){
        GL10 gl = mGLGraphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose(){
        GL10 gl = mGLGraphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        int textureIds[] = {textureId};
        gl.glDeleteTextures(1, textureIds, 0);
    }
}
