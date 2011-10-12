package edu.incense.android.sensor;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import edu.incense.android.datatask.data.GpsData;

public class GpsSensor extends Sensor implements Runnable {
    private final static long MIN_RATE_TIME = 5000L; // 1 minute
    private final static long MAX_TIME_WITHOUT_NEW_LOCATION = 2 * 60 * 1000L; // 2
                                                                              // minutes
    private final static long RESTART_TIME = 5 * 60 * 1000L; // 5 minutes
    private final static String TAG = "GpsSensor";

    private LocationManager locationManager;

    private long lastLocationTime;
    private boolean locationAdded;

    public GpsSensor(Context context) {
        super(context);
        locationAdded = false;
        // LocationManager initialization
        String service = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(service);
    }

    private Location registerProvider(String provider) {
        long minTime = this.getPeriodTime() < MIN_RATE_TIME ? MIN_RATE_TIME
                : getPeriodTime();
        Location location = null;
        try {
            locationManager.requestLocationUpdates(provider, minTime, 0.0F,
                    locationListener);
            // Initialize it with the last known location (it is better than
            // nothing at all).
            location = locationManager.getLastKnownLocation(provider);
            if(location != null){
                Log.i(TAG, "New location: " + location.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "Requesting location updates failed", e);
        }
        Log.i(TAG, "Location Provider registered: " + provider);
        return location;
    }

    @Override
    public void start() {
        super.start();
        // We are using any provider (GPS, NETWORK or PASSIVE)
        addLocationListenerWithAllProviders();
        Thread thread = new Thread(this);
        thread.start();
    }

    private void addLocationListenerWithAllProviders() {
        List<String> providers = locationManager.getAllProviders();
        Location location = null;
        if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            location = registerProvider(LocationManager.PASSIVE_PROVIDER);
        }
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            location = registerProvider(LocationManager.NETWORK_PROVIDER);
        }
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            location = registerProvider(LocationManager.GPS_PROVIDER);
        }

        if (location != null) {
            GpsData newData = new GpsData(location);
            currentData = newData;
        }
        // Very important flags
        lastLocationTime = System.currentTimeMillis();
        locationAdded = true;
    }

    private void removeLocationListener() {
        locationManager.removeUpdates(locationListener);
        locationAdded = false;
    }

    @Override
    public void stop() {
        super.stop();
        removeLocationListener();
    }

    /*** LocationListener Methods ***/
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (isSensing()) {
                GpsData newData = new GpsData(location);
                currentData = newData;
                Log.i(TAG, "New location: " + location.toString());
                Log.i(TAG, "With provider: " + location.getProvider());
                lastLocationTime = newData.getTimestamp();
            }
        }

        // In case GPS is disabled.
        public void onProviderDisabled(String provider) {
            // Needed by LocationListener, not used.
        }

        public void onProviderEnabled(String provider) {
            // Needed by LocationListener, not used.
        }

        // Check provider availability
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "Provider ["+provider+"] status changed to: "+status);
        }
    };

    public void run() {
        Looper.prepare();
        long timeElapsed;
        while (isSensing()) {
            try {
                Thread.sleep(MAX_TIME_WITHOUT_NEW_LOCATION);
            } catch (InterruptedException e) {
                Log.e(TAG, "GpsSensor controller failed", e);
            }
            timeElapsed = System.currentTimeMillis() - lastLocationTime;

            if (timeElapsed > MAX_TIME_WITHOUT_NEW_LOCATION && locationAdded) {
                removeLocationListener();
                Log.d(TAG, "LocationListener removed: " + !locationAdded);
            }

            if (timeElapsed > (MAX_TIME_WITHOUT_NEW_LOCATION + RESTART_TIME)
                    && !locationAdded) {
                addLocationListenerWithAllProviders();
                Log.d(TAG, "LocationListener added: " + locationAdded);
            }
        }
        Log.d(TAG, "sensorController finished");
    }

}
