package com.jimmy.tyler.friendtrackerapp.view.receivers.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.view.receivers.NotifyReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * receiver for handling the reminding of a meeting
 */
public class RemindMeetingNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //dismiss the notification
        Intent dismissIntent = new Intent(context, DismissNotification.class);
        dismissIntent.putExtra("id", intent.getIntExtra("id", -1));
        context.sendBroadcast(dismissIntent);

        //set the meetings notified
        Meeting meeting = ModelImpl.getSingletonInstance().getMeeting(intent.getStringExtra("meetingId"));
        meeting.setNotified();

        //generate a new alarm
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotifyReceiver.class);
        alarmIntent.putExtra("default", false);
        alarmIntent.putExtra("meetingId", meeting.getMeetingId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String delay = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_meeting_remind_list_key), "5");

        Calendar toNotify = new GregorianCalendar();
        toNotify.add(Calendar.MINUTE, Integer.parseInt(delay));

        //check whether the reminder will occur before the meeting
        if (meeting.getStartTime().compareTo(toNotify) < 0) {
            Toast.makeText(context, "The new reminder would be shown after meeting start therefore it has not been set", Toast.LENGTH_LONG).show();
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, toNotify.getTimeInMillis(), pendingIntent);
            Toast.makeText(context, String.format("Reminder set for %s minute(s)", delay), Toast.LENGTH_SHORT).show();
        }

    }
}
