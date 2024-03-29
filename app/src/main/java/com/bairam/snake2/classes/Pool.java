package com.bairam.snake2.classes;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
    public interface PoolObjectFactory<T>{
        public T createObject();
    }

    private final List<T> freeObject;
    private final PoolObjectFactory<T> mFactory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize){
        mFactory = factory;
        this.maxSize = maxSize;
        this.freeObject = new ArrayList<T>(maxSize);
    }

    public T newObject(){
        T object = null;

        if (freeObject.size() == 0){
            object = mFactory.createObject();
        }else {
            object = freeObject.remove(freeObject.size() -1);
        }

        return object;
    }

    public void free(T object){
        if (freeObject.size() < maxSize){
            freeObject.add(object);
        }
    }
}
