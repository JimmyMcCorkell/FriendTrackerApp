package com.jimmy.tyler.friendtrackerapp.controller.friend;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * the controller that handles the confirmation in the birthday date picker dialog
 */
public class FriendBirthdayDialogListener implements DatePickerDialog.OnDateSetListener {

    private TextView textView;

    /**
     * primary constructor for instantiation
     *
     * @param textView the text view to update
     */
    public FriendBirthdayDialogListener(TextView textView) {
        this.textView = textView;
    }

    /**
     * the listener for handling the confirm event
     *
     * @param datePicker the date picker in the dialog
     * @param i          the year
     * @param i1         the month
     * @param i2         the day
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        textView.setText(sdf.format(new GregorianCalendar(i, i1, i2).getTime()));
    }
}
