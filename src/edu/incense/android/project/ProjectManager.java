/**
 * 
 */
package edu.incense.android.project;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import edu.incense.android.R;
import edu.incense.android.session.Session;
import edu.incense.android.session.SessionService;

/**
 * ProjectManager loads/read the project configuration and programs each of its
 * sessions accordingly. If a session is programmed to be started sometime in
 * the future, an alarm is used (AlarmManager). If the stating time of a session
 * has passed, it's started immediately.
 * 
 * Checks for updates from the server. If an update is available, any running
 * session is stopped to start new ones.
 * 
 * @author mxpxgx
 * 
 */
public class ProjectManager extends WakefulIntentService implements
        ProjectUpdateListener {
    private static final String TAG = "ProjectManager";
    public final static String PROJECT_START_ACTION = "edu.incense.android.PROJECT_START_ACTION";
    public final static String PROJECT_UPDATE_ACTION = "edu.incense.android.PROJECT_UPDATE_ACTION";
    public final static String ACTION_ID_FIELDNAME = "action_id";
    private volatile Project project;
    private Map<String, Session> sessions;
    private PendingIntent updateIntent;

    /**
     * This constructor is never used directly, it is used by the superclass
     * methods when it's first created.
     */
    public ProjectManager() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadProject();
        setUpdateAlarm();
        sessions = getProject().getSessions();
        updateIntent = null;
        Thread.setDefaultUncaughtExceptionHandler(onRuntimeError);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ProjectManager destroyed");
    }

    /**
     * 
     * @see com.commonsware.cwac.wakeful.WakefulIntentService#doWakefulWork(android
     *      .content.Intent)
     */
    @Override
    protected void doWakefulWork(Intent intent) {
        // Do not proceed if project wasn't loaded
        if (getProject() == null) {
            Log.e(TAG, "Project is null. It wasn't loaded correctly.");
            return;
        }

        if (intent.getAction().compareTo(PROJECT_START_ACTION) == 0) {

        } else if (intent.getAction().compareTo(PROJECT_UPDATE_ACTION) == 0) {

        } else {
            Log.e(TAG, "Unknown action received: " + intent.getAction());
            return;
        }
    }

    private Intent generateProjectActionIntent(String action) {
        Intent projectIntent = new Intent(this, ProjectManager.class);
        // Point out this action was triggered by a user
        projectIntent.setAction(action);
        // Send unique id for this action
        long actionId = UUID.randomUUID().getLeastSignificantBits();
        projectIntent.putExtra(SessionService.ACTION_ID_FIELDNAME, actionId);
        return projectIntent;
    }

    /* PROJECT UPDATE */

    private void setUpdateAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (updateIntent != null) {
            am.cancel(updateIntent);
        }
        Intent intent = generateProjectActionIntent(PROJECT_UPDATE_ACTION);
        updateIntent = PendingIntent.getService(this, 0, intent, 0);
        // Set the trigger time to the next 12am occurrence (today or tomorrow)
        long triggerAtTime = obtainNextOcurranceOf(0, 0);
        // Interval of 24hrs
        long interval = 24L * 60L * 60L * 1000L;
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, interval,
                updateIntent);
    }

    private long obtainNextOcurranceOf(int hour, int minute) {
        Time now = new Time();
        now.setToNow();
        Time next = new Time();
        next.set(now);
        next.set(0, minute, hour, now.monthDay, now.month, now.year);
        if (!next.after(now)) {
            next.set(next.second, next.minute, next.hour, next.monthDay + 1,
                    next.month, next.year);
        }
        return next.normalize(false);
    }

    /* RECORDING SESION */

    /**
     * Set an alarm for recording sessions and surveys.
     */
    public void setAlarmsFor(Session session) {
        Intent projectManagerIntent = new Intent(
                ProjectManager.this.getApplicationContext(),
                SessionService.class);
        // Point out this action was triggered by a user
        projectManagerIntent.setAction(ProjectManager.PROJECT_START_ACTION);
        // Send unique id for this action
        long actionId = UUID.randomUUID().getLeastSignificantBits();
        projectManagerIntent.putExtra(SessionService.ACTION_ID_FIELDNAME,
                actionId);
        // startService(sessionServiceIntent);
        WakefulIntentService.sendWakefulWork(
                ProjectManager.this.getApplicationContext(),
                projectManagerIntent);
    }

    /**
     * @return the project
     */
    private synchronized Project getProject() {
        return project;
    }

    /**
     * @param project
     *            the project to set
     */
    private synchronized void setProject(Project project) {
        this.project = project;
    }

    /**
     * Reads project from JSON
     */
    private void loadProject() {
        JsonProject jsonProject = new JsonProject();
        String projectFilename = getResources().getString(
                R.string.project_filename);
        InputStream input = null;
        try {
            input = this.openFileInput(projectFilename);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File [" + projectFilename + "] not found", e);
        }
        setProject(jsonProject.getProject(input));
    }

    /**
     * @see edu.incense.android.project.ProjectUpdateListener#update(edu.incense.android.project.Project)
     */
    public void update(Project newProject) {
        setProject(newProject);
        // TODO STOP SessionService AND RESET!!
    }

    /* In case of crashes */

    private Thread.UncaughtExceptionHandler onRuntimeError = new Thread.UncaughtExceptionHandler() {
        private long actionId;

        public void uncaughtException(Thread thread, Throwable ex) {
            // Start service for it to run the recording session
            Intent projectManagerIntent = new Intent(
                    ProjectManager.this.getApplicationContext(),
                    SessionService.class);
            // Point out this action was triggered by a user
            projectManagerIntent.setAction(ProjectManager.PROJECT_START_ACTION);
            // Send unique id for this action
            actionId = UUID.randomUUID().getLeastSignificantBits();
            projectManagerIntent.putExtra(ProjectManager.ACTION_ID_FIELDNAME,
                    actionId);
            // startService(sessionServiceIntent);
            WakefulIntentService.sendWakefulWork(
                    ProjectManager.this.getApplicationContext(),
                    projectManagerIntent);
        }
    };

}
