package com.jimmy.tyler.friendtrackerapp.controller.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.view.activities.FriendDetailsActivity;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingDetailsActivity;

/**
 * controller that handles the clicking of an info window on a map
 */
public class InfoWindowListener implements GoogleMap.OnInfoWindowClickListener {

    private AppCompatActivity activity;
    private Model model;

    /**
     * primary constructor for instantiation
     *
     * @param activity the host of the map view
     */
    public InfoWindowListener(AppCompatActivity activity) {
        this.activity = activity;
        this.model = ModelImpl.getSingletonInstance();
    }

    /**
     * listener for the click event
     *
     * @param marker the associated marker attached to the info window
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //check if the marker is a meeting or a friend
        if (marker.getSnippet().contains("Meeting Id: ")) {
            //start the meeting details activity
            Intent intent = new Intent(activity, MeetingDetailsActivity.class);
            intent.putExtra("meetingId", marker.getSnippet().replace("Meeting Id: ", ""));
            activity.startActivity(intent);
        } else if (marker.getSnippet().contains("Friend Id: ")) {
            //start the friend details activity
            Intent intent = new Intent(activity, FriendDetailsActivity.class);
            intent.putExtra("friend", model.getFriend(marker.getSnippet().replace("Friend Id: ", "")));
            activity.startActivity(intent);
        }
    }
}
