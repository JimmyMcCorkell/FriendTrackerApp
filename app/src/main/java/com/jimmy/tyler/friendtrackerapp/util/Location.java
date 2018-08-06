package com.jimmy.tyler.friendtrackerapp.util;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.jimmy.tyler.friendtrackerapp.controller.location.LocationRequestFailureListener;
import com.jimmy.tyler.friendtrackerapp.controller.location.LocationRequestSuccessListener;
import com.jimmy.tyler.friendtrackerapp.view.fragments.MapFragment;

public class Location {

    public static LatLng userLocation = null;

    private FusedLocationProviderClient lClient;
    private LocationRequest lRequest;

    private AppCompatActivity activity;
    private MapFragment fragment;

    public Location(AppCompatActivity activity, MapFragment fragment) {
        this.activity = activity;
        this.fragment = fragment;

        lClient = LocationServices.getFusedLocationProviderClient(this.activity);

        createLocationRequest();
    }

    /**
     * Create a location request to hardware
     */
    private void createLocationRequest() {
        // Configure the request
        lRequest = new LocationRequest();
        lRequest.setInterval(7500);
        lRequest.setFastestInterval(5000);
        lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(lRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        // Set task success and fail listeners
        task.addOnSuccessListener(activity, new LocationRequestSuccessListener(this, fragment));
        task.addOnFailureListener(activity, new LocationRequestFailureListener(activity));
    }

    /**
     * Generate the location callback, to update the user marker position
     */
    public void handleLocationSuccess() throws SecurityException {
        LocationCallback lCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                userLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                fragment.updateUserMarker(userLocation);
            }
        };

        lClient.requestLocationUpdates(lRequest, lCallback, null);
    }
}
