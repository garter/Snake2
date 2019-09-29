package com.bairam.snake2.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerHandler implements SensorEventListener {

    float mAccelX;
    float mAccelY;
    float mAccelZ;

    public AccelerometerHandler(Context context){
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0){
            Sensor accelormetr = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelormetr, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mAccelX = sensorEvent.values[0];
        mAccelY = sensorEvent.values[1];
        mAccelZ = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getAccelX() {
        return mAccelX;
    }

    public float getAccelY() {
        return mAccelY;
    }

    public float getAccelZ() {
        return mAccelZ;
    }
}
