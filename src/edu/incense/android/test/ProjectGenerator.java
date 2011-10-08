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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import edu.incense.android.R;
import edu.incense.android.datatask.filter.WifiTimeConnectedFilter;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskRelation;
import edu.incense.android.datatask.model.TaskType;
import edu.incense.android.datatask.trigger.Condition;
import edu.incense.android.datatask.trigger.GeneralTrigger;
import edu.incense.android.datatask.trigger.JsonTrigger;
import edu.incense.android.project.Project;
import edu.incense.android.sensor.AccelerometerSensor;
import edu.incense.android.sensor.WifiConnectionSensor;
import edu.incense.android.session.Session;
import edu.incense.android.survey.Question;
import edu.incense.android.survey.QuestionType;
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
        session.setDuration(60 * 1000 * 5);

        List<Task> tasks = new ArrayList<Task>();

        Task as = new Task();
        as.setName("AudioSensor");
        as.setTaskType(TaskType.AudioSensor);
        as.setSampleFrequency(44100);
        JsonNode extrasNodea2 = mapper.createObjectNode();
        ((ObjectNode) extrasNodea2).put("duration", 1000*60); //1 minute
        as.setJsonNode(extrasNodea2);
        tasks.add(as);

        Task ask = new Task();
        ask.setName("AudioSink");
        ask.setTaskType(TaskType.AudioSink);
        ask.setSampleFrequency(44100);
        tasks.add(ask);
        
        Task at = new Task();
        // task3.setName("RandomSurveyTrigger");
        at.setName("AudioTrigger");
        at.setTaskType(TaskType.Trigger);
        at.setPeriodTime(1000);
        //Conditions
        Condition ac1 = new Condition();
        ac1.setData("value");
        ac1.setType(GeneralTrigger.DataType.BOOLEAN.name());
        ac1.setOperator(GeneralTrigger.booleanOperators[0]); //"is true"
        ArrayList<Condition> aconditions = new ArrayList<Condition>();
        aconditions.add(ac1);
        JsonNode extrasNode = mapper.createObjectNode();
        ((ObjectNode) extrasNode).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode aconditionsNode = mapper.valueToTree(aconditions);
        ((ObjectNode) extrasNode).put(JsonTrigger.CONDITIONS, aconditionsNode);
        at.setJsonNode(extrasNode);
        tasks.add(at);
        
        Task ts = new Task();
        ts.setName("TimerSensor");
        ts.setTaskType(TaskType.TimerSensor);
        ts.setPeriodTime(1000);
        JsonNode extrasNodea3 = mapper.createObjectNode();
        ((ObjectNode) extrasNodea3).put("period", 1000*60*60); //each hour
        ts.setJsonNode(extrasNodea3);
        tasks.add(ts);

        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(ts.getName(), at.getName()),
                new TaskRelation(at.getName(), as.getName()),
                new TaskRelation(as.getName(), ask.getName())});

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        String projectFilename = context.getResources().getString(R.string.project_filename);
        String parentDirectory = context.getResources()
                .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
//            File file = new File(parent, projectFilename);
            OutputStream output = context.openFileOutput(projectFilename, 0);
