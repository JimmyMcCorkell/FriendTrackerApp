package com.jimmy.tyler.friendtrackerapp.model.interfaces;

import java.io.Serializable;
import java.util.Calendar;

/**
 * interface for an application friend
 */
public interface Friend extends Serializable {

    /**
     * get the friend id
     *
     * @return the friend id
     */
    String getFriendId();

    /**
     * get the friend name
     *
     * @return the friend name
     */
    String getName();

    /**
     * get the friend email
     *
     * @return the friend email
     */
    String getEmail();

    /**
     * get the friend location
     *
     * @return the friend location
     */
    String getLocation();

    /**
     * get the friend birthday
     *
     * @return the friend birthday
     */
    Calendar getBirthday();

    /**
     * get the color id of the photo
     *
     * @return the color id
     */
    int getPhotoColorId();

    /**
     * set the friend name
     *
     * @param name the new name
     */
    void setName(String name);

    /**
     * set the friend email
     *
     * @param email the new email
     */
    void setEmail(String email);

    /**
     * set the friend location
     *
     * @param location the new location
     */
    void setLocation(String location);

    /**
     * set the friend birthday
     *
     * @param birthday the new birthday
     * @return whether the new birthday is valid
     */
    boolean setBirthday(Calendar birthday);

    /**
     * set the color id of the photo
     *
     * @param colorId the new color id
     */
    void setPhotoColorId(int colorId);
}
