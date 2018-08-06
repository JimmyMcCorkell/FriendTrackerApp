package com.jimmy.tyler.friendtrackerapp.view.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.view.receivers.notifications.CancelMeetingNotification;
import com.jimmy.tyler.friendtrackerapp.view.receivers.notifications.DismissNotification;
import com.jimmy.tyler.friendtrackerapp.view.receivers.notifications.RemindMeetingNotification;
import com.jimmy.tyler.friendtrackerapp.view.receivers.notifications.ViewMeetingNotification;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * receiver that handles the checking of upcoming meetings
 */
public class NotifyReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "Upcoming Meetings";

    private static final int NOTIFY_CODE = 542;

    private static final String CHANNEL_ID = "00";
    private static final String CHANNEL_NAME = "Upcoming Meetings";

    public NotifyReceiver() {
    }

    /**
     * primary constructor used for instantiation
     *
     * @param activity the main activity
     */
    public NotifyReceiver(AppCompatActivity activity) {
        NotificationChannel notifyChannel = null;

        //create the notification channel if the API requires it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifyChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notifyChannel.setDescription("Upcoming Meeting Notification");
            notifyChannel.enableLights(true);
            notifyChannel.setLightColor(Color.RED);
            notifyChannel.enableVibration(true);
            notifyChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            ((NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(notifyChannel);
        }

        //create the alarm that will be used for repeated checking
        AlarmManager manager = (AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, NotifyReceiver.class);
        intent.putExtra("default", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, NOTIFY_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, (new GregorianCalendar()).getTimeInMillis(), 60000, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Model model = ModelImpl.getSingletonInstance();

        //check if the user wants upcoming meetings notifications
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.pref_meeting_notify_key), true)) {
            Log.i(LOG_TAG, "Checking upcoming meetings..");

            //check if this was a regular check or reminder
            if (intent.getBooleanExtra("default", true)) {
                for (Meeting m : model.getMeetings()) {
                    if (!m.getNotified()) {
                        //create the required dates for comparison
                        Calendar now = new GregorianCalendar();
                        Calendar notifyBound = new GregorianCalendar();
                        notifyBound.setTimeInMillis(m.getStartTime().getTimeInMillis());
                        String timeBeforeNotify = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_meeting_notify_list_key), "10");
                        notifyBound.add(Calendar.MINUTE, Integer.parseInt(timeBeforeNotify) * -1);

                        //check if the meeting is within the specified period
                        if (now.compareTo(notifyBound) > 0 && now.compareTo(m.getStartTime()) < 0) {
                            postNotification(context, m.getMeetingId());
                        }
                    }
                }
            } else {
                postNotification(context, intent.getStringExtra("meetingId"));
            }
        }
    }

    /**
     * post the notification to the device
     *
     * @param context the context
     * @param id      the notification id
     */
    private void postNotification(Context context, String id) {
        Meeting m = ModelImpl.getSingletonInstance().getMeeting(id);

        Log.i(LOG_TAG, String.format("Sending Notification For %s", m.getTitle()));

        //generate a notification id
        int notificationId = m.getMeetingId().hashCode();

        //create a view meeting action
        Intent viewMeetingIntent = new Intent(context, ViewMeetingNotification.class);
        viewMeetingIntent.putExtra("id", notificationId);
        viewMeetingIntent.putExtra("meetingId", m.getMeetingId());
        PendingIntent viewMeetingPendingIntent = PendingIntent.getBroadcast(context, 0,
                viewMeetingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //create a reminder action
        Intent remindMeetingIntent = new Intent(context, RemindMeetingNotification.class);
        remindMeetingIntent.putExtra("id", notificationId);
        remindMeetingIntent.putExtra("meetingId", m.getMeetingId());
        PendingIntent remindMeetingPendingIntent = PendingIntent.getBroadcast(context, 1,
                remindMeetingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelMeetingIntent = new Intent(context, CancelMeetingNotification.class);
        cancelMeetingIntent.putExtra("id", notificationId);
        cancelMeetingIntent.putExtra("meetingId", m.getMeetingId());
        PendingIntent cancelMeetingPendingIntent = PendingIntent.getBroadcast(context, 2,
                cancelMeetingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //create a dismiss intent
        Intent dismissIntent = new Intent(context, DismissNotification.class);
        dismissIntent.putExtra("id", notificationId);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 3,
                dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = null;

        String delay = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_meeting_remind_list_key), "5");

        //create the notification based on the API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notify_meeting)
                    .setContentTitle("Upcoming Meeting")
                    .setContentText(m.getTitle())
                    .setContentIntent(viewMeetingPendingIntent)
                    .addAction(new Notification.Action.Builder(
                            Icon.createWithResource(context,
                                    R.drawable.ic_notification_remind_meeting),
                            String.format("REMIND ME IN %s MIN", delay), remindMeetingPendingIntent)
                            .build())
                    .addAction(new Notification.Action.Builder(
                            Icon.createWithResource(context,
                                    R.drawable.ic_notification_cancel_meeting),
                            "CANCEL", cancelMeetingPendingIntent)
                            .build())
                    .addAction(new Notification.Action.Builder(
                            Icon.createWithResource(context,
                                    R.drawable.ic_notification_dismiss),
                            "DISMISS", dismissPendingIntent)
                            .build())
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notify_meeting)
                    .setContentTitle("Upcoming Meeting")
                    .setContentText(m.getTitle())
                    .setContentIntent(viewMeetingPendingIntent)
                    .addAction(R.drawable.ic_notification_remind_meeting, String.format("REMIND ME IN %s MINUTES", delay), remindMeetingPendingIntent)
                    .addAction(R.drawable.ic_notification_cancel_meeting, "CANCEL MEETING", cancelMeetingPendingIntent)
                    .addAction(R.drawable.ic_notification_dismiss, "DISMISS", dismissPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
        }

        //push the notification
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).notify(notificationId, notification);
    }
}
