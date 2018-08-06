package com.jimmy.tyler.friendtrackerapp.controller.meeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingEditActivity;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingSelectLocationActivity;

/**
 * controller that handles the 'select location' field
 */
public class MeetingSelectLocationListener implements View.OnClickListener, GoogleMap.OnMapClickListener {

    private AppCompatActivity activity;

    /**
     * primary constructor for instantiation
     *
     * @param activity the host of the map that is clicked
     */
    public MeetingSelectLocationListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * the click listener for handling the field click event
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        handleClick();
    }

    /**
     * the click listener for handling the map click event
     *
     * @param latLng the latlng of the click
     */
    @Override
    public void onMapClick(LatLng latLng) {
        handleClick();
    }

    /**
     * a separate method to merge the functionality of the two click events
     */
    private void handleClick() {
        //start the select location activity
        Intent intent = new Intent(activity, MeetingSelectLocationActivity.class);
        activity.startActivityForResult(intent, MeetingEditActivity.LOCATION_SELECT_REQUEST_CODE);
    }
}