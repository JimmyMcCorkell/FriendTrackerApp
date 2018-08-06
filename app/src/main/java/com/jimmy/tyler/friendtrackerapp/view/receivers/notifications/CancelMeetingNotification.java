package com.jimmy.tyler.friendtrackerapp.view.receivers.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;

/**
 * receiver that handles the cancelling a meeting
 */
public class CancelMeetingNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //dismiss the notification
        Intent dismissIntent = new Intent(context, DismissNotification.class);
        dismissIntent.putExtra("id", intent.getIntExtra("id", -1));
        context.sendBroadcast(dismissIntent);

        //cancel the meeting
        String title = ModelImpl.getSingletonInstance().getMeeting(intent.getStringExtra("meetingId")).getTitle();
        ModelImpl.getSingletonInstance().removeMeeting(intent.getStringExtra("meetingId"));
        Toast.makeText(context, String.format("%s cancelled", title), Toast.LENGTH_SHORT).show();
    }
}
