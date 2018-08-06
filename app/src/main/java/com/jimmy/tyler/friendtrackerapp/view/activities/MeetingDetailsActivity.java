package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.fab.DetailsFloatingActionButtonListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.view.adapters.FriendsAdapter;

import java.text.SimpleDateFormat;

import static com.jimmy.tyler.friendtrackerapp.R.id.fab;

/**
 * activity for handling meeting details
 */
public class MeetingDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker meetingMarker;

    private Meeting meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout to display
        setContentView(R.layout.activity_meeting_details);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set the floating action button click listener
        ((FloatingActionButton) findViewById(fab)).setOnClickListener(new DetailsFloatingActionButtonListener());

        //create the map view
        mapView = (MapView) findViewById(R.id.meeting_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        meeting = ModelImpl.getSingletonInstance().getMeeting(getIntent().getStringExtra("meetingId"));

        //generate the recycler view
        generateRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();

        //repopulate the fields with current meeting
        populateFields();

        //update the recycler view
        ((RecyclerView) findViewById(R.id.list_attendees)).getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //fixes fragment refresh bug
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the meeting details options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_details, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //edit meeting clicked
            case R.id.meeting_edit:
                //start the edit meeting activity
                Intent edit = new Intent(getApplicationContext(), MeetingEditActivity.class);
                edit.putExtra("MEETING", meeting.getMeetingId());
                edit.putExtra("MODE", MeetingEditActivity.EDIT);
                startActivity(edit);
                break;
            //remove meeting clicked
            case R.id.meeting_remove:
                ModelImpl.getSingletonInstance().removeMeeting(meeting.getMeetingId());
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //set the google map and disable some UI settings
        this.googleMap = googleMap;
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        //get the meeting location
        String latLng[] = meeting.getLocation().split(",");
        LatLng meetingLocation = new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1]));

        //add the meeting to the map
        meetingMarker = this.googleMap.addMarker(new MarkerOptions()
                .position(meetingLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_meeting)));

        //move the camera to the meeting
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingLocation, 18));

        //populate the fields
        populateFields();
    }

    /**
     * generate the recycler view to display the list of meetings
     */
    private void generateRecyclerView() {
        //create the recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_attendees);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setFocusable(false);
        findViewById(R.id.meeting_detail_scroll).setNestedScrollingEnabled(false);

        //create the friend adapter and add it to the recycler view
        FriendsAdapter friendAdapter = new FriendsAdapter(this, recyclerView, meeting.getFriends(), false);
        recyclerView.setAdapter(friendAdapter);
    }

    /**
     * populate the fields with the data found in the meeting object
     */
    private void populateFields() {
        //get an up to date version of the meeting
        meeting = ModelImpl.getSingletonInstance().getMeeting(meeting.getMeetingId());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mma");

        //populate the fields in the layout
        ((TextView) findViewById(R.id.meeting_detail_title)).setText(meeting.getTitle());
        ((TextView) findViewById(R.id.meeting_detail_location)).setText(meeting.getLocation());
        ((TextView) findViewById(R.id.meeting_detail_start)).setText(sdf.format(meeting.getStartTime().getTime()));
        ((TextView) findViewById(R.id.meeting_detail_end)).setText(sdf.format(meeting.getEndTime().getTime()));
        ((TextView) findViewById(R.id.meeting_detail_numfriends)).setText(String.format("%d attending", meeting.getNumberOfFriends()));

        //get the meeting location
        String latLng[] = meeting.getLocation().split(",");
        LatLng meetingLocation = new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1]));

        //check is the map is ready
        if (googleMap != null) {
            //move the marker and camera
            meetingMarker.setPosition(meetingLocation);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingLocation, 18));
        }
    }
}
