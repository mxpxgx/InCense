/**
 * 
 */
package edu.incense.android.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.os.Environment;
import edu.incense.android.R;
import edu.incense.android.datatask.filter.WifiTimeConnectedFilter;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskRelation;
import edu.incense.android.datatask.model.TaskType;
import edu.incense.android.datatask.trigger.Condition;
import edu.incense.android.datatask.trigger.GeneralTrigger;
import edu.incense.android.project.Project;
import edu.incense.android.sensor.WifiConnectionSensor;
import edu.incense.android.session.Session;
import edu.incense.android.survey.Survey;

/**
 * @author Moises Perez (incense.cicese@gmail.com)
 * @version 0.1, May 20, 2011
 * 
 */
public class ProjectGenerator {
    /**
     * Audio project
     */
    public static void buildProjectJsonA(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDurationUnits(60);
        session.setDurationMeasure("minutes");

        List<Task> tasks = new ArrayList<Task>();

        Task audioSensor = TaskGenerator.createAudioSensor(mapper, 44100,
                1000 * 60 * 15); // rate: 44100Hz, duration: 25 seconds
        tasks.add(audioSensor);

        Task audioSink = TaskGenerator.createTaskWithPeriod(mapper,
                "AudioSink", TaskType.AudioSink, 1000);
        tasks.add(audioSink);

        Condition ifTimerSaysSo = TaskGenerator.createCondition("value",
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is true"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(ifTimerSaysSo);
        Task audioTrigger = TaskGenerator.createTrigger(mapper, "AudioTrigger",
                1000, GeneralTrigger.matches[0], conditions);
        tasks.add(audioTrigger);

        Task timerSensor = TaskGenerator.createTimerSensor(mapper, 1000,
                1000 * 60 * 30); // each 2min
        tasks.add(timerSensor);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] {
                        new TaskRelation(timerSensor.getName(), audioTrigger
                                .getName()),
                        new TaskRelation(audioTrigger.getName(), audioSensor
                                .getName()),
                        new TaskRelation(audioSensor.getName(), audioSink
                                .getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        writeProject(context, mapper, project);
    }

    /**
     * Survey + Shake
     * 
     * @param resources
     */
    public static void buildProjectJsonB(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Survey
        Survey survey = SurveyGenerator.createWanderingMindSurvey();

        // Session
        Session session = new Session();
        session.setDurationUnits(1);
        session.setDurationMeasure("minutes");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task accSensor = TaskGenerator.createAccelerometerSensor(mapper, 5,
                1000, 500);
        tasks.add(accSensor);

        Task shakeFilter = new Task();
        shakeFilter.setName("ShakeFilter");
        shakeFilter.setTaskType(TaskType.ShakeFilter);
        shakeFilter.setPeriodTime(1000);
        tasks.add(shakeFilter);

        Condition ifShake = TaskGenerator.createCondition("isShake",
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is true"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(ifShake);
        Task surveyTrigger = TaskGenerator.createTrigger(mapper,
                "SurveyTrigger", 1000, GeneralTrigger.matches[0], conditions);
        tasks.add(surveyTrigger);

        Task nfcSensor = TaskGenerator.createNfcSensor(mapper, 44100);
        tasks.add(nfcSensor);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] {
                        new TaskRelation(accSensor.getName(), shakeFilter
                                .getName()),
                        new TaskRelation(shakeFilter.getName(), surveyTrigger
                                .getName()),
                        new TaskRelation(surveyTrigger.getName(), "mainSurvey") });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);

        writeProject(context, mapper, project);
    }

