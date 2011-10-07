package edu.incense.android.datatask;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import edu.incense.android.datatask.filter.AccelerometerMeanFilter;
import edu.incense.android.datatask.filter.ShakeFilter;
import edu.incense.android.datatask.filter.WifiTimeConnectedFilter;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.sink.DataSink;
import edu.incense.android.datatask.sink.JsonSinkWritter;
import edu.incense.android.datatask.sink.RawAudioSinkWritter;
import edu.incense.android.datatask.trigger.Condition;
import edu.incense.android.datatask.trigger.GeneralTrigger;
import edu.incense.android.datatask.trigger.JsonTrigger;
import edu.incense.android.datatask.trigger.SurveyTrigger;
import edu.incense.android.sensor.AccelerometerSensor;
import edu.incense.android.sensor.AudioSensor;
import edu.incense.android.sensor.BluetoothConnectionSensor;
import edu.incense.android.sensor.BluetoothSensor;
import edu.incense.android.sensor.GpsSensor;
import edu.incense.android.sensor.NfcSensor;
import edu.incense.android.sensor.PhoneCallSensor;
import edu.incense.android.sensor.PhoneStateSensor;
import edu.incense.android.sensor.WifiConnectionSensor;
import edu.incense.android.sensor.WifiScanSensor;

public class DataTaskFactory {
    public static DataTask createDataTask(Task task, Context context) {
        DataTask dataTask = null;

        switch (task.getTaskType()) {
        case AccelerometerSensor:
            dataTask = new DataSource(AccelerometerSensor.createAccelerometer(context));
            break;
        case AudioSensor:
            AudioSensor as = new AudioSensor(context);
            dataTask = new AudioDataSource(as);
            as.addSourceTask((AudioDataSource)dataTask); //AudioSensor is faster than DataTask
            break;
        case BluetoothSensor:
            dataTask = new DataSource(new BluetoothSensor(context));
            break;
        case BluetoothConnectionSensor:
            dataTask = new DataSource(new BluetoothConnectionSensor(context, task.getString("address", "")));
            break;
        case GpsSensor:
            dataTask = new DataSource(new GpsSensor(context));
            break;
        case GyroscopeSensor:
            dataTask = new DataSource(AccelerometerSensor.createGyroscope(context));
            break;
        case CallSensor:
            dataTask = new DataSource(new PhoneCallSensor(context));
            break;
        case StateSensor:
            dataTask = new DataSource(new PhoneStateSensor(context));
            break;
        case NfcSensor:
            dataTask = new DataSource(new NfcSensor(context));
            break;
        case WifiScanSensor:
            dataTask = new DataSource(new WifiScanSensor(context));
            break;
        case WifiConnectionSensor:
            String[] ap = task.getStringArray("accessPoints");
            List<String> apList = Arrays.asList(ap);
            dataTask = new DataSource(new WifiConnectionSensor(context, apList));
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
        case WifiTimeConnectedFilter:
            dataTask = new WifiTimeConnectedFilter();
            break;
        case SurveyTrigger:
            dataTask = new SurveyTrigger(context);
            ((SurveyTrigger) dataTask).setSurveyName("mainSurvey");// task.getString("surveyName",
            break;
        case Trigger:
            String matches = task.getString(JsonTrigger.MATCHES, null);
            JsonTrigger jsonTrigger = new JsonTrigger();
            List<Condition> conditionsList = jsonTrigger.toConditions(task.getJsonNode());
            dataTask = new GeneralTrigger(context, conditionsList, matches);
            break;
        default:
            return null;
        }
//        dataTask.setSampleFrequency(task.getSampleFrequency());
        dataTask.setPeriodTime(1000);
        dataTask.setTaskType(task.getTaskType());
        return dataTask;
    }
}
