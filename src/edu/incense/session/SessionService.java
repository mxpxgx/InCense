/**
 * 
 */
package edu.incense.session;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import edu.incense.InCenseApplication;
import edu.incense.R;
import edu.incense.project.JsonProject;
import edu.incense.project.Project;

/**
 * Service that runs recording sessions and surveys according to the project and
 * user settings and context.
 * 
 * @author Moises Perez (incense.cicese@gmail.com)
 * @version 0.1, May 24, 2011
 * 
 */
public class SessionService extends IntentService {
    public final static String SESSION_USER_ACTION = "edu.incense.SESSION_USER_ACTION";
    public final static String SESSION_ALARM_ACTION = "edu.incense.SESSION_ALARM_ACTION";
    public final static String ACTION_ID_FIELDNAME = "action_id";

    /**
     * This constructor is never used directly, it is used by the
     * superclass methods when it's first created.
     */
    public SessionService() {
        super("SessionService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        readProject();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
    }

    /* INTENT_SERVICE METHODS */
    /**
     * This method is invoked on the worker thread with a request to process.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

    }

    /* SESSION METHODS */

    private void readProject() {
        // Start project
        JsonProject jsonProject = new JsonProject();
        String projectFilename = getResources().getString(
                R.string.project_filename);
        Project project = jsonProject.getProject(projectFilename);
        InCenseApplication.getInstance().setProject(project);
        Session session = project.getSession("mainSession");
        long duration = session.getDuration();
        Log.i(getClass().getName(), "Project duration: " + duration);
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
