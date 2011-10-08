package edu.incense.android.datatask.data;


public class AccelerometerFrameData extends Data {
    private final static int FRAME_ITEM_SIZE = 4;
    private final static int X_AXIS = 0;
    private final static int Y_AXIS = 1;
    private final static int Z_AXIS = 2;
    private final static int TIMESTAMP = 3;
    private double[][] frame;
    private int size, index;

    private AccelerometerFrameData(DataType type, int size) {
        super(type);
        this.size = size;
        frame = new double[size][FRAME_ITEM_SIZE];
        index=0;
    }
    
    public AccelerometerFrameData(int size) {
        this(DataType.ACCELEROMETER, size);
    }
    
    public static AccelerometerFrameData createGyroFrameData(int size) {
        AccelerometerFrameData gyroData = new AccelerometerFrameData(DataType.GYROSCOPE, size);
        return gyroData;
    }
    
    public void add(double x, double y, double z, double timestamp){
        if(index < size && index >= 0){
            frame[index][X_AXIS] = x;
            frame[index][Y_AXIS] = y;
            frame[index][Z_AXIS] = z;
            frame[index][TIMESTAMP] = timestamp;
            index++;
        }
    }
    
    public double[] get(int index){
        if(index < size && index >= 0){
            return frame[index];
        }
//        return new double[]{0,0,0};
        return null;
    }

    public void setTimestamp(long timestamp){
        super.setTimestamp(timestamp);
    }

    /**
     * @return the frame
     */
    public double[][] getFrame() {
        return frame;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    
    public boolean full(){
        return (index >= size);
    }

}
