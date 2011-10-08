/**
 * 
 */
package edu.incense.android.sensor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import edu.incense.android.datatask.data.others.BooleanData;

/**
 * @author mxpxgx
 *
 */
public class TimerSensor extends Sensor{
    private ScheduledThreadPoolExecutor stpe;
    private long period;
    
    /**
     * @param context
     */
    public TimerSensor(Context context, long period) {
        super(context);
        this.period = period;
    }
    
    /**
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * @see edu.incense.android.sensor.Sensor#start()
     */
    @Override
    public synchronized void start() {
        super.start();
        stpe = new ScheduledThreadPoolExecutor(1);
        stpe.scheduleAtFixedRate(timerRunnable, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * @see edu.incense.android.sensor.Sensor#stop()
     */
    @Override
    public synchronized void stop() {
        super.stop();
        stpe.shutdown();
    }

    Runnable timerRunnable = new Runnable() {
        public void run() {
            TimerSensor.this.currentData = new BooleanData(true);
        }
    };

}
