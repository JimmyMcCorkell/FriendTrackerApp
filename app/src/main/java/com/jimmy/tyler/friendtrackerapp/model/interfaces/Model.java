package com.jimmy.tyler.friendtrackerapp.model.interfaces;

import java.util.Calendar;
import java.util.List;

/**
 * interface for the application model
 */
public interface Model {

    int SORT_FRIEND_AZ = 0;
    int SORT_FRIEND_ZA = 1;
    int SORT_MEETING_NL = 0;
    int SORT_MEETING_LN = 1;

    /**
     * add a friend to the friends list
     *
     * @param name     the friend name
     * @param email    the friend email
     * @param birthday the friend birthday
     * @return whether the insertion was successful
     */
    boolean addFriend(String name, String email, Calendar birthday);

    /**
     * add a friend to the friends list
     *
     * @param friend adds a friend object
     * @return whether the insertion was successful
     */
    boolean addFriend(Friend friend);

    /**
     * remove a friend from the friends list
     *
     * @param id the friend id
     * @return whether the removal was successful
     */
    boolean removeFriend(String id);

    /**
     * get a friend from the friends list
     *
     * @param id the friend id
     * @return the friend from the friends list
     */
    Friend getFriend(String id);

    /**
     * get the list of friends
     *
     * @return the list of friends
     */
    List<Friend> getFriends();

    /**
     * sort the friend list
     */
    void sortFriends();

    /**
     * get the current sort type of the friends list
     *
     * @return the sort type
     */
    int getFriendSortType();

    /**
     * set the current sort type of the friends list
     *
     * @param type the sort type
     */
    void setFriendSortType(int type);

    /**
     * add a meeting to the list of meetings
     *
     * @param title     the meeting title
     * @param location  the meeting location
     * @param friends   the meeting friends list
     * @param startTime the meeting start time
     * @param endTime   the meeting end time
     * @return whether the insertion was successful
     */
    boolean addMeeting(String title, String location, List<Friend> friends, Calendar startTime, Calendar endTime);

    /**
     * add a meeting to the list of meetings
     *
     * @param meeting Adds a meeting object
     * @return whether the insertion was successful
     */
    boolean addMeeting(Meeting meeting);

    /**
     * remove a meeting from the meetings list
     *
     * @param id the meeting id
     * @return whether the removal was successful
     */
    boolean removeMeeting(String id);

    /**
     * get a meeting from the meetings list
     *
     * @param id the meeting id
     * @return the meeting from the meetings list
     */
    Meeting getMeeting(String id);

    /**
     * get a meeting from the meetings list
     *
     * @return the meeting from the meetings list
     */
    List<Meeting> getMeetings();

    /**
     * sort the meetings list
     */
    void sortMeetings();

    /**
     * get the current sort type of the meetings list
     *
     * @return the sort type
     */
    int getMeetingSortType();

    /**
     * set the current sort type of the meetings list
     *
     * @param type the sort type
     */
    void setMeetingSortType(int type);

    /**
     * Clears the friends list
     */
    void clearFriends();

    /**
     * Clears the meeting list
     */
    void clearMeetings();
}
