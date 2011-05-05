package edu.incense;

public class MainService {
	
	private final static int PERIOD_TIME=1; //Period in Minutes to check new update
	
	/*private void initAlarmManager(){
		AlarmManager alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		//int alarmType = AlarmManager.RTC_WAKEUP;
		//long timeOrLengthofWait = 10000; 
		String ALARM_ACTION = "UPDATE_PROJECT";
		Intent intentToFire = new Intent(ALARM_ACTION);
		PendingIntent elapsedIntent = PendingIntent.getService(this, 0, intentToFire, 0);
		//alarms.set(alarmType, timeOrLengthofWait, pendingIntent);
		alarms.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, PERIOD_TIME*60*1000, PERIOD_TIME*60*1000, elapsedIntent);
	}*/

}
