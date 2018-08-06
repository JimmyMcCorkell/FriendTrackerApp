package com.jimmy.tyler.friendtrackerapp.controller.meeting;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/**
 * the controller that handles the 'change date' field
 */
public class MeetingDateTimeButtonListener implements View.OnClickListener {

    private TextView textView;
    private Calendar cal;
    private boolean isStart;

    /**
     * primary constructor for instantiation
     *
     * @param tv      the text view holding the date/time
     * @param c       the initial date/time to set the dialog
     * @param isStart whether c is the start or end date/time
     */
    public MeetingDateTimeButtonListener(TextView tv, Calendar c, boolean isStart) {
        textView = tv;
        cal = c;
        this.isStart = isStart;
    }

    /**
     * the click listener for handling the click event
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        //Create a date picker dialog and show it
        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), new MeetingDateTimeDialogListener(textView, cal, isStart), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dpd.show();
    }
}
