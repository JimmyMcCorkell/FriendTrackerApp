package com.jimmy.tyler.friendtrackerapp.model.database;


import android.provider.BaseColumns;

public class DbTables {

    public static class FriendEntry implements BaseColumns {
        public static final String TABLE_NAME = "friend";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_BIRTHDAY = "birthday";
        public static final String COLUMN_NAME_LOCATION = "location";
    }

    public static class MeetingEntry implements BaseColumns {
        public static final String TABLE_NAME = "meeting";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_NOTIFIED = "notified";
    }

    public static class MeetingFriendEntry implements BaseColumns {
        public static final String TABLE_NAME = "meeting_friend";
        public static final String COLUMN_NAME_FRIEND_ID = "friend_id";
        public static final String COLUMN_NAME_MEETING_ID = "meeting_id";
    }
}
