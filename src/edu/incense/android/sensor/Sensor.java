package edu.incense.android.sensor;

import java.util.ArrayList;
import java.util.List;

import edu.incense.android.datatask.data.Data;

import android.content.Context;

/**
 * Abstract class with basic sensor functionality. This class cannot be
 * instantiated. Please extends this class when implementing a new sensor.
 * 
 * Sensed data should be stored in the currentData attribute. If a sensor
 * generates a set of data (not just one), it should use dataList to store them.
 * 
 * @author mxpxgx
 * 
 */
public abstract class Sensor {
    private Context context; // Most sensors need context access
    private float sampleFrequency; // Sample frequency
    private int periodTime; // Sleep time for each cycle (period time in
                            // milliseconds)
    private volatile boolean sensing = false; // True when sensor is
                                              // active/running
    protected Data currentData = null; // Used when just one sensed data is
                                       // generated
    protected List<Data> dataList = null; // Used when the sensor generates a
                                          // set of data values (e.g. access
                                          // points found by wifi sensors)

    private Sensor() {
        sensing = false;
        dataList = null;
    }

    protected Sensor(Context context) {
        this();
        this.setContext(context);
    }

    /**
     * Starts the sensing process, should be overridden by a child class.
     */
    public synchronized void start() {
        sensing = true;
    }

    /**
     * Stops the sensing process, should be overridden by a child class.
     */
    public synchronized void stop() {
        sensing = false;
    }

    /**
     * Returns the data generated/sensed. When data is cleared after accessed
     * (similar to a pop() from a stack)
     * 
     * @return
     */
    public Data getData() {
        if (dataList == null) {
            Data temp = currentData;
            currentData = null;
            return temp;
        } else {
            if (dataList.isEmpty()) {
                return null;
            } else {
                return dataList.remove(0);
            }
        }
    }

    /**
     * Returns a set of data sensed when required (e.g. access points found by a
     * Wi-Fi sensor). If DataList is null (not applicable), Returns a list with
     * only one Data element
     * 
     * @return
     */
    public List<Data> getDataList() {
        if (dataList != null) {
            List<Data> tmpList = dataList;
            dataList = null;
            dataList = new ArrayList<Data>();
            return tmpList;
        } else {
            dataList = new ArrayList<Data>(1);
            dataList.add(currentData);
            return dataList;
        }
    }

    /**
     * Computes the period time based on a sample frequency
     * 
     * @param sampleFrequency
     * @return
     */
    private int computePeriodTime(float sampleFrequency) {
        int periodTime = (int) ((1 / sampleFrequency) * 1000);
        return periodTime;
    }

    /**
     * Computes the sample frequency based on a period time
     * 
     * @param periodTime
     * @return
     */
    private float computeSampleFrequency(float periodTime) {
        float sampleFrequency = ((1 / periodTime) * 1000);
        return sampleFrequency;
    }

    /* SETS AND GETS */

    public void setSampleFrequency(float sampleFrequency) {
        this.sampleFrequency = sampleFrequency;
        periodTime = computePeriodTime(sampleFrequency);
    }

    public float getSampleFrequency() {
        return sampleFrequency;
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
        sampleFrequency = computeSampleFrequency(periodTime);
    }

    protected int getPeriodTime() {
        return periodTime;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setSensing(boolean sensing) {
        this.sensing = sensing;
    }

    public boolean isSensing() {
        return sensing;
    }
}
