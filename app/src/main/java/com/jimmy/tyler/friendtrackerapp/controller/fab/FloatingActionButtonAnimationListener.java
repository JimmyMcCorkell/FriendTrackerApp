package com.jimmy.tyler.friendtrackerapp.controller.fab;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.jimmy.tyler.friendtrackerapp.R;

/**
 * controller that handles the floating action button animation
 */
public class FloatingActionButtonAnimationListener implements Animation.AnimationListener {

    private AppCompatActivity activity;
    private int icon;
    private boolean isRegrown;

    /**
     * primary constructor for instantiation
     *
     * @param activity  the host of the fab
     * @param icon      the id of the icon to change to
     * @param isRegrown whether the action button needs to be hidden
     */
    public FloatingActionButtonAnimationListener(AppCompatActivity activity, int icon, boolean isRegrown) {
        this.activity = activity;
        this.icon = icon;
        this.isRegrown = isRegrown;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    /**
     * listener for when the shrink animation ends so growing can occur afterwards
     *
     * @param animation the animation firing the onAnimation event
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);

        //check whether the fab needs to be present
        if (isRegrown) {
            //create an animation set
            AnimationSet s = new AnimationSet(false);

            //change the fab color and icon
            fab.show();
            fab.setBackgroundTintList(ContextCompat.getColorStateList(activity.getApplicationContext(), R.color.colorAccent));
            fab.setImageDrawable(ContextCompat.getDrawable(activity.getApplicationContext(), icon));

            //add a rotation animation
            Animation rotate = new RotateAnimation(60.0f, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            rotate.setDuration(150);
            rotate.setInterpolator(new DecelerateInterpolator());
            s.addAnimation(rotate);

            //add a scale up animation
            ScaleAnimation expand = new ScaleAnimation(0.1f, 1f, 0.1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            expand.setDuration(150);
            expand.setInterpolator(new DecelerateInterpolator());
            s.addAnimation(expand);

            //start the animation
            fab.startAnimation(s);
        } else {
            //hide the fab
            fab.hide();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
