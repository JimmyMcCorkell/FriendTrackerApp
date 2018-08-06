package com.jimmy.tyler.friendtrackerapp.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts.AbstractFragment;

/**
 * fragment used to house the preference fragment [as needed to extend AbstractFragment]
 */
public class SettingsFragment extends AbstractFragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //show the preference fragment
        PrefFragment prefFragment = new PrefFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.prefFragment, prefFragment)
                .commit();
    }

    @Override
    public void fabAction(View view) {
        //EMPTY BODY AS FAB IS HIDDEN
    }

}
