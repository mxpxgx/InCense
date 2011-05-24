package edu.incense;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.util.Log;
import edu.incense.datatask.DataTask;
import edu.incense.project.Project;
import edu.incense.survey.Survey;
import edu.incense.survey.SurveyController;
import edu.incense.test.SurveyGenerator;

/**
 * InCenseApplication is a subclass of android.app.Application, a base class for
 * maintaining global application state. In this case, it maintains a
 * SurveyController instance to... TODO This is no the best approach, I need
 * something like the The Static Starter Pattern
 * http://fupeg.blogspot.com/2011/02/static-starter-pattern.html
 * 
 * @author Moises Perez (mxpxgx@gmail.com)
 * @since 2011/04/10 //TODO probably before
 * @version 1.1, 2011/05/20
 * 
 */

public class InCenseApplication extends Application {
    // Static singleton of this application
    private static InCenseApplication singleton; 
    // Temporary SurveyController reference to be started
    private SurveyController surveyController;
    private Map<String, DataTask> taskCollection; // TODO
    private Project project; // TODO

    /**
     * Returns this application instance (an static singleton).
     * 
     * @return the instance of this application (an static singleton).
     */
    public static InCenseApplication getInstance() {
        return singleton;
    }

    /**
     * @see android.app.Application#onCreate()
     */
    @Override
    public final void onCreate() {
        super.onCreate();
        SurveyGenerator.buildProjectJson(getResources());
        Log.i(getClass().getName(), "Project.json saved");
        singleton = this;
        taskCollection = new HashMap<String, DataTask>();
        surveyController = null;
    }

    /**
     * Set an instance of survey in order to be used by a new SurveyController
     * 
     * @param survey
     */
    public void setSurvey(Survey survey) {
        surveyController = new SurveyController(survey);
    }

    public void setSurveyController(SurveyController surveyController) {
        this.surveyController = surveyController;
    }

    public SurveyController getSurveyController() {
        SurveyController tempController = this.surveyController;
        this.surveyController = null;
        return tempController;
    }

    public void setTaskCollection(Map<String, DataTask> taskCollection) {
        this.taskCollection = taskCollection;
    }

    public Map<String, DataTask> getTaskCollection() {
        return taskCollection;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
