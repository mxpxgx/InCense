package edu.incense.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.incense.R;
import edu.incense.setup.LoginTask;
import edu.incense.ui.MainActivity;

/**
 * This is the first screen the user sees when starts the InCense application
 * for the first time. The user can enter his username and password to login and
 * setup, or select to sign-up to the InCense project (starting SignupActivity).
 * Alerts/notices will be presented with Toasts (eg. unknown user, wrong
 * password).
 * 
 * @author Moises Perez (mxpxgx@gmail.com)
 * @since 2011/05/17
 * @version 0.3, 2011/05/18
 */
public class LoginActivity extends Activity implements
        LoginTask.LoginTaskListener {
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_PASSWORD = "password";

    private String username = "Unknown User";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText etUsername = (EditText) findViewById(R.id.edittext_username);
        final EditText etPassword = (EditText) findViewById(R.id.edittext_password);

        final Button bLogin = (Button) findViewById(R.id.button_login);
        final Button bRegister = (Button) findViewById(R.id.button_register);

        bLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                username = etUsername.getText().toString();
                login(username, etPassword.getText().toString());
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                register();
            }
        });
    }

    /**
     * Starts a login task with the username and password provided, if it's
     * successful, starts a new setup session (TODO setup session?).
     */
    private void login(String username, String password) {
        if (username.length() == 0) {
            Toast.makeText(this, getText(R.string.username_empty),
                    Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(this, getText(R.string.password_empty),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Start login task: authentication of the user
            LoginTask loginTask = new LoginTask(this);
            loginTask.execute(username, password);
        }
    }

    /**
     * Starts the MainActivity. This method is called by a LoginTask if the user
     * was successfully logged in.
     */
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Starts a SignupActivity in order to register a new user
     */
    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Receives the the result from the authentication process and shows the
     * result to the user.
     * 
     * @param result
     */
    public void onLoginTaskComplete(int result) {
        String resultMessage = null;
        if (result == LoginTask.LOGGED_IN) {
            resultMessage = getString(R.string.logged_in_message) + username;
            startMainActivity();
        }
        if (resultMessage != null) {
            Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
