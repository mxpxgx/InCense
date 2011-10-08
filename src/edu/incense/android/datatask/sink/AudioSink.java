/**
 * 
 */
package edu.incense.android.datatask.sink;

import android.util.Log;
import edu.incense.android.datatask.Input;
import edu.incense.android.datatask.data.Data;

/**
 * @author mxpxgx
 *
 */
public class AudioSink extends DataSink{
    private final static String TAG = "AudioSink";
    private final static long MAX_TIME_WITHOUT_AUDIO = 2000;
    private long lastDataTime;
    
    /**
     * @param sinkWritter
     */
    public AudioSink(SinkWritter sinkWritter) {
        super(sinkWritter);
        lastDataTime =  System.currentTimeMillis() + MAX_TIME_WITHOUT_AUDIO; //TODO not sure about this addition
    }

    @Override
    protected void compute() {
        Data latestData = null;
        for (Input i : inputs) {
            do {
                latestData = i.pullData();
                if (latestData != null) {
//                    Log.d(TAG, "Audio frame received");
                    sink.add(latestData);
                    lastDataTime = System.currentTimeMillis();
                } else {
//                    Log.d(TAG, "Data NOT added to sink!");
                }
            } while (latestData != null);
        }
        long timeLength = System.currentTimeMillis() - lastDataTime;
//        Log.d(TAG, "Comparing: "+timeLength+" >= "+MAX_TIME_WITHOUT_AUDIO);
        if(sink !=null && sink.size() > 0 && timeLength >= MAX_TIME_WITHOUT_AUDIO){
            Log.d(TAG, "Sink sent to writter with size: "+sink.size());
            sinkWritter.writeSink(name, removeSink());
        }
    }

}
