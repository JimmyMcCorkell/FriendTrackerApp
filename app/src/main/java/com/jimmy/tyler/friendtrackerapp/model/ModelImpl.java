package com.jimmy.tyler.friendtrackerapp.model;

import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.util.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * the implementation class for the model interface
 */
public class ModelImpl implements Model {

    private static Model model = new ModelImpl();

    private List<Friend> friends;
    private List<Meeting> meetings;

    private int friendSortType = SORT_FRIEND_AZ;
    private int meetingSortType = SORT_MEETING_NL;

    /**
     * primary constructor for instantiation
     */
    private ModelImpl() {
        this.friends = new ArrayList<>();
        this.meetings = new ArrayList<>();
    }

    /**
     * get the singleton instance of the model
     *
     * @return the singleton instance of the model
     */
    public static Model getSingletonInstance() {
        return model;
    }

    @Override
    public boolean addFriend(String name, String email, Calendar birthday) {
        //generate a unique id
        String id;
        do {
            id = Utilities.getRandomString(20);
        } while (getFriend(id) != null);

        //sort the list if the insertion was successful
        if (friends.add(new FriendImpl(id, name, email, birthday))) {
            sortFriends();
            return true;
        }

        return false;
    }

    @Override
    public boolean addFriend(Friend friend) {
        if (getFriend(friend.getFriendId()) != null) {
            return false;
        }
        friends.add(friend);
        return true;
    }

    @Override
    public boolean removeFriend(String id) {
        //remove any instance of the friend from meeting friends lists
        for (Friend f : friends) {
            if (f.getFriendId().equals(id)) {
                for (Meeting m : meetings) {
                    m.removeFriend(id);
                }
                friends.remove(f);
                return true;
            }
        }

        return false;
    }

    @Override
    public Friend getFriend(String id) {
        //check if the friend is in the friends list
        for (Friend f : friends) {
            if (f.getFriendId().equals(id)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public void sortFriends() {
        switch (friendSortType) {
            //sort the friends A-Z by name
            case SORT_FRIEND_AZ:
                Collections.sort(friends, new Comparator<Friend>() {
                    @Override
                    public int compare(Friend f1, Friend f2) {
                        return f1.getName().compareTo(f2.getName());
                    }
                });
                break;
            //sort the friends Z-A by name
            case SORT_FRIEND_ZA:
                Collections.sort(friends, new Comparator<Friend>() {
                    @Override
                    public int compare(Friend f1, Friend f2) {
                        return f2.getName().compareTo(f1.getName());
                    }
                });
                break;
        }
    }

    @Override
    public int getFriendSortType() {
        return friendSortType;
    }

    @Override
    public void setFriendSortType(int type) {
        this.friendSortType = type;
    }

    @Override
    public boolean addMeeting(String title, String location, List<Friend> friends, Calendar startTime, Calendar endTime) {
        //generate a unique id
        String id;
        do {
            id = Utilities.getRandomString(20);
        } while (getMeeting(id) != null);

        //create the meeting
        Meeting meeting = new MeetingImpl(id, title, startTime, endTime);
        meeting.setLocation(location);

        for (Friend f : friends) {
            meeting.addFriend(f);
        }

        //sort the list if the insertion was sucessful
        if (meetings.add(meeting)) {
            sortMeetings();
            return true;
        }

        return false;
    }

    @Override
    public boolean addMeeting(Meeting meeting) {
        if (getMeeting(meeting.getMeetingId()) != null) {
            return false;
        }
        meetings.add(meeting);
        return true;
    }

    @Override
    public boolean removeMeeting(String id) {
        //remove the meeting if it exists
        for (Meeting m : meetings) {
            if (m.getMeetingId().equals(id)) {
                meetings.remove(m);
                return true;
            }
        }

        return false;
    }

    @Override
    public Meeting getMeeting(String id) {
        //check if the meeting is in the meetings list
        for (Meeting m : meetings) {
            if (m.getMeetingId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public void sortMeetings() {
        switch (meetingSortType) {
            //sort the meetings Next-Last by start time
            case SORT_MEETING_NL:
                Collections.sort(meetings, new Comparator<Meeting>() {
                    @Override
                    public int compare(Meeting m1, Meeting m2) {
                        return m1.getStartTime().compareTo(m2.getStartTime());
                    }
                });
                break;
            //sort the meetings Last-Next by start time
            case SORT_MEETING_LN:
                Collections.sort(meetings, new Comparator<Meeting>() {
                    @Override
                    public int compare(Meeting m1, Meeting m2) {
                        return m2.getStartTime().compareTo(m1.getStartTime());
                    }
                });
                break;
        }
    }

    @Override
    public int getMeetingSortType() {
        return meetingSortType;
    }

    @Override
    public void setMeetingSortType(int type) {
        this.meetingSortType = type;
    }

    @Override
    public void clearFriends() {
        friends.clear();
    }

    @Override
    public void clearMeetings() {
        meetings.clear();
    }
}
