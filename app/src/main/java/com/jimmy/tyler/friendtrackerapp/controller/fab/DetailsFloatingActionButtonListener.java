package com.jimmy.tyler.friendtrackerapp.controller.fab;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * controller that handles the details floating action button
 */
public class DetailsFloatingActionButtonListener implements View.OnClickListener {

    /**
     * listener that handles the clicking of the fab
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        //TODO jump to the location
        Snackbar.make(view, "Jumping To Location Will Happen Here", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
