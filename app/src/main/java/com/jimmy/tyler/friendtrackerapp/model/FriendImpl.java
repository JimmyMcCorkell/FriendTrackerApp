package com.jimmy.tyler.friendtrackerapp.model;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * the implementation class for the friend interface
 */
public class FriendImpl implements Friend {

    private String friendId;
    private String name, email, location;
    private Calendar birthday;
    private int photoColorId;

    /**
     * primary constructor for instantiation
     *
     * @param id       the friend id
     * @param name     the friend name
     * @param email    the friend email
     * @param birthday the friend birthday
     */
    public FriendImpl(String id, String name, String email, Calendar birthday) {
        this.friendId = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;

        this.location = null;
        this.photoColorId = ColorGenerator.MATERIAL.getColor(0);
    }

    @Override
    public String getFriendId() {
        return friendId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Calendar getBirthday() {
        return birthday;
    }

    @Override
    public int getPhotoColorId() {
        return photoColorId;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean setBirthday(Calendar birthday) {
        //check that birthday is before the current date
        if (birthday.compareTo(new GregorianCalendar()) <= 0) {
            this.birthday = birthday;
            return true;
        }

        return false;
    }

    @Override
    public void setPhotoColorId(int colorId) {
        this.photoColorId = colorId;
    }
}