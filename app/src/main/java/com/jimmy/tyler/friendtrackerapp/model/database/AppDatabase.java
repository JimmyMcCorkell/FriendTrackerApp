package com.jimmy.tyler.friendtrackerapp.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jimmy.tyler.friendtrackerapp.model.FriendImpl;
import com.jimmy.tyler.friendtrackerapp.model.MeetingImpl;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;

import java.util.Calendar;


public class AppDatabase {

    public static final String TAG_DB = "DB";

    private static AppDBHelper db;
    private static SQLiteDatabase dbWrite;
    private static SQLiteDatabase dbRead;

    /**
     * Inits the database, context is required for this
     *
     * @param context
     */
    public static void initDatabase(Context context) {
        db = new AppDBHelper(context);
        Log.i(TAG_DB, "Init");
    }

    /**
     * Closes the database conserving memory
     */
    public static void closeDatabase() {
        db.close();
    }

    /**
     * Loads the database into the model sigleton
     */
    public static void loadDatabaseIntoModel() {
        dbRead = db.getReadableDatabase();

        // Clear the model out
        ModelImpl.getSingletonInstance().clearFriends();
        ModelImpl.getSingletonInstance().clearMeetings();

        // Get and then loop over all objects in the Friends table
        Cursor friendCursor = dbRead.rawQuery("SELECT * FROM " + DbTables.FriendEntry.TABLE_NAME, null);
        try {
            if (friendCursor.moveToFirst()) {
                do {
                    Calendar birthday = Calendar.getInstance();
                    birthday.setTimeInMillis(friendCursor.getLong(friendCursor.getColumnIndex(DbTables.FriendEntry.COLUMN_NAME_BIRTHDAY)));

                    // Create a friend instance from the database values
                    Friend friend = new FriendImpl(friendCursor.getString(friendCursor.getColumnIndex(DbTables.FriendEntry._ID)),
                            friendCursor.getString(friendCursor.getColumnIndex(DbTables.FriendEntry.COLUMN_NAME_NAME)),
                            friendCursor.getString(friendCursor.getColumnIndex(DbTables.FriendEntry.COLUMN_NAME_EMAIL)), birthday);
                    friend.setLocation(friendCursor.getString(friendCursor.getColumnIndex(DbTables.FriendEntry.COLUMN_NAME_LOCATION)));

                    // Add the friend to the model
                    ModelImpl.getSingletonInstance().addFriend(friend);
                    Log.i(TAG_DB, "Loading friend: " + friend.getFriendId());
                } while (friendCursor.moveToNext());
            }
        } finally {
            friendCursor.close();
        }

        // Get and then loop over all objects in the meeting table
        Cursor meetingCursor = dbRead.rawQuery("SELECT * FROM " + DbTables.MeetingEntry.TABLE_NAME, null);
        try {
            if (meetingCursor.moveToFirst()) {
                do {
                    // Convert the long to a calendar
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    start.setTimeInMillis(meetingCursor.getLong(meetingCursor.getColumnIndex(DbTables.MeetingEntry.COLUMN_NAME_START_TIME)));
                    end.setTimeInMillis(meetingCursor.getLong(meetingCursor.getColumnIndex(DbTables.MeetingEntry.COLUMN_NAME_END_TIME)));

                    // Create the meeting object
                    MeetingImpl meeting = new MeetingImpl(meetingCursor.getString(meetingCursor.getColumnIndex(DbTables.MeetingEntry._ID)),
                            meetingCursor.getString(meetingCursor.getColumnIndex(DbTables.MeetingEntry.COLUMN_NAME_TITLE)), start, end);

                    if (meetingCursor.getInt(meetingCursor.getColumnIndex(DbTables.MeetingEntry.COLUMN_NAME_NOTIFIED)) == 1) {
                        meeting.setNotified();
                    }

                    // Set the location and add the meeting to the model
                    meeting.setLocation(meetingCursor.getString(meetingCursor.getColumnIndex(DbTables.MeetingEntry.COLUMN_NAME_LOCATION)));
                    ModelImpl.getSingletonInstance().addMeeting(meeting);

                    Log.i(TAG_DB, "Loading meeting: " + meeting.getMeetingId());
                } while (meetingCursor.moveToNext());
            }
        } finally {
            meetingCursor.close();
        }

        // Get and then loop over all objects in the friend meeting table
        Cursor meetingFriendCursor = dbRead.rawQuery("SELECT * FROM " + DbTables.MeetingFriendEntry.TABLE_NAME, null);
        try {
            if (meetingFriendCursor.moveToFirst()) {
                do {
                    // Find a meeting and add the friend id
                    String meetingID = meetingFriendCursor.getString(meetingFriendCursor.getColumnIndex(DbTables.MeetingFriendEntry.COLUMN_NAME_MEETING_ID));
                    Friend friend = ModelImpl.getSingletonInstance().getFriend(meetingFriendCursor.getString(meetingFriendCursor.getColumnIndex(DbTables.MeetingFriendEntry.COLUMN_NAME_FRIEND_ID)));
                    ModelImpl.getSingletonInstance().getMeeting(meetingID).addFriend(friend);

                    Log.i(TAG_DB, "Loading friend: " + friend.getFriendId() + " into meeting: " + meetingID);
                } while (meetingFriendCursor.moveToNext());
            }
        } finally {
            meetingFriendCursor.close();
        }

        Log.i(TAG_DB, "Populating Database");
    }

