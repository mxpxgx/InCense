package edu.incense.ui;

import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.incense.InCenseApplication;
import edu.incense.R;
import edu.incense.project.JsonProject;
import edu.incense.project.Project;
import edu.incense.session.Session;
import edu.incense.session.SessionTask;

/**
 * Activity where the user can start a recording session.
 * 
 * @author Moises Perez (mxpxgx@gmail.com)
 * @since 2011/04/28?
 * @version 1.1 2011/05/20
 */

public class RecordActivity extends MainMenuActivity {

    // UI elements
    private ProgressDialog progressDialog = null;
    private TextView statusTextView;
    private EditText usernameEditText;

    private String username = null;

    /** Called when the activity is first created. */
    @Override
    public synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get UI elements
        usernameEditText = (EditText) findViewById(R.id.textview_username);
        statusTextView = (TextView) findViewById(R.id.textview_status);
        final Button startButton = (Button) findViewById(R.id.button_start);

        // Initialize username and usernameEditText according to the
        // SharedPreferences
        updateUsernameFromPrefs();

        // Set the instructions text
        statusTextView.setText(getResources().getText(R.string.inst_start));

        // Add click listener in START button
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verify is a valid username
                if (username == null || !(username.length() > 0)) {
                    // Visible notice to the user
                    Toast.makeText(
                            getBaseContext(),
                            getResources()
                                    .getText(R.string.no_username_message),
                            Toast.LENGTH_LONG).show();

                } else {
                    /*** START RECORDING SESSION ***/
                    startSession();
                }
            }
        });

    }

    /**
     * Get the username from SharedPreferences
     * 
     * @return username string
     */
    private String getUsernameFromPrefs() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        return sp.getString("editTextUsername", "Unknown");
    }

    /**
     * Update EditText with the username from SharedPreferences (if necessary)
     */
    private void updateUsernameFromPrefs() {
        this.username = getUsernameFromPrefs();
        usernameEditText.setText(this.username);
    }

    /*** Overridden methods from Activity ***/

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
        updateUsernameFromPrefs();
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause() {
        // Suspend UI updates, threads, or CPU intensive processes
        // that don't need to be updated when the Activity isn't
        // the active foreground activity.
        super.onPause();
        suspendRecordingSession();
    }

    /*** RECORDING SESSION ***/

    SessionTask sessionTask;

    /**
     * Start recording session and the thread from this class, show the progress
     * dialog
     */
    private synchronized void startSession() {
        // Show progress dialog
        Resources res = getResources();
        progressDialog = ProgressDialog.show(this,
                res.getText(R.string.session_title),
                res.getText(R.string.session_active_message));
        
        // Start project
        JsonProject jsonProject = new JsonProject();
        String projectFilename = getResources().getString(
                R.string.project_filename);
        Project project = jsonProject.getProject(projectFilename);
        InCenseApplication.getInstance().setProject(project);
        Session session = project.getSession("mainSession");
        // duration = session.getDuration();
        // Log.i(getClass().getName(), "Project duration: " + duration);
        
        sessionTask = new SessionTask(this);
        sessionTask.execute(session);

        // if(serviceIntent != null)
        // serviceIntent = null;

        // Initialize recording session
        // serviceIntent = new Intent(MainActivity.this,
        // edu.incense.inutil.RecordingSession.class);
        // startService(serviceIntent);

        // try {
        // // Initialize thread if necessary
        // if (thread != null) {
        // if (thread.getState() != Thread.State.NEW) {
        // thread = null;
        // thread = new Thread(this);
        // }
        // } else {
        // thread = new Thread(this);
        // }
        //
        // // Start sensing and collecting process
        // thread.start();
        //
        // } catch (Exception e) {
        // Log.e(getClass().getName(), "Starting recording session failed.", e);
        // }
    }

    // Suspend recording session and the thread from this class, dismiss the
    // progress dialog
    private synchronized void suspendRecordingSession() {

        // Suspend recording session
        /*
         * if(sensingActivity != null){ sensingActivity.suspend(); try {
         * Thread.sleep(1000); } catch (Exception e) {
         * Log.e(getClass().getName(), "Sleep: " + e); } sensingActivity = null;
         * }
         */
//        if (serviceIntent != null) {
//            stopService(serviceIntent);
//            serviceIntent = null;
//        }

        // Reset thread
        // if (thread != null)
        // thread = null;

        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public synchronized void run() {
        // if ( serviceIntent!=null ){//sensingActivity != null) {
        // sensingActivity.startProcess();
        // startService(serviceIntent);
        // try {
        // // Thread.sleep(11000);
        // //Thread.sleep(duration);
        // // TODO Verify this is not affected by wait
        // wait(duration);
        // } catch (InterruptedException e) {
        // Log.e(getClass().getName(), "Failed to sleep for " + duration
        // + " ms", e);
        // }
        // }
        if (sessionTask != null) {
            try {
                sessionTask.get();
            } catch (InterruptedException e) {
                Log.e(getClass().getName(), "", e);
            } catch (ExecutionException e) {
                Log.e(getClass().getName(), "", e);
            }
        }
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            suspendRecordingSession();
            // Intent intent = new Intent(MainActivity.this,
            // ResultsListActivity.class); // ResultsActivity.class
            // Intent intent = testSurvey();
            // startActivity(intent);
        }
    };

}