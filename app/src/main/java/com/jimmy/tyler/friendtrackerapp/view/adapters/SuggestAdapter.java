package com.jimmy.tyler.friendtrackerapp.view.adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Model;
import com.jimmy.tyler.friendtrackerapp.view.activities.MeetingEditActivity;
import com.jimmy.tyler.friendtrackerapp.view.activities.SuggestMeetingActivity;

import java.util.List;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder> {

    // Private ViewHolder class
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView suggestImage;
        private TextView suggestName, suggestWalkTime;

        /**
         * primary constructor for instantiation
         *
         * @param itemView the view
         */
        ViewHolder(View itemView) {
            super(itemView);
            suggestImage = itemView.findViewById(R.id.suggest_icon);
            suggestName = itemView.findViewById(R.id.suggest_name);
            suggestWalkTime = itemView.findViewById(R.id.suggest_walk_time);
        }
    }

    private AppCompatActivity parent;
    private RecyclerView rv;
    private List<SuggestMeetingActivity.Suggestion> suggestions;

    /**
     * Construct a new friends adapter
     *
     * @param parent      the parent fragment
     * @param rv          the recycler view
     * @param suggestions a list of friends
     */
    public SuggestAdapter(AppCompatActivity parent, RecyclerView rv, List<SuggestMeetingActivity.Suggestion> suggestions) {
        this.parent = parent;
        this.rv = rv;
        this.suggestions = suggestions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model model = ModelImpl.getSingletonInstance();

        final SuggestMeetingActivity.Suggestion suggestion = suggestions.get(position);
        final Friend friend = model.getFriend(suggestion.friendId);

        // Generate the friends icon, using their initial
        String fl = String.valueOf(friend.getName().charAt(0));

        // Update the holders info
        holder.suggestImage.setImageDrawable(TextDrawable.builder().buildRound(fl, friend.getPhotoColorId()));
        holder.suggestName.setText(friend.getName());
        holder.suggestWalkTime.setText(String.format("Walk time to generated location: %s", suggestion.walkTimeText));

        // Set the normal onclick listener, opening the add meeting
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, MeetingEditActivity.class);
                intent.putExtra("friend", suggestion.friendId);
                intent.putExtra("location", suggestion.midpoint);
                intent.putExtra("start", suggestion.walkTimeValue);
                parent.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }
}