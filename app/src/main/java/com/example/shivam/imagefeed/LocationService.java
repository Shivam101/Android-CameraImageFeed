package com.example.shivam.imagefeed;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by Shivam on 07/05/15 at 4:26 PM.
 */
public class LocationService extends Service implements LocationListener {

    LocationManager manager;
    Location location;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    public LocationService(Context context)
    {
        manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation(String s)
    {
        if (manager.isProviderEnabled(s)) {
            manager.requestLocationUpdates(s,
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (manager != null) {
                location = manager.getLastKnownLocation(s);
                return location;
            }
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
