package com.jimmy.tyler.friendtrackerapp.view.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.model.supportcode.DummyLocationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationService extends IntentService {

    private static final String LOG_TAG = "Location Service";

    public static final String UPDATE = "UPDATE";

    /**
     * Constructs a new location service with default name
     */
    public LocationService() {
        super("LocationService");
    }

    /**
     * Constructs a new location service with the given name
     *
     * @param name of the service
     */
    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Get the model
        Model model = ModelImpl.getSingletonInstance();
        DummyLocationService dls = DummyLocationService.getSingletonInstance(this);

        DateFormat df = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());

        while (true) {
            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getResources().getString(R.string.pref_friend_location_key), true)) {
                Log.i(LOG_TAG, "Checking friend locations..");

                List<DummyLocationService.FriendLocation> matched = dls.getFriendLocationsForTime(new Date(), 7, 0);

                // Update each friends location
                for (DummyLocationService.FriendLocation fl : matched) {
                    Log.i(LOG_TAG, fl.name);
                    for (Friend f : model.getFriends()) {
                        if (f.getName().equals(fl.name)) {
                            Log.i(LOG_TAG, "Updating location for " + f.getName() + "..");
                            f.setLocation(fl.latitude + "," + fl.longitude);
                            break;
                        }
                    }
                }

                // Broadcast the changes
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra(UPDATE, true);
                sendBroadcast(broadcastIntent);
            }

            String updateFrequency = PreferenceManager.getDefaultSharedPreferences(this).getString(getResources().getString(R.string.pref_friend_location_list_key), "1");

            // Sleep for user specified time
            try {
                Thread.sleep(Integer.parseInt(updateFrequency) * 60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
