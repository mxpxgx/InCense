/**
 * 
 */
package edu.incense.session;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import edu.incense.R;
import edu.incense.project.JsonProject;
import edu.incense.project.Project;
import edu.incense.survey.Survey;
import edu.incense.survey.SurveyActivity;
import edu.incense.survey.SurveyController;

/**
 * Service that runs recording sessions and surveys according to the project and
 * user settings and context.
 * 
 * @author Moises Perez (incense.cicese@gmail.com)
 * @version 0.5, 2011/05/25
 * 
 */
public class SessionService extends IntentService {

    private static final String TAG = "SessionService";

    /**
     * This constructor is never used directly, it is used by the superclass
     * methods when it's first created.
     */
    public SessionService() {
        super("SessionService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadProject();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
    }

    /* INTENT_SERVICE METHODS */

    public final static String SESSION_ACTION = "edu.incense.SESSION_ACTION";
    public final static String SURVEY_ACTION = "edu.incense.SURVEY_ACTION";
    public final static String SURVEY_ACTION_COMPLETE = "edu.incense.SURVEY_ACTION_COMPLETE";
    public final static String SESSION_USER_ACTION = "edu.incense.SESSION_USER_ACTION";
    public final static String SESSION_USER_ACTION_COMPLETE = "edu.incense.SESSION_USER_ACTION_COMPLETE";
    public final static String SESSION_ALARM_ACTION = "edu.incense.SESSION_ALARM_ACTION";
    public final static String SESSION_ALARM_ACTION_COMPLETE = "edu.incense.SESSION_ALARM_ACTION_COMPLETE";
    public final static String ACTION_ID_FIELDNAME = "action_id";
    public final static String SURVEY_NAME_FIELDNAME = "survey_name";
    public final static String SESSION_NAME_FIELDNAME = "session_name";

    /**
     * This method is invoked on the worker thread with a request to process.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Do not proceed if project wasn't loaded
        if (project == null) {
            Log.e(TAG, "Project is null. It wasn't loaded correctly.");
            return;
        }
        
        /* SESSION ACTION */
        if (intent.getAction().compareTo(SESSION_ACTION) == 0) {
            String sessionName = intent.getStringExtra(SESSION_NAME_FIELDNAME);
            if (sessionName == null)
                sessionName = "mainSession";
            Session session = project.getSession(sessionName);
            if (session == null) {
                Log.e(TAG, "Session is null. Session [" + sessionName
                        + "] doesn't exist in the project.");
                return;
            }

            Log.d(TAG, "Starting session action: " + sessionName);
            startSession(session);
            Log.d(TAG, "Session action [" + sessionName + "] finished");

            // Send broadcast the end of this process
            Intent broadcastIntent = new Intent(SESSION_USER_ACTION_COMPLETE);
            broadcastIntent.putExtra(ACTION_ID_FIELDNAME,
                    intent.getLongExtra(ACTION_ID_FIELDNAME, -1));
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "Completion message for [" + sessionName
                    + "] was broadcasted");
        }
        
        /* SURVEY ACTION */
        if (intent.getAction().compareTo(SURVEY_ACTION) == 0) {
            String surveyName = intent.getStringExtra(SURVEY_NAME_FIELDNAME);
            if(surveyName == null)
                surveyName="mainSurvey";
            Survey survey = project.getSurvey(surveyName);
            if (survey == null) {
                Log.e(TAG, "Survey is null. Session [" + surveyName
                        + "] doesn't exist in the project.");
                return;
            }
            Log.d(TAG, "Starting survey action: " + surveyName);
            startSurvey(survey);
            Log.d(TAG, "Survey action [" + surveyName + "] finished");

            // Send broadcast the end of this process
            //TODO The following code is repeated, please improve.
            Intent broadcastIntent = new Intent(SURVEY_ACTION_COMPLETE);
            broadcastIntent.putExtra(ACTION_ID_FIELDNAME,
                    intent.getLongExtra(ACTION_ID_FIELDNAME, -1));
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "Completion message for [" + surveyName
                    + "] was broadcasted");
        }
    }

    /* SESSION METHODS */
    // The project this device/user is assigned to.
    private Project project;

    /**
     * Reads project from JSON
     */
    private void loadProject() {
        JsonProject jsonProject = new JsonProject();
        String projectFilename = getResources().getString(
                R.string.project_filename);
        project = jsonProject.getProject(projectFilename);
    }

    private void startSession(Session session) {
        SessionController controller = new SessionController(this, session);
        controller.prepareSession();
        controller.start();
    }

    private void startSurvey(Survey survey) {
        if (survey != null) {
            Log.i(getClass().getName(), "Starting survey");
            Intent intent = new Intent(this, SurveyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SurveyController surveyController = new SurveyController(survey);
            intent.putExtra(SurveyActivity.SURVEY_CONTROLLER, surveyController);
            startActivity(intent);
        } else {
            Log.i(TAG, "Survey was null.");
        }
    }

    // private final static int PERIOD_TIME=1; //Period in Minutes to check new
    // update

    /*
     * private void initAlarmManager(){ AlarmManager alarms =
     * (AlarmManager)getSystemService(Context.ALARM_SERVICE); //int alarmType =
     * AlarmManager.RTC_WAKEUP; //long timeOrLengthofWait = 10000; String
     * ALARM_ACTION = "UPDATE_PROJECT"; Intent intentToFire = new
     * Intent(ALARM_ACTION); PendingIntent elapsedIntent =
     * PendingIntent.getService(this, 0, intentToFire, 0);
     * //alarms.set(alarmType, timeOrLengthofWait, pendingIntent);
     * alarms.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
     * PERIOD_TIME*60*1000, PERIOD_TIME*60*1000, elapsedIntent); }
     */

}
