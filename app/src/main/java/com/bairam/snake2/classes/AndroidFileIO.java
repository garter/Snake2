package com.bairam.snake2.classes;

import android.content.res.AssetManager;

import com.bairam.snake2.FileIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AndroidFileIO implements FileIO {

    AssetManager mAssetManager;
    String externalStoragePath;

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return null;
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return null;
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return null;
    }
}
