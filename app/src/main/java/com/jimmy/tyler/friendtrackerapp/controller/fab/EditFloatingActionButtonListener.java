package com.jimmy.tyler.friendtrackerapp.controller.fab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingSelectFriendActivity;

import static com.jimmy.tyler.friendtrackerapp.view.activities.MeetingEditActivity.FRIEND_SELECT_REQUEST_CODE;

/**
 * controller that handles the edit floating action button
 */
public class EditFloatingActionButtonListener implements View.OnClickListener {

    AppCompatActivity activity;

    /**
     * primary constructor for instantiation
     *
     * @param activity the host of the fab
     */
    public EditFloatingActionButtonListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * listener that handles the clicking of the fab
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        //start the select friends activity
        Intent intent = new Intent(activity, MeetingSelectFriendActivity.class);
        activity.startActivityForResult(intent, FRIEND_SELECT_REQUEST_CODE);
    }
}
