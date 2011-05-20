package edu.incense.datatask;

import java.util.ArrayList;

import android.util.Log;

import edu.incense.datatask.data.Data;
import edu.incense.sensor.Sensor;

public class DataSource extends DataTask implements OutputEnabledTask {
    Sensor sensor;

    public DataSource(Sensor sensor) {
        super();
        this.sensor = sensor;
        outputs = new ArrayList<Output>();
        clear();
    }

    @Override
    public void start() {
        sensor.start();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        sensor.stop();
    }

    public void setSampleFrequency(float sampleFrequency) {
        sensor.setSampleFrequency(sampleFrequency);
        super.setSampleFrequency(sampleFrequency);
    }

    protected void clearInputs() {
        // No inputs for DataSource
        inputs = null;
    }

    @Override
    protected void compute() {
        Data newData;// = sensor.getData();
        // do{
        Log.i(getClass().getName(), "Asking for new data");
        newData = sensor.getData();
        if (newData != null) {
            this.pushToOutputs(newData);
            Log.i(getClass().getName(), "NEW DATA: " + newData.toString());
        } else {
            Log.i(getClass().getName(), "NO DATA");
        }

        // }while(newData != null);
    }

    @Override
    public void addOutput(Output o) {
        super.addOutput(o);
    }

}
