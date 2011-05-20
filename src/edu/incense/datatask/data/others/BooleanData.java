package edu.incense.datatask.data.others;

import edu.incense.datatask.data.Data;
import edu.incense.datatask.data.DataType;

public class BooleanData extends Data {
    private boolean value;

    public BooleanData(boolean value) {
        super(DataType.BOOLEAN);
        setValue(value);
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