//            mapper.writeValue(file, project);
            mapper.writeValue(output, project);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Survey + Shake
     * 
     * @param resources
     */
    public static void buildProjectJsonB(Context context) {
        ObjectMapper mapper = new ObjectMapper();

        // Survey
        Survey survey = new Survey();
        survey.setId(101);
        survey.setTitle("Demo: Avance 2");

        Question question = new Question();
        question.setQuestion("How are you feeling right now?");
        question.setType(QuestionType.SEEKBAR);
        question.setSkippable(false);
        String[] options = { "Bad", "Good" };
        question.setOptions(options);
        int[] nextQuestions1 = { 1 };
        question.setNextQuestions(nextQuestions1);
        survey.add(question);

        question = new Question();
        question.setQuestion("What are you doing right now?");
        question.setType(QuestionType.OPENTEXT);
        question.setSkippable(false);
        int[] nextQuestions2 = { 2 };
        question.setNextQuestions(nextQuestions2);
        survey.add(question);

        question = new Question();
        question.setQuestion("Are you thinking about something other than what you’re currently doing?");
        question.setType(QuestionType.RADIOBUTTONS);
        question.setSkippable(false);
        String[] options2 = { "No", "Yes, something pleasant",
                "Yes, something neutral", "Yes, something unpleasant" };
        question.setOptions(options2);
        int[] nextQuestions3 = { 0, 0, 0, 0 };
        question.setNextQuestions(nextQuestions3);
        survey.add(question);

        // Session
        Session session = new Session();
        session.setDuration(60 * 1000);
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task task1 = new Task();
        task1.setName("AccelerometerSensor");
        task1.setTaskType(TaskType.AccelerometerSensor);
        task1.setPeriodTime(1000);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setName("ShakeFilter");
        task2.setTaskType(TaskType.ShakeFilter);
        task2.setPeriodTime(1000);
        tasks.add(task2);

        Task task3 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task3.setName("SurveyTrigger");
        task3.setTaskType(TaskType.Trigger);
        task3.setPeriodTime(1000);
        //Conditions
        Condition c1 = new Condition();
        c1.setData("isShake");
        c1.setType(GeneralTrigger.DataType.BOOLEAN.name());
        c1.setOperator(GeneralTrigger.booleanOperators[0]); //"is true"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(c1);
        JsonNode extrasNode = mapper.createObjectNode();
        ((ObjectNode) extrasNode).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode = mapper.valueToTree(conditions);
        ((ObjectNode) extrasNode).put(JsonTrigger.CONDITIONS, conditionsNode);
        task3.setJsonNode(extrasNode);
        tasks.add(task3);

        Task task4 = new Task();
        task4.setName("NfcSensor");
        task4.setTaskType(TaskType.NfcSensor);
        task4.setSampleFrequency(44100);
        tasks.add(task4);
        
        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(task1.getName(), task2.getName()),
                new TaskRelation(task2.getName(), task3.getName()),
                new TaskRelation(task3.getName(), "mainSurvey")});

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);

        String projectFilename = context.getResources().getString(R.string.project_filename);
        String parentDirectory = context.getResources()
                .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
//            File file = new File(parent, projectFilename);
            OutputStream output = context.openFileOutput(projectFilename, 0);
