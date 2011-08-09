package edu.incense.android.datatask.sink;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import edu.incense.android.datatask.data.AudioData;
import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.data.DataType;
import edu.incense.android.results.QueueFileTask;
import edu.incense.android.results.ResultFile;

public class RawAudioSinkWritter implements SinkWritter {
    private static final String TAG = "RawAudioSinkWritter";
    private Context context;

    public RawAudioSinkWritter(Context context) {
        this.context = context;
    }

    public void writeSink(DataSink dataSink) {
        ResultFile resultFile = ResultFile.createAudioInstance(context,
                dataSink.getName());
        try {
            List<Data> dataList = dataSink.removeSink();

            // Create a new output file stream that�s private to this
            // application.
            Log.d(TAG, "Saving to file: " + resultFile.getFileName());
            File file = new File(resultFile.getFileName());
//            FileOutputStream fos = context.openFileOutput(
//                    resultFile.getFileName(), Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            DataOutputStream dos = new DataOutputStream(bos);

            AudioData ad;
            //StringBuilder sb;
            
            
            for (Data d : dataList) {
                if (d.getDataType() == DataType.AUDIO) {
                    ad = (AudioData) d;
                    byte[] buffer = ad.getAudioFrame();
                    // Write whole frame
                    
                    //sb = new StringBuilder();
                    for (int i = 0; i < buffer.length; i++) {
                        dos.writeByte(buffer[i]);
                        //sb.append(buffer[i]+" ");
                    }
                    //Log.d(TAG, "["+sb.toString() +"]");
                }
            }

            dos.flush();
            dos.close();
            bos.close();
            
            Toast.makeText(context, "Application saved: "+dataList.size() + " stream",
                    Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.e(TAG, "Writing RAW audio file failed", e);
        }
        new QueueFileTask(context).execute(resultFile);
    }

}