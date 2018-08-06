package com.jimmy.tyler.friendtrackerapp.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.jimmy.tyler.friendtrackerapp.R;

/**
 * fragment that shows the preferences used bhy the application
 */
public class PrefFragment extends PreferenceFragmentCompat {

    /**
     * primary constructor for instantiation
     */
    public PrefFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add the preferences found in the pref_general.xml
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
