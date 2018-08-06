package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.fab.MainActivityFloatingActionButtonListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.database.AppDatabase;
import com.jimmy.tyler.friendtrackerapp.util.Location;
import com.jimmy.tyler.friendtrackerapp.util.Utilities;
import com.jimmy.tyler.friendtrackerapp.view.fragments.AboutFragment;
import com.jimmy.tyler.friendtrackerapp.view.fragments.FriendsFragment;
import com.jimmy.tyler.friendtrackerapp.view.fragments.MapFragment;
import com.jimmy.tyler.friendtrackerapp.view.fragments.MeetingsFragment;
import com.jimmy.tyler.friendtrackerapp.view.fragments.SettingsFragment;
import com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts.AbstractFragment;
import com.jimmy.tyler.friendtrackerapp.view.receivers.ContextReceiver;
import com.jimmy.tyler.friendtrackerapp.view.receivers.LocationReceiver;
import com.jimmy.tyler.friendtrackerapp.view.receivers.NetworkChangeReceiver;
import com.jimmy.tyler.friendtrackerapp.view.receivers.NotifyReceiver;
import com.jimmy.tyler.friendtrackerapp.view.services.LocationService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LocationReceiver locationReceiver;
    private Intent locationService;

    private ContextReceiver contextReceiver = null;
    private NotifyReceiver notifyReceiver = null;

    private SparseArray<AbstractFragment> fragments;
    private MenuItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init the database
        AppDatabase.initDatabase(getApplicationContext());

        // Load the database into the model
        AppDatabase.loadDatabaseIntoModel();

        //inflate the layout to display
        setContentView(R.layout.activity_main);

        //generate the fragments used in the activity
        generateFragments();

        //generate the location service for grabbing dummy locations
        generateServices();

        //start the contextual service
        if (contextReceiver == null) {
            contextReceiver = new ContextReceiver(this);
        }

        if (notifyReceiver == null) {
            notifyReceiver = new NotifyReceiver(this);
        }

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check if this is the first creation of the activity
        if (savedInstanceState == null) {
            //set the current item and title to the map
            currentItem = ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_map);
            currentItem.setChecked(true);
            setTitle(getResources().getString(R.string.title_map));

            //replace the layout with the initial fragment
            AbstractFragment fragment = fragments.get(R.id.nav_map);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(fragment.getFragmentName())
                    .replace(R.id.fragmentContent, fragment)
                    .commit();
            animateFab(fragment);
        }

        //set the floating action button click listener
        findViewById(R.id.fab).setOnClickListener(new MainActivityFloatingActionButtonListener(getSupportFragmentManager()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //check if the drawer is open
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //close the drawer
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager manager = getSupportFragmentManager();

            //check if this is the initial fragment
            if (manager.getBackStackEntryCount() > 1) {
                //get the previous fragment and display it
                manager.popBackStackImmediate();
                AbstractFragment fragment = (AbstractFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContent);
                currentItem = ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(fragments.keyAt(fragments.indexOfValue(fragment)));
                currentItem.setChecked(true);
                setTitle(getResources().getString(fragment.getStringId()));
                animateFab(fragment);
            } else {
                //exit the activity
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_suggest:
                if (!NetworkChangeReceiver.isConnected(this)) {
                    Snackbar.make(findViewById(R.id.content_main), "No internet connection. Try again soon.", Snackbar.LENGTH_SHORT)
                            .show();
                } else if (Location.userLocation == null) {
                    Snackbar.make(findViewById(R.id.content_main), "Current location unknown. Try again soon.", Snackbar.LENGTH_SHORT)
                            .show();
                } else if (ModelImpl.getSingletonInstance().getFriends().isEmpty()) {
                    Snackbar.make(findViewById(R.id.content_main), "No friends found in friends list.", Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    final String location = Location.userLocation.latitude + "," + Location.userLocation.longitude;

                    Intent intent = new Intent(this, SuggestMeetingActivity.class);
                    intent.putExtra("location", location);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //check if the fragment is already shown
        if (!item.isChecked()) {
            //display the fragment associated with the menu item
            AbstractFragment fragment = fragments.get(item.getItemId());
            currentItem = item;
            setTitle(fragment.getFragmentName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(fragment.getFragmentName())
                    .replace(R.id.fragmentContent, fragment)
                    .commit();
            animateFab(fragment);
        }

        //close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the database
        AppDatabase.closeDatabase();
        //clean up any services used in the background
        unregisterReceiver(locationReceiver);
        stopService(locationService);
    }

    /**
     * generates the fragments array used in this activity
     */
    private void generateFragments() {
        fragments = new SparseArray<>();

        //add the new fragments to the array
        fragments.append(R.id.nav_map, AbstractFragment.newInstance(new MapFragment(), getResources().getString(R.string.title_map), R.drawable.ic_location, R.string.title_map));
        fragments.append(R.id.nav_meetings, AbstractFragment.newInstance(new MeetingsFragment(), getResources().getString(R.string.title_meetings), R.drawable.ic_add_meeting, R.string.title_meetings));
        fragments.append(R.id.nav_friends, AbstractFragment.newInstance(new FriendsFragment(), getResources().getString(R.string.title_friends), R.drawable.ic_add_friend, R.string.title_friends));
        fragments.append(R.id.nav_about, AbstractFragment.newInstance(new AboutFragment(), getResources().getString(R.string.title_about), R.drawable.ic_about, R.string.title_about));
        fragments.append(R.id.nav_settings, AbstractFragment.newInstance(new SettingsFragment(), getResources().getString(R.string.title_settings), R.drawable.ic_settings, R.string.title_settings));
    }

    /**
     * generate the location service used to get location data from dummy_data
     */
    private void generateServices() {
        //create an intent filter to filter the broadcast responses
        IntentFilter locationFilter = new IntentFilter(LocationReceiver.LOCATION_RESPONSE);
        locationFilter.addCategory(Intent.CATEGORY_DEFAULT);

        //create the location receiver
        locationReceiver = new LocationReceiver((MapFragment) fragments.get(R.id.nav_map));
        registerReceiver(locationReceiver, locationFilter);

        //create the location service intent and start the service
        locationService = new Intent(this, LocationService.class);
        startService(locationService);

        IntentFilter networkFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(new NetworkChangeReceiver(), networkFilter);
    }

    /**
     * handle whether the fab needs to be displayed for the new fragment
     *
     * @param fragment the fragment to be displayed
     */
    private void animateFab(AbstractFragment fragment) {
        switch (currentItem.getGroupId()) {
            //the fab needs to be shown
            case R.id.navgroup_main:
                Utilities.animateFab(this, fragment.getFabDrawableId(), true);
                break;
            //the fab needs to be hidden
            case R.id.navgroup_secondary:
                Utilities.animateFab(this, fragment.getFabDrawableId(), false);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        // Save the model into the database
        AppDatabase.saveModelToDatabase();
        super.onStop();
    }

}