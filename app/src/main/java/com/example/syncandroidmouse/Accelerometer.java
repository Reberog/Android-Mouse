package com.example.syncandroidmouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {

    public Listener listener;
    public Sensor sensor;
    public SensorManager sensorManager;

    public SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (Accelerometer.this.listener != null) {
                Accelerometer.this.listener.onTranslation(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };


    public interface Listener {
        void onTranslation(float f, float f2, float f3);
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    Accelerometer(Context context) {
        SensorManager sensorManager2 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorManager = sensorManager2;
        this.sensor = sensorManager2.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void register() {
        this.sensorManager.registerListener(this.sensorEventListener, this.sensor, 3);
    }

    public void unregister() {
        this.sensorManager.unregisterListener(this.sensorEventListener);
    }
}
