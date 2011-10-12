package edu.incense.android.session;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;

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
public class SessionService extends WakefulIntentService{//extends IntentService {
    private static final String TAG = "SessionService";
    private volatile boolean sessionRunning;

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
        sessionRunning = false;
        Thread.setDefaultUncaughtExceptionHandler(onRuntimeError);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        Log.d(TAG, "Service destroyed");
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
    protected void doWakefulWork(Intent intent) {
//    protected synchronized void onHandleIntent(Intent intent) {
        // Do not proceed if project wasn't loaded
        if (project == null) {
            Log.e(TAG, "Project is null. It wasn't loaded correctly.");
            return;
        }

        /* SESSION ACTION */
        if (intent.getAction().compareTo(SESSION_ACTION) == 0) {
            if(sessionRunning){
                Toast.makeText(this, "Session currently running, please wait...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Session currently running, please wait...");
                return;
            }
            sessionRunning = true;
            
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
            sessionRunning = false;
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
        InputStream input = null;
        try {
            input = this.openFileInput(projectFilename);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File [" + projectFilename + "] not found", e);
        }
        project = jsonProject.getProject(input);
    }
    
//    /**
//     * Reads project from JSON
//     */
//    private void loadPublicProject() {
//        JsonProject jsonProject = new JsonProject();
//        String projectFilename = getResources().getString(
//                R.string.project_filename);
//        String parentDirectory = getResources()
//        .getString(R.string.application_root_directory);
//        File parent = new File(Environment.getExternalStorageDirectory(), parentDirectory);
//        File file = new File(parent, projectFilename);
//        project = jsonProject.getProject(file);
//    }

    private void startSession(Session session) {
        SessionController controller = new SessionController(this, session);
        Log.d(TAG, "Session controller initiated");
        controller.prepareSession();
        Log.d(TAG, "Session controller prepared");
        controller.start();
        Log.d(TAG, "Session started");
    }
    
    private Thread.UncaughtExceptionHandler onRuntimeError= new Thread.UncaughtExceptionHandler() {
        private long actionId;
        public void uncaughtException(Thread thread, Throwable ex) {
            // Start service for it to run the recording session
            Intent sessionServiceIntent = new Intent(SessionService.this.getApplicationContext(), SessionService.class);
            // Point out this action was triggered by a user
            sessionServiceIntent.setAction(SessionService.SESSION_ACTION);
            // Send unique id for this action
            actionId = UUID.randomUUID().getLeastSignificantBits();
            sessionServiceIntent.putExtra(SessionService.ACTION_ID_FIELDNAME,
                    actionId);
//            startService(sessionServiceIntent);
            WakefulIntentService.sendWakefulWork(SessionService.this.getApplicationContext(), sessionServiceIntent);
        }
    };

}
