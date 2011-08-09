package edu.incense.android.datatask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;
import edu.incense.android.datatask.data.AudioData;
import edu.incense.android.sensor.Sensor;

public class AudioDataSource extends DataTask implements OutputEnabledTask {
    private final static String TAG = "AudioDataSource";
    Sensor sensor;
    private Object mutex = new Object();

    public AudioDataSource(Sensor sensor) {
        super();
        this.sensor = sensor;
        outputs = new ArrayList<Output>();
        clear();
    }

    private ByteArrayOutputStream baos;
    private BufferedOutputStream bos;
    private DataOutputStream dos;
    private int outPos = 0;
    private byte[] bufferArray = new byte[0];
    private byte[] tempArray;

    @Override
    public void start() {
        // File file = new File("/sdcard/audio.raw");
        // if (file.exists()) {
        // file.delete();
        // }
        // file.createNewFile();
        // bos = new BufferedOutputStream(new FileOutputStream(file));
        baos = new ByteArrayOutputStream(256);
        bos = new BufferedOutputStream(baos);
        dos = new DataOutputStream(bos);

        sensor.start();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        sensor.stop();
        try {
            dos.flush();
            dos.close();
            bos.close();
        } catch (IOException e) {
            Log.e(TAG, "Writing RAW audio file failed", e);
        }
    }

    public void setSampleFrequency(float sampleFrequency) {
        sensor.setSampleFrequency(sampleFrequency);
        super.setSampleFrequency(sampleFrequency * 2 * 2);
    }

    protected void clearInputs() {
        // No inputs for DataSource
        inputs = null;
    }

    @Override
    protected void compute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Failed to sleep", e);
        }
        
        synchronized (mutex){
            bufferArray = baos.toByteArray();
        }
        
        if(bufferArray.length > outPos){
            tempArray = new byte[bufferArray.length-outPos];
            System.arraycopy(bufferArray, outPos, tempArray, 0, tempArray.length);
            AudioData newData = new AudioData();
            newData.setAudioFrame(tempArray);
            outPos = bufferArray.length;
            if (newData != null) {
                this.pushToOutputs(newData);
                // Log.i(getClass().getName(), "NEW DATA: " + newData.toString());
            } 
//            else {
//                // Log.i(getClass().getName(), "NO DATA");
//            }
        }
    }

    public void pushDataToBuffer(short[] tempBuffer, int bufferSize) {
        synchronized(mutex){
            try {
                for (int i = 0; i < bufferSize; i++) {
                    dos.writeShort(tempBuffer[i]);
                }
                
            } catch (IOException e) {
                Log.e("Prueba", "Writing RAW audio file failed", e);
            }
            
            // AudioData newData = new AudioData();
            // newData.setAudioFrame(tempBuffer);
            // pushToOutputs(newData);
        }
    }

    @Override
    public void addOutput(Output o) {
        super.addOutput(o);
    }

}
