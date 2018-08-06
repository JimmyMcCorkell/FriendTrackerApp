package com.jimmy.tyler.friendtrackerapp.controller.meeting;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jimmy.tyler.friendtrackerapp.util.MeetingHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * controller that handles the confirmation of the date/time dialog
 */
public class MeetingDateTimeDialogListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Calendar cal;
    private TextView textView;
    boolean isStart;

    /**
     * primary constructor for instantiation
     *
     * @param tv      the text view holding the date/time
     * @param c       the initial date/time to set the dialog
     * @param isStart whether c is the start or end date/time
     */
    public MeetingDateTimeDialogListener(TextView tv, Calendar c, boolean isStart) {
        cal = c;
        textView = tv;
        this.isStart = isStart;
    }

    /**
     * the listener for handling the date confirm event
     *
     * @param datePicker the date picker in the dialog
     * @param i          the year
     * @param i1         the month
     * @param i2         the day
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //create the time picker dialog and show it
        cal.set(i, i1, i2);
        TimePickerDialog tpd = new TimePickerDialog(textView.getContext(), MeetingDateTimeDialogListener.this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
        tpd.show();
    }

    /**
     * the listener for handling the time confirm event
     *
     * @param timePicker the time picker in the dialog
     * @param i          the hour
     * @param i1         the minutes
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        //update the calender date/time and the meeting holder field
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), i, i1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mma");
        if (isStart) {
            MeetingHolder.getSingletonInstance().startTime = cal;
        } else {
            MeetingHolder.getSingletonInstance().endTime = cal;
        }

        textView.setText(sdf.format(cal.getTime()));
    }
}
