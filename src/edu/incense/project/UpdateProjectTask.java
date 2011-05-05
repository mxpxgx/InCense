package edu.incense.project;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateProjectTask extends AsyncTask<Void,Void,Boolean> {
	private Context context;

	public UpdateProjectTask(Context context){
		super();
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		ProjectUpdater updater = new ProjectUpdater(context);
		return updater.updateProject();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result == true){
			Toast.makeText(context,"The project has been updated.",Toast.LENGTH_LONG).show();
		}
	}

}
