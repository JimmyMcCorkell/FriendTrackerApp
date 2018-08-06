package com.jimmy.tyler.friendtrackerapp.view.adapters;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.meeting.MeetingItemTouchListener;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Meeting;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    // Private view holder class
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView meetingTitle, meetingLocation, meetingNumFriends, meetingTime, meetingDate;

        public ViewHolder(View itemView) {
            super(itemView);
            meetingTitle = itemView.findViewById(R.id.meeting_title);
            meetingLocation = itemView.findViewById(R.id.meeting_location);
            meetingNumFriends = itemView.findViewById(R.id.meeting_numfriends);
            meetingTime = itemView.findViewById(R.id.meeting_time);
            meetingDate = itemView.findViewById(R.id.meeting_date);
        }
    }

    private List<Meeting> meetings;
    private RecyclerView rv;
    private FragmentActivity parent;

    /**
     * Constructs the meeting adapter
     *
     * @param parent   the parent fragment activity
     * @param rv       the recycler view
     * @param meetings a list of meetings
     */
    public MeetingsAdapter(FragmentActivity parent, RecyclerView rv, List<Meeting> meetings) {
        this.parent = parent;
        this.rv = rv;
        this.meetings = meetings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Meeting meeting = meetings.get(position);

        // Update the holder info
        holder.meetingTitle.setText(meeting.getTitle());
        holder.meetingLocation.setText(meeting.getLocation());
        holder.meetingNumFriends.setText(String.format("%d friends attending", meeting.getNumberOfFriends()));

        Calendar startTime = meeting.getStartTime();
        SimpleDateFormat time = new SimpleDateFormat("hh:mma");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");

        // Format and set the meeting dates and items
        holder.meetingTime.setText(time.format(startTime.getTime()).toUpperCase());
        holder.meetingDate.setText(date.format(startTime.getTime()).toUpperCase());

        // Set the meeting touch listener
        MeetingItemTouchListener listener = new MeetingItemTouchListener(parent, rv, meeting);

        holder.itemView.setOnClickListener(listener);

        // If listing meetings add the long click listener
        if (rv.getId() == R.id.list_meetings) {
            holder.itemView.setOnLongClickListener(listener);
        }

        // Set the single click listener to open meeting details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, MeetingDetailsActivity.class);
                intent.putExtra("meetingId", meeting.getMeetingId());
                parent.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }
}
