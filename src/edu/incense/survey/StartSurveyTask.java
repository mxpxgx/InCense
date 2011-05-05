package edu.incense.survey;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import edu.incense.InCenseApplication;

public class StartSurveyTask extends AsyncTask<String,Integer,Boolean>{
	public final static int STARTED = 0;
	public final static int COMPLETED = 2;
	public final static int CANCELED = 3;
	private Context context;
	
	public StartSurveyTask(Context context){
		super();
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		publishProgress(STARTED);
		Survey survey = InCenseApplication.getInstance().getProject().getSurvey(params[0]);
		if(survey != null){
			Log.i(getClass().getName(), "Starting survey");
			InCenseApplication.getInstance().setSurvey(survey);
			Intent intent = new Intent(InCenseApplication.getInstance(), SurveyActivity.class);
			context.startActivity(intent);
		} else {
			Log.i(getClass().getName(), "Survey was null.");
		}
		publishProgress(COMPLETED);
		return true;
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		publishProgress(CANCELED);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}