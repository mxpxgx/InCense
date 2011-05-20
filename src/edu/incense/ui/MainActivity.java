package edu.incense.ui;

import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * Main activity to start a recording session. Includes an optional text area
 * for user tagging.
 * 
 * @author Moises Perez (mxpxgx@gmail.com)
 * @version 2011/04/28
 */

public class MainActivity extends MainMenuActivity implements Runnable {

    // UI Elements
    private ProgressDialog progressDialog = null;
    private TextView statusTextView;
    private EditText userNameEditText;

    /*
     * Delegate class that do the sensing and collecting of data SensingActivity
     * sensingActivity = null;
     */

    // Thread to perform the data extraction
    private Thread thread;

    private Intent serviceIntent = null;
    private volatile long duration;

    /** Called when the activity is first created. */
    @Override
    public synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        duration = 1000;
        thread = new Thread(this);
        serviceIntent = null;

        // Get references to UI widgets
        userNameEditText = (EditText) findViewById(R.id.usernameEditText);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        final Button startButton = (Button) findViewById(R.id.startButton);

        // Show Username according to the SharedPreferences
        userNameEditText.setText(getUsername());

        // Set the instructions text
        statusTextView.setText(getResources().getText(R.string.inst_start));

        // Add click listener for START button
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get the userName string
                String userName = (userNameEditText.getText()).toString();

                // Verify is a valid userName
                if (userName == null || !(userName.length() > 0)) {
                    // Visible notice to the user
                    Toast.makeText(getBaseContext(),
                            getResources().getText(R.string.alert_username),
                            Toast.LENGTH_LONG).show();

                } else {
                    /*** START RECORDING SESSION ***/
                    // TODO start session
                    startSession(userName);
                }
            }
        });

    }

    // Called after onCreate has finished, use to restore UI state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart() {
        super.onRestart();
        // Load changes knowing that the activity has already
        // been visible within this process.
        // thread = new Thread(this);
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
        // thread = new Thread(this);
        userNameEditText.setText(getUsername());
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume() {
        super.onResume();

        // Resume any paused UI updates, threads, or processes required
        // by the activity but suspended when it was inactive.
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
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

    // Called at the end of the visible lifetime.
    @Override
    public void onStop() {
        // Suspend remaining UI updates, threads, or processing
        // that aren't required when the Activity isn't visible.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.
        super.onStop();
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy() {
        // Clean up any resources including ending threads,
        // closing database connections etc.
        super.onDestroy();
    }

    public synchronized void run() {
        // if ( serviceIntent!=null ){//sensingActivity != null) {
        // sensingActivity.startProcess();
        // startService(serviceIntent);
        try {
            // Thread.sleep(11000);
            //Thread.sleep(duration);
            // TODO Verify this is not affected by wait
            wait(duration);
        } catch (InterruptedException e) {
            Log.e(getClass().getName(), "Failed to sleep for " + duration
                    + " ms", e);
        }
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

    private String getUsername() {
        Context context = this.getApplicationContext();
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String username = sp.getString("editTextUsername", "Unavailable");
        return username;
    }

    /*** RECORDING SESSION ***/

    SessionTask sessionTask;

    // Start recording session and the thread from this class, show the progress
    // dialog
    private synchronized void startSession(String username) {

        // Show progress dialog
        progressDialog = ProgressDialog.show(this, "Collecting data",
                getResources().getText(R.string.inst_sensing));

        // Save username and tags
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        // Retrieve an editor to modify the shared preferences.
        SharedPreferences.Editor editor = sp.edit();

        // Store new primitive types in the shared preferences object.
        editor.putString("givenUsername", username);

        // Commit the changes.
        editor.commit();

        // Start project
        JsonProject jsonProject = new JsonProject();
        String projectFilename = getResources().getString(
                R.string.project_filename);
        Project project = jsonProject.getProject(projectFilename);
        InCenseApplication.getInstance().setProject(project);
        Session session = project.getSession("mainSession");
        duration = session.getDuration();
        Log.i(getClass().getName(), "Project duration: " + duration);

        sessionTask = new SessionTask(this);
        sessionTask.execute(session);

        // if(serviceIntent != null)
        // serviceIntent = null;

        // Initialize recording session
        // serviceIntent = new Intent(MainActivity.this,
        // edu.incense.inutil.RecordingSession.class);
        // startService(serviceIntent);

        try {
            // Initialize thread if necessary
            if (thread != null) {
                if (thread.getState() != Thread.State.NEW) {
                    thread = null;
                    thread = new Thread(this);
                }
            } else {
                thread = new Thread(this);
            }

            // Start sensing and collecting process
            thread.start();

        } catch (Exception e) {
            Log.e(getClass().getName(), "Starting recording session failed.", e);
        }
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
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

        // Reset thread
        if (thread != null)
            thread = null;

        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }
    }

}