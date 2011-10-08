/**
 * 
 */
package edu.incense.android.project;

import java.util.UUID;

import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import edu.incense.android.session.Session;
import edu.incense.android.session.SessionService;

/**
 * @author mxpxgx
 *
 */
public class ProjectManager extends WakefulIntentService {
    private static final String TAG = "ProjectManager";
    private long actionId;

    public ProjectManager() {
        super(TAG);
    }

    /* (non-Javadoc)
     * @see com.commonsware.cwac.wakeful.WakefulIntentService#doWakefulWork(android.content.Intent)
     */
    @Override
    protected void doWakefulWork(Intent intent) {
     // Start service for it to run the recording session
        Intent sessionServiceIntent = new Intent(this, SessionService.class);
        // Point out this action was triggered by a user
        sessionServiceIntent.setAction(SessionService.SESSION_ACTION);
        // Send unique id for this action
        actionId = UUID.randomUUID().getLeastSignificantBits();
        sessionServiceIntent.putExtra(SessionService.ACTION_ID_FIELDNAME,
                actionId);
        startService(sessionServiceIntent);
    }
    
    public void setUpdateAlarm(){
        
    }
    
    /**
     * Set an alarm for recording sessions and surveys.
     */
    public void setAlarmsFor(Session session){
        // TODO
    }
    

}
