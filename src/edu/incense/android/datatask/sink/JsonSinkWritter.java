package edu.incense.android.datatask.sink;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.util.Log;

import edu.incense.android.results.QueueFileTask;
import edu.incense.android.results.ResultFile;

public class JsonSinkWritter implements SinkWritter {
    private static final String TAG = "JsonSinkWritter";
    private Context context;
    private ObjectMapper mapper;

    public JsonSinkWritter(Context context) {
        this.context = context;
        mapper = new ObjectMapper();
    }

    public void writeSink(DataSink dataSink) {
        ResultFile resultFile = ResultFile.createDataInstance(context,
                dataSink.getName());
        try {
            mapper.writeValue(new File(resultFile.getFileName()), dataSink);
        } catch (JsonParseException e) {
            Log.e(TAG, "Parsing JSON file failed", e);
        } catch (JsonMappingException e) {
            Log.e(TAG, "Mapping JSON file failed", e);
        } catch (IOException e) {
            Log.e(TAG, "Reading JSON file failed", e);
        }
        new QueueFileTask(context).execute(resultFile);
    }

}