    /**
     * GPS + Wifi + Acc, no audio
     * 
     * @param resources
     */
    public static void buildProjectJsonE(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        Survey survey = SurveyGenerator.createWanderingMindSurvey();

        // Session
        Session session = new Session();
        session.setDurationUnits(24L * 4L); //4days
        session.setDurationMeasure("hours");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task wifiSensor = TaskGenerator.createWifiConnectionSensor(mapper,
                1000, new String[] { "AppleBS4" });
        tasks.add(wifiSensor);

        Task accSensor = TaskGenerator.createAccelerometerSensor(mapper, 20,
                10000, 5000);
        tasks.add(accSensor);

        Condition ifNotConnected = TaskGenerator.createCondition(
                WifiConnectionSensor.ATT_ISCONNECTED,
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[1]); // "is false"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(ifNotConnected);
        Task gpsTrigger = TaskGenerator.createTrigger(mapper, "GpsTrigger",
                1000, GeneralTrigger.matches[0], conditions);
        tasks.add(gpsTrigger);

        Task dataSink = TaskGenerator.createTaskWithPeriod(mapper, "DataSink",
                TaskType.DataSink, 1000);
        tasks.add(dataSink);

        // Task gpsSensor = TaskGenerator.createGpsSensor(mapper, period);
        // tasks.add(gpsSensor);

        Condition ifConnected = TaskGenerator.createCondition(
                WifiConnectionSensor.ATT_ISCONNECTED,
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is false"
        conditions = new ArrayList<Condition>();
        conditions.add(ifConnected);
        Task gpsStopTrigger = TaskGenerator.createStopTrigger(mapper,
                "GpsStopTrigger", 1000, GeneralTrigger.matches[0], conditions);
        tasks.add(gpsStopTrigger);

        Task audioSensor = TaskGenerator.createAudioSensor(mapper, 44100,
                1000 * 25); // rate: 44100Hz, duration: 25 seconds
        tasks.add(audioSensor);

        Task audioSink = TaskGenerator.createTaskWithPeriod(mapper,
                "AudioSink", TaskType.AudioSink, 1000);
        tasks.add(audioSink);

        Condition ifTimerSaysSo = TaskGenerator.createCondition("value",
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is true"
        conditions = new ArrayList<Condition>();
        conditions.add(ifTimerSaysSo);
        Task audioTrigger = TaskGenerator.createTrigger(mapper, "AudioTrigger",
                1000, GeneralTrigger.matches[0], conditions);
        tasks.add(audioTrigger);

        Task timerSensor = TaskGenerator.createTimerSensor(mapper, 1000,
                1000 * 60 * 60 * 3); // each 3 hour
        tasks.add(timerSensor);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] {
                        new TaskRelation(timerSensor.getName(), audioTrigger
                                .getName()),
                        new TaskRelation(audioTrigger.getName(), audioSensor
                                .getName()),
                        new TaskRelation(audioSensor.getName(), audioSink
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), gpsTrigger
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), gpsStopTrigger
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), dataSink
                                .getName()),
                        new TaskRelation(accSensor.getName(), dataSink
                                .getName()) });
        // new TaskRelation(gpsSensor.getName(), dataSink.getName()),
        // new TaskRelation(gpsStopTrigger.getName(), gpsSensor.getName()),
        // new TaskRelation(gpsTrigger.getName(), gpsSensor.getName())});

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);

        writeProject(context, mapper, project);
    }

    private static void writeProject(Context context, ObjectMapper mapper,
            Project project) {
        String projectFilename = context.getResources().getString(
                R.string.project_filename);
        String parentDirectory = context.getResources().getString(
                R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
            // File file = new File(parent, projectFilename);
            OutputStream output = context.openFileOutput(projectFilename, 0);
            // mapper.writeValue(file, project);
            mapper.writeValue(output, project);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Audio
     * 
     * @param resources
     */
    public static void buildProjectJsonF(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDurationUnits(1); //1 minute
        session.setDurationMeasure("minutes");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task wifiSensor = TaskGenerator.createWifiConnectionSensor(mapper,
                1000, new String[] { "AppleBS4" });
        tasks.add(wifiSensor);

        Task wifiFilter = new Task();
        wifiFilter.setName("WifiTimeConnectedFilter");
        wifiFilter.setTaskType(TaskType.WifiTimeConnectedFilter);
        wifiFilter.setPeriodTime(1000);
        tasks.add(wifiFilter);

        Condition ifTimeDisconnectedGreater = TaskGenerator.createCondition(
                WifiTimeConnectedFilter.ATT_TIMEDISCONNECTED,
                GeneralTrigger.DataType.NUMERIC.name(),
                GeneralTrigger.numericOperators[2],// "is greater than"
                String.valueOf(5000));
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(ifTimeDisconnectedGreater);
        Task surveyTrigger = TaskGenerator.createTrigger(mapper,
                "SurveyTrigger", 1000, GeneralTrigger.matches[0], conditions);
        tasks.add(surveyTrigger);

        Condition ifTimeConnectedGreater = TaskGenerator.createCondition(
                WifiTimeConnectedFilter.ATT_TIMECONNECTED,
                GeneralTrigger.DataType.NUMERIC.name(),
                GeneralTrigger.numericOperators[2],// "is greater than"
                String.valueOf(5000));
        conditions = new ArrayList<Condition>();
        conditions.add(ifTimeConnectedGreater);
        Task surveyTrigger2 = TaskGenerator.createTrigger(mapper,
                "SurveyTrigger2", 1000, GeneralTrigger.matches[0], conditions);
        tasks.add(surveyTrigger2);

        // Task nfcSensor = TaskGenerator.createNfcSensor(mapper, 44100);
        // tasks.add(nfcSensor);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] {
                        new TaskRelation(wifiSensor.getName(), wifiFilter
                                .getName()),
                        new TaskRelation(wifiFilter.getName(), surveyTrigger
                                .getName()),
                        new TaskRelation(wifiFilter.getName(), surveyTrigger2
                                .getName()),
                        new TaskRelation(surveyTrigger2.getName(), "mainSurvey"),
                        new TaskRelation(surveyTrigger.getName(), "mainSurvey") });

        session.setTasks(tasks);
        session.setRelations(relations);

        Survey survey = SurveyGenerator.createWanderingMindSurvey();

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);

        writeProject(context, mapper, project);
    }

    /**
     * Accelerometer
     * 
     * @param context
     */
    public static void buildProjectJsonG(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDurationUnits(24L * 4L); //4days
        session.setDurationMeasure("hours");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task accSensor = TaskGenerator.createAccelerometerSensor(mapper, 20,
                10000, 5000);
        tasks.add(accSensor);

        Task dataSink = TaskGenerator.createTaskWithPeriod(mapper, "DataSink",
                TaskType.DataSink, 1000);
        tasks.add(dataSink);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] { new TaskRelation(accSensor
                        .getName(), dataSink.getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        writeProject(context, mapper, project);
    }

    /**
     * GPS
     * 
     * @param context
     */
    public static void buildProjectJsonH(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDurationUnits(1000L * 60L * 60L * 24L * 4L); // 4days
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task accSensor = TaskGenerator.createGpsSensor(mapper, 10000);
        tasks.add(accSensor);

        Task dataSink = TaskGenerator.createTaskWithPeriod(mapper, "DataSink",
                TaskType.DataSink, 1000);
        tasks.add(dataSink);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] { new TaskRelation(accSensor
                        .getName(), dataSink.getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        writeProject(context, mapper, project);
    }

    /**
     * WiFi
     * 
     * @param context
     */
    public static void buildProjectJsonI(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDurationUnits(24L * 4L); //4days
        session.setDurationMeasure("hours");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task wifiSensor = TaskGenerator.createWifiConnectionSensor(mapper,
                1000, new String[] { "AppleBS4" });
        tasks.add(wifiSensor);

        Task dataSink = TaskGenerator.createTaskWithPeriod(mapper, "DataSink",
                TaskType.DataSink, 1000);
        tasks.add(dataSink);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] { new TaskRelation(wifiSensor
                        .getName(), dataSink.getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        writeProject(context, mapper, project);
    }

    /**
     * GPS + Wifi + Acc, no audio
     * 
     * @param resources
     */
    public static void buildProjectJsonJ(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        Survey survey = SurveyGenerator.createWanderingMindSurvey();

        // Session
        Session session = new Session();
        session.setDurationUnits(24L * 4L); //4days
        session.setDurationMeasure("hours");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task wifiSensor = TaskGenerator.createWifiConnectionSensor(mapper,
                1000, new String[] { "AppleBS4" });
        tasks.add(wifiSensor);

        Task accSensor = TaskGenerator.createAccelerometerSensor(mapper, 44,
                10000, 5000);
        tasks.add(accSensor);

        Condition ifNotConnected = TaskGenerator.createCondition(
                WifiConnectionSensor.ATT_ISCONNECTED,
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[1]); // "is false"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(ifNotConnected);
        Task gpsTrigger = TaskGenerator.createTrigger(mapper, "GpsTrigger",
                1000, GeneralTrigger.matches[0], conditions);
        tasks.add(gpsTrigger);

        Task dataSink = TaskGenerator.createTaskWithPeriod(mapper, "DataSink",
                TaskType.DataSink, 1000);
        tasks.add(dataSink);

        Task gpsSensor = TaskGenerator.createGpsSensor(mapper, 1000L * 30L);
        tasks.add(gpsSensor);

        Condition ifConnected = TaskGenerator.createCondition(
                WifiConnectionSensor.ATT_ISCONNECTED,
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is true"
        conditions = new ArrayList<Condition>();
        conditions.add(ifConnected);
        Task gpsStopTrigger = TaskGenerator.createStopTrigger(mapper,
                "GpsStopTrigger", 1000, GeneralTrigger.matches[0], conditions);
        tasks.add(gpsStopTrigger);

        Task audioSensor = TaskGenerator.createAudioSensor(mapper, 44100,
                1000 * 25); // rate: 44100Hz, duration: 25 seconds
        tasks.add(audioSensor);

        Task audioSink = TaskGenerator.createTaskWithPeriod(mapper,
                "AudioSink", TaskType.AudioSink, 1000);
        tasks.add(audioSink);

        Condition ifTimerSaysSo = TaskGenerator.createCondition("value",
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is true"
        conditions = new ArrayList<Condition>();
        conditions.add(ifTimerSaysSo);
        Task audioTrigger = TaskGenerator.createTrigger(mapper, "AudioTrigger",
                1000, GeneralTrigger.matches[0], conditions);
        tasks.add(audioTrigger);

        Task timerSensor = TaskGenerator.createTimerSensor(mapper, 1000,
                1000 * 60 * 60 * 3); // each 3 hour
        tasks.add(timerSensor);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] {
                        new TaskRelation(timerSensor.getName(), audioTrigger
                                .getName()),
                        new TaskRelation(audioTrigger.getName(), audioSensor
                                .getName()),
                        new TaskRelation(audioSensor.getName(), audioSink
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), gpsTrigger
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), gpsStopTrigger
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), dataSink
                                .getName()),
                        new TaskRelation(accSensor.getName(), dataSink
                                .getName()),
                        new TaskRelation(gpsSensor.getName(), dataSink
                                .getName()),
                        new TaskRelation(gpsStopTrigger.getName(), gpsSensor
                                .getName()),
                        new TaskRelation(gpsTrigger.getName(), gpsSensor
                                .getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);

        writeProject(context, mapper, project);
    }

    /**
     * GPS + Wifi + Acc, no audio
     * 
     * @param resources
     */
    public static void buildProjectJsonK(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        Survey survey = SurveyGenerator.createWanderingMindSurvey();

        // Session
        Session session = new Session();
        session.setDurationUnits(24L * 4L); //4days
        session.setDurationMeasure("hours");
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task wifiSensor = TaskGenerator.createWifiConnectionSensor(mapper,
                1000, new String[] { "AppleBS4" });
        tasks.add(wifiSensor);

        Task accSensor = TaskGenerator.createAccelerometerSensor(mapper, 20,
                10000, 5000);
        tasks.add(accSensor);
        
        Task dataSink = TaskGenerator.createTaskWithPeriod(mapper, "DataSink",
                TaskType.DataSink, 1000);
        tasks.add(dataSink);

        Task gpsSensor = TaskGenerator.createGpsSensor(mapper, 1000L * 30L);
        tasks.add(gpsSensor);

        Task audioSensor = TaskGenerator.createAudioSensor(mapper, 44100,
                1000 * 25); // rate: 44100Hz, duration: 25 seconds
        tasks.add(audioSensor);

        Task audioSink = TaskGenerator.createTaskWithPeriod(mapper,
                "AudioSink", TaskType.AudioSink, 1000);
        tasks.add(audioSink);

        Condition ifTimerSaysSo = TaskGenerator.createCondition("value",
                GeneralTrigger.DataType.BOOLEAN.name(),
                GeneralTrigger.booleanOperators[0]); // "is true"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(ifTimerSaysSo);
        Task audioTrigger = TaskGenerator.createTrigger(mapper, "AudioTrigger",
                1000, GeneralTrigger.matches[0], conditions);
        tasks.add(audioTrigger);

        Task timerSensor = TaskGenerator.createTimerSensor(mapper, 1000,
                1000 * 60 * 60 * 1); // each 3 hour
        tasks.add(timerSensor);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] {
                        new TaskRelation(timerSensor.getName(), audioTrigger
                                .getName()),
                        new TaskRelation(audioTrigger.getName(), audioSensor
                                .getName()),
                        new TaskRelation(audioSensor.getName(), audioSink
                                .getName()),
                        new TaskRelation(wifiSensor.getName(), dataSink
                                .getName()),
                        new TaskRelation(accSensor.getName(), dataSink
                                .getName()),
                        new TaskRelation(gpsSensor.getName(), dataSink
                                .getName()),
                                });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);

        writeProject(context, mapper, project);
    }

}
