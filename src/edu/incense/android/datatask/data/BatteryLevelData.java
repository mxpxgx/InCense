/**
 * 
 */
package edu.incense.android.datatask.data;

/**
 * @author mxpxgx
 *
 */
public class BatteryLevelData extends Data {
    
    private int level = -1;
    
    /**
     * @param dataType
     */
    public BatteryLevelData(DataType dataType) {
        super(dataType);
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

}
