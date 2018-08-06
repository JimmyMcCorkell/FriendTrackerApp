package com.jimmy.tyler.friendtrackerapp.model.interfaces;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * interface for an application meeting
 */
public interface Meeting extends Serializable {

    /**
     * add a friend to the attending friends
     *
     * @param friend the friend to add
     * @return whether the insertion was successful
     */
    boolean addFriend(Friend friend);

    /**
     * remove a friend from the attending friends
     *
     * @param id the id of the friend to remove
     * @return whether the removal was successful
     */
    boolean removeFriend(String id);

    /**
     * update the attending friends with a new list
     *
     * @param friends the new friends list
     * @return whether the replacement was successful
     */
    boolean updateFriends(List<Friend> friends);

    /**
     * set the meeting title
     *
     * @param title the new title
     */
    void setTitle(String title);

    /**
     * set the meeting start time
     *
     * @param startTime the new start time
     * @return whether the new start date is valid
     */
    boolean setStartTime(Calendar startTime);

    /**
     * set the meeting end time
     *
     * @param endTime the new end time
     * @return whether the new end time is valid
     */
    boolean setEndTime(Calendar endTime);

    /**
     * set the meeting location
     *
     * @param location the new location
     */
    void setLocation(String location);

    /**
     * set that the meeting notification has been sent
     */
    void setNotified();

    /**
     * get the meeting id
     *
     * @return the meeting id
     */
    String getMeetingId();

    /**
     * get the meeting title
     *
     * @return the meeting title
     */
    String getTitle();

    /**
     * get the meeting start time
     *
     * @return the meeting start time
     */
    Calendar getStartTime();

    /**
     * get the meeting end time
     *
     * @return the meeting end time
     */
    Calendar getEndTime();

    /**
     * get the list of attending friends
     *
     * @return the list of attending friends
     */
    List<Friend> getFriends();

    /**
     * get the number of attending friends
     *
     * @return the number of attending friends
     */
    int getNumberOfFriends();

    /**
     * get the meeting location
     *
     * @return the meeting location
     */
    String getLocation();

    /**
     * get whether the meeting notification has been sent
     *
     * @return whether the notification has been sent
     */
    boolean getNotified();
}
