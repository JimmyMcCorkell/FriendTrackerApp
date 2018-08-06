package com.jimmy.tyler.friendtrackerapp.view.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jimmy.tyler.friendtrackerapp.view.fragments.MapFragment;

public class LocationReceiver extends BroadcastReceiver {

    public static final String LOCATION_RESPONSE = "com.jimmy.mccorkell.friendtrackerapp.view.receivers.LOCATION";

    private MapFragment mapFragment;

    /**
     * Construct a new location receiver
     *
     * @param mapFragment
     */
    public LocationReceiver(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mapFragment.updateMarkers();
    }
}
