package com.bairam.snake2.classes;

import android.graphics.Bitmap;

import com.bairam.snake2.Graphics;
import com.bairam.snake2.Pixmap;

public class AndroidPixmap implements Pixmap {

    Bitmap mBitmap;
    Graphics.PixmapFormat mFormat;

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapFormat format){
        mBitmap = bitmap;
        mFormat = format;
    }

    @Override
    public int getWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return mFormat;
    }

    @Override
    public void dispose() {
        mBitmap.recycle();
    }
}
