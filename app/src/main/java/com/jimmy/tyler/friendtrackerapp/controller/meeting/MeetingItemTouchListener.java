package com.jimmy.tyler.friendtrackerapp.controller.meeting;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.popups.PopupMeetingItemListener;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingDetailsActivity;

/**
 * controller that handles click of a meeting item inside a recycler view
 */
public class MeetingItemTouchListener implements View.OnClickListener, View.OnLongClickListener {

    private FragmentActivity parent;
    private RecyclerView rv;
    private Meeting meeting;

    /**
     * primary constructor for instantiation
     *
     * @param parent  the host of the recycler view
     * @param rv      the recycler view
     * @param meeting the meeting attached to the item
     */
    public MeetingItemTouchListener(FragmentActivity parent, RecyclerView rv, Meeting meeting) {
        this.parent = parent;
        this.rv = rv;
        this.meeting = meeting;
    }

    /**
     * the listener for handling a single touch
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        //start the meeting details activity
        Intent intent = new Intent(parent, MeetingDetailsActivity.class);
        intent.putExtra("meetingId", meeting.getMeetingId());
        parent.startActivity(intent);
    }

    /**
     * the listener for handling a long touch
     *
     * @param view the view that fired the event
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onLongClick(View view) {
        //create a popup menu using the context menu layout
        PopupMenu pm = new PopupMenu(parent, view, Gravity.CENTER);
        pm.getMenuInflater().inflate(R.menu.context_menu_meeting_item, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMeetingItemListener(parent, rv, meeting));
        pm.show();

        return true;
    }
}
