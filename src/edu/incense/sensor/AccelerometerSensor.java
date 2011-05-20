package edu.incense.sensor;

import edu.incense.datatask.data.AccelerometerData;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerSensor extends edu.incense.sensor.Sensor implements
        SensorEventListener {

    private SensorManager sm;
    private Sensor accSensor;

    // Constructor
    public AccelerometerSensor(Context context) {
        super(context);

        // Accelerometer initialization
        String service = Context.SENSOR_SERVICE;
        sm = (SensorManager) context.getSystemService(service);
        int sensorType = Sensor.TYPE_ACCELEROMETER;
        accSensor = sm.getDefaultSensor(sensorType);
        System.out.println("Sensor name: " + accSensor.getName());
    }

    @Override
    public void start() {
        boolean success = sm.registerListener(this, accSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// getPeriodTime());
        if (success)
            System.out.println("SensorEventLister registered!!!");
        else
            System.out.println("SensorEventLister NOT registered!!!");
        super.start();
    }

    @Override
    public void stop() {
        sm.unregisterListener(this);
        super.stop();
    }

    private void setNewReadings(float newX, float newY, float newZ) {
        currentData = new AccelerometerData(newX, newY, newZ);
    }

    /*** SensorEventListener methods ***/

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        // System.out.println("Sensor changed: "+event.sensor.getName());
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAxis_lateralA = event.values[0];
            float yAxis_longitudinalA = event.values[1];
            float zAxis_verticalA = event.values[2];

            setNewReadings(xAxis_lateralA, yAxis_longitudinalA, zAxis_verticalA);
        }
    }

}
