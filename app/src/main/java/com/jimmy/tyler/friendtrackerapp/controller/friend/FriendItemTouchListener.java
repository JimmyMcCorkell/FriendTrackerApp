package com.jimmy.tyler.friendtrackerapp.controller.friend;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.popups.PopupFriendItemListener;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.view.activities.FriendDetailsActivity;

/**
 * the controller that handles the touching of an item in the friend recycler view
 */
public class FriendItemTouchListener implements View.OnClickListener, View.OnLongClickListener {

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
    public FriendItemTouchListener(FragmentActivity parent, RecyclerView rv, Friend friend) {
        this.parent = parent;
        this.rv = rv;
        this.friend = friend;
    }

    /**
     * the listener for handling a single touch
     *
     * @param view the view that fired the event
     */
    @Override
    public void onClick(View view) {
        //start the friends details activity
        Intent intent = new Intent(parent, FriendDetailsActivity.class);
        intent.putExtra("friend", friend);
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
        pm.getMenuInflater().inflate(R.menu.context_menu_friend_item, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupFriendItemListener(parent, rv, friend));
        pm.show();

        return true;
    }
}