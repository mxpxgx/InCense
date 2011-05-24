/**
 * 
 */
package edu.incense.test;

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

import edu.incense.R;
import edu.incense.datatask.model.Task;
import edu.incense.datatask.model.TaskRelation;
import edu.incense.datatask.model.TaskType;
import edu.incense.project.Project;
import edu.incense.session.Session;
import edu.incense.survey.Question;
import edu.incense.survey.QuestionType;
import edu.incense.survey.Survey;

/**
 * @author Moises Perez (incense.cicese@gmail.com)
 * @version 0.1, May 20, 2011
 *
 */
public class SurveyGenerator {
    /**
     * 
     */
    public static void buildProjectJson(Resources resources) {
        ObjectMapper mapper = new ObjectMapper();

        // Survey
        Survey survey = new Survey();
        survey.setId(101);
        survey.setTitle("Demo: Avance 2");

        String[] options = { "Strongly agree", "Agree", "Neutral", "Disagree",
                "Strongly disagree" };

        Question question = new Question();
        question.setQuestion("I am satisfied with my occupation:");
        question.setType(QuestionType.RADIOBUTTONS);
        question.setSkippable(false);
        question.setOptions(options);
        int[] nextQuestions1 = { 1, 1, 1, 1, 1 };
        question.setNextQuestions(nextQuestions1);
        survey.add(question);

        question = new Question();
        question.setQuestion("I teach too many classes:");
        question.setType(QuestionType.RADIOBUTTONS);
        question.setSkippable(true);
        question.setOptions(options);
        int[] nextQuestions2 = { 2, 2, 2, 2, 2 };
        question.setNextQuestions(nextQuestions2);
        survey.add(question);

        question = new Question();
        question.setQuestion("I'm satisfied with my income:");
        question.setType(QuestionType.RADIOBUTTONS);
        question.setSkippable(false);
        question.setOptions(options);
        int[] nextQuestions3 = { 0, 0, 0, 0, 0 };
        question.setNextQuestions(nextQuestions3);
        survey.add(question);

        // Session
        Session session = new Session();
        session.setDuration(10000);

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

        String projectFilename = resources.getString(
                R.string.project_filename);
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
