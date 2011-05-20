package edu.incense.sensor;

import java.util.ArrayList;
import java.util.List;

import edu.incense.datatask.data.Data;

import android.content.Context;

public abstract class Sensor {

    protected Context context;
    private float sampleFrequency; // Sample frequency
    private int periodTime; // Sleep time for each cycle (period time in
                            // milliseconds)
    protected volatile boolean isSensing = false;
    protected Data currentData = null;
    protected List<Data> dataList = null;

    protected Sensor() {
        isSensing = false;
        dataList = null;
    }

    protected Sensor(Context context) {
        this();
        this.context = context;
    }

    public synchronized void start() {
        isSensing = true;
    }

    public synchronized void stop() {
        isSensing = false;
    }

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

    /* If DataList is null, Returns a list with only one Data element */
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

    private int obtainPeriodTime(float sampleFrequency) {
        int periodTime = (int) ((1 / sampleFrequency) * 1000);
        return periodTime;
    }

    private float computeSampleFrequency(float periodTime) {
        float sampleFrequency = ((1 / periodTime) * 1000);
        return sampleFrequency;
    }

    public void setSampleFrequency(float sampleFrequency) {
        this.sampleFrequency = sampleFrequency;
        periodTime = obtainPeriodTime(sampleFrequency);
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
        sampleFrequency = computeSampleFrequency(periodTime);
    }

    public float getSampleFrequency() {
        return sampleFrequency;
    }

    protected int getPeriodTime() {
        return periodTime;
    }
}
