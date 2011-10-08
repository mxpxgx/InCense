package edu.incense.android.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import edu.incense.android.datatask.data.AccelerometerFrameData;

/**
 * Stores new values sensed by the accelerometer in a AccelerometerFrameData object.
 * @author mxpxgx
 * @version 1.1, 05/09/2011
 */

public class AccelerometerSensor extends edu.incense.android.sensor.Sensor
        implements SensorEventListener {
    private static final String TAG = "AccelerometerSensor";
    public static final String ATT_FRAMETIME = "frameTime";
    private static final int DEFAUL_FRAME_SIZE = 10;
    private SensorManager sm;
    private Sensor accelerometer;
    private AccelerometerFrameData workingData;
    private int sensorType;
    private long lastTimestamp;
    private long wantedPeriod;
    private int frameSize;
    private long frameTime;

    public AccelerometerSensor(Context context, int sensorType, long frameTime) {
        super(context);
        this.sensorType = sensorType;
        this.frameTime = frameTime;
        // Accelerometer initialization
        String service = Context.SENSOR_SERVICE;
        sm = (SensorManager) context.getSystemService(service);
        accelerometer = sm.getDefaultSensor(sensorType);
        Log.d(TAG, "Sensor name: " + accelerometer.getName());
        
        workingData = null;
        frameSize = DEFAUL_FRAME_SIZE;
    }
    
    public static AccelerometerSensor createAccelerometer(Context context, long frameTime){
        AccelerometerSensor sensor = new AccelerometerSensor(context, Sensor.TYPE_ACCELEROMETER, frameTime);
        return sensor;
    }
    
    public static AccelerometerSensor createGyroscope(Context context, long frameTime){
        AccelerometerSensor sensor = new AccelerometerSensor(context, Sensor.TYPE_GYROSCOPE, frameTime);
        return sensor;
    }

    @Override
    public void start() {
        wantedPeriod = getPeriodTime()*1000000; //milliseconds to nanoseconds
        if(frameTime > getPeriodTime()){
            frameSize = (int)(frameTime / getPeriodTime());
            Log.d(TAG, "Frame size:"+frameSize);
        }
        lastTimestamp = 0;
        boolean success = sm.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);//SENSOR_DELAY_GAME);//.SENSOR_DELAY_NORMAL);
        if (success){
            super.setSensing(true);
            Log.d(TAG, "SensorEventLister registered!");
        } else {
            super.setSensing(false);
            Log.d(TAG, "SensorEventLister NOT registered!");
        }
    }

    @Override
    public void stop() {
        sm.unregisterListener(this);
        super.setSensing(false);
    }

    /**
     * Stores new axis values in a AccelerometerData object.
     * @param newX
     * @param newY
     * @param newZ
     */
    private void setNewReadings(double newX, double newY, double newZ, long timestamp) {
        if(workingData == null){
            if(sensorType == Sensor.TYPE_ACCELEROMETER){
                workingData = new AccelerometerFrameData(frameSize);
            } else {
                workingData = AccelerometerFrameData.createGyroFrameData(frameSize);
            }
        }
        workingData.add(newX, newY, newZ, timestamp);
        
        if(workingData.full()){
            currentData = workingData;
            workingData = null;
        }
    }

    /* SensorEventListener methods */
    
    /**
     *  Stores new accelerometer values when a change is sensed
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long period = event.timestamp - lastTimestamp;
//            Log.d(TAG, "Substracting: "+event.timestamp+" - "+lastTimestamp+" = "+period);
//            Log.d(TAG, "Comparing: "+period+" >= "+wantedPeriod);
            if(period >= wantedPeriod){
                double xAxis_lateralA = event.values[0];
                double yAxis_longitudinalA = event.values[1];
                double zAxis_verticalA = event.values[2];
                setNewReadings(xAxis_lateralA, yAxis_longitudinalA, zAxis_verticalA, System.currentTimeMillis());
                lastTimestamp = event.timestamp;
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
