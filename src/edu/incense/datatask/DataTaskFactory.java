package edu.incense.datatask;

import android.content.Context;
import edu.incense.datatask.filter.*;
import edu.incense.datatask.model.Task;
import edu.incense.datatask.sink.DataSink;
import edu.incense.datatask.sink.JsonSinkWritter;
import edu.incense.datatask.trigger.SurveyTrigger;
import edu.incense.sensor.AccelerometerSensor;
import edu.incense.sensor.AudioSensor;
import edu.incense.sensor.BluetoothSensor;
import edu.incense.sensor.GpsSensor;
import edu.incense.sensor.PhoneCallSensor;
import edu.incense.sensor.PhoneStateSensor;

public class DataTaskFactory {
    public static DataTask createDataTask(Task task, Context context) {
        DataTask dataTask = null;

        switch (task.getTaskType()) {
        case AccelerometerSensor:
            dataTask = new DataSource(new AccelerometerSensor(context));
            break;
        case AudioSensor:
            dataTask = new DataSource(new AudioSensor(context));
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
            dataTask = new DataSink(new JsonSinkWritter(context));
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
