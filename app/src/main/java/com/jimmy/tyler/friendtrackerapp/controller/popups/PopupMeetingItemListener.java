package com.jimmy.tyler.friendtrackerapp.controller.popups;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingDetailsActivity;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingEditActivity;

/**
 * controller that handles the clicking of a meeting popup menu item
 */
public class PopupMeetingItemListener implements PopupMenu.OnMenuItemClickListener {

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
    public PopupMeetingItemListener(FragmentActivity parent, RecyclerView rv, Meeting meeting) {
        this.parent = parent;
        this.rv = rv;
        this.meeting = meeting;
    }

    /**
     * listener that handles the item click event
     *
     * @param item the menu item clicked
     * @return whether the click event was handled correctly
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            //meeting details was clicked
            case R.id.context_meeting_details:
                //start the meeting details activity
                Intent detailIntent = new Intent(parent, MeetingDetailsActivity.class);
                detailIntent.putExtra("meetingId", meeting.getMeetingId());
                parent.startActivity(detailIntent);
                break;
            //meeting location was clicked
            case R.id.context_meeting_location:
                //TODO implement jump to location
                Toast.makeText(parent, "Jumping To Location Will Occur Here", Toast.LENGTH_LONG).show();
                break;
            //edit meeting clicked
            case R.id.context_meeting_edit:
                //start the edit meeting activity
                Intent editIntent = new Intent(parent, MeetingEditActivity.class);
                editIntent.putExtra("MEETING", meeting);
                parent.startActivity(editIntent);
                break;
            //remove meeting clicked
            case R.id.context_meeting_remove:
                //remove the meeting and update the recycler view
                ModelImpl.getSingletonInstance().removeMeeting(meeting.getMeetingId());
                rv.getAdapter().notifyDataSetChanged();
                break;
        }
        return true;
    }
}
