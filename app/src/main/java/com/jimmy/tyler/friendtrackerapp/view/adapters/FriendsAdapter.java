package com.jimmy.tyler.friendtrackerapp.view.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.friend.FriendItemTouchListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.util.MeetingHolder;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    // Private ViewHolder class
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView contactImage;
        private TextView contactName, contactEmail;

        ViewHolder(View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            contactEmail = itemView.findViewById(R.id.contact_email);
        }
    }

    private FragmentActivity parent;
    private RecyclerView rv;
    private List<Friend> friends;
    private boolean listOnlyMode;

    /**
     * Construct a new friends adapter
     *
     * @param parent       the parent fragment
     * @param rv           the recycler view
     * @param friends      a list of friends
     * @param listOnlyMode if in list only mode
     */
    public FriendsAdapter(FragmentActivity parent, RecyclerView rv, List<Friend> friends, boolean listOnlyMode) {
        this.parent = parent;
        this.rv = rv;
        this.friends = friends;
        this.listOnlyMode = listOnlyMode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Friend friend = friends.get(position);
        // Generate the friends icon, using their initial
        String fl = String.valueOf(friend.getName().charAt(0));

        ColorGenerator cg = ColorGenerator.MATERIAL;
        friend.setPhotoColorId(cg.getColor(position));

        // Update the holders info
        holder.contactImage.setImageDrawable(TextDrawable.builder().buildRound(fl, friend.getPhotoColorId()));
        holder.contactName.setText(friend.getName());
        holder.contactEmail.setText(friend.getEmail());

        // If not in list only mode enable the touch listener
        if (!listOnlyMode) {
            FriendItemTouchListener listener = new FriendItemTouchListener(parent, rv, friend);

            holder.itemView.setOnClickListener(listener);

            // If in friends list enable the long click popup menu
            if (rv.getId() == R.id.list_friends) {
                holder.itemView.setOnLongClickListener(listener);
            }
        } else {
            // Set the normal onclick listener, add the friend to the meeting when clicked
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!MeetingHolder.getSingletonInstance().friends.contains(ModelImpl.getSingletonInstance().getFriend(friend.getFriendId()))) {
                        MeetingHolder.getSingletonInstance().friends.add(ModelImpl.getSingletonInstance().getFriend(friend.getFriendId()));
                    }
                    parent.finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}