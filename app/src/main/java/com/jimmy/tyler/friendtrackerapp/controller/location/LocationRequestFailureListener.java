package com.jimmy.tyler.friendtrackerapp.controller.location;

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;

/**
 * the controller that handles the failure of a location request
 */
public class LocationRequestFailureListener implements OnFailureListener {

    private static final int REQUEST_CHECK_SETTINGS = 201;

    private AppCompatActivity activity;

    /**
     * primary constructor for instantiation
     *
     * @param activity the host of the requester
     */
    public LocationRequestFailureListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * the listener for handling a failed request
     *
     * @param e the exception thrown
     */
    @Override
    public void onFailure(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();

        switch (statusCode) {
            //error can be fixed through a resolution dialog
            case CommonStatusCodes.RESOLUTION_REQUIRED:
                try {
                    //show the dialog and check the result
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    sendEx.printStackTrace();
                }
                break;
            //settings can't be fixed so don't show a dialog
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }
}
