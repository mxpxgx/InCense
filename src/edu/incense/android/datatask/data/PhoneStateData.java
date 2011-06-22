package edu.incense.android.datatask.data;

public class PhoneStateData extends Data {
    // private int isCharging;
    private int dataState;

    public PhoneStateData() {
        super(DataType.STATES);
    }

    public void setState(int state) {
        this.dataState = state;
    }

    public int getState() {
        return dataState;
    }
}
