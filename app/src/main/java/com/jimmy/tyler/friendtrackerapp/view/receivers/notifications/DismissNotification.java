package com.jimmy.tyler.friendtrackerapp.view.receivers.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * receiver that handles the dismissing of notifications from the drawer
 */
public class DismissNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //get the notification id
        int id = intent.getIntExtra("id", -1);

        //cancel the notification
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).cancel(id);

        //hide the notification drawer
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
