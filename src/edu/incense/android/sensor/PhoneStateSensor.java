package edu.incense.android.sensor;

import android.content.Context;
//import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;

public class PhoneStateSensor extends Sensor {
    private TelephonyManager telephonyManager;

    // private PhoneStateData phoneState;
    // private PhoneCallData phoneCall;

    public PhoneStateSensor(Context context) {
        super(context);
        String service = Context.TELEPHONY_SERVICE;
        telephonyManager = (TelephonyManager) context.getSystemService(service);
        telephonyManager.listen(new PhoneStateListener() {
            public void onDataActivity(int direction) {
                // TODO
                // phoneState.setState(telephonyManager.getDataState());
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void start() {
        // super.start();
    }

    public void stop() {
        super.stop();
    }

}
