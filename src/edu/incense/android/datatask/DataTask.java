package edu.incense.android.datatask;

import java.util.ArrayList;
import java.util.List;

import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.model.TaskType;

import android.util.Log;

public abstract class DataTask implements Runnable {
    private final static String TAG = "DataTask";
    protected List<Input> inputs;
    protected List<Output> outputs;
    private float sampleFrequency; // Sample frequency
    protected int periodTime = 10000; // Sleep time for each cycle (period time in
                                    // milliseconds)
    private TaskType taskType;
    private Thread thread = null;
    protected boolean isRunning = false;

    public DataTask() {
        // thread = new Thread(this);
        isRunning = false;
        periodTime = 5000;
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
                if(getPeriodTime() > 1){
                    Log.d(TAG, "sleeping: "+getPeriodTime());
                    Thread.sleep(getPeriodTime());
                }
            } catch (Exception e) {
                Log.e(getClass().getName(), "Sleep: " + e);
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
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Task thread join failed", e);
        }
        if (thread != null) {
            // thread.interrupt();
            thread = null;
        }
    }

    /*** Frequency & Period Times ***/

    private int obtainPeriodTime(float sampleFrequency) {
        int periodTime = (int) ((1 / sampleFrequency) * 1000);
        return periodTime;
    }

    private float obtainSampleFrequency(float periodTime) {
        float sampleFrequency = ((1 / periodTime) * 1000);
        return sampleFrequency;
    }

    public void setSampleFrequency(float sampleFrequency) {
        this.sampleFrequency = sampleFrequency;
        periodTime = obtainPeriodTime(sampleFrequency);
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
        sampleFrequency = obtainSampleFrequency(periodTime);
    }

    protected float getSampleFrequency() {
        return sampleFrequency;
    }

    protected int getPeriodTime() {
        return periodTime;
    }

    /**
     * @param taskType the taskType to set
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
