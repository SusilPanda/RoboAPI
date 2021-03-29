package com.robo.bean;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RoboLocation {

    public String name;
    public String number;

    public RoboLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RoboLocation(String username, String num) {
        this.name = username;
        this.number = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}