    /**
     * Saves the model to the database
     */
    public static void saveModelToDatabase() {
        dbWrite = db.getWritableDatabase();

        // Clear all the database tables
        dbWrite.delete(DbTables.FriendEntry.TABLE_NAME, null, null);
        dbWrite.delete(DbTables.MeetingEntry.TABLE_NAME, null, null);
        dbWrite.delete(DbTables.MeetingFriendEntry.TABLE_NAME, null, null);

        for (Friend friend : ModelImpl.getSingletonInstance().getFriends()) {
            ContentValues f = new ContentValues();
            f.put(DbTables.FriendEntry._ID, friend.getFriendId());
            f.put(DbTables.FriendEntry.COLUMN_NAME_NAME, friend.getName());
            f.put(DbTables.FriendEntry.COLUMN_NAME_EMAIL, friend.getEmail());
            f.put(DbTables.FriendEntry.COLUMN_NAME_BIRTHDAY, friend.getBirthday().getTimeInMillis());
            f.put(DbTables.FriendEntry.COLUMN_NAME_LOCATION, friend.getLocation());
            Log.i(TAG_DB, "Saving friend: " + friend.getFriendId());
            // Add friend to DB
            dbWrite.insert(DbTables.FriendEntry.TABLE_NAME, null, f);
        }

        for (Meeting meeting : ModelImpl.getSingletonInstance().getMeetings()) {
            ContentValues m = new ContentValues();
            m.put(DbTables.MeetingEntry._ID, meeting.getMeetingId());
            m.put(DbTables.MeetingEntry.COLUMN_NAME_TITLE, meeting.getTitle());
            m.put(DbTables.MeetingEntry.COLUMN_NAME_START_TIME, meeting.getStartTime().getTimeInMillis());
            m.put(DbTables.MeetingEntry.COLUMN_NAME_END_TIME, meeting.getEndTime().getTimeInMillis());
            m.put(DbTables.MeetingEntry.COLUMN_NAME_LOCATION, meeting.getLocation());
            m.put(DbTables.MeetingEntry.COLUMN_NAME_NOTIFIED, meeting.getNotified() ? 1 : 0);
            Log.i(TAG_DB, "Saving meeting: " + meeting.getMeetingId());
            // Add friend to DB
            dbWrite.insert(DbTables.MeetingEntry.TABLE_NAME, null, m);

            // Save all the friends from the meeting
            for (Friend friend : meeting.getFriends()) {
                ContentValues f = new ContentValues();
                f.put(DbTables.MeetingFriendEntry.COLUMN_NAME_FRIEND_ID, friend.getFriendId());
                f.put(DbTables.MeetingFriendEntry.COLUMN_NAME_MEETING_ID, meeting.getMeetingId());

                dbWrite.insert(DbTables.MeetingFriendEntry.TABLE_NAME, null, f);
                Log.i(TAG_DB, "Saving friend: " + friend.getFriendId() + " to meeting: " + meeting.getMeetingId());
            }
        }

        Log.i(TAG_DB, "Saving Model To Database");
    }
}