//            mapper.writeValue(file, project);
            mapper.writeValue(output, project);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Bluetooth
     * 
     * @param resources
     */
    public static void buildProjectJsonC(Resources resources) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDuration(60 * 1000);
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task task1 = new Task();
        task1.setName("BluetoothConnectionSensor");
        task1.setTaskType(TaskType.BluetoothConnectionSensor);
        task1.setSampleFrequency(0.1f);
        JsonNode surveyNameNode = mapper.createObjectNode();
        ((ObjectNode) surveyNameNode).put("address", "00:0C:78:7A:BE:6D");
        task1.setJsonNode(surveyNameNode);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setName("Sink");
        task2.setTaskType(TaskType.DataSink);
        task2.setSampleFrequency(44100);
        tasks.add(task2);

        Task task3 = new Task();
        task3.setName("NFC");
        task3.setTaskType(TaskType.NfcSensor);
        task3.setSampleFrequency(44100);
        tasks.add(task3);

        List<TaskRelation> relations = Arrays
                .asList(new TaskRelation[] { new TaskRelation(task1.getName(),
                        task2.getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        String projectFilename = resources.getString(R.string.project_filename);
        String parentDirectory = resources
                .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
            File file = new File(parent, projectFilename);
            mapper.writeValue(file, project);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Wifi
     * 
     * @param resources
     */
    public static void buildProjectJsonD(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        
        // Survey
        Survey survey = new Survey();
        survey.setId(101);
        survey.setTitle("Demo: Avance 2");

        Question question = new Question();
        question.setQuestion("How are you feeling right now?");
        question.setType(QuestionType.SEEKBAR);
        question.setSkippable(false);
        String[] options = { "Bad", "Good" };
        question.setOptions(options);
        int[] nextQuestions1 = { 1 };
        question.setNextQuestions(nextQuestions1);
        survey.add(question);

        question = new Question();
        question.setQuestion("What are you doing right now?");
        question.setType(QuestionType.OPENTEXT);
        question.setSkippable(false);
        int[] nextQuestions2 = { 2 };
        question.setNextQuestions(nextQuestions2);
        survey.add(question);

        question = new Question();
        question.setQuestion("Are you thinking about something other than what you’re currently doing?");
        question.setType(QuestionType.RADIOBUTTONS);
        question.setSkippable(false);
        String[] options2 = { "No", "Yes, something pleasant",
                "Yes, something neutral", "Yes, something unpleasant" };
        question.setOptions(options2);
        int[] nextQuestions3 = { 0, 0, 0, 0 };
        question.setNextQuestions(nextQuestions3);
        survey.add(question);

        // Session
        Session session = new Session();
        session.setDuration(60 * 1000 * 20);
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task task1 = new Task();
        task1.setName("WifiConnectionSensor");
        task1.setTaskType(TaskType.WifiConnectionSensor);
        task1.setPeriodTime(1000);
        JsonNode accessPoints = mapper.createObjectNode();
        ArrayNode array = ((ObjectNode) accessPoints).putArray("accessPoints");
        array.add("AppleBS4");
        task1.setJsonNode(accessPoints);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setName("WifiTimeConnectedFilter");
        task2.setTaskType(TaskType.WifiTimeConnectedFilter);
        task2.setPeriodTime(1000);
        tasks.add(task2);

        Task task3 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task3.setName("SurveyTrigger");
        task3.setTaskType(TaskType.Trigger);
        task3.setSampleFrequency(40);
        //Conditions
        Condition c1 = new Condition();
        c1.setData(WifiTimeConnectedFilter.ATT_TIMEDISCONNECTED);
        c1.setType(GeneralTrigger.DataType.NUMERIC.name());
        c1.setOperator(GeneralTrigger.numericOperators[2]); //"is greater than"
        c1.setValue1(String.valueOf(5000));
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(c1);
        JsonNode extrasNode = mapper.createObjectNode();
        ((ObjectNode) extrasNode).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode = mapper.valueToTree(conditions);
        ((ObjectNode) extrasNode).put(JsonTrigger.CONDITIONS, conditionsNode);
        task3.setJsonNode(extrasNode);
        tasks.add(task3);
        
        Task task5 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task5.setName("SurveyTrigger");
        task5.setTaskType(TaskType.Trigger);
        task5.setSampleFrequency(40);
        //Conditions
        Condition c2 = new Condition();
        c2.setData(WifiTimeConnectedFilter.ATT_TIMECONNECTED);
        c2.setType(GeneralTrigger.DataType.NUMERIC.name());
        c2.setOperator(GeneralTrigger.numericOperators[2]); //"is greater than"
        c2.setValue1(String.valueOf(5000));
        ArrayList<Condition> conditions2 = new ArrayList<Condition>();
        conditions.add(c2);
        JsonNode extrasNode2 = mapper.createObjectNode();
        ((ObjectNode) extrasNode2).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode2 = mapper.valueToTree(conditions);
        ((ObjectNode) extrasNode2).put(JsonTrigger.CONDITIONS, conditionsNode2);
        task5.setJsonNode(extrasNode2);
        tasks.add(task5);

        Task task4 = new Task();
        task4.setName("NfcSensor");
        task4.setTaskType(TaskType.NfcSensor);
        task4.setSampleFrequency(44100);
        tasks.add(task4);
        
        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(task1.getName(), task2.getName()),
                new TaskRelation(task2.getName(), task3.getName()),
                new TaskRelation(task2.getName(), task5.getName()),
                new TaskRelation(task5.getName(), "mainSurvey"),
                new TaskRelation(task3.getName(), "mainSurvey")});

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);


        String projectFilename = context.getResources().getString(R.string.project_filename);
        String parentDirectory = context.getResources()
                .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
//            File file = new File(parent, projectFilename);
            OutputStream output = context.openFileOutput(projectFilename, 0);
//            mapper.writeValue(file, project);
            mapper.writeValue(output, project);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
    
    /**
     * GPS + Wifi + Acc, no audio
     * 
     * @param resources
     */
    public static void buildProjectJsonE(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        
        
        
        // Session
        Session session = new Session();
        session.setDuration(1000L * 60L * 60L * 24L * 4L); //4days
        // session.setStartDate(new Calendar())
        
        List<Task> tasks = new ArrayList<Task>();
        
        Task task1 = new Task();
        task1.setName("WifiConnectionSensor");
        task1.setTaskType(TaskType.WifiConnectionSensor);
        task1.setPeriodTime(1000);
        JsonNode accessPoints = mapper.createObjectNode();
        ArrayNode array = ((ObjectNode) accessPoints).putArray("accessPoints");
        array.add("AppleBS4");
        task1.setJsonNode(accessPoints);
        tasks.add(task1);
        
        Task task2 = new Task();
        task2.setName("AccelerometerSensor");
        task2.setTaskType(TaskType.AccelerometerSensor);
        task2.setSampleFrequency(5); //40Hz or 25ms
        JsonNode extrasNode2 = mapper.createObjectNode();
        ((ObjectNode) extrasNode2).put(AccelerometerSensor.ATT_FRAMETIME, 1000);
        task2.setJsonNode(extrasNode2);
        tasks.add(task2);
        
        Task task3 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task3.setName("GpsTrigger");
        task3.setTaskType(TaskType.Trigger);
        task3.setPeriodTime(1000);
        //Conditions
        Condition c1 = new Condition();
        c1.setData(WifiConnectionSensor.ATT_ISCONNECTED);
        c1.setType(GeneralTrigger.DataType.BOOLEAN.name());
        c1.setOperator(GeneralTrigger.booleanOperators[1]); //"is false"
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(c1);
        JsonNode extrasNode = mapper.createObjectNode();
        ((ObjectNode) extrasNode).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode = mapper.valueToTree(conditions);
        ((ObjectNode) extrasNode).put(JsonTrigger.CONDITIONS, conditionsNode);
        task3.setJsonNode(extrasNode);
        tasks.add(task3);
        
        Task task4 = new Task();
        task4.setName("DataSink");
        task4.setTaskType(TaskType.DataSink);
        task4.setPeriodTime(1000);
        tasks.add(task4);
        
//        Task task5 = new Task();
//        task5.setName("GpsSensor");
//        task5.setTaskType(TaskType.GpsSensor);
//        task5.setPeriodTime(20000);
//        tasks.add(task5);
        
        Task task6 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task6.setName("GpsStopTrigger");
        task6.setTaskType(TaskType.StopTrigger);
        task6.setPeriodTime(1000);
        //Conditions
        Condition c2 = new Condition();
        c2.setData(WifiConnectionSensor.ATT_ISCONNECTED);
        c2.setType(GeneralTrigger.DataType.BOOLEAN.name());
        c2.setOperator(GeneralTrigger.booleanOperators[0]); //"is true"
        ArrayList<Condition> conditions2 = new ArrayList<Condition>();
        conditions.add(c2);
        JsonNode extrasNode3 = mapper.createObjectNode();
        ((ObjectNode) extrasNode3).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode2 = mapper.valueToTree(conditions2);
        ((ObjectNode) extrasNode3).put(JsonTrigger.CONDITIONS, conditionsNode2);
        task6.setJsonNode(extrasNode3);
        tasks.add(task6);
        
        Task as = new Task();
        as.setName("AudioSensor");
        as.setTaskType(TaskType.AudioSensor);
        as.setSampleFrequency(44100);
        JsonNode extrasNodea2 = mapper.createObjectNode();
        ((ObjectNode) extrasNodea2).put("duration", 1000*25); //1 minute
        as.setJsonNode(extrasNodea2);
        tasks.add(as);

        Task ask = new Task();
        ask.setName("AudioSink");
        ask.setTaskType(TaskType.AudioSink);
        ask.setSampleFrequency(44100);
        tasks.add(ask);
        
        Task at = new Task();
        // task3.setName("RandomSurveyTrigger");
        at.setName("AudioTrigger");
        at.setTaskType(TaskType.Trigger);
        at.setPeriodTime(1000);
        //Conditions
        Condition ac1 = new Condition();
        ac1.setData("value");
        ac1.setType(GeneralTrigger.DataType.BOOLEAN.name());
        ac1.setOperator(GeneralTrigger.booleanOperators[0]); //"is true"
        ArrayList<Condition> aconditions = new ArrayList<Condition>();
        aconditions.add(ac1);
        JsonNode extrasNodea = mapper.createObjectNode();
        ((ObjectNode) extrasNodea).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode aconditionsNode = mapper.valueToTree(aconditions);
        ((ObjectNode) extrasNodea).put(JsonTrigger.CONDITIONS, aconditionsNode);
        at.setJsonNode(extrasNodea);
        tasks.add(at);
        
        Task ts = new Task();
        ts.setName("TimerSensor");
        ts.setTaskType(TaskType.TimerSensor);
        ts.setPeriodTime(1000);
        JsonNode extrasNodea3 = mapper.createObjectNode();
        ((ObjectNode) extrasNodea3).put("period", 1000*60*6); //each 3 hour
        ts.setJsonNode(extrasNodea3);
        tasks.add(ts);
        
        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(ts.getName(), at.getName()),
                new TaskRelation(at.getName(), as.getName()),
                new TaskRelation(as.getName(), ask.getName()),
                new TaskRelation(task1.getName(), task3.getName()),
                new TaskRelation(task1.getName(), task6.getName()),
                new TaskRelation(task1.getName(), task4.getName()),
                new TaskRelation(task2.getName(), task4.getName())});
