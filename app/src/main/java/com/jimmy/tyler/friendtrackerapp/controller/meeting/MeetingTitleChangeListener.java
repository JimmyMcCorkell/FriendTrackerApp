package com.jimmy.tyler.friendtrackerapp.controller.meeting;

import android.text.Editable;
import android.text.TextWatcher;

import com.jimmy.tyler.friendtrackerapp.util.MeetingHolder;

/**
 * controller that handles the changing of the meeting title
 */
public class MeetingTitleChangeListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * the listener that fires after a text change
     *
     * @param editable the text found inside the view
     */
    @Override
    public void afterTextChanged(Editable editable) {
        //update the meeting title
        MeetingHolder.getSingletonInstance().title = editable.toString();
    }
}
