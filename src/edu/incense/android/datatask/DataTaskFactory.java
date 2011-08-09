package edu.incense.android.datatask;

import android.content.Context;
import edu.incense.android.datatask.filter.AccelerometerMeanFilter;
import edu.incense.android.datatask.filter.ShakeFilter;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.sink.DataSink;
import edu.incense.android.datatask.sink.JsonSinkWritter;
import edu.incense.android.datatask.sink.RawAudioSinkWritter;
import edu.incense.android.datatask.trigger.SurveyTrigger;
import edu.incense.android.sensor.AccelerometerSensor;
import edu.incense.android.sensor.AudioSensor;
import edu.incense.android.sensor.BluetoothSensor;
import edu.incense.android.sensor.GpsSensor;
import edu.incense.android.sensor.PhoneCallSensor;
import edu.incense.android.sensor.PhoneStateSensor;

public class DataTaskFactory {
    public static DataTask createDataTask(Task task, Context context) {
        DataTask dataTask = null;

        switch (task.getTaskType()) {
        case AccelerometerSensor:
            dataTask = new DataSource(new AccelerometerSensor(context));
            break;
        case AudioSensor:
            AudioSensor as = new AudioSensor(context);
            dataTask = new AudioDataSource(as);
            as.addSourceTask((AudioDataSource)dataTask); //AudioSensor is faster than DataTask
            break;
        case BluetoothSensor:
            dataTask = new DataSource(new BluetoothSensor(context));
            break;
        case GpsSensor:
            dataTask = new DataSource(new GpsSensor(context));
            break;
        case CallSensor:
            dataTask = new DataSource(new PhoneCallSensor(context));
            break;
        case StateSensor:
            dataTask = new DataSource(new PhoneStateSensor(context));
            break;
        case AccelerometerMeanFilter:
            dataTask = new AccelerometerMeanFilter();
            break;
        case DataSink:
            // Set SinkWritter type (Json)
            // It will write results to a JSON file
            dataTask = new DataSink(new JsonSinkWritter(context));
            ((DataSink) dataTask).setName(task.getName());
            break;
        case AudioSink:
            // Set SinkWritter type (Json)
            // It will write results to a RAW file
            dataTask = new DataSink(new RawAudioSinkWritter(context));
            ((DataSink) dataTask).setName(task.getName());
            break;
        case ShakeFilter:
            dataTask = new ShakeFilter();
            break;
        case SurveyTrigger:
            dataTask = new SurveyTrigger(context);
            ((SurveyTrigger) dataTask).setSurveyName("mainSurvey");// task.getString("surveyName",
                                                                   // "mainSurvey"));
            break;
        default:
            return null;
        }

        dataTask.setSampleFrequency(task.getSampleFrequency());
        return dataTask;
    }
}
