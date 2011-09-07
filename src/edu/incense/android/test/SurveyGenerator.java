/**
 * 
 */
package edu.incense.android.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import android.content.res.Resources;

import edu.incense.android.R;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskRelation;
import edu.incense.android.datatask.model.TaskType;
import edu.incense.android.project.Project;
import edu.incense.android.session.Session;
import edu.incense.android.survey.Question;
import edu.incense.android.survey.QuestionType;
import edu.incense.android.survey.Survey;

/**
 * @author Moises Perez (incense.cicese@gmail.com)
 * @version 0.1, May 20, 2011
 * 
 */
public class SurveyGenerator {
    /**
     * 
     */
    public static void buildProjectJsonA(Resources resources) {
        ObjectMapper mapper = new ObjectMapper();

        // Session
        Session session = new Session();
        session.setDuration(15*1000);

        List<Task> tasks = new ArrayList<Task>();

        Task task1 = new Task();
        task1.setName("AudioSensor");
        task1.setTaskType(TaskType.AudioSensor);
        task1.setSampleFrequency(44100);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setName("AudioSink");
        task2.setTaskType(TaskType.AudioSink);
        task2.setSampleFrequency(44100);
        tasks.add(task2);

        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(task1.getName(), task2.getName()) });

        session.setTasks(tasks);
        session.setRelations(relations);

        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);

        String projectFilename = resources.getString(R.string.project_filename);
        try {
            File file = new File(projectFilename);
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
    
    public static void buildProjectJsonB(Resources resources) {
        ObjectMapper mapper = new ObjectMapper();
        
        // Survey
        Survey survey = new Survey();
        survey.setId(101);
        survey.setTitle("Demo: Avance 2");
        
        // String[] options = { "Strongly agree", "Agree", "Neutral",
        // "Disagree",
        // "Strongly disagree" };
        
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
        
        /*
         * Question question = new Question();
         * question.setQuestion("I am satisfied with my occupation:");
         * question.setType(QuestionType.RADIOBUTTONS);
         * question.setSkippable(false); question.setOptions(options); int[]
         * nextQuestions1 = { 1, 1, 1, 1, 1 };
         * question.setNextQuestions(nextQuestions1); survey.add(question);
         * 
         * question = new Question();
         * question.setQuestion("I teach too many classes:");
         * question.setType(QuestionType.RADIOBUTTONS);
         * question.setSkippable(true); question.setOptions(options); int[]
         * nextQuestions2 = { 2, 2, 2, 2, 2 };
         * question.setNextQuestions(nextQuestions2); survey.add(question);
         * 
         * question = new Question();
         * question.setQuestion("I'm satisfied with my income:");
         * question.setType(QuestionType.RADIOBUTTONS);
         * question.setSkippable(false); question.setOptions(options); int[]
         * nextQuestions3 = { 0, 0, 0, 0, 0 };
         * question.setNextQuestions(nextQuestions3); survey.add(question);
         */
        
        // Session
        Session session = new Session();
        session.setDuration(60*1000);
        // session.setStartDate(new Calendar())
        
        List<Task> tasks = new ArrayList<Task>();
        
        Task task1 = new Task();
        task1.setName("AccelerometerSensor");
        task1.setTaskType(TaskType.AccelerometerSensor);
        task1.setSampleFrequency(40);
        tasks.add(task1);
        
        Task task2 = new Task();
        task2.setName("ShakeFilter");
        task2.setTaskType(TaskType.ShakeFilter);
        task2.setSampleFrequency(40);
        tasks.add(task2);
        
        Task task3 = new Task();
        //task3.setName("RandomSurveyTrigger");
        task3.setName("SurveyTrigger");
        task3.setTaskType(TaskType.SurveyTrigger);
        task3.setSampleFrequency(40);
        JsonNode surveyNameNode = mapper.createObjectNode();
        ((ObjectNode) surveyNameNode).put("surveyName", "mainSurvey");
        task3.setJsonNode(surveyNameNode);
        tasks.add(task3);
        
        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(task1.getName(), task2.getName()),
                new TaskRelation(task2.getName(), task3.getName()) });
        
        session.setTasks(tasks);
        session.setRelations(relations);
        
        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(1);
        project.put("mainSurvey", survey);
        
        String projectFilename = resources.getString(R.string.project_filename);
        try {
            File file = new File(projectFilename);
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
    
    public static void buildProjectJsonC(Resources resources) {
        ObjectMapper mapper = new ObjectMapper();
 
        
        // Session
        Session session = new Session();
        session.setDuration(60*1000);
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

        List<TaskRelation> relations = Arrays.asList(new TaskRelation[] {
                new TaskRelation(task1.getName(), task2.getName()) });
        
        session.setTasks(tasks);
        session.setRelations(relations);
        
        Project project = new Project();
        project.setSessionsSize(1);
        project.put("mainSession", session);
        project.setSurveysSize(0);
        
        String projectFilename = resources.getString(R.string.project_filename);
        try {
            File file = new File(projectFilename);
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
}
