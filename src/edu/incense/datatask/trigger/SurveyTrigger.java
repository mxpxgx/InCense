package edu.incense.datatask.trigger;

import edu.incense.datatask.data.Data;
import edu.incense.datatask.data.others.BooleanData;
import edu.incense.survey.StartSurveyTask;
import android.content.Context;

public class SurveyTrigger extends DataTrigger {
	private String surveyName;
	//private boolean trigger;

	public SurveyTrigger(Context context) {
		super(context);
		//trigger =false;
	}

	@Override
	protected void trigger() {
		(new StartSurveyTask(context)).execute(surveyName);
	}

	protected void computeSingleData(Data data) {
		BooleanData bData = (BooleanData)data;
		if(bData.getValue()){
			//trigger = true;
			trigger();
			//stop();
		}
	}
	
	/*@Override
	public void stop(){
		if(trigger) trigger();
		super.stop();
	}*/

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getSurveyName() {
		return surveyName;
	}

}
