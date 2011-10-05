/**
 * 
 */
package edu.incense.android.datatask.filter;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import edu.incense.android.datatask.data.Data;
import edu.incense.android.datatask.data.DataType;
import edu.incense.android.datatask.data.WifiData;

/**
 * @author mxpxgx
 *
 */
public class WifiConnectionFilter extends DataFilter {
    public final static String ATT_ISCONNECTED = "isConnected";
    private boolean connected;
    private List<WifiData> apList;
    private WifiManager wifi;

    public WifiConnectionFilter(Context context) {
        super();
        setFilterName("WifiConnectionFilter");
        connected = false;
        apList = new ArrayList<WifiData>();
        String service = Context.WIFI_SERVICE;
        wifi = (WifiManager)context.getSystemService(service);
    }

    @Override
    protected void computeSingleData(Data data) {
//        if(data.getDataType() == DataType.WIFI){
//            newData = (WifiData) data;
//            Log.d(this.getFilterName(), "Connecting to: ["+newData.getBssid()+"]");
//            connect(newData);
//            if(apList.contains(newData.getSsid())){
//                newData.getExtras().putBoolean(ATT_ISCONNECTED, true);
//            } else {
//                newData.getExtras().putBoolean(ATT_ISCONNECTED, false);
//            }
//        } else {
//            pushToOutputs(data);
//        }
    }

    /**
     * @param newData
     */
    private void connect(WifiData newData) {
//        if (!wifi.isWifiEnabled()){
//            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
//            wifi.setWifiEnabled(true);
//            }
//        }
//        
//        WifiInfo info = wifi.getConnectionInfo(); if (info.getBSSID() != null) {
//            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5); int speed = info.getLinkSpeed();
//            String units = WifiInfo.LINK_SPEED_UNITS;
//            String ssid = info.getSSID();
//            String cSummary = String.format("Connected to %s at %s%s. Strength %s/5", ssid, speed, units, strength);
//            }
//        
//     // Get a list of available configurations
//        List<WifiConfiguration> configurations = wifi.getConfiguredNetworks(); // Get the network ID for the first one.
//        if (configurations.size() > 0) {
//        int netID = configurations.get(0).networkId;
//        // Enable that network.
//        boolean disableAllOthers = true; 
//        wifi.enableNetwork(netID, disableAllOtherstrue);
//        }
////        if(connected)
        // TODO Auto-generated method stub
        
    }
    
    public class ConnectionMonitor extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            
        }
    }
}
