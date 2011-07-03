package edu.incense.android.sensor;

import edu.incense.android.datatask.data.AudioData;
import edu.incense.android.results.FileType;
import edu.incense.android.results.QueueFileTask;
import edu.incense.android.results.ResultFile;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioSensor extends Sensor implements Runnable {

    private MediaRecorder mediaRecorder = null;
    private AudioData newData = null;
    private ResultFile resultFile = null;
    private Thread thread = null;

    public AudioSensor(Context context) {
        super(context);
        thread = new Thread(this);
        this.setPeriodTime(10000); // 10 seconds of audio
    }

    public synchronized void run() {
        while (super.isSensing()) {
            try {

                //Thread.sleep(getPeriodTime());
                // TODO Verify this is not affected by wait
                wait(getPeriodTime());

                newData = new AudioData();
                saveAndRestartRecording(newData);

            } catch (Exception e) {
                Log.e(getClass().getName(), "Sleep: " + e);
            }
        }
    }

    @Override
    public void start() {
        super.start();
        newData = new AudioData();
        resultFile = ResultFile.createInstance(getContext(), FileType.AUDIO);
        newData.setFilePath(resultFile.getFileName());
        startRecording(newData);
        thread.start();
    }

    @Override
    public void stop() {
        super.stop();
        finishRecording();
    }

    private void configureRecording(AudioData newData) {
        // Configure the input sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// MIC);
        // Set the output format
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // Specify the audio encoding
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mediaRecorder.setMaxDuration(1000 * 10);

        // Specify the output file
        // mediaRecorder.setOutputFile(AUDIO_FILE_PATH);
        mediaRecorder.setOutputFile(newData.getFilePath());
    }

    private void startRecording(AudioData newData) {

        // Start audio recording
        mediaRecorder = new MediaRecorder();
        configureRecording(newData);

        // Prepare to record
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            Log.e(getClass().getName(), "Audio recording failed.", e);
            finishRecording();
        }

    }

    private void finishRecording() {
        // Stop audio recording
        try {
            mediaRecorder.stop();
            mediaRecorder.release();

        } catch (Exception e) {
            Log.e(getClass().getName(), "Audio stop recording failed.", e);
        }
        new QueueFileTask(getContext()).execute(resultFile);
        currentData = newData;
    }

    private void saveAndRestartRecording(AudioData newData) {
        try {
            mediaRecorder.stop(); // reset?
            configureRecording(newData);
            // Prepare to record
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            Log.e(getClass().getName(), "Audio restart recording failed.", e);
        }
        currentData = newData;
    }

}
