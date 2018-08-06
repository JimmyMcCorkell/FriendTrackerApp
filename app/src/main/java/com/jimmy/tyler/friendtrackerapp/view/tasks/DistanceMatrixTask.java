package com.jimmy.tyler.friendtrackerapp.view.tasks;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.util.Utilities;
import com.jimmy.tyler.friendtrackerapp.view.activities.SuggestMeetingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * an async task that handles the gathering of distance matrix data via REST
 */
public class DistanceMatrixTask extends AsyncTask<String, String, List<SuggestMeetingActivity.Suggestion>> {

    private SuggestMeetingActivity activity;
    private String location;

    private AlertDialog progressDialog;
    private ProgressBar progressBar;

    /**
     * primary constructor for instantiation
     *
     * @param activity the activity
     * @param location the users location
     */
    public DistanceMatrixTask(SuggestMeetingActivity activity, String location) {
        this.activity = activity;
        this.location = location;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //create and show the progress dialog
        progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(false);
        progressBar.setMax(ModelImpl.getSingletonInstance().getFriends().size());
        progressBar.setProgress(0);

        progressDialog = new AlertDialog.Builder(activity)
                .setView(progressBar)
                .setMessage("Checking distances..")
                .setCancelable(false)
                .create();

        progressDialog.show();
    }

    @Override
    protected List<SuggestMeetingActivity.Suggestion> doInBackground(String... strings) {
        String key = activity.getString(R.string.google_distance_matrix_key);

        List<SuggestMeetingActivity.Suggestion> suggestions = new ArrayList<>();

        //for each friend grab their location
        for (Friend f : ModelImpl.getSingletonInstance().getFriends()) {
            publishProgress(f.getName());

            if (f.getLocation() != null) {
                //get the midpoint and the JSONObject from the sent GET request
                String midpoint = Utilities.getMidpoint(location, f.getLocation());
                JSONObject json = Utilities.getDistanceData(key, location, f.getLocation(), midpoint);

                //decode the JSONObject to retrieve the required data
                try {
                    int userTime = json.getJSONObject("user").getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getInt("value");
                    int friendTime = json.getJSONObject("friend").getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getInt("value");
                    int walkTimeValue = Math.max(userTime, friendTime);

                    String walkTimeText;
                    if (walkTimeValue == userTime) {
                        walkTimeText = json.getJSONObject("user").getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
                    } else {
                        walkTimeText = json.getJSONObject("friend").getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
                    }

                    //add the suggestion to the suggestions list
                    SuggestMeetingActivity.Suggestion suggestion = new SuggestMeetingActivity.Suggestion(f.getFriendId(), walkTimeText, midpoint, walkTimeValue);
                    suggestions.add(suggestion);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }

        return suggestions;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        //update the progress dialog
        progressBar.incrementProgressBy(1);
        progressDialog.setMessage(String.format("Performing Distance Matrix Calculations..\n\nAnalysing %s", values[0]));
    }

    @Override
    protected void onPostExecute(List<SuggestMeetingActivity.Suggestion> suggestions) {
        super.onPostExecute(suggestions);

        //sort the list by closest walk time and populate the recycler
        Collections.sort(suggestions, new Comparator<SuggestMeetingActivity.Suggestion>() {
            @Override
            public int compare(SuggestMeetingActivity.Suggestion s1, SuggestMeetingActivity.Suggestion s2) {
                return Integer.compare(s1.walkTimeValue, s2.walkTimeValue);
            }
        });

        activity.populateRecycler(suggestions);

        progressDialog.dismiss();
    }
}
