package edu.incense.android.session;

import java.io.File;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import edu.incense.android.R;
import edu.incense.android.project.JsonProject;
import edu.incense.android.project.Project;

/**
 * Service that runs recording sessionsaccording to the project and
 * user settings and context.
 * 
 * 
 * @author Moises Perez (incense.cicese@gmail.com)
 * @version 0.6, 2011/05/31
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

    public final static String SESSION_ACTION = "edu.incense.android.SESSION_ACTION";
    public final static String SESSION_USER_ACTION = "edu.incense.android.SESSION_USER_ACTION";
    public final static String SESSION_USER_ACTION_COMPLETE = "edu.incense.android.SESSION_USER_ACTION_COMPLETE";
    public final static String SESSION_ALARM_ACTION = "edu.incense.android.SESSION_ALARM_ACTION";
    public final static String SESSION_ALARM_ACTION_COMPLETE = "edu.incense.android.SESSION_ALARM_ACTION_COMPLETE";
    public final static String ACTION_ID_FIELDNAME = "action_id";
    public final static String SESSION_NAME_FIELDNAME = "session_name";
    
    /**
     * This method is invoked on the worker thread with a request to process.
     */
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
        } else{
            Log.e(TAG, "Non-recording-session action received: "+intent.getAction());
            return;
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
        String parentDirectory = getResources()
        .getString(R.string.application_root_directory);
        File parent = new File(Environment.getExternalStorageDirectory(), parentDirectory);
        File file = new File(parent, projectFilename);
        project = jsonProject.getProject(file);
    }

    private void startSession(Session session) {
        SessionController controller = new SessionController(this, session);
        controller.prepareSession();
        controller.start();
    }

}
