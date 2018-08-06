package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.fab.DetailsFloatingActionButtonListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;

import java.text.SimpleDateFormat;

import static com.jimmy.tyler.friendtrackerapp.R.id.fab;

/**
 * activity for handling friend details
 */
public class FriendDetailsActivity extends AppCompatActivity {

    Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout to display
        setContentView(R.layout.activity_friend_details);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set the floating action button click listener
        ((FloatingActionButton) findViewById(fab)).setOnClickListener(new DetailsFloatingActionButtonListener());

        friend = (Friend) getIntent().getSerializableExtra("friend");

        //populate the fields
        populateFields();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //repopulate the fields with current data
        populateFields();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //fixes fragment refresh bug
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the friend details options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_details, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //edit friend clicked
            case R.id.friend_edit:
                //start the edit friend activity
                Intent intent = new Intent(FriendDetailsActivity.this, FriendEditActivity.class);
                intent.putExtra(FriendEditActivity.NEW_CONTACT, false);
                intent.putExtra(FriendEditActivity.CONTACT, friend);
                startActivity(intent);
                break;
            //remove friend clicked
            case R.id.friend_remove:
                //remove the friend and exit the activity
                ModelImpl.getSingletonInstance().removeFriend(friend.getFriendId());
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * populate the fields with the data found in the friend object
     */
    public void populateFields() {
        //get an up to date version of the friend
        friend = ModelImpl.getSingletonInstance().getFriend(friend.getFriendId());

        String fl = String.valueOf(friend.getName().charAt(0));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        //populate the fields in the layout
        ((ImageView) findViewById(R.id.friend_detail_photo)).setImageDrawable(TextDrawable.builder().buildRound(fl, friend.getPhotoColorId()));
        ((TextView) findViewById(R.id.friend_detail_title)).setText(friend.getName());
        ((TextView) findViewById(R.id.friend_detail_email)).setText(friend.getEmail());
        ((TextView) findViewById(R.id.friend_detail_birthday)).setText(sdf.format(friend.getBirthday().getTime()));

        if (friend.getLocation() == null) {
            ((TextView) findViewById(R.id.friend_detail_location)).setText(getResources().getString(R.string.friend_details_no_location));
        } else {
            ((TextView) findViewById(R.id.friend_detail_location)).setText(friend.getLocation());
        }
    }
}
