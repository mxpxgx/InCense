package edu.incense.setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.incense.R;

/**
 * @author Moises Perez (mxpxgx@gmail.com)
 * @version 0.1, 2011/05/05
 */
public class SignupActivity extends Activity{
    private Button bSubmit;
    private Button bCancel;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        bSubmit = (Button) findViewById(R.id.button_signup);
        bCancel = (Button) findViewById(R.id.button_cancel);

        bSubmit.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                submit();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                cancel();
            }
        });

    }

    /**
     *
     */
    private void submit(){

    }

    /**
     * Cancel this sign-up process (activity), returning to the login activity
     */
    private void cancel(){
        finish();
    }
}
