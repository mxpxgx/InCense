/**
 * 
 */
package edu.incense.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import edu.incense.android.project.ProjectManager;

/**
 * @author mxpxgx
 *
 */
public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WakefulIntentService.sendWakefulWork(context, ProjectManager.class);
    }

}
