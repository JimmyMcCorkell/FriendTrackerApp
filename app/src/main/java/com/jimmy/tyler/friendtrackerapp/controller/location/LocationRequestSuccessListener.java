package com.jimmy.tyler.friendtrackerapp.controller.location;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jimmy.tyler.friendtrackerapp.util.Location;
import com.jimmy.tyler.friendtrackerapp.view.fragments.MapFragment;

/**
 * the controller that handles the failure of a location request
 */
public class LocationRequestSuccessListener implements OnSuccessListener<LocationSettingsResponse> {

    private Location locationHandler;
    private MapFragment fragment;

    /**
     * primary constructor for instantiation
     *
     * @param locationHandler the location class holding location functionality
     * @param fragment        the host of the location client
     */
    public LocationRequestSuccessListener(Location locationHandler, MapFragment fragment) {
        this.locationHandler = locationHandler;
        this.fragment = fragment;
    }

    /**
     * the listener for handling a failed request
     *
     * @param locationSettingsResponse the response from the location settings
     */
    @Override
    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        //generate a callback and create the client
        try {
            locationHandler.handleLocationSuccess();
            fragment.setLocationRequested(true);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }
}
