package com.jimmy.tyler.friendtrackerapp.util;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.fab.FloatingActionButtonAnimationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

/**
 * utility class to hold general all-purpose functionality
 */
public abstract class Utilities {

    /**
     * generate a random string of specified length
     *
     * @param length the length of the string
     * @return the random string
     */
    public static String getRandomString(int length) {
        Random rand = new Random(System.currentTimeMillis());
        char[] symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabsdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder string = new StringBuilder();

        //grab a random char and add it to the string
        for (int i = 0; i < length; i++) {
            string.append(symbols[rand.nextInt(symbols.length)]);
        }

        return string.toString();
    }

    /**
     * animate the floating action button for the respective main activity fragment
     *
     * @param activity  the host of the fab
     * @param icon      the id of the icon to change to
     * @param isRegrown whether the action button needs to be hidden
     */
    public static void animateFab(final AppCompatActivity activity, final int icon, final boolean isRegrown) {
        //get the floating action button and clear its animation
        final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        fab.clearAnimation();

        //create a shrink animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.1f, 1f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(100);     // animation duration in milliseconds
        shrink.setInterpolator(new AccelerateInterpolator());

        //add an animation listener
        shrink.setAnimationListener(new FloatingActionButtonAnimationListener(activity, icon, isRegrown));

        //start the animation
        fab.startAnimation(shrink);
    }

    /**
     * get the midpoint between two friends' location
     *
     * @param f1 friend 1 location
     * @param f2 friend 2 location
     * @return the midpoint location
     */
    public static String getMidpoint(String f1, String f2) {
        //separate the latitude and longitude
        String[] latlng1 = f1.split(",");
        String[] latlng2 = f2.split(",");

        //find the midpoint of each direction
        float latMid = (Float.parseFloat(latlng1[0]) + Float.parseFloat(latlng2[0])) / 2;
        float lngMid = (Float.parseFloat(latlng1[1]) + Float.parseFloat(latlng2[1])) / 2;

        //return the new location
        return latMid + "," + lngMid;
    }

    /**
     * construct the URL to be used to calculate the distance between to locations
     *
     * @param origin      the location of the origin
     * @param destination the location of the destination
     * @return the constructed URL
     */
    private static URL getDistanceMatrixURL(String key, String origin, String destination) {
        try {
            return new URL(String.format("https://maps.googleapis.com/maps/api/distancematrix/json?%s&%s&%s&%s&%s&%s",
                    "mode=walking",
                    "unit=metric",
                    "avoid=indoor",
                    "origins=" + origin,
                    "destinations=" + destination,
                    "key=" + key));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get the resulting JSONObject from the Distance Matrix request
     *
     * @param key       the API key
     * @param userLoc   the users location
     * @param friendLoc the friends location
     * @param midpoint  the midpoint between the the two locations
     * @return the resulting JSONObject
     */
    public static JSONObject getDistanceData(String key, String userLoc, String friendLoc, String midpoint) {
        JSONObject jsonObject = new JSONObject();

        //Get the user JSONObject
        try {
            URL url = getDistanceMatrixURL(key, userLoc, midpoint);

            if (url != null) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Scanner response = new Scanner(connection.getInputStream());
                String responseBody = response.useDelimiter("\\A").next();

                jsonObject.put("user", new JSONObject(responseBody));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //Get the friend JSONObject
        try {
            URL url = Utilities.getDistanceMatrixURL(key, friendLoc, midpoint);

            if (url != null) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Scanner response = new Scanner(connection.getInputStream());
                String responseBody = response.useDelimiter("\\A").next();

                jsonObject.put("friend", new JSONObject(responseBody));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}