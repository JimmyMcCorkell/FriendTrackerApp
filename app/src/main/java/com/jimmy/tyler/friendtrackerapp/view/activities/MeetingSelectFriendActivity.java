package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.view.fragments.FriendsFragment;

/**
 * activity for handling selection of friends inside a meeting
 */
public class MeetingSelectFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout to display
        setContentView(R.layout.activity_meeting_select_friend);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);

        //send the friends fragment to the layout
        FriendsFragment ff = new FriendsFragment();
        ff.setListOnlyMode();
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.fragmentContent, ff).commit();
    }
}
