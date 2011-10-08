/**
 * 
 */
package edu.incense.android;

import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import edu.incense.android.project.ProjectManager;
import edu.incense.android.session.SessionService;

/**
 * @author mxpxgx
 *
 */
public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sessionServiceIntent = new Intent(context, SessionService.class);
        // Point out this action was triggered by a user
        sessionServiceIntent.setAction(SessionService.SESSION_ACTION);
        // Send unique id for this action
        long actionId = UUID.randomUUID().getLeastSignificantBits();
        sessionServiceIntent.putExtra(SessionService.ACTION_ID_FIELDNAME,
                actionId);
        WakefulIntentService.sendWakefulWork(context, sessionServiceIntent);
    }

}
