package com.jimmy.tyler.friendtrackerapp.model.supportcode;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

public class TestLocationService {

    private static final String LOG_TAG = DummyLocationService.class.getName();

    // call this method to run simple hard coded test
    public static void test(Context context) {
        DummyLocationService dummyLocationService = DummyLocationService.getSingletonInstance(context);

        Log.i(LOG_TAG, "File Contents:");
        dummyLocationService.logAll();
        List<DummyLocationService.FriendLocation> matched = null;
        try {
            // 2 mins either side of 9:46:30 AM
            matched = dummyLocationService.getFriendLocationsForTime(DateFormat.getTimeInstance(
                    DateFormat.MEDIUM).parse("9:46:30 AM"), 2, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "Matched Query:");
        dummyLocationService.log(matched);
    }
}
