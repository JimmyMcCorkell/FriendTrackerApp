package com.jimmy.tyler.friendtrackerapp.view.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.friend.FriendItemTouchListener;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.util.MeetingHolder;

import java.util.List;


public class FriendsRemoveAdapter extends RecyclerView.Adapter<FriendsRemoveAdapter.ViewHolder> {

    // Private ViewHolder class
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView contactImage, cancelImage;
        private TextView contactName, contactEmail;

        ViewHolder(View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            contactEmail = itemView.findViewById(R.id.contact_email);
            cancelImage = itemView.findViewById(R.id.contact_remove);
        }
    }

    private FragmentActivity parent;
    private List<Friend> friends;

    /**
     * Constructs a friend remove adapter
     *
     * @param parent  the parent fragment activity
     * @param friends the list of friends to display
     */
    public FriendsRemoveAdapter(FragmentActivity parent, List<Friend> friends) {
        this.parent = parent;
        this.friends = friends;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting_friend, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Friend friend = friends.get(position);
        // Generate the friends icon, using their initial
        String fl = String.valueOf(friend.getName().charAt(0));

        // Update the holders info
        holder.contactImage.setImageDrawable(TextDrawable.builder().buildRound(fl, friend.getPhotoColorId()));
        holder.contactName.setText(friend.getName());
        holder.contactEmail.setText(friend.getEmail());

        // Set the item touch listener
        FriendItemTouchListener listener = new FriendItemTouchListener(parent, MeetingHolder.getSingletonInstance().recyclerView, friend);

        holder.itemView.setOnClickListener(listener);

        // Set the cancel listener
        holder.cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingHolder.getSingletonInstance().friends.remove(friend);
                // Update the attending text view
                ((TextView) parent.findViewById(R.id.meeting_edit_numfriends)).setText(String.format("%d attending", getItemCount()));
                MeetingHolder.getSingletonInstance().recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}