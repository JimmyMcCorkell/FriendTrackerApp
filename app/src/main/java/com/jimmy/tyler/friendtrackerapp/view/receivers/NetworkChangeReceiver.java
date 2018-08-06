package com.jimmy.tyler.friendtrackerapp.view.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.util.Location;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String TAG = "Network_Change";

    private boolean alreadyConnected;

    public NetworkChangeReceiver() {
        this.alreadyConnected = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context) && !alreadyConnected) {
            Log.i(TAG, "Connected");
            alreadyConnected = true;

            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.pref_context_key), true)) {

                boolean locationsFound = false;

                for (Friend f : ModelImpl.getSingletonInstance().getFriends()) {
                    if (f.getLocation() != null) {
                        locationsFound = true;
                    }
                }

                if (Location.userLocation != null && locationsFound) {
                    Intent broadcastIntent = new Intent(context, ContextReceiver.class);
                    context.sendBroadcast(broadcastIntent);
                }
            }
        } else {
            Log.i(TAG, "Disconnected");
            alreadyConnected = false;
        }
    }

    /**
     * Returns if the network is available
     *
     * @param context the context
     * @return whether the network is connected
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                return true;
            }
        }

        return false;
    }
}
