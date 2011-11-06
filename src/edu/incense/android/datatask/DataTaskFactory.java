package edu.incense.android.datatask;

import java.util.List;

import android.content.Context;
import edu.incense.android.datatask.filter.AccelerometerMeanFilter;
import edu.incense.android.datatask.filter.ShakeFilter;
import edu.incense.android.datatask.filter.WifiTimeConnectedFilter;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.sink.AudioSink;
import edu.incense.android.datatask.sink.DataSink;
import edu.incense.android.datatask.sink.JsonSinkWritter;
import edu.incense.android.datatask.sink.RawAudioSinkWritter;
import edu.incense.android.datatask.trigger.Condition;
import edu.incense.android.datatask.trigger.GeneralTrigger;
import edu.incense.android.datatask.trigger.JsonTrigger;
import edu.incense.android.datatask.trigger.StopTrigger;
import edu.incense.android.datatask.trigger.SurveyTrigger;
import edu.incense.android.sensor.AccelerometerSensor;
import edu.incense.android.sensor.AudioSensor;
import edu.incense.android.sensor.BluetoothConnectionSensor;
import edu.incense.android.sensor.BluetoothSensor;
import edu.incense.android.sensor.GpsSensor;
import edu.incense.android.sensor.NfcSensor;
import edu.incense.android.sensor.PhoneCallSensor;
import edu.incense.android.sensor.PhoneStateSensor;
import edu.incense.android.sensor.PowerConnectionSensor;
import edu.incense.android.sensor.Sensor;
import edu.incense.android.sensor.TimerSensor;
import edu.incense.android.sensor.WifiConnectionSensor;
import edu.incense.android.sensor.WifiScanSensor;

public class DataTaskFactory {
    public static DataTask createDataTask(Task task, Context context) {
        DataTask dataTask = null;

        switch (task.getTaskType()) {
        case AccelerometerSensor:
            long frameTime = task.getLong(AccelerometerSensor.ATT_FRAMETIME, 1000);
            long duration = task.getLong(AccelerometerSensor.ATT_DURATION, 500);
            Sensor sensor = AccelerometerSensor.createAccelerometer(
                    context, frameTime, duration);
            if (task.getSampleFrequency() > 0) {
                sensor.setSampleFrequency(task.getSampleFrequency());
            } else if (task.getPeriodTime() > 0) {
                sensor.setPeriodTime(task.getPeriodTime());
            }
            dataTask = new DataSource(sensor);
            task.setPeriodTime(frameTime);
            task.setSampleFrequency(-1.0f);
            break;
        case TimerSensor:
            long period = task.getLong("period", 1000);
            dataTask = new DataSource(new TimerSensor(context, period));
            break;
        case AudioSensor:
            long audioDuration = task.getLong("duration", -1);
            AudioSensor as = new AudioSensor(context, task.getSampleFrequency());
            dataTask = new AudioDataSource(as, audioDuration);
            as.addSourceTask((AudioDataSource) dataTask); // AudioSensor is
                                                          // faster than
                                                          // DataTask
            break;
        case BluetoothSensor:
            dataTask = new DataSource(new BluetoothSensor(context));
            break;
        case BluetoothConnectionSensor:
            dataTask = new DataSource(new BluetoothConnectionSensor(context,
                    task.getString("address", "")));
            break;
        case GpsSensor:
            dataTask = new DataSource(new GpsSensor(context));
            break;
        case GyroscopeSensor:
            long frameTime2 = task.getLong(AccelerometerSensor.ATT_FRAMETIME, 1000);
            long duration2 = task.getLong(AccelerometerSensor.ATT_DURATION, 500);
            Sensor sensor2 = AccelerometerSensor.createGyroscope(
                    context, frameTime2, duration2);
            if (task.getSampleFrequency() > 0) {
                sensor2.setSampleFrequency(task.getSampleFrequency());
            } else if (task.getPeriodTime() > 0) {
                sensor2.setPeriodTime(task.getPeriodTime());
            }
            dataTask = new DataSource(sensor2);
            task.setPeriodTime(frameTime2);
            task.setSampleFrequency(-1.0f);
            break;
        case CallSensor:
            dataTask = new DataSource(new PhoneCallSensor(context));
            break;
        case StateSensor:
            dataTask = new DataSource(new PhoneStateSensor(context));
            break;
        case PowerConnectionSensor:
            dataTask = new DataSource(new PowerConnectionSensor(context));
            break;
        case NfcSensor:
            dataTask = new DataSource(new NfcSensor(context));
            break;
        case WifiScanSensor:
            dataTask = new DataSource(new WifiScanSensor(context));
            break;
        case WifiConnectionSensor:
            // String[] ap = task.getStringArray("accessPoints");
            // List<String> apList = Arrays.asList(ap);
            dataTask = new DataSource(new WifiConnectionSensor(context));
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
            dataTask = new AudioSink(new RawAudioSinkWritter(context));
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
            List<Condition> conditionsList = jsonTrigger.toConditions(task
                    .getJsonNode());
            dataTask = new GeneralTrigger(context, conditionsList, matches);
            break;
        case StopTrigger:
            String matches2 = task.getString(JsonTrigger.MATCHES, null);
            JsonTrigger jsonTrigger2 = new JsonTrigger();
            List<Condition> conditionsList2 = jsonTrigger2.toConditions(task
                    .getJsonNode());
            dataTask = new StopTrigger(context, conditionsList2, matches2);
            break;
        default:
            return null;
        }
        if (task.getSampleFrequency() > 0) {
            dataTask.setSampleFrequency(task.getSampleFrequency());
        } else if (task.getPeriodTime() > 0) {
            dataTask.setPeriodTime(task.getPeriodTime());
        }
        dataTask.setTaskType(task.getTaskType());
        return dataTask;
    }
}