//                new TaskRelation(task5.getName(), task4.getName()),
//                new TaskRelation(task6.getName(), task5.getName()),
//                new TaskRelation(task3.getName(), task5.getName())});
        
        session.setTasks(tasks);
        session.setRelations(relations);
        
        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);
        
        
        String projectFilename = context.getResources().getString(R.string.project_filename);
        String parentDirectory = context.getResources()
        .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
//            File file = new File(parent, projectFilename);
            OutputStream output = context.openFileOutput(projectFilename, 0);
//            mapper.writeValue(file, project);
            mapper.writeValue(output, project);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
        session.setDuration(1000 * 50);
        // session.setStartDate(new Calendar())

        List<Task> tasks = new ArrayList<Task>();

        Task task1 = new Task();
        task1.setName("AudioSensor");
        task1.setTaskType(TaskType.WifiConnectionSensor);
        task1.setPeriodTime(1000);
        JsonNode accessPoints = mapper.createObjectNode();
        ArrayNode array = ((ObjectNode) accessPoints).putArray("accessPoints");
        array.add("AppleBS4");
        task1.setJsonNode(accessPoints);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setName("WifiTimeConnectedFilter");
        task2.setTaskType(TaskType.WifiTimeConnectedFilter);
        task2.setPeriodTime(1000);
        tasks.add(task2);

        Task task3 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task3.setName("SurveyTrigger");
        task3.setTaskType(TaskType.Trigger);
        task3.setSampleFrequency(40);
        //Conditions
        Condition c1 = new Condition();
        c1.setData(WifiTimeConnectedFilter.ATT_TIMEDISCONNECTED);
        c1.setType(GeneralTrigger.DataType.NUMERIC.name());
        c1.setOperator(GeneralTrigger.numericOperators[2]); //"is greater than"
        c1.setValue1(String.valueOf(5000));
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        conditions.add(c1);
        JsonNode extrasNode = mapper.createObjectNode();
        ((ObjectNode) extrasNode).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode = mapper.valueToTree(conditions);
        ((ObjectNode) extrasNode).put(JsonTrigger.CONDITIONS, conditionsNode);
        task3.setJsonNode(extrasNode);
        tasks.add(task3);
        
        Task task5 = new Task();
        // task3.setName("RandomSurveyTrigger");
        task5.setName("SurveyTrigger");
        task5.setTaskType(TaskType.Trigger);
        task5.setSampleFrequency(40);
        //Conditions
        Condition c2 = new Condition();
        c2.setData(WifiTimeConnectedFilter.ATT_TIMECONNECTED);
        c2.setType(GeneralTrigger.DataType.NUMERIC.name());
        c2.setOperator(GeneralTrigger.numericOperators[2]); //"is greater than"
        c2.setValue1(String.valueOf(5000));
        ArrayList<Condition> conditions2 = new ArrayList<Condition>();
        conditions.add(c2);
        JsonNode extrasNode2 = mapper.createObjectNode();
        ((ObjectNode) extrasNode2).put(JsonTrigger.MATCHES, GeneralTrigger.matches[0]);
        JsonNode conditionsNode2 = mapper.valueToTree(conditions);
        ((ObjectNode) extrasNode2).put(JsonTrigger.CONDITIONS, conditionsNode2);
        task5.setJsonNode(extrasNode2);
        tasks.add(task5);

        Task task4 = new Task();
        task4.setName("NfcSensor");
        task4.setTaskType(TaskType.NfcSensor);
        task4.setSampleFrequency(44100);
        tasks.add(task4);
        
        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(task1.getName(), task2.getName()),
                new TaskRelation(task2.getName(), task3.getName()),
                new TaskRelation(task2.getName(), task5.getName()),
                new TaskRelation(task5.getName(), "mainSurvey"),
                new TaskRelation(task3.getName(), "mainSurvey")});

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        String projectFilename = context.getResources().getString(R.string.project_filename);
        String parentDirectory = context.getResources()
                .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(),
                parentDirectory);
        parent.mkdirs();
        try {
//            File file = new File(parent, projectFilename);
            OutputStream output = context.openFileOutput(projectFilename, 0);
//            mapper.writeValue(file, project);
            mapper.writeValue(output, project);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
