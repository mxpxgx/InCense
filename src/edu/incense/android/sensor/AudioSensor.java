package edu.incense.android.sensor;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;
import edu.incense.android.datatask.AudioDataSource;

public class AudioSensor extends Sensor implements Runnable {
    private final static String TAG = "AudioSensor";
    private Thread thread = null;
    private AudioRecord audioRecord;
    private int bufferSize;
    private short[] buffer;
    private int mSamplesRead;
    private AudioDataSource dataSource;

    public AudioSensor(Context context) {
        super(context);
        // this.setPeriodTime(10000); // 10 seconds of audio
        //this.setSampleFrequency(8000);
    }

    public void run() {
        // We’re important…
        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        Log.i(TAG, "Audio sensor REALLY STARTED");
        while (super.isSensing()) {
            try {

                //if (audioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
                    //Log.i(TAG, "Starting to read");
                    mSamplesRead = audioRecord.read(buffer, 0, bufferSize);

//                    if (mSamplesRead != AudioRecord.ERROR_BAD_VALUE
//                            && mSamplesRead != AudioRecord.ERROR_INVALID_OPERATION) {
                        //Log.i(TAG, "Valid reading");
                        //short[] tempBuffer = new short[bufferSize];
                        //System.arraycopy(buffer, 0, tempBuffer, 0, bufferSize);
                        
                        
                        if(dataSource != null){
                            dataSource.pushDataToBuffer(buffer, mSamplesRead);
                        }
//                        } else {
//                            newData = new AudioData();
//                            newData.pushDataToBuffer(buffer);
//                            currentData = newData;
//                        }
                    //}
                //}

            } catch (Exception e) {
                Log.e(getClass().getName(), "Audio recording failed: " + e);
            }
        }
        audioRecord.stop();
    }
    
    public void addSourceTask(AudioDataSource ds){
        dataSource = ds;
    }

    @Override
    public void start() {
        super.start();
        audioRecord = findAudioRecord((int)getSampleFrequency());
        Log.i(TAG, "AudioRecord initialized with buffer size: "+bufferSize);
        buffer = new short[bufferSize]; // bufferSize was obtained in the
                                        // finAudioRecord method
        startRecording();
        thread = new Thread(this);
        thread.start();
        Log.i(TAG, "AudioSensor started");
    }

    // private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100
    // };

    public AudioRecord findAudioRecord(int wantedRate) {
        int[] mSampleRates = new int[] { wantedRate, 8000, 11025, 22050, 44100 };
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] {
                    AudioFormat.ENCODING_PCM_8BIT,
                    AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] {
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: "
                                + audioFormat + ", channel: " + channelConfig);
                        bufferSize = AudioRecord.getMinBufferSize(rate,
                                channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(
                                    AudioSource.DEFAULT, rate, channelConfig,
                                    audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                return recorder;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void stop() {
//        Log.i(TAG, "Trying to stop");
        ///super.stop();
        super.setSensing(false);
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            Log.e(TAG, "Sensor thread join failed", e);
//        }
//        Log.i(TAG, "Thread stopped");
//        Log.i(TAG, "Sensor stopped");
        audioRecord.release();
        Log.i(TAG, "AudioSensor Stopped and released");
    }

    private void startRecording() {
        try {
            audioRecord.startRecording();
        } catch (Exception e) {
            Log.e("AudioRecord", "Recording start failed", e);
        }
    }

}
