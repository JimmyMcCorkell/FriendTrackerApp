package com.jimmy.tyler.friendtrackerapp.controller.friend;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * the controller that handles the 'change birthday' field
 */
public class FriendBirthdayButtonListener implements View.OnClickListener {

    private Calendar initalDate;

    /**
     * primary constructor for instantiation
     *
     * @param initalDate the initial date to use in the date picker
     */
    public FriendBirthdayButtonListener(Calendar initalDate) {
        this.initalDate = initalDate;
    }

    /**
     * the click listener for handling the button event
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), new FriendBirthdayDialogListener((TextView) view), initalDate.get(Calendar.YEAR), initalDate.get(Calendar.MONTH), initalDate.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMaxDate(new GregorianCalendar().getTimeInMillis());
        dpd.show();
    }
}
