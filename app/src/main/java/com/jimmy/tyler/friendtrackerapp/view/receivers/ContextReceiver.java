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
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.util.Location;
import com.jimmy.tyler.friendtrackerapp.view.activities.SuggestMeetingActivity;

import java.util.GregorianCalendar;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * receiver that handles the checking of possible meeting suggestions
 */
public class ContextReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "Contextual Meetings";

    private static final int CONTEXT_CODE = 759;

    private static final String CHANNEL_ID = "01";
    private static final String CHANNEL_NAME = "Contextual Meeting";

    public ContextReceiver() {
    }

    /**
     * primary constructor for instantiation
     *
     * @param activity the main activity
     */
    public ContextReceiver(AppCompatActivity activity) {
        NotificationChannel notifyChannel = null;

        //create the notification channel if the API requires it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifyChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notifyChannel.setDescription("Contextual Meeting Notification");
            notifyChannel.enableLights(true);
            notifyChannel.setLightColor(Color.RED);
            notifyChannel.enableVibration(true);
            notifyChannel.setVibrationPattern(new long[]{500, 400, 300, 200, 100});

            ((NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(notifyChannel);
        }

        //create the alarm that will be used for repeated checking
        AlarmManager manager = (AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, ContextReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, CONTEXT_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String delay = PreferenceManager.getDefaultSharedPreferences(activity).getString(activity.getResources().getString(R.string.pref_context_list_key), "1");
        manager.setRepeating(AlarmManager.RTC_WAKEUP, (new GregorianCalendar()).getTimeInMillis(), Integer.parseInt(delay) * 60000, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //check if the user wants the suggestions
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.pref_context_key), true)) {
            Log.i(LOG_TAG, "Sending Suggestion Notification");

            boolean locationsFound = false;

            //check if any friends have locations
            for (Friend f : ModelImpl.getSingletonInstance().getFriends()) {
                if (f.getLocation() != null) {
                    locationsFound = true;
                }
            }

            //check if the user location is available and if the device is connected to the network
            if (Location.userLocation != null && locationsFound && NetworkChangeReceiver.isConnected(context)) {
                postNotification(context);
            }
        }
    }

    /**
     * posts a notification to the device
     *
     * @param context the context
     */
    private void postNotification(Context context) {
        //get the users location
        final String location = Location.userLocation.latitude + "," + Location.userLocation.longitude;

        //generate the notifications pending intent
        Intent resultIntent = new Intent(context, SuggestMeetingActivity.class);
        resultIntent.putExtra("location", location);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 2,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;

        //generate the notification based on the API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notify_meeting)
                    .setContentTitle("Meeting Suggestions")
                    .setContentText("We have some meeting suggestions for you")
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notify_meeting)
                    .setContentTitle("Meeting Suggestions")
                    .setContentText("We have some meeting suggestions for you")
                    .setContentIntent(resultPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
        }

        //push the notification
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).notify(0, notification);
    }
}
