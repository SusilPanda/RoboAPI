package com.robo.bean;

import java.util.Date;

public class TemiNavigation {
    public String direction;
    public String tiltDegree;
    public Date lastUpdatedDate;

    public TemiNavigation() {
    }

    public TemiNavigation(String direction, Date date) {
        this.direction = direction;
        this.lastUpdatedDate = date;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTiltDegree() {
        return tiltDegree;
    }

    public void setTiltDegree(String tiltDegree) {
        this.tiltDegree = tiltDegree;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
