package com.robo.bean;

public class TemiNavigation {
    public String direction;
    public String tiltDegree;

    public TemiNavigation() {
    }

    public TemiNavigation(String direction) {
        this.direction = direction;
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
}
