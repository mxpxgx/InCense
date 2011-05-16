package edu.incense.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.incense.R;

/**
 * This is the first screen the user sees when using InCense application for
 * the first time. The user can enter his username and password to setup, or
 * select to sign-up to the InCense project (starting SignupActivity).
 *
 * @author Moises Perez (mxpxgx@gmail.com)
 * @version 0.1, 2011/05/05
 */
public class LoginActivity extends Activity{
    public static final String EXTRA_USERNAME="username";
    public static final String EXTRA_PASSWORD="password";

    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;
    private Button bSignup;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUsername = (EditText)findViewById(R.id.edittext_username);
        etPassword = (EditText)findViewById(R.id.edittext_password);

        bLogin = (Button)findViewById(R.id.button_signup);
        bSignup = (Button)findViewById(R.id.button_signup);

        bLogin.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                login(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });

        bSignup.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                signup();
            }
        });
    }

    /**
     * Starts a new setup session with the username and password provided
     */
    private void login(String username, String password){
        Intent intent = new Intent(this, SetupService.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        startService(intent);
    }

    /**
     * Starts a SignupActivity in order to register a new user
     */
    private void signup(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
