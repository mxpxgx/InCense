package edu.incense.android.sensor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.incense.android.datatask.data.GpsData;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GpsSensor extends Sensor implements LocationListener {

    // private Location latestLocation;
    private LocationManager locationManager;
    private List<String> providers;
    private Location[] candidates;

    public GpsSensor(Context context) {
        super(context);

        // Set period time to 2 second (0.5 Hertz)
        // setSampleFrequency(0.5f);

        String service = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(service);

        // At least GPS has to be enabled.
        String provider = LocationManager.GPS_PROVIDER;
        if (locationManager.isProviderEnabled(provider)) {
            // Provider is enabled
            Log.i(getClass().getName(), "Location Provider (" + provider
                    + "): Enabled");
            locationManager.requestLocationUpdates(provider, getPeriodTime(),
                    0f, this);
        } else {
            // Provider not enabled, prompt user to enable it
            Log.i(getClass().getName(), "Location Provider (" + provider
                    + "): Not enabled");
            Toast.makeText(context, "Please turn GPS on", Toast.LENGTH_LONG)
                    .show();
            Intent myIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }

    private Location registerProvider(LocationManager locationManager,
            String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            Log.i(getClass().getName(), "Location Provider: " + provider);

            locationManager.requestLocationUpdates(provider, 20000L, 0.0F, this);

            // Initialize it with the last known location (it is better than
            // nothing at all).
            Location location = locationManager.getLastKnownLocation(provider);
            return location;
        }
        return null;

    }

    private GpsData chooseBestCandidate(Location[] candidates) {
        if (candidates == null)
            return null;
        Arrays.sort(candidates, new Comparator<Location>() {
            public int compare(Location l1, Location l2) {
                if (l1 == l2)
                    return 0;
                if (l1 == null)
                    return -1;
                if (l2 == null)
                    return 1;
                float f1 = l1.getAccuracy();
                float f2 = l2.getAccuracy();
                return (int) (f1 - f2);
            }
        });
        if (candidates[candidates.length - 1] == null)
            return null;
        return new GpsData(candidates[candidates.length - 1]);
    }

    @Override
    public void start() {
        super.start();
        // We are using any provider (GPS, NETWORK or PASSIVE)
        providers = locationManager.getAllProviders();
        candidates = new Location[providers.size()];

        for (int i = 0; i < providers.size(); i++) {
            // Register all available providers
            Location location = registerProvider(locationManager,
                    providers.get(i));
            candidates[i] = location;
        }

        currentData = chooseBestCandidate(candidates);
    }

    @Override
    public void stop() {
        super.stop();
        locationManager.removeUpdates(this);
    }

    /*** LocationListener Methods ***/

    public void onLocationChanged(Location location) {
        Log.i(getClass().getName(), "Location changed!");
        // Toast.makeText(context,"Location changed!",Toast.LENGTH_LONG).show();
        if (isSensing()) {
            Log.i(getClass().getName(), "...[Sensando]...");
            if (location != null) {
                // latestLocation = location;
                GpsData newData = new GpsData(location);
                currentData = newData;
                Log.i(getClass().getName(),
                        "New location: " + location.toString());
                // Toast.makeText(context,"New location: "+location.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    // In case GPS is disabled.
    public void onProviderDisabled(String provider) {
        // stop();
    }

    public void onProviderEnabled(String provider) {
        // start();
    }

    // Check provider availability
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
        case LocationProvider.AVAILABLE:
            Log.i(getClass().getName(), "Status: AVAILABLE");
            // start();
            break;
        case LocationProvider.OUT_OF_SERVICE:
            Log.i(getClass().getName(), "Status: OUT_OF_SERVICE");
            // stop();
            break;
        case LocationProvider.TEMPORARILY_UNAVAILABLE:
            Log.i(getClass().getName(), "Status: TEMPORARILY_UNAVAILABLE");
            // stop();
            break;
        }
    }

}
