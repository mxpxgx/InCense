package edu.incense.android.datatask.sink;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import edu.incense.android.datatask.DataTask;
import edu.incense.android.datatask.Input;
import edu.incense.android.datatask.InputEnabledTask;
import edu.incense.android.datatask.data.Data;

public class DataSink extends DataTask implements InputEnabledTask {
    private final static String TAG = "DataSink";
    private String name;
    private List<Data> sink = null;

    private SinkWritter sinkWritter;

    public DataSink(SinkWritter sinkWritter) {
        this.sinkWritter = sinkWritter;
        inputs = new ArrayList<Input>();

        clear();
        initSinkList();
        setPeriodTime(1000);
    }

    public void start() {
        super.start();
        initSinkList();
    }
    
    public void stop() {
        super.stop();
        clearOutputs();
        sinkWritter.writeSink(name, sink);
        Log.d(TAG, "Sink sent to writter");
    }

    protected void clearOutputs() {
        // No outputs for DataSink
        // outputs.removeAll(outputs);
        outputs = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.UrbanMoments.DataTask.DataTask#compute() Collects the most
     * recent data from all inputs in a "sink" (List)
     */
    @Override
    protected void compute() {
        Data latestData = null;
        for (Input i : inputs) {
            do {
                latestData = i.pullData();
                if (latestData != null) {
                    //Log.d(TAG, "Data added to sink!");
                    sink.add(latestData);
                    if(sink.size() >= 100){
                        sinkWritter.writeSink(name, removeSink());
                    }
                } else {
                    //Log.d(TAG, "Data NOT added to sink!");
                }
            } while (latestData != null);
        }
    }

    public List<Data> getSink() {
        return sink;
    }
    
    /**
     * @param sink the sink to set
     */
    public void setSink(List<Data> sink) {
        this.sink = sink;
    }

    public List<Data> removeSink() {
        List<Data> temp = getSink();
        initSinkList();
        return temp;
    }

    private void initSinkList() {
        sink = new ArrayList<Data>();
    }

    @Override
    public void addInput(Input i) {
        super.addInput(i);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
