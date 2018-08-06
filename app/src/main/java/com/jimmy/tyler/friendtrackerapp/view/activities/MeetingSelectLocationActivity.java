package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.map.OnMarkerDragListener;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.util.MeetingHolder;

import java.util.List;

/**
 * activity for handling selection of location inside a meeting
 */
public class MeetingSelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    Marker meetingMarker;

    List<Friend> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout to display
        setContentView(R.layout.activity_meeting_select_location);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        friends = MeetingHolder.getSingletonInstance().friends;

        //create the map view
        MapView mapView = (MapView) findViewById(R.id.map_edit);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
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
        switch (item.getItemId()) {
            //edit meeting clicked
            case R.id.meeting_edit_save:
                //set the result
                String location = meetingMarker.getPosition().latitude + "," + meetingMarker.getPosition().longitude;
                MeetingHolder.getSingletonInstance().location = location;
                setResult(Activity.RESULT_OK);

                //exit the activity
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //set the google map and some UI settings
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);

        //generate al the map markers to be displayed on the map
        generateMapMarkers();

        //move the camera to the meeting
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingMarker.getPosition(), 18));
    }

    /**
     * generate the map markers for the map
     */
    private void generateMapMarkers() {
        //get the meeting location
        String location[] = MeetingHolder.getSingletonInstance().location.split(",");
        LatLng locationLatLng = new LatLng(Float.parseFloat(location[0]), Float.parseFloat(location[1]));

        //add the meeting to the map
        meetingMarker = googleMap.addMarker(new MarkerOptions()
                .position(locationLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_meeting))
                .draggable(true));

        googleMap.setOnMarkerDragListener(new OnMarkerDragListener(meetingMarker));

        //add the friends to the map
        for (Friend f : friends) {
            if (f.getLocation() != null) {
                String latLng[] = f.getLocation().split(",");
                String fl = String.valueOf(f.getName().charAt(0));

                Drawable drawable = TextDrawable.builder().buildRound(fl, f.getPhotoColorId());

                int width = drawable.getIntrinsicWidth();
                width = width > 0 ? width : 96; // Replaced the 1 by a 96
                int height = drawable.getIntrinsicHeight();
                height = height > 0 ? height : 96; // Replaced the 1 by a 96

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1])))
                        .title(f.getName())
                        .snippet("Friend Id: " + f.getFriendId())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            }
        }
    }
}
