package edu.incense.datatask.data;

import android.net.wifi.ScanResult;

public class WifiData extends Data{
	private String bssid;
	private String ssid;
	private String capabilities;
	private int frequency;
	private int level;
	
	public WifiData(ScanResult scanResult){
		super(DataType.WIFI);
		setBssid(scanResult.BSSID);
		setFrequency(scanResult.frequency);
		setLevel(scanResult.level);
		setSsid(scanResult.SSID);
		setCapabilities(scanResult.capabilities);
	}
	
	public void setBssid(String bssid) { this.bssid = bssid; }
	public String getBssid() { return bssid; }
	public void setSsid(String ssid) { this.ssid = ssid; }
	public String getSsid() { return ssid; }
	public void setCapabilities(String capabilities) { this.capabilities = capabilities; }
	public String getCapabilities() { return capabilities; }
	public void setFrequency(int frequency) { this.frequency = frequency; }
	public int getFrequency() { return frequency; }
	public void setLevel(int level) { this.level = level; }
	public int getLevel() { return level; }
}
