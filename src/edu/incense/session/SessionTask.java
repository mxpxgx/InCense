package edu.incense.session;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This task is used by the MainActivity (TODO?)
 * 
 * @author Moises Perez (mxpxgx@gmail.com)
 * @version 0.?, ?
 */

public class SessionTask extends AsyncTask<Session, Integer, Boolean> {
    public final static int STARTED = 0;
    public final static int SENSING = 1;
    public final static int COMPLETED = 2;
    public final static int CANCELED = 3;
    private Context context;
    private SessionController controller;

    public SessionTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Session... params) {
        publishProgress(STARTED);
        controller = new SessionController(context, params[0]);
        controller.prepareSession();

        publishProgress(SENSING);
        controller.start();

        // controller.stop();
        publishProgress(COMPLETED);
        return true;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (controller != null) {
            controller.stop();
        }
        publishProgress(CANCELED);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

}
