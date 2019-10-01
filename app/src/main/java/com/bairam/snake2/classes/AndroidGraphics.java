package com.bairam.snake2.classes;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bairam.snake2.Graphics;
import com.bairam.snake2.Pixmap;

import java.io.IOException;
import java.io.InputStream;

public class AndroidGraphics implements Graphics {

    AssetManager mAssetManager;
    Bitmap mFrameBuffer;
    Canvas mCanvas;
    Paint mPaint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assetManager, Bitmap frameBuffer){
        mAssetManager = assetManager;
        mFrameBuffer = frameBuffer;
        mCanvas = new Canvas(mFrameBuffer);
        mPaint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {

        Bitmap.Config config = null;
        if (format == PixmapFormat.ARGB4444){
            config = Bitmap.Config.ARGB_4444;
        }else if (format == PixmapFormat.RGB565){
            config = Bitmap.Config.RGB_565;
        }else if (format == PixmapFormat.ARGB8888){
            config = Bitmap.Config.ARGB_8888;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try{
            in = mAssetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null){
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
            }

        }catch (IOException e){
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        }finally {
            if (in != null){
                try{
                    in.close();
                }catch (IOException e){

                }
            }
        }

        if (bitmap.getConfig() == Bitmap.Config.RGB_565){
            format = PixmapFormat.RGB565;
        }else if (bitmap.getConfig() == Bitmap.Config.ARGB_4444){
            format = PixmapFormat.ARGB4444;
        }else if (bitmap.getConfig() == Bitmap.Config.ARGB_8888){
            format = PixmapFormat.ARGB8888;
        }
        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        mCanvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        mPaint.setColor(color);
        mCanvas.drawPoint(x, y, mPaint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        mPaint.setColor(color);
        mCanvas.drawLine(x, y, x2, y2, mPaint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawRect(x, y, x + width -1, y + height -1, mPaint );
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        mCanvas.drawBitmap(((AndroidPixmap) pixmap).mBitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        mCanvas.drawBitmap(((AndroidPixmap) pixmap).mBitmap, x, y, null);
    }

    @Override
    public int getWidth() {
        return mFrameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return mFrameBuffer.getHeight();
    }
}
