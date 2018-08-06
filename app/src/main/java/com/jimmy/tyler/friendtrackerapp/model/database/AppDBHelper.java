package com.jimmy.tyler.friendtrackerapp.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "finder.db";

    private static final String SQL_FRIEND_TABLE =
            "CREATE TABLE " + DbTables.FriendEntry.TABLE_NAME + " (" +
                    DbTables.FriendEntry._ID + " TEXT PRIMARY KEY," +
                    DbTables.FriendEntry.COLUMN_NAME_NAME + " TEXT," +
                    DbTables.FriendEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    DbTables.FriendEntry.COLUMN_NAME_BIRTHDAY + " LONG," +
                    DbTables.FriendEntry.COLUMN_NAME_LOCATION + " TEXT)";

    private static final String SQL_MEETING_FRIEND_TABLE =
            "CREATE TABLE " + DbTables.MeetingFriendEntry.TABLE_NAME + " (" +
                    DbTables.MeetingFriendEntry._ID + " INTEGER PRIMARY KEY," +
                    DbTables.MeetingFriendEntry.COLUMN_NAME_FRIEND_ID + " TEXT," +
                    DbTables.MeetingFriendEntry.COLUMN_NAME_MEETING_ID + " TEXT)";

    private static final String SQL_MEETING_TABLE =
            "CREATE TABLE " + DbTables.MeetingEntry.TABLE_NAME + " (" +
                    DbTables.MeetingEntry._ID + " TEXT PRIMARY KEY," +
                    DbTables.MeetingEntry.COLUMN_NAME_TITLE + " TEXT," +
                    DbTables.MeetingEntry.COLUMN_NAME_START_TIME + " LONG," +
                    DbTables.MeetingEntry.COLUMN_NAME_END_TIME + " LONG," +
                    DbTables.MeetingEntry.COLUMN_NAME_LOCATION + " TEXT," +
                    DbTables.MeetingEntry.COLUMN_NAME_NOTIFIED + " INTEGER)";

    private static final String SQL_DELETE_FRIEND_TABLE =
            "DROP TABLE IF EXISTS " + DbTables.FriendEntry.TABLE_NAME;

    private static final String SQL_DELETE_MEETING_TABLE =
            "DROP TABLE IF EXISTS " + DbTables.MeetingEntry.TABLE_NAME;

    private static final String SQL_DELETE_MEETING_FRIEND_TABLE =
            "DROP TABLE IF EXISTS " + DbTables.MeetingFriendEntry.TABLE_NAME;

    /**
     * Constructs a new db helper
     *
     * @param context
     */
    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creates the SQL tables
        sqLiteDatabase.execSQL(SQL_FRIEND_TABLE);
        sqLiteDatabase.execSQL(SQL_MEETING_TABLE);
        sqLiteDatabase.execSQL(SQL_MEETING_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Deletes then recreates the SQL tables
        sqLiteDatabase.execSQL(SQL_DELETE_FRIEND_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_MEETING_FRIEND_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_MEETING_TABLE);
        onCreate(sqLiteDatabase);
    }

}
