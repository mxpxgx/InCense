package edu.incense.android.datatask.filter;

import android.util.Log;
import edu.incense.android.datatask.data.AccelerometerData;
import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.data.others.BooleanData;

public class ShakeFilter extends DataFilter {
    private static final int SHAKE_THRESHOLD = 900;
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
            return new BooleanData(false);
        } else {
            float speed = (float) (Math.abs(x + y + z - last_x - last_y
                    - last_z)
                    / diffTime * 10000);
            setLast(x, y, z, curTime);
            if (speed > SHAKE_THRESHOLD) {
                Log.v(getClass().getName(), "SHAKE detected with speed: "
                        + speed);
                counter++;
                if (counter > 0) {
                    Log.v(getClass().getName(),
                            "DOUBLE SHAKE detected with speed: " + speed);
                    return new BooleanData(true);
                }
                return new BooleanData(false);
            } else {
//                Log.i(getClass().getName(), "SHAKE NOT detected with speed: "
//                        + speed);
                return new BooleanData(false);
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
