package com.jimmy.tyler.friendtrackerapp.util;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.jimmy.tyler.friendtrackerapp.R;

/**
 * utility class to hold permission functionality
 */
public abstract class Permissions {

    /**
     * the different permission types required
     */
    public enum PermissionType {
        CONTACT, LOCATION
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 808;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 849;

    /**
     * check that the permission has been granted previously
     *
     * @param activity the origin of the permission call
     * @param type     the permission type
     * @return whether the permission is granted
     */
    public static boolean checkPermission(AppCompatActivity activity, PermissionType type) {
        switch (type) {
            //check the contact permission status
            case CONTACT:
                return ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            //check the location permission status
            case LOCATION:
                return ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * request the permission of a given type
     *
     * @param activity the origin of the permission call
     * @param type     the permission type
     */
    public static void requestPermission(final AppCompatActivity activity, PermissionType type) {
        switch (type) {
            //request the contact permission
            case CONTACT:
                //check whether the permission has been denied previously
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_CONTACTS)) {
                    //create a dialog to inform the user why the permission is needed
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setMessage(R.string.contact_permission_message)
                            .setTitle(R.string.contact_permission_title)
                            .setPositiveButton(R.string.contact_permission_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(activity,
                                            new String[]{Manifest.permission.READ_CONTACTS},
                                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                } else {
                    //request the permission
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                break;
            //request the location permission
            case LOCATION:
                //check whether the permission has been denied previously
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setMessage(R.string.location_permission_message)
                            .setTitle(R.string.location_permission_title)
                            .setPositiveButton(R.string.location_permission_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(activity,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();

                } else {
                    //request the permission
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                break;
        }
    }
}
