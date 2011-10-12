package edu.incense.android.datatask;

import java.util.LinkedList;
import java.util.Queue;

import edu.incense.android.datatask.data.Data;

public class PipeBuffer implements Input, Output {
    Queue<Data> dataBuffer = null;

    public PipeBuffer() {
        dataBuffer = new LinkedList<Data>();
    }

    public Data pullData() {
        if (!dataBuffer.isEmpty())
            return dataBuffer.remove();
        else
            return null;
    }

    public void pushData(Data data) {
        if (dataBuffer != null)
            dataBuffer.offer(data);
    }

}
