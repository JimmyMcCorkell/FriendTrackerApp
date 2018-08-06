package com.jimmy.tyler.friendtrackerapp.view.receivers.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingDetailsActivity;

/**
 * receiver that handles the viewing a meeting
 */
public class ViewMeetingNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //dismiss the notification
        Intent dismissIntent = new Intent(context, DismissNotification.class);
        dismissIntent.putExtra("id", intent.getIntExtra("id", -1));
        context.sendBroadcast(dismissIntent);

        //open the meeting details
        Intent viewIntent = new Intent(context, MeetingDetailsActivity.class);
        viewIntent.putExtra("meetingId", intent.getStringExtra("meetingId"));
        context.startActivity(viewIntent);
    }
}
