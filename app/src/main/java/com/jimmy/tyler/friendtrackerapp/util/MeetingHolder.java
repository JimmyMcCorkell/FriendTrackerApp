package com.jimmy.tyler.friendtrackerapp.util;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;

import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;

import java.util.Calendar;
import java.util.List;

/**
 * utility class to hold temporary meeting data and retain over activity changes
 */
public class MeetingHolder {

    @SuppressLint("StaticFieldLeak")
    private static MeetingHolder singleton = new MeetingHolder();

    /**
     * get the singleton instance of the meeting holder
     *
     * @return the singleton instance of the meeting holder
     */
    public static MeetingHolder getSingletonInstance() {
        return singleton;
    }

    public String title, location;
    public Calendar startTime, endTime;
    public List<Friend> friends;
    public RecyclerView recyclerView;
}
