package com.jimmy.tyler.friendtrackerapp.model;

import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * the implementation class for the meeting interface
 */
public class MeetingImpl implements Meeting {

    private String meetingId;
    private String title, location;
    boolean notified;
    private Calendar startTime, endTime;
    private List<Friend> friends;

    /**
     * primary constructor for instantiation
     *
     * @param id        the meeting id
     * @param title     the meeting title
     * @param startTime the meeting start time
     * @param endTime   the meeting end time
     */
    public MeetingImpl(String id, String title, Calendar startTime, Calendar endTime) {
        this.meetingId = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;

        this.location = null;
        this.notified = false;

        this.friends = new ArrayList<>();
    }

    @Override
    public boolean addFriend(Friend friend) {
        //check that friend isn't already in the list
        if (!friends.contains(friend)) {
            friends.add(friend);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeFriend(String id) {
        //check that the friend is in the list
        for (Friend f : friends) {
            if (f.getFriendId().equals(id)) {
                friends.remove(f);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateFriends(List<Friend> friends) {
        this.friends = friends;
        return true;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean setStartTime(Calendar startTime) {
        //check that the start time is before the end time
        if (startTime.compareTo(endTime) < 0) {
            this.startTime = startTime;
        }

        return false;
    }

    @Override
    public boolean setEndTime(Calendar endTime) {
        //check that the end time is after the start time
        if (endTime.compareTo(startTime) > 0) {
            this.endTime = endTime;
        }

        return false;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setNotified() {
        notified = true;
    }

    @Override
    public String getMeetingId() {
        return meetingId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    @Override
    public Calendar getEndTime() {
        return endTime;
    }

    @Override
    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public int getNumberOfFriends() {
        return friends.size();
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public boolean getNotified() {
        return notified;
    }
}