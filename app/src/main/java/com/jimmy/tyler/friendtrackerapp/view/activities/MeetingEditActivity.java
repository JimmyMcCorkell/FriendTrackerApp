package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.fab.EditFloatingActionButtonListener;
import com.jimmy.tyler.friendtrackerapp.controller.meeting.MeetingDateTimeButtonListener;
import com.jimmy.tyler.friendtrackerapp.controller.meeting.MeetingSelectLocationListener;
import com.jimmy.tyler.friendtrackerapp.controller.meeting.MeetingTitleChangeListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.util.MeetingHolder;
import com.jimmy.tyler.friendtrackerapp.view.adapters.FriendsRemoveAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.jimmy.tyler.friendtrackerapp.R.id.fab;

/**
 * activity for handling editing meeting details
 */
public class MeetingEditActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int LOCATION_SELECT_REQUEST_CODE = 209;
    public static final int FRIEND_SELECT_REQUEST_CODE = 256;
    public static final int EDIT = 0;
    public static final int ADD = 1;

    private GoogleMap googleMap;
    private Marker meetingMarker;
    private MeetingSelectLocationListener mapEditListener;

    private Meeting meeting;

    private FriendsRemoveAdapter friendAdapter;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout to display
        setContentView(R.layout.activity_meeting_edit);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);

        //set the floating action button click listener
        ((FloatingActionButton) findViewById(fab)).setOnClickListener(new EditFloatingActionButtonListener(this));

        //create the map view
        MapView mapView = (MapView) findViewById(R.id.meeting_edit_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        //populate the fields
        populateFields();

        //generate the recycler view
        generateRecycler();
    }

    @Override
    public void onStart() {
        super.onStart();

        //repopulate the fields with current meeting
        updateAttendingField();

        //update the recycler view
        MeetingHolder.getSingletonInstance().recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //fixes fragment refresh bug
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the meeting edit options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Model model = ModelImpl.getSingletonInstance();

        switch (item.getItemId()) {
            //save details clicked
            case R.id.meeting_edit_save:
                MeetingHolder mh = MeetingHolder.getSingletonInstance();

                //validate the meeting title
                if (mh.title.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Supply A Meeting Title", Toast.LENGTH_LONG).show();
                    return super.onOptionsItemSelected(item);
                }

                //validate the dates
                if (mh.startTime.compareTo(mh.endTime) >= 0) {
                    Toast.makeText(getApplicationContext(), "Please Ensure End Date Is After Start Date", Toast.LENGTH_LONG).show();
                    return super.onOptionsItemSelected(item);
                }

                //check if the meeting is new or already exists
                if (mode == EDIT) {
                    //get the existing meeting and update its instance variables
                    String location = ((TextView) findViewById(R.id.meeting_edit_location)).getText().toString();
                    model.getMeeting(meeting.getMeetingId()).setTitle(mh.title);
                    model.getMeeting(meeting.getMeetingId()).setLocation(mh.location);
                    model.getMeeting(meeting.getMeetingId()).setStartTime(mh.startTime);
                    model.getMeeting(meeting.getMeetingId()).setEndTime(mh.endTime);
                    model.getMeeting(meeting.getMeetingId()).updateFriends(mh.friends);
                } else {
                    //add the meeting to the model
                    model.addMeeting(mh.title, mh.location, mh.friends, mh.startTime, mh.endTime);
                }

                //exit the activity
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //set the google map nad disable some UI settings
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setAllGesturesEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.setOnMapClickListener(mapEditListener);

        //get the meeting location
        String latLng[] = MeetingHolder.getSingletonInstance().location.split(",");
        LatLng meetingLocation = new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1]));

        //add the meeting to the map
        meetingMarker = this.googleMap.addMarker(new MarkerOptions()
                .position(meetingLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_meeting)));

        //move the camera to the meeting
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingLocation, 18));

        //update the location field in the layout
        ((TextView) findViewById(R.id.meeting_edit_location)).setText(MeetingHolder.getSingletonInstance().location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //select location result returned
            case LOCATION_SELECT_REQUEST_CODE:
                //check if the result is ok
                if (resultCode == Activity.RESULT_OK) {
                    //update the location field in the layout
                    ((TextView) findViewById(R.id.meeting_edit_location)).setText(MeetingHolder.getSingletonInstance().location);

                    //update the marker
                    updateMarker();
                }
                break;
            //select friend result returned
            case FRIEND_SELECT_REQUEST_CODE:
                //check if the result is ok
                if (resultCode == Activity.RESULT_OK) {
                    //update the recycler view
                    MeetingHolder.getSingletonInstance().recyclerView.getAdapter().notifyDataSetChanged();

                    //update the number of attendants
                    updateAttendingField();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * populate the fields with the data
     */
    private void populateFields() {
        mode = getIntent().getIntExtra("MODE", ADD);

        //get time field views
        TextView startTime = ((TextView) findViewById(R.id.meeting_edit_start));
        TextView endTime = ((TextView) findViewById(R.id.meeting_edit_end));

        findViewById(R.id.meeting_edit_scroll).setNestedScrollingEnabled(false);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mma");

        MeetingHolder mh = MeetingHolder.getSingletonInstance();

        //check if the meeting is new or already exists
        if (mode == EDIT) {
            meeting = ModelImpl.getSingletonInstance().getMeeting(getIntent().getStringExtra("MEETING"));

            //populate meeting holder with meeting instance variables
            mh.title = meeting.getTitle();
            mh.location = meeting.getLocation();
            mh.startTime = meeting.getStartTime();
            mh.endTime = meeting.getEndTime();
            mh.friends = meeting.getFriends();
        } else {
            //populate meeting holder with default values
            mh.title = "";

            if (getIntent().getStringExtra("location") != null) {
                mh.location = getIntent().getStringExtra("location");
            } else {
                mh.location = "-37.8106406,144.962854";
            }

            Calendar now = new GregorianCalendar();
            Calendar later = new GregorianCalendar();
            String defaultLength = PreferenceManager.getDefaultSharedPreferences(this).getString(getResources().getString(R.string.pref_meeting_default_length_key), "30");
            later.add(Calendar.MINUTE, Integer.parseInt(defaultLength));

            if (getIntent().getIntExtra("start", -1) != -1) {
                now.add(Calendar.SECOND, getIntent().getIntExtra("start", 0));
                later.add(Calendar.SECOND, getIntent().getIntExtra("start", 0));
            }

            mh.startTime = now;
            mh.endTime = later;

            mh.friends = new ArrayList<>();

            if (getIntent().getStringExtra("friend") != null) {
                mh.friends.add(ModelImpl.getSingletonInstance().getFriend(getIntent().getStringExtra("friend")));
            }
        }

        //add listeners
        mapEditListener = new MeetingSelectLocationListener(this);
        startTime.setOnClickListener(new MeetingDateTimeButtonListener(startTime, mh.startTime, true));
        endTime.setOnClickListener(new MeetingDateTimeButtonListener(endTime, mh.endTime, false));
        ((TextView) findViewById(R.id.meeting_edit_title)).addTextChangedListener(new MeetingTitleChangeListener());
        findViewById(R.id.meeting_edit_location).setOnClickListener(mapEditListener);

        //populate the fields in the layout
        ((TextView) findViewById(R.id.meeting_edit_title)).setText(mh.title);
        ((TextView) findViewById(R.id.meeting_edit_location)).setText(mh.location);
        startTime.setText(sdf.format(mh.startTime.getTime()));
        endTime.setText(sdf.format(mh.endTime.getTime()));

        ((EditText) findViewById(R.id.meeting_edit_title)).requestFocus();
    }

    /**
     * generate the recycler view to display the list of meetings
     */
    private void generateRecycler() {
        MeetingHolder mh = MeetingHolder.getSingletonInstance();

        //create the recycler view
        mh.recyclerView = (RecyclerView) findViewById(R.id.list_edit_attendees);
        mh.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mh.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mh.recyclerView.setFocusable(false);

        //create the friends adapter and add it to the recycler view
        friendAdapter = new FriendsRemoveAdapter(this, mh.friends);
        mh.recyclerView.setAdapter(friendAdapter);

        //update the number of attendants
        updateAttendingField();
    }

    /**
     * update the marker with the new location
     */
    private void updateMarker() {
        //check if the meeting marker is initialised
        if (meetingMarker != null) {
            //get the location and set the new position
            String location[] = MeetingHolder.getSingletonInstance().location.split(",");
            meetingMarker.setPosition(new LatLng(Float.parseFloat(location[0]), Float.parseFloat(location[1])));

            //move the camera to the new location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingMarker.getPosition(), 18));
        }
    }

    /**
     * update the number of attendants field
     */
    private void updateAttendingField() {
        //update the field in the layout
        ((TextView) findViewById(R.id.meeting_edit_numfriends)).setText(String.format("%d attending", MeetingHolder.getSingletonInstance().recyclerView.getAdapter().getItemCount()));
    }
}