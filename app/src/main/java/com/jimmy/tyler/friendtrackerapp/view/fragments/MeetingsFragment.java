package com.jimmy.tyler.friendtrackerapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingEditActivity;
import com.jimmy.tyler.friendtrackerapp.view.adapters.MeetingsAdapter;
import com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts.AbstractFragment;

public class MeetingsFragment extends AbstractFragment {

    private RecyclerView recyclerView;

    public MeetingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meetings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Notify the recycler view to update
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enable menu options
        setHasOptionsMenu(true);

        // Add data to the recycler
        populateRecycler();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_lists, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                // Get the model
                Model model = ModelImpl.getSingletonInstance();
                // Sort the meeting changing the meeting format
                switch (model.getMeetingSortType()) {
                    case Model.SORT_MEETING_NL:
                        ((TextView) getActivity().findViewById(R.id.meetings_sort_info)).setText(R.string.sort_ln);
                        model.setMeetingSortType(Model.SORT_MEETING_LN);
                        break;
                    case Model.SORT_MEETING_LN:
                        ((TextView) getActivity().findViewById(R.id.meetings_sort_info)).setText(R.string.sort_nl);
                        model.setMeetingSortType(Model.SORT_MEETING_NL);
                        break;
                }
                model.sortMeetings();
                recyclerView.getAdapter().notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fabAction(View view) {
        // Create a new intent to open the meeting add
        Intent newMeeting = new Intent(getContext(), MeetingEditActivity.class);
        newMeeting.putExtra("MODE", MeetingEditActivity.ADD);
        startActivity(newMeeting);
    }

    /**
     * Populate the recycler view with meeting data
     */
    private void populateRecycler() {
        Model model = ModelImpl.getSingletonInstance();

        recyclerView = getActivity().findViewById(R.id.list_meetings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Update sorting information
        ((TextView) getActivity().findViewById(R.id.meetings_sort_info)).setText(R.string.sort_nl);
        model.setMeetingSortType(Model.SORT_MEETING_NL);
        model.sortMeetings();

        // Setup the meeting adapter and apply it to the recycler view
        MeetingsAdapter meetingsAdapter = new MeetingsAdapter(getActivity(), recyclerView, model.getMeetings());
        recyclerView.setAdapter(meetingsAdapter);
    }
}