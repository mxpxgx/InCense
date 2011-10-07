package edu.incense.android.datatask.filter;

import android.util.Log;
import edu.incense.android.datatask.data.AccelerometerData;
import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.data.others.BooleanData;

public class ShakeFilter extends DataFilter {
    private static final String ATT_ISSHAKE = "isShake";
    private static final int SHAKE_THRESHOLD = 500; //900;
    private double last_x, last_y, last_z;
    private long lastUpdate;
    private boolean last;
    private int counter;

    public ShakeFilter() {
        super();
        setFilterName(this.getClass().getName());
        last_x = 0;
        last_y = 0;
        last_z = 0;
        lastUpdate = 0;
        last = false;
        counter = 0;
    }

    @Override
    public void start() {
        super.start();
        last = false;
        counter = 0;
    }

    @Override
    protected void computeSingleData(Data data) {
        Data newData = seekForShake(data);
        pushToOutputs(newData);
    }

    private Data seekForShake(Data data) {
        AccelerometerData accData = (AccelerometerData) data;
        double x = accData.getAxisX();
        double y = accData.getAxisY();
        double z = accData.getAxisZ();
        long curTime = accData.getTimestamp();
        long diffTime = (curTime - lastUpdate);

        if (!last) {
            setLast(x, y, z, curTime);
            last = true;
            //return new BooleanData(false);
            data.getExtras().putBoolean(ATT_ISSHAKE, false);
            return data;
        } else {
            float velocity = (float) (Math.abs(x + y + z - last_x - last_y
                    - last_z)
                    / diffTime * 10000000000L);
            setLast(x, y, z, curTime);
            if (velocity > SHAKE_THRESHOLD) {
                Log.v(getClass().getName(), "SHAKE detected with speed: "
                        + velocity);
                counter++;
                if (counter > 0) {
                    Log.v(getClass().getName(),
                            "DOUBLE SHAKE detected with speed: " + velocity);
//                    return new BooleanData(true);
                    data.getExtras().putBoolean(ATT_ISSHAKE, true);
                    return data;
                }
//                return new BooleanData(false);
                data.getExtras().putBoolean(ATT_ISSHAKE, false);
                return data;
            } else {
//                Log.i(getClass().getName(), "SHAKE NOT detected with velocity: "
//                        + velocity);
//                return new BooleanData(false);
                data.getExtras().putBoolean(ATT_ISSHAKE, false);
                return data;
            }
        }

    }

    public void setLast(double x, double y, double z, long lastUpdate) {
        last_x = x;
        last_y = y;
        last_z = z;
        this.lastUpdate = lastUpdate;
    }

}
