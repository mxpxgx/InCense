package edu.incense.android.datatask.data;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

public class WifiData extends Data {
    private String bssid;
    private String ssid;
    private String capabilities;
    private int frequency;
    private int level;

    public WifiData(ScanResult scanResult) {
        super(DataType.WIFI);
        setBssid(scanResult.BSSID);
        setFrequency(scanResult.frequency);
        setLevel(scanResult.level);
        setSsid(scanResult.SSID);
        setCapabilities(scanResult.capabilities);
    }
    
    public WifiData(WifiConfiguration config) {
        super(DataType.WIFI);
        setBssid(config.BSSID);
        setSsid(config.SSID);
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
