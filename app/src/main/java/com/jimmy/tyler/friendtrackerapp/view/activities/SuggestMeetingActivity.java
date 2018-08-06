package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.view.adapters.SuggestAdapter;
import com.jimmy.tyler.friendtrackerapp.view.tasks.DistanceMatrixTask;

import java.util.List;

/**
 * activity for showing meeting suggestions based on distance matrix calculations
 */
public class SuggestMeetingActivity extends AppCompatActivity {

    /**
     * static inner class used to hold suggestion data for the recyclerView
     */
    public static class Suggestion {
        public String friendId, walkTimeText, midpoint;
        public int walkTimeValue;

        /**
         * primary constructor for instantiation
         *
         * @param friendId      the id of the friend
         * @param walkTimeText  the user-friendly version of walk time
         * @param midpoint      the meeting location
         * @param walkTimeValue the raw value of walk time
         */
        public Suggestion(String friendId, String walkTimeText, String midpoint, int walkTimeValue) {
            this.friendId = friendId;
            this.walkTimeText = walkTimeText;
            this.midpoint = midpoint;
            this.walkTimeValue = walkTimeValue;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggest_meeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);

        //get the uer location
        String userLocation = getIntent().getStringExtra("location");

        //start the networking task to gather distance matrix data
        DistanceMatrixTask networkTask = new DistanceMatrixTask(this, userLocation);
        networkTask.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //fixes fragment refresh bug
        onBackPressed();
        return true;
    }

    /**
     * populate the recycler with suggestion data
     *
     * @param suggestions the list of suggestions
     */
    public void populateRecycler(List<Suggestion> suggestions) {
        //get the recycler from the view
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.list_suggest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Start the suggestions adapter to manage the recycler view items
        SuggestAdapter suggestAdapter = new SuggestAdapter(this, recyclerView, suggestions);
        recyclerView.setAdapter(suggestAdapter);
    }
}
