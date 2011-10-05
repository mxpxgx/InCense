/**
 * 
 */
package edu.incense.android.datatask.filter;

import java.util.ArrayList;

import android.util.Log;

import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.data.DataType;
import edu.incense.android.datatask.data.WifiData;

/**
 * Checks if the network name detected (in a scan) is included in the list of access points registered 
 * @author mxpxgx
 *
 */
public class WifiLocationFilter extends DataFilter {
    public final static String ATT_ISINLOCATION = "isInLocation";
    private ArrayList<String> apList;

    public WifiLocationFilter() {
        super();
        setFilterName("WifiLocationFilter");
        apList = new ArrayList<String>();
    }
    
    public WifiLocationFilter createFilter(){
        return new WifiLocationFilter();
    }
    
    public WifiLocationFilter add(String apString){
        this.addAP(apString);
        return this;
    }
    
    public void addAP(String apString){
        apList.add(apString);
    }

    @Override
    protected void computeSingleData(Data data) {
        if(data.getDataType() == DataType.WIFI){
            WifiData newData = (WifiData) data;
            Log.d(this.getFilterName(), "Searching for: ["+newData.getBssid()+"]");
//            if(apList.contains(newData.getBssid())){
            if(apList.contains(newData.getSsid())){
                newData.getExtras().putBoolean(ATT_ISINLOCATION, true);
            } else {
                newData.getExtras().putBoolean(ATT_ISINLOCATION, false);
            }
        }
        pushToOutputs(data);
    }
    
}