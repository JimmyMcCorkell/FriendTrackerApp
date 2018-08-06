package com.jimmy.tyler.friendtrackerapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.util.Permissions;
import com.jimmy.tyler.friendtrackerapp.view.activities.FriendEditActivity;
import com.jimmy.tyler.friendtrackerapp.view.adapters.FriendsAdapter;
import com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts.AbstractFragment;

import static com.jimmy.tyler.friendtrackerapp.util.Permissions.PermissionType.CONTACT;

public class FriendsFragment extends AbstractFragment {

    private RecyclerView recyclerView;
    private boolean listOnly;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Refresh the recycler view on startup of the fragment
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enable the option menu
        setHasOptionsMenu(true);

        // In listonly mode remove all sorting
        if (listOnly) {
            getActivity().findViewById(R.id.friends_sort_layout).setVisibility(View.INVISIBLE);
        }

        // Fill the recycler view with data
        populateRecycler();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Only inflate the menu if not in listonly mode
        if (!listOnly) {
            inflater.inflate(R.menu.menu_fragment_lists, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // If the sort menuitem is clicked
            case R.id.action_sort:
                Model model = ModelImpl.getSingletonInstance();
                // Depending on the sorttype currently, with the type and update the display.
                switch (model.getFriendSortType()) {
                    case Model.SORT_FRIEND_AZ:
                        model.setFriendSortType(Model.SORT_FRIEND_ZA);
                        ((TextView) getActivity().findViewById(R.id.friends_sort_info)).setText(R.string.sort_za);
                        break;
                    case Model.SORT_FRIEND_ZA:
                        ((TextView) getActivity().findViewById(R.id.friends_sort_info)).setText(R.string.sort_az);
                        model.setFriendSortType(Model.SORT_FRIEND_AZ);
                        break;
                }
                // Sort the friends and update recycler view
                model.sortFriends();
                recyclerView.getAdapter().notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fabAction(View view) {
        // Either request permissions for contact or start the contact picker
        if (Permissions.checkPermission((AppCompatActivity) getActivity(), CONTACT)) {
            Intent intent = new Intent(getActivity(), FriendEditActivity.class);
            intent.putExtra(FriendEditActivity.NEW_CONTACT, true);
            getActivity().startActivity(intent);
        } else {
            Permissions.requestPermission((AppCompatActivity) getActivity(), CONTACT);
        }
    }

    private void populateRecycler() {
        // Get the model and update the recyclerView with the data.
        Model model = ModelImpl.getSingletonInstance();

        recyclerView = getActivity().findViewById(R.id.list_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Update the sorting of friends
        ((TextView) getActivity().findViewById(R.id.friends_sort_info)).setText(R.string.sort_az);
        model.setFriendSortType(Model.SORT_FRIEND_AZ);
        model.sortFriends();

        // Start the friends Adapter to manage the recycler view items
        FriendsAdapter friendAdapter = new FriendsAdapter(getActivity(), recyclerView, model.getFriends(), listOnly);
        recyclerView.setAdapter(friendAdapter);
    }

    /**
     * Enables list only mode
     */
    public void setListOnlyMode() {
        listOnly = true;
    }
}