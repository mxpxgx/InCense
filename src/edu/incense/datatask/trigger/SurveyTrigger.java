package edu.incense.datatask.trigger;

import java.util.UUID;

import edu.incense.datatask.data.Data;
import edu.incense.datatask.data.others.BooleanData;
import edu.incense.session.SessionService;
import android.content.Context;
import android.content.Intent;

public class SurveyTrigger extends DataTrigger {
    private String surveyName;
    private long actionId;

    // private boolean trigger;

    public SurveyTrigger(Context context) {
        super(context);
        // trigger =false;
    }

    @Override
    protected void trigger() {
        //(new StartSurveyTask(context)).execute(surveyName);
        // Start service for it to run the recording session
        Intent sessionServiceIntent = new Intent(context, SessionService.class);
        // Point out this action was triggered by a user
        sessionServiceIntent.setAction(SessionService.SURVEY_ACTION);
        // Send unique id for this action
        actionId = UUID.randomUUID().getLeastSignificantBits();
        sessionServiceIntent.putExtra(SessionService.ACTION_ID_FIELDNAME,
                actionId);
        context.startService(sessionServiceIntent);
    }

    protected void computeSingleData(Data data) {
        BooleanData bData = (BooleanData) data;
        if (bData.getValue()) {
            // trigger = true;
            trigger();
            // stop();
        }
    }

    /*
     * @Override public void stop(){ if(trigger) trigger(); super.stop(); }
     */

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyName() {
        return surveyName;
    }

}
