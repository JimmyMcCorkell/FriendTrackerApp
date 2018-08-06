package com.jimmy.tyler.friendtrackerapp.controller.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * controller for handling dragging of a map marker
 */
public class OnMarkerDragListener implements GoogleMap.OnMarkerDragListener {

    private Marker meetingMarker;

    /**
     * primary constructor
     *
     * @param meetingMarker the marker associated with the listener
     */
    public OnMarkerDragListener(Marker meetingMarker) {
        this.meetingMarker = meetingMarker;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * listener for end of a drag event
     *
     * @param marker the marker that was dragged
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        //update the marker position
        meetingMarker.setPosition(marker.getPosition());
    }
}
