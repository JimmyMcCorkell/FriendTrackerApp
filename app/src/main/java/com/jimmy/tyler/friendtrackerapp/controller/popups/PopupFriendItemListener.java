package com.jimmy.tyler.friendtrackerapp.controller.popups;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.view.activities.FriendDetailsActivity;
import com.jimmy.tyler.friendtrackerapp.view.activities.FriendEditActivity;

/**
 * controller that handles the clicking of a friend popup menu item
 */
public class PopupFriendItemListener implements PopupMenu.OnMenuItemClickListener {

    private FragmentActivity parent;
    private RecyclerView rv;
    private Friend friend;

    /**
     * primary constructor for instantiation
     *
     * @param parent the host of the recycler view
     * @param rv     the recycler view
     * @param friend the friend attached to the item
     */
    public PopupFriendItemListener(FragmentActivity parent, RecyclerView rv, Friend friend) {
        this.parent = parent;
        this.rv = rv;
        this.friend = friend;
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
            //friend details was clicked
            case R.id.context_friend_details:
                //start the friend details activity
                Intent detailIntent = new Intent(parent, FriendDetailsActivity.class);
                detailIntent.putExtra("friend", friend);
                parent.startActivity(detailIntent);
                break;
            //friend location was clicked
            case R.id.context_friend_location:
                //TODO implement jump to location
                Toast.makeText(parent, "Jumping To Location Will Occur Here", Toast.LENGTH_LONG).show();
                break;
            //edit friend was clicked
            case R.id.context_friend_edit:
                //start the edit friend activity
                Intent editIntent = new Intent(parent, FriendEditActivity.class);
                editIntent.putExtra(FriendEditActivity.NEW_CONTACT, false);
                editIntent.putExtra(FriendEditActivity.CONTACT, friend);
                parent.startActivity(editIntent);
                break;
            //remove friend was clicked
            case R.id.context_friend_remove:
                //remove the friend and update the recycler view
                ModelImpl.getSingletonInstance().removeFriend(friend.getFriendId());
                rv.getAdapter().notifyDataSetChanged();
                break;
        }
        return true;
    }
}
