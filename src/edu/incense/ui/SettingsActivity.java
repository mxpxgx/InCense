package edu.incense.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import edu.incense.R;

public class SettingsActivity extends PreferenceActivity {
    SharedPreferences sharedPreferences;
    CheckBoxPreference bluetoothCheckBoxPreference;
    CheckBoxPreference wifiCheckBoxPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        bluetoothCheckBoxPreference = (CheckBoxPreference) getPreferenceScreen()
                .findPreference("checkboxBluetooth");
        bluetoothCheckBoxPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        // setWifiOff();
                        wifiCheckBoxPreference.setChecked(false);
                        verifyBluetooth();
                        return true;
                    }
                });

        // Get a reference to the checkbox preference
        CheckBoxPreference gpsCheckBoxPreference = (CheckBoxPreference) getPreferenceScreen()
                .findPreference("checkboxGps");
        gpsCheckBoxPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        verifyGps();
                        return true;
                    }
                });

        wifiCheckBoxPreference = (CheckBoxPreference) getPreferenceScreen()
                .findPreference("checkboxWifi");
        wifiCheckBoxPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        // setBluetoothOff();
                        bluetoothCheckBoxPreference.setChecked(false);
                        verifyWifi();
                        return true;
                    }
                });
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
    }

    private void verifyGps() {
        boolean isGpsOn = sharedPreferences.getBoolean("checkboxGps", false);

        if (isGpsOn) {
            String service = Context.LOCATION_SERVICE;
            LocationManager locationManager = (LocationManager) getSystemService(service);
            String provider = LocationManager.GPS_PROVIDER;

            if (!locationManager.isProviderEnabled(provider)) {
                // Provider not enabled, prompt user to enable it
                Toast.makeText(this, "Please turn GPS on", Toast.LENGTH_LONG)
                        .show();
                Intent myIntent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        }
    }

    private void verifyWifi() {
        boolean isWifiOn = sharedPreferences.getBoolean("checkboxWifi", false);

        if (isWifiOn) {
            String service = Context.WIFI_SERVICE;
            WifiManager wifiManager = (WifiManager) getSystemService(service);
            if (!wifiManager.isWifiEnabled()) {
                // Provider not enabled, prompt user to enable it
                Toast.makeText(this, "Please turn WiFi on", Toast.LENGTH_LONG)
                        .show();
                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        }
    }

    private void verifyBluetooth() {
        boolean isBluetoothOn = sharedPreferences.getBoolean(
                "checkboxBluetooth", false);

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (isBluetoothOn && bluetooth != null) {

            if (!bluetooth.isEnabled()) {
                // Provider not enabled, prompt user to enable it
                Toast.makeText(this, "Please turn WiFi on", Toast.LENGTH_LONG)
                        .show();
                Intent myIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        }
    }

    /*
     * private void setSensorOff(String sensorName){ // Retrieve an editor to
     * modify the shared preferences. SharedPreferences.Editor editor =
     * sharedPreferences.edit();
     * 
     * // Store new primitive types in the shared preferences object.
     * editor.putBoolean(sensorName, false);
     * 
     * // Commit the changes. editor.commit(); }
     * 
     * private void setWifiOff(){ setSensorOff("checkboxWifi"); }
     * 
     * private void setBluetoothOff(){ setSensorOff("checkboxBluetooth"); }
     */

    /*** MENU ***/

    // This inflates/populates your menu resource (convert the XML resource into
    // a programmable object)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    // This method passes the MenuItem that the user selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
        case R.id.new_recording:
            Intent mainIntent = new Intent(this, RecordActivity.class);
            startActivity(mainIntent);
            return true;
        case R.id.settings:
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            // startActivityForResult(settingsIntent, SHOW_PREFERENCES);
            return true;
        case R.id.results:
            Intent resultsIntent = new Intent(this, ResultsListActivity.class);
            startActivity(resultsIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
