package com.jimmy.tyler.friendtrackerapp.view.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.jimmy.tyler.friendtrackerapp.controller.map.InfoWindowListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.util.Location;
import com.jimmy.tyler.friendtrackerapp.util.Permissions;
import com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts.AbstractFragment;

import java.util.HashMap;
import java.util.Map;

import static com.jimmy.tyler.friendtrackerapp.R.id.map;
import static com.jimmy.tyler.friendtrackerapp.util.Permissions.PermissionType.LOCATION;

public class MapFragment extends AbstractFragment implements OnMapReadyCallback {

    private Model model;

    private GoogleMap googleMap;

    private int permissionRequestCount = 0;
    private boolean lRequested = false;

    private Marker userMarker = null;

    private Map<String, Marker> meetingMarkers;
    private Map<String, Marker> friendMarkers;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Update the map location center
        handleLocationRequest(false);
    }

    @Override
    public void fabAction(View view) {
        handleLocationRequest(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(false);

        model = ModelImpl.getSingletonInstance();
        meetingMarkers = new HashMap<>();
        friendMarkers = new HashMap<>();

        // Create the mapView
        MapView mapView = view.findViewById(map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Config UI settings for the map
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);

        // Add the map markers
        generateMapMarkers();

        // Set the map click listener
        this.googleMap.setOnInfoWindowClickListener(new InfoWindowListener((AppCompatActivity) getActivity()));
    }

    /**
     * Generates a stores the meeting and friend markers
     */
    private void generateMapMarkers() {
        // For each meeting generate a marker
        for (Meeting m : model.getMeetings()) {
            String latLng[] = m.getLocation().split(",");
            meetingMarkers.put(m.getMeetingId(), googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1])))
                    .title(m.getTitle())
                    .snippet("Meeting Id: " + m.getMeetingId())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_meeting))));
        }
        // For each friend generate a marker
        for (Friend f : model.getFriends()) {
            if (f.getLocation() != null) {
                String latLng[] = f.getLocation().split(",");
                String fl = String.valueOf(f.getName().charAt(0));

                // Create a drawable, of a background colour and inital
                Drawable drawable = TextDrawable.builder().buildRound(fl, f.getPhotoColorId());

                int width = drawable.getIntrinsicWidth();
                width = width > 0 ? width : 96; // Replaced the 1 by a 96
                int height = drawable.getIntrinsicHeight();
                height = height > 0 ? height : 96; // Replaced the 1 by a 96

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                // Add the marker
                friendMarkers.put(f.getFriendId(), googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1])))
                        .title(f.getName())
                        .snippet("Friend Id: " + f.getFriendId())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))));
            }
        }

        // Snap to the location of the first meeting
        LatLng location;
        if (model.getMeetings().isEmpty()) {
            location = new LatLng(-37.809520, 144.963857);
        } else {
            String latLng[] = model.getMeetings().get(model.getMeetings().size() - 1).getLocation().split(",");
            location = new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1]));
        }

        // Add the current location marker
        userMarker = googleMap.addMarker(new MarkerOptions()
                .position(location)
                .snippet("device")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_current)));
        userMarker.setVisible(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
    }

    /**
     * Updates the stored markers
     */
    public void updateMarkers() {
        // Update the meeting markers
        for (Meeting m : model.getMeetings()) {
            if (m.getLocation() != null) {
                String latLng[] = m.getLocation().split(",");
                Marker marker = meetingMarkers.get(m.getMeetingId());
                marker.setPosition(new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1])));
                marker.setTitle(m.getTitle());
            }
        }

        // Update the friend markers
        for (Friend f : model.getFriends()) {
            if (f.getLocation() != null) {
                String latLng[] = f.getLocation().split(",");
                Marker marker = friendMarkers.get(f.getFriendId());
                marker.setPosition(new LatLng(Float.parseFloat(latLng[0]), Float.parseFloat(latLng[1])));
                marker.setTitle(f.getName());
            }
        }
    }

    /**
     * Handle the location request
     *
     * @param fromFab depending on origin perform different actions
     */
    private void handleLocationRequest(boolean fromFab) {
        // If location permissions have been given the update the location
        if (Permissions.checkPermission((AppCompatActivity) getActivity(), LOCATION)) {
            if (!lRequested) {
                new Location((AppCompatActivity) getActivity(), this);
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userMarker.getPosition(), 18));
            }
        } else {
            // From fab needs to request permission
            if (fromFab) {
                Permissions.requestPermission((AppCompatActivity) getActivity(), LOCATION);
                return;
            }
            // Only allow every second press. To prevent spamming
            if (permissionRequestCount % 2 == 0) {
                Permissions.requestPermission((AppCompatActivity) getActivity(), LOCATION);
            }
            permissionRequestCount++;
        }
    }

    public void updateUserMarker(LatLng location) {
        userMarker.setVisible(true);
        userMarker.setPosition(location);
    }

    /**
     * Set the location requested
     *
     * @param lRequested the location requested
     */
    public void setLocationRequested(boolean lRequested) {
        this.lRequested = lRequested;
    }
}