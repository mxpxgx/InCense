package edu.incense.android.datatask;

import java.util.ArrayList;
import java.util.List;

import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.model.TaskType;

import android.util.Log;

public abstract class DataTask implements Runnable {
    private final static String TAG = "DataTask";
    private final static int DEFAULT_PERIOD_TIME = 1000;
    protected List<Input> inputs;
    protected List<Output> outputs;
    private float sampleFrequency; // Sample frequency
    protected long periodTime; // Sleep time for each cycle (period time in
                               // milliseconds)
    private TaskType taskType;
    private Thread thread = null;
    protected boolean isRunning = false;

    /**
     * @return the isRunning
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public DataTask() {
        // thread = new Thread(this);
        isRunning = false;
        setPeriodTime(DEFAULT_PERIOD_TIME);
    }

    protected void clearInputs() {
        if (inputs != null)
            inputs.clear();
        inputs = new ArrayList<Input>();
    }

    protected void clearOutputs() {
        if (outputs != null)
            outputs.clear();
        outputs = new ArrayList<Output>();
    }

    public void clear() {
        clearInputs();
        clearOutputs();
    }

    protected abstract void compute();

    /*** Inputs & Outputs ***/

    protected void addOutput(Output o) {
        if (outputs != null)
            outputs.add(o);
    }

    protected void addInput(Input i) {
        if (inputs != null)
            inputs.add(i);
    }

    protected void pushToOutputs(Data data) {
        if (outputs != null) {
            for (Output o : outputs) {
                o.pushData(data);
            }
        }
    }

    /*** Threads & Runnable ***/

    public void run() {
        while (isRunning) {
            try {

                compute();
                if (getPeriodTime() > 1) {
                    Thread.sleep(getPeriodTime());
                }
            } catch (Exception e) {
                Log.e(TAG, "Sleep: " + e);
            }
        }
    }

    public void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public void stop() {
        isRunning = false;
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            Log.e(TAG, "Task thread join failed", e);
//        }
        if (thread != null) {
            thread = null;
        }
    }

    /**
     * Computes the period time (milliseconds) based on a sample frequency in Hz
     * 
     * @param sampleFrequency
     * @return
     */
    private long computePeriodTime(float sampleFrequency) {
        long periodTime = (long) ((1.0f / sampleFrequency) * 1000f);
        return periodTime;
    }

    /**
     * Computes the sample frequency (Hz) based on a period time in milliseconds
     * 
     * @param periodTime
     * @return
     */
    private float computeSampleFrequency(float periodTime) {
        float sampleFrequency = (float) ((1f / periodTime) * 1000f);
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

    public void setPeriodTime(long periodTime) {
        this.periodTime = periodTime;
        sampleFrequency = computeSampleFrequency(periodTime);
    }

    protected long getPeriodTime() {
        return periodTime;
    }

    /**
     * @param taskType
     *            the taskType to set
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * @return the taskType
     */
    public TaskType getTaskType() {
        return taskType;
    }
}
