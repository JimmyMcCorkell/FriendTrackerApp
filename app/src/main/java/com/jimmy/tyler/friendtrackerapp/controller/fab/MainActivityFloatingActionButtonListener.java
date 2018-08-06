package com.jimmy.tyler.friendtrackerapp.controller.fab;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.view.fragments.abstracts.AbstractFragment;

/**
 * controller that handles the main activity floating action button
 */
public class MainActivityFloatingActionButtonListener implements View.OnClickListener {

    private FragmentManager fragmentManager;

    /**
     * primary constructor for instantiation
     *
     * @param fragmentManager the fragment manager used to change fragments
     */
    public MainActivityFloatingActionButtonListener(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * listener that handles the clicking of the fab
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        //call the current fragments fab action
        ((AbstractFragment) fragmentManager.findFragmentById(R.id.fragmentContent)).fabAction(view);
    }
}
