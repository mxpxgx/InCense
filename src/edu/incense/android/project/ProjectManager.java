/**
 * 
 */
package edu.incense.android.project;

import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import edu.incense.android.session.Session;

/**
 * @author mxpxgx
 *
 */
public class ProjectManager extends WakefulIntentService {
    private static final String TAG = "ProjectManager";

    public ProjectManager() {
        super(TAG);
    }

    /* (non-Javadoc)
     * @see com.commonsware.cwac.wakeful.WakefulIntentService#doWakefulWork(android.content.Intent)
     */
    @Override
    protected void doWakefulWork(Intent intent) {
        // TODO Auto-generated method stub
        
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
