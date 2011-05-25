package edu.incense.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.incense.R;
import edu.incense.session.SessionService;

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
    private TextView usernameTextView;
    private Button startButton;

    private String username = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        // Get UI elements
        usernameTextView = (TextView) findViewById(R.id.textview_username);
        statusTextView = (TextView) findViewById(R.id.textview_status);
        startButton = (Button) findViewById(R.id.button_start);

        // Initialize username and usernameEditText according to the
        // SharedPreferences
        updateUsernameFromPrefs();

        // Set the instructions text
        statusTextView.setText(getResources().getText(
                R.string.record_instructions));

        // Add click listener in START button
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verify is a valid username
                if (username == null || !(username.length() > 0)) {
                    // Visible notice to the user
                    Toast.makeText(
                            RecordActivity.this,
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
        usernameTextView.setText(this.username);
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
    
    @Override
    protected void onResume() {
        super.onResume();
        resetUI();
    }

    /*** RECORDING SESSION ***/
    private Intent sessionServiceIntent;

    /**
     * Start recording session and the thread from this class, show the progress
     * dialog
     */
    private void startSession() {
        startButton.setEnabled(false);

        // Show progress dialog
        Resources res = getResources();
        progressDialog = ProgressDialog.show(this,
                res.getText(R.string.session_title),
                res.getText(R.string.session_active_message));

        // Start service for it to run the recording session
        sessionServiceIntent = new Intent(this, SessionService.class);
        // Point out this action was triggered by a user
        sessionServiceIntent.setAction(SessionService.SESSION_USER_ACTION);
        startService(sessionServiceIntent);
    }

    // Suspend recording session and the thread from this class, dismiss the
    // progress dialog
    private void suspendRecordingSession() {
        stopService(sessionServiceIntent);
        resetUI();
    }
    
    public void resetUI(){
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }
        
        if(!startButton.isEnabled())
            startButton.setEnabled(true);
    }
}